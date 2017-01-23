package intranet.client.network;

import java.util.List;

import intranet.client.network.model.Announcement;

public interface FetchAnnouncementsListener {

    void onAnnouncementsFetchFailure();

    void onAnnouncementsFetchSuccess(List<Announcement> objects);
}
