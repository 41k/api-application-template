package root.functional

import groovy.json.JsonOutput
import root.dto.UserDto
import root.dto.search.Filter
import root.dto.search.PageableSearchResult
import root.dto.search.SearchContext
import root.dto.search.Sort
import root.model.User
import spock.lang.Unroll

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.apache.http.HttpStatus.*
import static org.springframework.data.domain.Sort.Direction.DESC
import static root.controller.AccessTokenAuthenticationFilter.ACCESS_TOKEN_HEADER
import static root.util.CommonTestConstants.JSON_CONTENT_TYPE
import static root.util.UserDataFactory.*

class UserControllerFunctionalTest extends BaseFunctionalTest {

    private static final USERS_URL = '/api/v1/users'
    private static final USER_URI = "$USERS_URL/$USER_ID_1"
    private static final USERS_SEARCH_URL = "$USERS_URL/search"


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


    // --- Users search

    @Unroll
    def 'should perform users search with sorting and pagination successfully'() {
        given:
        userRepository.saveAllAndFlush([
                User.builder().id('u-0').email('e-0').password('pwd').active(true).build(),
                User.builder().id('u-1').email('e-1').password('pwd').active(true).build(),
                User.builder().id('u-2').email('e-2').password('pwd').active(false).build(),
                User.builder().id('u-3').email('e-3').password('pwd').active(false).build(),
                User.builder().id('u-4').email('e-4').password('pwd').active(true).build(),
                User.builder().id('u-5').email('e-5').password('pwd').active(true).build(),
                User.builder().id('u-6').email('e-6').password('pwd').active(true).build(),
                User.builder().id('u-7').email('e-7').password('pwd').active(true).build(),
                User.builder().id('u-8').email('e-8').password('pwd').active(true).build(),
                User.builder().id('u-9').email('e-9').password('pwd').active(true).build()
        ])

        and:
        def searchContext = SearchContext.builder()
                .start(4).limit(2)
                .filters([new Filter('id', ['u-1','u-2','u-3','u-4','u-5','u-6','u-7','u-8'])])
                .sorts([new Sort('email', DESC)])
                .build()

        when:
        def searchResult = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(JSON_CONTENT_TYPE)
                .body(JsonOutput.toJson(searchContext))
                .when()
                .post(USERS_SEARCH_URL)
                .then()
                .statusCode(SC_OK)
                .extract().body().as(PageableSearchResult)

        then:
        searchResult.start == 4
        searchResult.limit == 2
        searchResult.totalPages == 4
        searchResult.totalElements == 8

        and:
        def ids = (searchResult.data).collect({ it.id })
        ids == ['u-4','u-3']
    }

    @Unroll
    def 'should get 400 response code for user search request if pagination settings has [#reason]'() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(JSON_CONTENT_TYPE)
                .body(JsonOutput.toJson(SearchContext.builder().start(start).limit(limit).build()))
                .when()
                .post(USERS_SEARCH_URL)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains(errorMessage)

        where:
        reason        | start | limit | errorMessage
        'wrong start' | -1    | 4     | 'Start must not be less than 0'
        'wrong limit' | 3     | 0     | 'Limit must not be less than 1'
    }

    @Unroll
    def 'should get 400 response code for user search request if filtration settings has [#reason]'() {
        when:
        def response = given()
                .header(ACCESS_TOKEN_HEADER, accessToken)
                .contentType(JSON_CONTENT_TYPE)
                .body(JsonOutput.toJson(SearchContext.builder().start(0).limit(1).filters(filters).build()))
                .when()
                .post(USERS_SEARCH_URL)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract().body().asString()

        then:
        response.contains(errorMessage)

        where:
        reason                 | filters                                 | errorMessage
        'unsupported property' | [new Filter('unsupported-property', 1)] | 'Filtering by property [unsupported-property] is not supported'
        'wrong value type'     | [new Filter('id', 'id-1')]              | 'Value type for filtration property [id] is incorrect'
    }

    def 'should get 401 response code for user search request without valid access token'() {
        expect:
        when()
                .get(USERS_SEARCH_URL)
                .then()
                .statusCode(SC_UNAUTHORIZED)
    }
}
