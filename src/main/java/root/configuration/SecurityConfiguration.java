package root.configuration;

import root.configuration.properties.SecurityProperties;
import root.controller.AccessTokenAuthenticationFilter;
import root.model.Role;
import root.service.RoleService;
import root.service.SecurityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.time.Clock;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@Slf4j
public class SecurityConfiguration {

    @Bean
    public SecurityService securityService(
            SecurityProperties properties,
            RoleService roleService,
            Clock clock
    ) {
        return new SecurityService(properties, roleService, clock);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // NOTE: this bean is necessary to specify that we do not need to use prefix before role name
        // because by default Spring Security uses ROLE_ prefix, e.g. if we specified .hasRole("ADMIN")
        // it will search for role with name ROLE_ADMIN instead of just ADMIN during authorization.
        return new GrantedAuthorityDefaults(EMPTY);
    }

    @Bean
    public AccessTokenAuthenticationFilter accessTokenAuthenticationFilter(SecurityService securityService) {
        return new AccessTokenAuthenticationFilter(securityService);
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AccessTokenAuthenticationFilter accessTokenAuthenticationFilter) {
        http.csrf().disable()
                .addFilterBefore(accessTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationFailureHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/me").hasRole(Role.ROLE1.name())
                .antMatchers("/api/v1/users/**").hasRole(Role.ROLE1.name())
                .anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v3/api-docs/**");
    }

    private AuthenticationEntryPoint authenticationFailureHandler() {
        return (request, response, e) -> {
            log.warn("### Unauthorized access attempt to endpoint [{} {}]: {}",
                    request.getMethod(), request.getServletPath(), e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        };
    }
}
