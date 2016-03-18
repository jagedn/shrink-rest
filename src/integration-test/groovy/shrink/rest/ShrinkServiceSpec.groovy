package shrink.rest


import grails.test.mixin.integration.Integration
import grails.transaction.*
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.*

@Integration
@Rollback
class ShrinkServiceSpec extends Specification {

    @Autowired
    ShrinkService shrinkService

    def setup() {
    }

    def cleanup() {
    }

    void "test something #test"() {

        String code

        code = shrinkService.consumeCode("http://www.google.es")
        println "$test $code"
        expect:"fix me"
            code != null
            code != ""

        where:
        test << (0..40000)
    }
}
