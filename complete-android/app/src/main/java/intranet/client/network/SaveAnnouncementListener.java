package intranet.client.network;

import intranet.client.network.model.Announcement;

public interface SaveAnnouncementListener {
    void onAnnouncementSaveSuccess(Announcement announcement);

    void onAnnouncementSaveFailure();

    void onAnnouncementSaveFailureUnauthorized();
}
