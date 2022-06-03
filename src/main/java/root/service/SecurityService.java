package root.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import root.configuration.properties.SecurityProperties;
import root.model.Role;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

public class SecurityService {

    private static final String USER_ID = "userId";
    private static final String ROLES = "roles";
    private static final String ROLES_SEPARATOR = ",";

    private final SecurityProperties properties;
    private final RoleService roleService;
    private final Clock clock;
    private final PasswordEncoder encoder;
    private final Random randomGenerator;
    private final Algorithm tokenKey;
    private final JWTVerifier tokenVerifier;

    @SneakyThrows
    public SecurityService(SecurityProperties properties, RoleService roleService, Clock clock) {
        this.properties = properties;
        this.roleService = roleService;
        this.clock = clock;
        this.encoder = new BCryptPasswordEncoder();
        this.randomGenerator = new Random();
        this.tokenKey = Algorithm.HMAC256(properties.getTokenKey());
        this.tokenVerifier = ((JWTVerifier.BaseVerification) JWT.require(tokenKey)).build(() -> new Date(clock.millis()));
    }

    public String encode(String value) {
        return encoder.encode(value);
    }

    public boolean matches(String value, String encodedValue) {
        return encoder.matches(value, encodedValue);
    }

    public String generateVerificationCode() {
        return String.format("%04d", randomGenerator.nextInt(9999));
    }

    public String generateAccessToken(String userId) {
        var roles = roleService.getRoles(userId).stream().map(Enum::name).collect(Collectors.joining(ROLES_SEPARATOR));
        return JWT.create()
                .withPayload(Map.of(
                        USER_ID, userId,
                        ROLES, roles
                ))
                .withExpiresAt(new Date(clock.millis() + properties.getTokenTtlInMillis()))
                .sign(tokenKey);
    }

    public void setupSecurityContext(String accessToken) {
        try {
            var decodedAccessToken = tokenVerifier.verify(accessToken);
            var userId = Optional.ofNullable(decodedAccessToken.getClaim(USER_ID))
                    .map(Claim::asString)
                    .orElseThrow();
            var authorities = Optional.ofNullable(decodedAccessToken.getClaim(ROLES))
                    .map(Claim::asString)
                    .map(rolesString -> rolesString.split(ROLES_SEPARATOR))
                    .map(List::of)
                    .orElseThrow()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            var principal = new User(userId, userId, authorities);
            var authentication = new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
    }

    public String getRequesterId() {
        return getUserDetails().map(UserDetails::getUsername).orElseThrow(SecurityException::new);
    }

    private Collection<Role> getRequesterRoles() {
        return getUserDetails()
                .map(UserDetails::getAuthorities)
                .orElseThrow(SecurityException::new)
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }

    private Optional<UserDetails> getUserDetails() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(principal -> (UserDetails) principal);
    }
}
