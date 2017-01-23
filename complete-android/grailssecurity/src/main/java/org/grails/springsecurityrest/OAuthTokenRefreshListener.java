package org.grails.springsecurityrest;

public interface OAuthTokenRefreshListener {
    void onOAuthTokenRefreshFailed();

    void onOAuthTokenRefreshForbidden();

    void onOAuthTokenRefreshSuccess();
}
