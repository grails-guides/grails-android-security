package intranet.client.network;

public interface FetchAnnouncementJsonStringListener {

    void onAnnouncementJsonStringFetchSuccess(String jsonString);

    void onAnnouncementJsonStringFetchFailure();
}
