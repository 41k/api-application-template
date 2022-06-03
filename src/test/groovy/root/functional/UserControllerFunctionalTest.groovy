package root.functional

import root.dto.UserDto
import root.model.User

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.apache.http.HttpStatus.*
import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER
import static root.util.TestUtils.*

class UserControllerFunctionalTest extends BaseFunctionalTest {

    // --- Active user retrieval

    def 'should retrieve user'() {
        given:
        def user = User.builder().id(USER_ID_1).email(NORMALIZED_EMAIL).password(PASSWORD_1_ENCODED).active(true).build()
        def userDto = UserDto.from(user)
        userRepository.saveAndFlush(user)

        when:
        def retrievedUserDto = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .get(USER_URI)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .as(UserDto)

        then:
        retrievedUserDto == userDto
    }

    def 'should get 400 response code for user retrieval request if active user is not found by id'() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .get(USER_URI)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains('Validation exception: Wrong user id.')
    }

    def 'should get 401 response code for user retrieval request without valid access token'() {
        expect:
        when()
                .get(USER_URI)
                .then()
                .statusCode(SC_UNAUTHORIZED)
    }
}
