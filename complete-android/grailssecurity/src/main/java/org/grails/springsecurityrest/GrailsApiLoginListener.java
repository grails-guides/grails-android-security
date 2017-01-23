package org.grails.springsecurityrest;

public interface GrailsApiLoginListener {

    void onLoginSuccess();

    void onLoginUnauthorized();

    void onLoginFailed();
}
