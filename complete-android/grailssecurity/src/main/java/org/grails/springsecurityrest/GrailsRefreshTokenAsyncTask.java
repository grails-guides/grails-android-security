package org.grails.springsecurityrest;

import android.os.AsyncTask;

import org.grails.springsecurityrest.client.GrailsSpringSecurityRestClient;
import org.grails.springsecurityrest.client.JwtResponse;
import org.grails.springsecurityrest.client.RefreshRequest;

public class GrailsRefreshTokenAsyncTask extends AsyncTask<RefreshRequest, Void, JwtResponse> {

    JwtResponseDelegate delegate;

    GrailsRefreshTokenAsyncTask(JwtResponseDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JwtResponse doInBackground(RefreshRequest... requests) {
        if (requests != null && requests.length >= 1) {
            RefreshRequest refreshRequest = requests[0];
            GrailsSpringSecurityRestClient client = new GrailsSpringSecurityRestClient();
            return client.refreshToken(refreshRequest);
        }
        return null;
    }

    protected void onPostExecute(JwtResponse response) {
        if ( delegate != null ) {
            delegate.onJwtResponseFetched(response);
        }
    }
}
