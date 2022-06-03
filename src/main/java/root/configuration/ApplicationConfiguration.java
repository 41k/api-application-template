package root.configuration;

import root.repository.UserRepository;
import root.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    @Bean
    public UserService userService(
            IdGenerator idGenerator,
            UserRepository userRepository,
            SecurityService securityService,
            MailService mailService
    ) {
        return new UserService(idGenerator, userRepository, securityService, mailService);
    }

    @Bean
    public RoleService roleService() {
        return new RoleService();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
