package root.functional

import org.springframework.beans.factory.annotation.Autowired
import root.dto.search.Filter
import root.model.User
import root.service.UserSpecificationBuilder
import spock.lang.Unroll

class UserSpecificationBuilderTest extends BaseFunctionalTest {

    @Autowired
    private UserSpecificationBuilder specificationBuilder

    @Unroll
    def 'should perform users search [#criteria] successfully'() {
        given:
        userRepository.saveAllAndFlush([
                User.builder().id('u-1').email('e-1').password('pwd').firstName('fn36').active(true).build(),
                User.builder().id('u-2').email('e-2').password('pwd').firstName('fn11').active(false).build(),
                User.builder().id('u-3').email('e-3').password('pwd').firstName('fn02').active(false).build(),
                User.builder().id('u-4').email('e-4').password('pwd').firstName('fn19').active(true).build()
        ])

        when:
        def specification = specificationBuilder.build(filters)

        and:
        def users = userRepository.findAll(specification)

        then:
        def ids = users.collect({ it.id })
        ids.size() == expectedIds.size()
        expectedIds.containsAll(ids)

        where:
        criteria              | filters                                                             | expectedIds
        'by ids'              | [new Filter('id', ['u-1', 'u-3'])]                                  | ['u-1', 'u-3']
        'by emails'           | [new Filter('email', ['e-2', 'e-3'])]                               | ['u-2', 'u-3']
        'by first name'       | [new Filter('firstName', 'n1')]                                     | ['u-2', 'u-4']
        'by active flag'      | [new Filter('active', true)]                                        | ['u-1', 'u-4']
        'by combined filters' | [new Filter('id', ['u-2','u-3','u-4']), new Filter('active', true)] | ['u-4']
        'when filters empty'  | []                                                                  | ['u-1', 'u-2', 'u-3', 'u-4']
        'when filters null'   | null                                                                | ['u-1', 'u-2', 'u-3', 'u-4']
    }

    def 'should throw exception for unsupported filtration property'() {
        given:
        def unsupportedPropertyName = 'unsupported-property'
        def filters = [
                new Filter('id', ['u-3']),
                new Filter(unsupportedPropertyName, 'value'),
                new Filter('active', true)
        ]

        when:
        specificationBuilder.build(filters)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "Filtering by property [$unsupportedPropertyName] is not supported"
    }

    def 'should throw exception for incorrect filtration value type'() {
        given:
        def propertyName = 'id'
        def filters = [
                new Filter(propertyName, 'value-with-incorrect-type'),
                new Filter('active', false)
        ]

        when:
        specificationBuilder.build(filters)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "Value type for filtration property [$propertyName] is incorrect"
    }
}
