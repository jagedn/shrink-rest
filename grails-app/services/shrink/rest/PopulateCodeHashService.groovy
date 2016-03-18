package shrink.rest

import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.apache.commons.pool2.impl.GenericObjectPool


@GrailsCompileStatic
class PopulateCodeHashService {

    /**
     * Número máximo de códigos a generar
     */
    int maxCodes = 1000

    /**
     * Si queremos reservar los primeros códigos para uso particular
     */
    int reserveFirst = 62

    /**
     * populateCodes busca el último Id de la base de datos y a partir de él continúa generando códigos hasta
     * alcanzar el número máximo de maxCodes configurados
     */

    @Transactional
    void populateCodes() {

        CodeHash lastCodeHash = CodeHash.last()
        long nextId = (lastCodeHash ? lastCodeHash.id  : 0) as long

        for(int i=0; i<maxCodes; i++ ){
            CodeHash add = new CodeHash(id:nextId, code:ShrinkPoolHelper.toCode(nextId), url : i<reserveFirst ? '#' : null, permanent: i<reserveFirst)
            add.save()
            nextId++
        }

        println "Hay ${CodeHash.count()} codigos. ${CodeHash.first().code} : ${CodeHash.last().code}"
    }

    GenericObjectPool shrinkCodeConnection

    /**
     * initPool inicializa el pool de conexiones para que existan un mínimo en el arranque
     */

    void initPool(){
        (0..shrinkCodeConnection.minIdle-1).each{
            shrinkCodeConnection.addObject()
        }
    }

}
