package root.service.mapper

import spock.lang.Specification

import static root.util.UserDataFactory.createUser
import static root.util.UserDataFactory.createUserDto

class UserMapperTest extends Specification {

    private mapper = new UserMapperImpl()

    def 'should map correctly'() {
        expect:
        mapper.toDto(createUser()) == createUserDto()
    }
}
