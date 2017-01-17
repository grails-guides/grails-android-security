package intranet.backend

import grails.plugin.json.builder.JsonOutput
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic(TypeCheckingMode.SKIP)
trait WatsonLogin extends LoginAs {

    /**
     *
     * @return watson access_token after successful login
     */
    String loginAsWatson() {
        loginAs('watson', '221Bbakerstreet')
    }
}
