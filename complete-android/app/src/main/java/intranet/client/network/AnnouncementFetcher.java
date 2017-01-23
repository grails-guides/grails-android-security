package intranet.client.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.grails.springsecurityrest.GrailsApi;
import org.grails.springsecurityrest.OAuthTokenRefreshListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import intranet.client.network.builder.AnnouncementBuilder;
import intranet.client.network.model.Announcement;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnnouncementFetcher {
    private static final String TAG = AnnouncementFetcher.class.getSimpleName();
    private OkHttpClient client = new OkHttpClient();
    private AnnouncementBuilder builder = new AnnouncementBuilder();

    public AnnouncementFetcher() {
    }

    public void fetchAnnouncement(Context context, Long announcementId, final FetchAnnouncementListener listener) {

        String url = Constants.GRAILS_APP_URL + Constants.ANNOUNCEMENTS_PATH + "/" + announcementId;
        fetchAnnouncementJsonString(context, url, new FetchAnnouncementJsonStringListener() {
            @Override
            public void onAnnouncementJsonStringFetchSuccess(String jsonString) {
                if ( listener != null) {
                    Announcement announcement = builder.buildAnnouncementFromJsonString(jsonString);
                    listener.onAnnouncementFetchSuccess(announcement);
                }
            }

            @Override
            public void onAnnouncementJsonStringFetchFailure() {
                if ( listener != null) {
                    listener.onAnnouncementFetchFailure();
                }
            }
        }, 1);
    }

    private void fetchAnnouncementJsonString(final Context context, final String url, final FetchAnnouncementJsonStringListener listener, int unAuthorizedRetryAttempts)  {

        final Request request = NetworkTask.requestWithUrl(context, url);

        try {
            Response response = client.newCall(request).execute();

            if ( response.code() == 200) {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringFetchSuccess(response.body().string());
                }
                return;
            }

            if ( response.code() == 401 && unAuthorizedRetryAttempts > 0 ) {
                if (GrailsApi.shouldTryToRefreshAccessToken(context)) {
                    GrailsApi.refreshAccessToken(context, Constants.GRAILS_APP_URL, oAuthTokenRefreshListener(context, url, listener));
                    return;
                }
            }

            if ( listener!=null ) {
                listener.onAnnouncementJsonStringFetchFailure();
            }

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getLocalizedMessage());
            if ( listener!=null ) {
                listener.onAnnouncementJsonStringFetchFailure();
            }
        }
    }

    private OAuthTokenRefreshListener oAuthTokenRefreshListener(final Context context, final String url, final FetchAnnouncementJsonStringListener listener) {
        return new OAuthTokenRefreshListener() {
            @Override
            public void onOAuthTokenRefreshFailed() {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringFetchFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshForbidden() {
                if ( listener!=null ) {
                    listener.onAnnouncementJsonStringFetchFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshSuccess() {
                fetchAnnouncementJsonString(context, url, listener, 0);
            }
        };
    }
}
