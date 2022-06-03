package root.functional

import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import root.repository.UserRepository
import root.service.SecurityService
import spock.lang.Specification

import java.time.Clock

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static root.util.CommonTestConstants.CLOCK
import static root.util.UserDataFactory.USER_ID_1

@ActiveProfiles(['test'])
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [BaseTestContextConfiguration])
abstract class BaseFunctionalTest extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    protected UserRepository userRepository

    @Autowired
    protected SecurityService securityService

    protected String accessToken

    def setup() {
        RestAssured.port = port
        accessToken = securityService.generateAccessToken(USER_ID_1)
        userRepository.deleteAll()
        userRepository.flush()
    }

    @TestConfiguration
    static class BaseTestContextConfiguration {
        @Bean
        Clock clock() { CLOCK }
    }
}
