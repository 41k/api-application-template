package root.service

import spock.lang.Specification

class IdGeneratorTest extends Specification {

    private idGenerator = new IdGenerator()

    def 'should generate id'() {
        expect:
        1.upto(20, {
            assert idGenerator.generate().length() == 8
        })
    }
}
