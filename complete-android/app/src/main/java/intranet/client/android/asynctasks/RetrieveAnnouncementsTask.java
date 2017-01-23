package intranet.client.android.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import intranet.client.android.delegates.RetrieveAnnouncementsDelegate;
import intranet.client.network.AnnouncementsFetcher;
import intranet.client.network.FetchAnnouncementsListener;
import intranet.client.network.model.Announcement;

public class RetrieveAnnouncementsTask { //} extends AsyncTask<Void, Void, List<Announcement>> {

    private AnnouncementsFetcher fetcher = new AnnouncementsFetcher();
    private RetrieveAnnouncementsDelegate delegate;

    Context context;
    public RetrieveAnnouncementsTask(Context context, RetrieveAnnouncementsDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void execute() {
        new Thread(new Runnable() {
            public void run() {
                fetcher.fetchAnnouncements(context, new FetchAnnouncementsListener() {
                    @Override
                    public void onAnnouncementsFetchFailure() {
                    }

                    @Override
                    public void onAnnouncementsFetchSuccess(List<Announcement> announcements) {
                        if ( delegate != null ) {
                            delegate.onAnnouncementsFetched(announcements);
                        }
                    }
                });
            }
        }).start();
    }
}
