package shrink.rest

import grails.transaction.Transactional
import reactor.spring.context.annotation.*

@Consumer
@Transactional
class StadisticService {

    @Selector('codeConsumed')
    void codeConsumed(Object data) {
        println "stadistic log: $data.code generated for $data.url at ${new Date()}"
    }
}
