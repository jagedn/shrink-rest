package shrink.rest

import grails.transaction.Transactional


class CleanerCodeHashService {

    int secondsToRemove = 60  // after n seconds reuse the code

    /**
     * Actualiza los CodeHash caducos
     * @return
     */

    @Transactional
    def removeOld() {

        Calendar c = Calendar.instance
        c.add(Calendar.SECOND, -1*secondsToRemove)
        CodeHash.executeUpdate """
                update CodeHash c set c.url=null, c.consumedAt = null
                where c.consumedAt is not null and c.consumedAt < :from and c.permanent=false""", [
                from : c.time
        ]

    }
}
