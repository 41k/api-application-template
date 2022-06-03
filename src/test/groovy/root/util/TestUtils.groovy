package root.util

import com.google.common.io.Resources
import root.dto.*

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TestUtils {

    public static final USER_ID_1 = 'user-id-1'
    public static final USER_ID_2 = 'user-id-2'
    public static final EMAIL = 'eMaiL-1@mail.com'
    public static final NORMALIZED_EMAIL = 'email-1@mail.com'
    public static final PASSWORD_1 = 'password-1'
    public static final PASSWORD_1_ENCODED = 'password-1-encoded'
    public static final PASSWORD_2 = 'password-2'
    public static final PASSWORD_2_ENCODED = 'password-2-encoded'
    public static final VERIFICATION_CODE = 'verification-code'
    public static final COUNTRY_CODE = 'BY'
    public static final CITY = 'MINSK'
    public static final ACCESS_TOKEN = 'access-token'
    public static final FIRST_NAME = 'first-name'
    public static final LAST_NAME = 'last-name'

    public static final USER_REGISTRATION_DTO = new UserRegistrationDto(email: EMAIL, password: PASSWORD_1)
    public static final USER_ACTIVATION_DTO = new UserActivationDto(email: EMAIL, verificationCode: VERIFICATION_CODE)
    public static final SIGN_IN_DTO = new SignInDto(email: EMAIL, password: PASSWORD_1)
    public static final RESET_PASSWORD_DTO = new ResetPasswordDto(email: EMAIL)
    public static final USER_UPDATE_DTO = new UserUpdateDto(
            password: PASSWORD_2,
            firstName: FIRST_NAME,
            lastName: LAST_NAME,
            countryCode: COUNTRY_CODE,
            city: CITY
    )

    public static final TIMESTAMP = 1641273825000L
    public static final CLOCK = Clock.fixed(Instant.ofEpochMilli(TIMESTAMP), ZoneId.systemDefault())

    public static final JSON_CONTENT_TYPE = 'application/json'
    public static final BASE_API_V1_URL = '/api/v1'
    public static final BASE_AUTH_API_URL = "$BASE_API_V1_URL/auth"
    public static final ME_API_URL = "$BASE_API_V1_URL/me"
    public static final USER_URI = "$BASE_API_V1_URL/users/$USER_ID_1"

    public static final USER_UPDATE_REQUEST_BODY = Resources.getResource('json/user-update-request.json').text
            .replace('$PASSWORD', PASSWORD_2)
            .replace('$FIRST_NAME', FIRST_NAME)
            .replace('$LAST_NAME', LAST_NAME)
            .replace('$COUNTRY_CODE', COUNTRY_CODE)
            .replace('$CITY', CITY)
}
