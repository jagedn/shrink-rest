package shrink.rest

import groovy.transform.CompileStatic

/**
 * Created by jorge on 16/03/16.
 */
@CompileStatic
class ChunkShrinkCodeProvider implements ShrinkCodeProvider{

    List<String> codes = []

    String started

    /**
     * devuelve true si no quedan más códigos en la lista
     * @return
     */
    boolean isExhausted(){
        codes.size() > 0
    }

    ChunkShrinkCodeProvider(){
    }

    public void setCodes(List<String>codes){
        started = codes[0]
        this.codes = codes
    }

    /**
     * removemos un código de la lista
     * @param url
     * @return
     */
    @Override
    String peekCode(String url) {
        String code = codes.remove(0)
        return code
    }
}
