package org.grails.springsecurityrest;

import android.content.Context;

import org.grails.springsecurityrest.client.Jwt;

public interface JwtStorage {

    Jwt getJwt(Context context);

    /**
     * @return true if the save works, false otherwise.
     */
    boolean saveJwt(Context context, Jwt jwt);

    /**
     * @return true if jwt token was succesfully removed, false otherwise.
     */
    boolean deleteJwt(Context context);
}
