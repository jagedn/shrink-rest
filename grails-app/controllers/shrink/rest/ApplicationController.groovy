package shrink.rest

import grails.core.GrailsApplication
import grails.util.Environment
import grails.plugins.*
import static grails.async.Promises.*
class ApplicationController implements PluginManagerAware {

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        [grailsApplication: grailsApplication,
         pluginManager: pluginManager ]
    }
}
