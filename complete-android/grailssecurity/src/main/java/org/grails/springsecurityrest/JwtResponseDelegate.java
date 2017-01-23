package org.grails.springsecurityrest;

import org.grails.springsecurityrest.client.JwtResponse;

public interface JwtResponseDelegate {

    void onJwtResponseFetched(JwtResponse rsp);
}
