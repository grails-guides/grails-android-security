package intranet.client.network;

public interface FetchAnnouncementsJsonStringListener {
    void onAnnouncementsJsonStringFetchSuccess(String jsonString);

    void onAnnouncementsJsonStringFetchFailure();
}
