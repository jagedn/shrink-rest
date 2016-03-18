package shrink.rest

class CodeHash {

    static constraints = {
        code maxLength:4
        url nullable:true, maxLength : 2000
        consumedAt nullable:true
    }

    static mappings = {
        id generator: 'assigned'
        code index:'code_index'
    }

    String      code
    String      url
    Date        consumedAt
    Boolean     permanent = false
}
