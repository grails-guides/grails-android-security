package org.grails.springsecurityrest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.grails.springsecurityrest.client.Jwt;
import org.grails.springsecurityrest.client.JwtImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtSharedPreferencesManager implements JwtStorage {
    private final static String KEY_USERNAME = "jwtUsername";
    private final static String KEY_TOKEN_TYPE = "jwtTokenType";
    private final static String KEY_ACCESS_TOKEN = "jwtAccessToken";
    private final static String KEY_REFRESH_TOKEN = "jwtRefreshToken";
    private final static String KEY_EXPIRES_IN = "jwtExpiresIn";
    private final static String KEY_ROLES = "jwtRoles";

    @Override
    public boolean saveJwt(Context context, Jwt jwt) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, jwt.getUsername());
        editor.putString(KEY_TOKEN_TYPE, jwt.getTokenType());
        editor.putString(KEY_ACCESS_TOKEN, jwt.getAccessToken());
        editor.putString(KEY_REFRESH_TOKEN, jwt.getRefreshToken());
        editor.putInt(KEY_EXPIRES_IN, jwt.getExpiresIn());
        Set<String> uniqueRoles = new HashSet<>();
        uniqueRoles.addAll(jwt.getRoles());
        editor.putStringSet(KEY_ROLES, uniqueRoles);
        return editor.commit();
    }

    @Override
    public boolean deleteJwt(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_TOKEN_TYPE);
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_EXPIRES_IN);
        editor.remove(KEY_ROLES);
        return editor.commit();
    }

    @Override
    public Jwt getJwt(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        JwtImpl jwt = new JwtImpl();
        jwt.setUsername(sharedPreferences.getString(KEY_USERNAME, null));
        jwt.setTokenType(sharedPreferences.getString(KEY_TOKEN_TYPE, null));
        jwt.setAccessToken(sharedPreferences.getString(KEY_ACCESS_TOKEN, null));
        jwt.setRefreshToken(sharedPreferences.getString(KEY_REFRESH_TOKEN, null));
        jwt.setExpiresIn(sharedPreferences.getInt(KEY_EXPIRES_IN, 0));
        jwt.setRoles(jwtRolesAtSharedPreferences(sharedPreferences));
        return jwt;
    }

    private List<String> jwtRolesAtSharedPreferences(SharedPreferences sharedPreferences) {
        Set<String> uniqueRoles = sharedPreferences.getStringSet(KEY_ROLES, null);
        List<String> roles = new ArrayList<>();
        if ( uniqueRoles != null ) {
            roles.addAll(uniqueRoles);
        }
        return roles;
    }


}
