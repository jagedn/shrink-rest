package shrink.rest

import grails.async.Promise

import static java.util.concurrent.TimeUnit.*
import static grails.async.Promises.*
import static ShrinkPoolHelper.withCommonsPool

import org.apache.commons.pool2.ObjectPool

class ShrinkService {

    ObjectPool<ShrinkCodeProvider> shrinkCodeConnection

    int consumeCounter = 0

    /**
     * método para obtener un código sin asignar y asignarle la URL solicitada
     * @param url
     * @return
     */
    String consumeCode( String url) {

        Promise pConsume  = task {
            String code
            withCommonsPool(shrinkCodeConnection) { shrinkCode ->
                code = shrinkCode.peekCode(url)
            }
            def result = [code: code, url: url]

            CodeHash.withNewSession {
                CodeHash codeHash = CodeHash.findByCode(result.code)
                codeHash.url = url
                codeHash.consumedAt = new Date()
                codeHash.save(flush:true)
            }

            result
        }

        pConsume.onComplete {
            consumeCounter++
        }

        def ret = pConsume.get(10,SECONDS)
        notify "codeConsumed", ret
        ret.code
    }


}
