import org.quartz.Scheduler
import shrink.rest.PopulateCodeHashService

class BootStrap {

    PopulateCodeHashService populateCodeHashService

    def init = { servletContext ->

        populateCodeHashService.populateCodes()

        populateCodeHashService.initPool()
    }


    def destroy = {
    }
}
