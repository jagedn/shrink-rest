package shrink.rest

import org.apache.commons.pool2.ObjectPool
import org.apache.commons.pool2.impl.GenericObjectPool

/**
 * Created by jorge on 16/03/16.
 */
class ShrinkPoolHelper {

    static List codes = ('a'..'z') + ('A'..'Z')+ ('0'..'9')

    static int codesSize = codes.size()

    static String toCode(long id ){
        String ret = ''

        if( ! id )
            return codes[0]

        while(id){
            ret += codes[ (id % codesSize) as int ]
            id = id / codesSize
        }

        ret
    }

    static def withCommonsPool(ObjectPool<ShrinkCodeProvider> pool, Closure closure) {
        def borrowedObject = pool.borrowObject()
        try {
            return closure.call(borrowedObject)
        } catch (Exception e) {
            println "exception caught using pool, invalidating connection $e"
            if (null != borrowedObject) {
                pool.invalidateObject(borrowedObject)
            }
            borrowedObject = null
            throw e
        } finally {
            if (null != borrowedObject) {
                pool.returnObject(borrowedObject)
            }
        }
    }

}
