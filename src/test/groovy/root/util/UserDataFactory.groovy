package root.util

import root.dto.UserDto
import root.model.User

class UserDataFactory {

    public static final USER_ID_1 = 'user-id-1'
    public static final USER_ID_2 = 'user-id-2'
    public static final EMAIL = 'eMaiL-1@mail.com'
    public static final NORMALIZED_EMAIL = 'email-1@mail.com'
    public static final PASSWORD_1 = 'password-1'
    public static final PASSWORD_1_ENCODED = 'password-1-encoded'
    public static final PASSWORD_2 = 'password-2'
    public static final PASSWORD_2_ENCODED = 'password-2-encoded'
    public static final VERIFICATION_CODE = 'verification-code'
    public static final COUNTRY_CODE = 'US'
    public static final CITY = 'NEW-YORK'
    public static final ACCESS_TOKEN = 'access-token'
    public static final FIRST_NAME = 'first-name'
    public static final LAST_NAME = 'last-name'
    public static final ACTIVE = true

    private static def DEFAULT_USER() {
        [
                id               : USER_ID_1,
                email            : EMAIL,
                normalizedEmail  : NORMALIZED_EMAIL,
                password         : PASSWORD_1,
                encodedPassword  : PASSWORD_1_ENCODED,
                firstName        : FIRST_NAME,
                lastName         : LAST_NAME,
                countryCode      : COUNTRY_CODE,
                city             : CITY,
                verificationCode : VERIFICATION_CODE,
                active           : ACTIVE
        ]
    }

    // NOTE: default settings can be overridden in the next way, e.g. email and city:
    // createUser(normalizedEmail: 'another-email@mail.com', city: 'LONDON')
    static User createUser(def properties = [:]) {
        def data = DEFAULT_USER() + properties
        User.builder()
                .id(data.id)
                .email(data.normalizedEmail)
                .password(data.encodedPassword)
                .firstName(data.firstName)
                .lastName(data.lastName)
                .countryCode(data.countryCode)
                .city(data.city)
                .verificationCode(data.verificationCode)
                .active(data.active)
                .build()
    }

    static UserDto createUserDto(def properties = [:]) {
        def data = DEFAULT_USER() + properties
        UserDto.builder()
                .id(data.id)
                .email(data.normalizedEmail)
                .firstName(data.firstName)
                .lastName(data.lastName)
                .countryCode(data.countryCode)
                .city(data.city)
                .build()
    }
}
