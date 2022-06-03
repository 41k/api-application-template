package root.repository

import org.springframework.orm.jpa.JpaSystemException
import root.functional.BaseFunctionalTest
import root.model.User

import javax.validation.ConstraintViolationException

import static root.util.UserDataFactory.*

class UserRepositoryTest extends BaseFunctionalTest {

    def 'should throw exception during saving if [id] field is not set'() {
        when:
        userRepository.saveAndFlush(User.builder().build())

        then:
        def exception = thrown(JpaSystemException)
        exception.message.contains('ids for this class must be manually assigned before calling save')
    }

    def 'should throw exception during saving if email is not unique'() {
        given:
        def user1 = User.builder().id(USER_ID_1).email(NORMALIZED_EMAIL).password(PASSWORD_1_ENCODED).build()
        def user2 = User.builder().id(USER_ID_2).email(NORMALIZED_EMAIL).password(PASSWORD_2_ENCODED).build()

        when:
        userRepository.saveAndFlush(user1)
        userRepository.saveAndFlush(user2)

        then:
        def exception = thrown(Exception)
        exception.cause.cause.message.contains("Duplicate entry '$NORMALIZED_EMAIL' for key 'email'")
    }

    def 'should throw exception during saving if required fields are not set'() {
        given:
        def requiredFields = ['email', 'password']

        when:
        userRepository.saveAndFlush(User.builder().id(USER_ID_1).build())

        then:
        def exception = thrown(ConstraintViolationException)
        requiredFields.forEach({
            field -> assert exception.message.contains("interpolatedMessage='must not be null', propertyPath=$field")
        })
    }
}
