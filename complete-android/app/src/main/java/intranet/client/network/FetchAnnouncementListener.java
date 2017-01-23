package intranet.client.network;

import java.util.List;

import intranet.client.network.model.Announcement;

public interface FetchAnnouncementListener {
    void onAnnouncementFetchFailure();

    void onAnnouncementFetchSuccess(Announcement object);
}
