package shrink.rest

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'shrink', action:'index')

        "/?$id"(controller: 'shrink', action:'index')

        // "/"(controller: 'application', action:'index')

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
