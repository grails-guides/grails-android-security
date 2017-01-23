package org.grails.springsecurityrest;

import org.grails.springsecurityrest.client.AuthenticationRequest;

public class GrailsSpringSecurityRestService {

    void onLoginFormSubmitedForUrl(LoginFormView view, String serverUrl) {

        String username = (String) view.getUsernameTextView().getText();
        String password = (String) view.getPasswordTextView().getText();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest.Builder()
                .serverUrl(serverUrl)
                .username(username)
                .password(password)
                .build();

        //new GrailsAuthenticateAsyncTask();



    }
}
