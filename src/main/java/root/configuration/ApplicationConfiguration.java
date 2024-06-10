package root.configuration;

import root.repository.UserRepository;
import root.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import root.service.UserMapper;
import root.service.UserSpecificationBuilder;

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
            UserMapper userMapper,
            SecurityService securityService,
            MailService mailService,
            UserSpecificationBuilder specificationBuilder
    ) {
        return new UserService(idGenerator, userRepository, userMapper, securityService, mailService, specificationBuilder);
    }

    @Bean
    public UserSpecificationBuilder userSpecificationBuilder() {
        return new UserSpecificationBuilder();
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
