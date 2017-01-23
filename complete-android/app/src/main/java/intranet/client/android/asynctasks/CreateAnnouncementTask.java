package intranet.client.android.asynctasks;

import android.content.Context;

import intranet.client.android.delegates.CreateAnnouncementTaskDelegate;
import intranet.client.network.AnnouncementNetworkSave;
import intranet.client.network.SaveAnnouncementListener;
import intranet.client.network.model.Announcement;

public class CreateAnnouncementTask {

    private AnnouncementNetworkSave networkSave = new AnnouncementNetworkSave();
    private CreateAnnouncementTaskDelegate delegate;

    Context context;
    public CreateAnnouncementTask(Context context, CreateAnnouncementTaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void execute(final String title, final String message) {
        new Thread(new Runnable() {
            public void run() {

                networkSave.saveAnnouncement(context, title, message, new SaveAnnouncementListener() {
                    @Override
                    public void onAnnouncementSaveSuccess(Announcement announcement) {
                        if ( delegate != null ) {
                            delegate.onAnnouncementCreatedSuccess();
                        }
                    }

                    @Override
                    public void onAnnouncementSaveFailure() {
                        if ( delegate != null ) {
                            delegate.onAnnouncementCreatedFailed();
                        }
                    }

                    @Override
                    public void onAnnouncementSaveFailureUnauthorized() {
                        if ( delegate != null ) {
                            delegate.onAnnouncementCreatedUnauthorized();
                        }
                    }


                });
            }
        }).start();
    }
}
