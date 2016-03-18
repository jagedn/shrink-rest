package shrink.rest

import groovy.transform.CompileStatic

/**
 * Created by jorge on 16/03/16.
 */
@CompileStatic
interface ShrinkCodeProvider {

    String peekCode( String url)

    boolean isExhausted()
}
