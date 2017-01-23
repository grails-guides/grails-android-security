package intranet.client.network;

import android.content.Context;

import org.grails.springsecurityrest.GrailsApi;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

class NetworkTask {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    static Request requestWithUrl(Context context, String url) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", GrailsApi.authorizationHeaderValue(context))
                .header("Accept-Version", Constants.ACCEPT_VERSION)
                .build();
    }

    static Request postRequestWithUrlAndJson(Context context, String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .header("Authorization", GrailsApi.authorizationHeaderValue(context))
                .header("Accept-Version", Constants.ACCEPT_VERSION)
                .header("Content-Type", "application/json")
                .post(body)
                .build();
    }
}