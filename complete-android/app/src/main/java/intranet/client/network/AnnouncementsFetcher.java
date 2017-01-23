package intranet.client.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.grails.springsecurityrest.GrailsApi;
import org.grails.springsecurityrest.OAuthTokenRefreshListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import intranet.client.network.builder.AnnouncementBuilder;
import intranet.client.network.model.Announcement;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnnouncementsFetcher {

    private final static String TAG = AnnouncementsFetcher.class.getSimpleName();

    private OkHttpClient client = new OkHttpClient();
    private AnnouncementBuilder builder = new AnnouncementBuilder();

    public void fetchAnnouncements(Context context, final FetchAnnouncementsListener listener) {

        String url = Constants.GRAILS_APP_URL + Constants.ANNOUNCEMENTS_PATH;
        fetchAnnouncementsJsonString(context, url, new FetchAnnouncementsJsonStringListener() {
            @Override
            public void onAnnouncementsJsonStringFetchSuccess(String jsonString) {
                if ( listener != null) {
                    List<Announcement> announcements = builder.buildAnnouncementsFromJsonString(jsonString);
                    listener.onAnnouncementsFetchSuccess(announcements);
                }
            }

            @Override
            public void onAnnouncementsJsonStringFetchFailure() {
                if ( listener != null) {
                    listener.onAnnouncementsFetchFailure();
                }
            }
        }, 1);
    }

    private void fetchAnnouncementsJsonString(final Context context, final String url, final FetchAnnouncementsJsonStringListener listener, int unAuthorizedRetryAttempts)  {
        final Request request = NetworkTask.requestWithUrl(context, url);

        try {
            Response response = client.newCall(request).execute();

            if ( response.code() == 200) {
                if (listener != null) {
                    listener.onAnnouncementsJsonStringFetchSuccess(response.body().string());
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
                listener.onAnnouncementsJsonStringFetchFailure();
            }

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getLocalizedMessage());
            if ( listener!=null ) {
                listener.onAnnouncementsJsonStringFetchFailure();
            }
        }
    }


    private OAuthTokenRefreshListener oAuthTokenRefreshListener(final Context context, final String url, final FetchAnnouncementsJsonStringListener listener) {
        return new OAuthTokenRefreshListener() {
            @Override
            public void onOAuthTokenRefreshFailed() {
                if ( listener!=null ) {
                    listener.onAnnouncementsJsonStringFetchFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshForbidden() {
                if ( listener!=null ) {
                    listener.onAnnouncementsJsonStringFetchFailure();
                }
            }

            @Override
            public void onOAuthTokenRefreshSuccess() {
                fetchAnnouncementsJsonString(context, url, listener, 0);
            }
        };
    }



}
