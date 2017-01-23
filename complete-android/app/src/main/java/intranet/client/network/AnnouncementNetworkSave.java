package intranet.client.network;

import android.content.Context;
import android.util.Log;

import org.grails.springsecurityrest.GrailsApi;
import org.grails.springsecurityrest.OAuthTokenRefreshListener;

import java.io.IOException;

import intranet.client.network.builder.AnnouncementBuilder;
import intranet.client.network.model.Announcement;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnnouncementNetworkSave {
    private OkHttpClient client = new OkHttpClient();
    private AnnouncementBuilder builder = new AnnouncementBuilder();

    public AnnouncementNetworkSave() {
    }

    public void saveAnnouncement(Context context, String title, String message, final SaveAnnouncementListener listener) {

        String url = Constants.GRAILS_APP_URL + Constants.ANNOUNCEMENTS_PATH;
        saveAnnouncementJsonString(context, url, title, message, new SaveAnnouncementJsonStringListener() {
            @Override
            public void onAnnouncementJsonStringSaveFailure() {
                if ( listener != null) {
                    listener.onAnnouncementSaveFailure();
                }
            }

            @Override
            public void onAnnouncementJsonStringSaveSuccess(String json) {
                if ( listener != null) {
                    Announcement announcement = builder.buildAnnouncementFromJsonString(json);
                    listener.onAnnouncementSaveSuccess(announcement);
                }
            }

            @Override
            public void onAnnouncementJsonStringSaveFailureUnauthorized() {
                if ( listener != null) {
                    listener.onAnnouncementSaveFailureUnauthorized();
                }
            }
        }, 1);
    }

    private static String jsonWithTitleAndMessage(String title, String message) {
        return "{\"title\":\""+title+"\",\"body\":\""+message+"\"}";
    }

    private void saveAnnouncementJsonString(final Context context, final String url, final String title, String message, final SaveAnnouncementJsonStringListener listener, int unAuthorizedRetryAttempts)  {
        final String json = jsonWithTitleAndMessage(title, message);
        final Request request = NetworkTask.postRequestWithUrlAndJson(context, url, json);

        try {
            Response response = client.newCall(request).execute();

            if ( response.code() == 201) {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringSaveSuccess(response.body().string());
                }
                return;
            }

            if ( response.code() == 401 && unAuthorizedRetryAttempts > 0 ) {
                if (GrailsApi.shouldTryToRefreshAccessToken(context)) {
                    GrailsApi.refreshAccessToken(context, Constants.GRAILS_APP_URL, oAuthTokenRefreshListener(context, title, message, url, listener));
                    return;
                }
            }

            if ( response.code() == 403 ) {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringSaveFailureUnauthorized();
                }
                return;
            }

            if ( listener!=null ) {
                listener.onAnnouncementJsonStringSaveFailure();
            }

        } catch (IOException e) {
            if ( listener!=null ) {
                listener.onAnnouncementJsonStringSaveFailure();
            }
        }
    }

    private OAuthTokenRefreshListener oAuthTokenRefreshListener(final Context context, final String url, final String title, final String message, final SaveAnnouncementJsonStringListener listener) {
        return new OAuthTokenRefreshListener() {
            @Override
            public void onOAuthTokenRefreshFailed() {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringSaveFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshForbidden() {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringSaveFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshSuccess() {
                saveAnnouncementJsonString(context, url, title, message, listener, 0);
            }
        };
    }
}
