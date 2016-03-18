package shrink.rest

import grails.compiler.GrailsCompileStatic
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject

/**
 * Created by jorge on 16/03/16.
 */
@GrailsCompileStatic
class ShrinkCodeFactory extends BasePooledObjectFactory<ShrinkCodeProvider>{

    long offset = -1

    int chunkSize = 10

    /***
     * cada vez que se necesita un nuevo objeto en el pool buscaremos en la base de datos
     * un #chunkSize disponibles. En caso de que todos los códigos se encuentren asignados se
     * produce una RuntimeException a la espera de que se liberen más códigos
     * @return
     * @throws Exception
     */
    @Override
    ShrinkCodeProvider create() throws Exception {

        List<CodeHash> items

        if( offset == -1 ) {
            long hay = CodeHash.countByUrlIsNull() as long
            offset = (hay / 2) as long
            println "Offset aleatorio $offset"
        }

        items = CodeHash.createCriteria().list(offset: offset , max: chunkSize) {
            isNull 'url'
        } as List<CodeHash>

        if( !items.size() ){
            offset = 0
            throw new RuntimeException("Codehash exhausted, add more code at bootstrap")
        }

        offset+=items.size()

        println "new code provider started at ${items.first().code} : ${items.last().code}"
        List<String> codes = items*.code as List<String>
        println codes
        ShrinkCodeProvider codeProvider = new ChunkShrinkCodeProvider(codes:codes)

        return codeProvider
    }

    @Override
    PooledObject<ShrinkCodeProvider> wrap(ShrinkCodeProvider obj) {
        return new DefaultPooledObject<ShrinkCodeProvider>(obj)
    }

    /**
     * Comprobamos si el pool ha consumido todos sus códigos
     * @param p
     * @return
     */
    @Override
    boolean validateObject(PooledObject<ShrinkCodeProvider> p) {
        return p.object.exhausted
    }

}
