package org.grails.springsecurityrest;

import android.os.AsyncTask;

import org.grails.springsecurityrest.client.AuthenticationRequest;
import org.grails.springsecurityrest.client.GrailsSpringSecurityRestClient;
import org.grails.springsecurityrest.client.JwtResponse;


public class GrailsAuthenticateAsyncTask extends AsyncTask<AuthenticationRequest, Void, JwtResponse> {

    JwtResponseDelegate delegate;

    GrailsAuthenticateAsyncTask(JwtResponseDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JwtResponse doInBackground(AuthenticationRequest... authenticationRequests) {
        if (authenticationRequests != null && authenticationRequests.length >= 1) {
            AuthenticationRequest authenticationRequest = authenticationRequests[0];
            GrailsSpringSecurityRestClient client = new GrailsSpringSecurityRestClient();
            return client.authenticate(authenticationRequest);
        }
        return null;
    }

    protected void onPostExecute(JwtResponse response) {
        if ( delegate != null ) {
            delegate.onJwtResponseFetched(response);
        }
    }
}
