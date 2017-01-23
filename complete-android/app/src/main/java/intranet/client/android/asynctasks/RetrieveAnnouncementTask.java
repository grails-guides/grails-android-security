package intranet.client.android.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import intranet.client.android.delegates.RetrieveAnnouncementDelegate;
import intranet.client.network.AnnouncementFetcher;
import intranet.client.network.FetchAnnouncementListener;
import intranet.client.network.FetchAnnouncementsListener;
import intranet.client.network.model.Announcement;

public class RetrieveAnnouncementTask {
    private static final String TAG = RetrieveAnnouncementTask.class.getSimpleName();
    private AnnouncementFetcher fetcher = new AnnouncementFetcher();
    private RetrieveAnnouncementDelegate delegate;
    private Context context;
    public RetrieveAnnouncementTask(Context context, RetrieveAnnouncementDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void execute(final Long announcementId) {
        new Thread(new Runnable() {
            public void run() {
                fetcher.fetchAnnouncement(context, announcementId, new FetchAnnouncementListener() {

                    @Override
                    public void onAnnouncementFetchFailure() {

                    }

                    @Override
                    public void onAnnouncementFetchSuccess(Announcement announcement) {
                        if ( delegate != null ) {
                            delegate.onAnnouncementFetched(announcement);
                        }
                    }
                });
            }
        }).start();
    }

    protected void onPostExecute(Announcement announcement) {

    }
}
