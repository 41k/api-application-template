package root.service

import org.springframework.security.core.context.SecurityContextHolder
import root.configuration.properties.SecurityProperties
import root.model.Role
import spock.lang.Specification

import static root.util.CommonTestConstants.CLOCK
import static root.util.UserDataFactory.USER_ID_1

class SecurityServiceTest extends Specification {

    private static final ACCESS_TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEUxLFJPTEUyIiwiZXhwIjoxNjQxMjczODg1LCJ1c2VySWQiOiJ1c2VyLWlkLTEifQ.kHt23ajbMbClUhGL-Fmv3Ox4G-fzAmnuYbmDxP5M63s'
    private static final ROLES = [Role.ROLE1, Role.ROLE2]

    private properties = new SecurityProperties(
            salt: 'salt',
            tokenKey: 'token-key',
            tokenTtlInMillis: 60000
    )
    private roleService = Mock(RoleService)
    private securityService = new SecurityService(properties, roleService, CLOCK)


    def 'should encode value and check if encoded value matches original value'() {
        given:
        def value = 'value'

        when:
        def encodedValue = securityService.encode(value)

        then:
        securityService.matches(value, encodedValue)
    }

    def 'should generate verification code as 4 digit sequence'() {
        given:
        def codePattern = ~/\d{4}/

        when:
        def code = securityService.generateVerificationCode()

        then:
        codePattern.matcher(code).matches()
    }

    def 'should generate access token'() {
        given:
        1 * roleService.getRoles(USER_ID_1) >> ROLES
        0 * _

        expect:
        securityService.generateAccessToken(USER_ID_1) == ACCESS_TOKEN
    }

    def 'should setup security context successfully'() {
        when:
        securityService.setupSecurityContext(ACCESS_TOKEN)

        then:
        SecurityContextHolder.getContext().getAuthentication()
        securityService.getRequesterId() == USER_ID_1
        securityService.getRequesterRoles() == ROLES
    }

    def 'should not setup security context if access token is incorrect'() {
        when:
        securityService.setupSecurityContext('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlNQRUNUQVRPUixEUklWRVIiLCJleHAiOjE2NDEyNzM4ODV9.so02SE2RG-8FxjThuSnUWtfntNOm-qCzewSXBDnMyLc')

        then:
        !SecurityContextHolder.getContext().getAuthentication()

        when:
        securityService.setupSecurityContext('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDEyNzM4ODUsInVzZXJJZCI6InVzZXItaWQtMSJ9.RgfBNQT2dNQX6D64Id7eUwfjNT7cniKHafZO25stnFA')

        then:
        !SecurityContextHolder.getContext().getAuthentication()

        when:
        securityService.setupSecurityContext('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlNQRUNUQVRPUixEUklWRVIiLCJleHAiOjE2NDEyNzM4ODUsInVzZXJJZCI6InVzZXItaWQtMSJ9.oG45tERplBsFSX5wjZaxCRADuEJs_XEG7esxa256Arb')

        then:
        !SecurityContextHolder.getContext().getAuthentication()

        when:
        securityService.setupSecurityContext('abc')

        then:
        !SecurityContextHolder.getContext().getAuthentication()
    }
}
