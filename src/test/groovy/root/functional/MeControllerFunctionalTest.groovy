package root.functional

import root.dto.UserDto
import root.model.User

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.apache.http.HttpStatus.*
import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER
import static root.util.CommonTestConstants.JSON_CONTENT_TYPE
import static root.util.UserDataFactory.*

class MeControllerFunctionalTest extends BaseFunctionalTest {

    private static final ME_API_URL = '/api/v1/me'
    public static final USER_UPDATE_REQUEST_BODY = """
        {
            "password": "$PASSWORD_2",
            "firstName": "$FIRST_NAME",
            "lastName": "$LAST_NAME",
            "countryCode": "$COUNTRY_CODE",
            "city": "$CITY"
        }
    """


    // --- Requester's user retrieval

    def "should retrieve requester's user successfully"() {
        given:
        def user = createUser()
        def userDto = createUserDto()
        userRepository.saveAndFlush(user)

        when:
        def retrievedUserDto = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .get(ME_API_URL)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .as(UserDto)

        then:
        retrievedUserDto == userDto
    }

    def "should get 400 response code for requester's user retrieval request if active user is not found by id"() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .get(ME_API_URL)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains('Validation exception: Wrong user id.')
    }

    def "should get 401 response code for requester's user retrieval request without valid access token"() {
        expect:
        when()
                .get(ME_API_URL)
                .then()
                .statusCode(SC_UNAUTHORIZED)
    }


    // --- Requester's user update

    def "should update requester's user successfully"() {
        given:
        userRepository.saveAndFlush(
                User.builder().id(USER_ID_1).email(NORMALIZED_EMAIL).password(PASSWORD_1_ENCODED).active(true).build())

        when:
        given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(JSON_CONTENT_TYPE)
                .body(USER_UPDATE_REQUEST_BODY)
                .when()
                .put(ME_API_URL)
                .then()
                .statusCode(SC_OK)

        then:
        def updatedUser = userRepository.findById(USER_ID_1).get()
        updatedUser.id == USER_ID_1
        updatedUser.email == NORMALIZED_EMAIL
        securityService.matches(PASSWORD_2, updatedUser.password)
        updatedUser.firstName == FIRST_NAME
        updatedUser.lastName == LAST_NAME
        updatedUser.countryCode == COUNTRY_CODE
        updatedUser.city == CITY
        updatedUser.active
    }

    def "should get 400 response code for requester's user update request if active user is not found by id"() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(JSON_CONTENT_TYPE)
                .body(USER_UPDATE_REQUEST_BODY)
                .when()
                .put(ME_API_URL)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains('Validation exception: Wrong user id.')
    }

    def "should get 401 response code for requester's user update request without valid access token"() {
        expect:
        given()
                .contentType(JSON_CONTENT_TYPE)
                .body(USER_UPDATE_REQUEST_BODY)
                .when()
                .put(ME_API_URL)
                .then()
                .statusCode(SC_UNAUTHORIZED)
    }


    // --- Requester's user deactivation

    def "should deactivate requester's user successfully"() {
        given:
        def user = User.builder().id(USER_ID_1).email(NORMALIZED_EMAIL).password(PASSWORD_1_ENCODED).active(true).build()
        def deactivatedUser = user.toBuilder().active(false).build()
        userRepository.saveAndFlush(user)

        when:
        given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .delete(ME_API_URL)
                .then()
                .statusCode(SC_OK)

        then:
        userRepository.findById(USER_ID_1).get() == deactivatedUser
    }

    def "should get 400 response code for requester's user deactivation request if active user is not found by id"() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .when()
                .delete(ME_API_URL)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains('Validation exception: Wrong user id.')
    }

    def "should get 401 response code for requester's user deactivation request without valid access token"() {
        expect:
        when()
                .delete(ME_API_URL)
                .then()
                .statusCode(SC_UNAUTHORIZED)
    }
}
