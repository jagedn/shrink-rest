// Place your Spring DSL code here


import shrink.rest.ShrinkCodeFactory
import shrink.rest.ShrinkPoolHelper
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

beans = {

    shrinkCodeFactory(ShrinkCodeFactory)

    shrinkPoolConfig(GenericObjectPoolConfig){

        JmxEnabled = false

        maxTotal = 100
        maxIdle = 10
        minIdle = 4

        //61 seconds to wait for object creation
        maxWaitMillis = 61000

        // minutes between checks for evictions
        timeBetweenEvictionRunsMillis = 2 * 1000

        testOnBorrow = true

        testOnReturn = true

        blockWhenExhausted = true
    }

    shrinkCodeConnection(GenericObjectPool,shrinkCodeFactory, shrinkPoolConfig)

}
