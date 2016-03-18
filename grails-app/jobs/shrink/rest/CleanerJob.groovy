package shrink.rest

class CleanerJob {

    static triggers = {
      //simple repeatInterval: 1000*l, startDelay: 500 // execute job once in 5 seconds
      cron name: 'cleanerTrigger', cronExpression: "0/1 * * * * ?"
    }

    CleanerCodeHashService cleanerCodeHashService

    def execute() {

        cleanerCodeHashService.removeOld()

    }
}
