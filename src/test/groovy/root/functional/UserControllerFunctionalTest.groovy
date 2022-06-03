package root.functional

import root.dto.UserDto

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.apache.http.HttpStatus.*
import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER
import static root.util.UserDataFactory.*

class UserControllerFunctionalTest extends BaseFunctionalTest {

    private static final USER_URI = "/api/v1/users/$USER_ID_1"

    // --- Active user retrieval

    def 'should retrieve user'() {
        given:
        def user = createUser()
        def userDto = createUserDto()
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
