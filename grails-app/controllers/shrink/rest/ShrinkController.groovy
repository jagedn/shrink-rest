package shrink.rest

import grails.core.GrailsApplication
import grails.rest.*
import grails.converters.*

class ShrinkController {

    GrailsApplication grailsApplication

    static allowedMethods = [save:'POST', show:'GET']

    ShrinkService shrinkService

    /**
     * index busca en el queryString un código válido para obtener la URL asignada
     * @return
     */
    def index() {

        if( !request.queryString ){
            redirect status:302, url:grailsApplication.config.grails.clientURL
            return
        }

        CodeHash codeHash = CodeHash.findByCode(request.queryString)
        if( !codeHash ){
            notify "codeNotFound", request.queryString
            render status:404
            return
        }

        notify "codeDispatched", request.queryString

        redirect status:302, url:codeHash.url
    }

    /**
     * generate, accesible mediante POST permite asignar la URL que se pase como parametro en el body,
     * devolviendo un JSON con el código y la Shrink URL a utilizar.
     * @return
     */
    def generate(){
        String code = shrinkService.consumeCode( params.url )
        [shrink : [code:code, url : "$grailsApplication.config.grails.serverURL?$code"]]
    }
}
