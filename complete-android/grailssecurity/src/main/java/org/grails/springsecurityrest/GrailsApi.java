package org.grails.springsecurityrest;

import android.content.Context;

import org.grails.springsecurityrest.client.AuthenticationRequest;
import org.grails.springsecurityrest.client.Jwt;
import org.grails.springsecurityrest.client.JwtResponse;
import org.grails.springsecurityrest.client.JwtResponseForbidden;
import org.grails.springsecurityrest.client.JwtResponseOK;
import org.grails.springsecurityrest.client.JwtResponseUnauthorized;
import org.grails.springsecurityrest.client.RefreshRequest;

public class GrailsApi {

    private static final String OAUTH_ACCESS_TOKEN_TYPE_BEARER = "Bearer";

    public static boolean hasRole(Context context, String roleName) {
        Jwt jwt = new JwtSharedPreferencesManager().getJwt(context);
        if ( jwt == null ) {
            return false;
        }
        if ( jwt.getRoles() == null ) {
            return false;
        }
        return jwt.getRoles().contains(roleName);
    }

    public static void login(final Context context, String grailsServerUrl, String username, String password, final GrailsApiLoginListener listener) {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest.Builder()
                .serverUrl(grailsServerUrl)
                .username(username)
                .password(password)
                .build();
        loginWithAuthenticationRequest(context, authenticationRequest, listener);
    }

    public static void loginWithAuthenticationRequest(final Context context, AuthenticationRequest authenticationRequest, final GrailsApiLoginListener listener) {

        new GrailsAuthenticateAsyncTask(new JwtResponseDelegate() {
            @Override
            public void onJwtResponseFetched(JwtResponse rsp) {

                if ( rsp instanceof JwtResponseOK) {
                    Jwt jwt = ((JwtResponseOK)rsp).getJwt();
                    boolean savedJwtSuccessfully =
                            new JwtSharedPreferencesManager().saveJwt(context, jwt);

                    if ( listener != null ) {
                        if ( savedJwtSuccessfully ) {
                            listener.onLoginSuccess();
                        } else {
                            listener.onLoginFailed();
                        }
                    }
                    return;

                } else if ( rsp instanceof JwtResponseUnauthorized ) {
                    if ( listener != null ) {
                        listener.onLoginUnauthorized();
                    }
                    return;
                }


                if ( listener != null ) {
                    listener.onLoginFailed();
                }
            }
        }).execute(authenticationRequest);
    }

    public static String authorizationHeaderValue(Context context) {

        StringBuilder sb = new StringBuilder();
        sb.append(OAUTH_ACCESS_TOKEN_TYPE_BEARER);
        sb.append(" ");
        Jwt jwt = new JwtSharedPreferencesManager().getJwt(context);
        if ( jwt != null && jwt.getAccessToken() != null) {
            sb.append(jwt.getAccessToken());
        }
        return sb.toString();
    }

    public static boolean shouldTryToRefreshAccessToken(Context context) {
        return (getRefreshToken(context) != null);
    }

    public static String getRefreshToken(Context context) {
        Jwt jwt = new JwtSharedPreferencesManager().getJwt(context);
        return jwt != null ? jwt.getRefreshToken() : null;
    }

    public static void refreshAccessToken(final Context context, String grailsServerUrl, final OAuthTokenRefreshListener listener) {
        RefreshRequest refreshRequest = new RefreshRequest.Builder()
                .serverUrl(grailsServerUrl)
                .refreshToken(getRefreshToken(context))
                .build();

        new GrailsRefreshTokenAsyncTask(new JwtResponseDelegate() {
            @Override
            public void onJwtResponseFetched(JwtResponse rsp) {
                if ( rsp instanceof JwtResponseOK) {
                    Jwt jwt = ((JwtResponseOK)rsp).getJwt();
                    boolean savedJwtSuccessfully =
                            new JwtSharedPreferencesManager().saveJwt(context, jwt);

                    if ( listener != null ) {
                        if ( savedJwtSuccessfully ) {
                            listener.onOAuthTokenRefreshSuccess();
                        } else {
                            listener.onOAuthTokenRefreshFailed();
                        }
                    }
                    return;

                } else if ( rsp instanceof JwtResponseForbidden) {
                    if ( listener != null ) {
                        listener.onOAuthTokenRefreshForbidden();
                    }
                    return;
                }


                if ( listener != null ) {
                    listener.onOAuthTokenRefreshFailed();
                }
            }
        }).execute(refreshRequest);
    }

    public static void logout(Context context) {
        boolean deleteSuccessful = new JwtSharedPreferencesManager().deleteJwt(context);
    }

    public static String getLoggedUsername(Context context) {
        Jwt jwt = new JwtSharedPreferencesManager().getJwt(context);
        if ( jwt == null ) {
            return null;
        }
        return jwt.getUsername();
    }
}
