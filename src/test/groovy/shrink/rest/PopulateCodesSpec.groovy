package shrink.rest

import spock.lang.Specification

/**
 * Created by jorge on 17/03/16.
 */
class PopulateCodesSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test #number"() {

        when:
        String code = ShrinkPoolHelper.toCode(number)

        then:
        code == expected


        where:
        number | expected
        0    | 'a'
        1    | 'b'
        10   | 'k'
        61   | '9'
        62   | 'ab'
        68   | 'gb'
        1024   | 'Gq'
        99999   | '3aA'
    }
}
