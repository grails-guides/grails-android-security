package intranet.client.network;

public interface SaveAnnouncementJsonStringListener {
    void onAnnouncementJsonStringSaveFailure();

    void onAnnouncementJsonStringSaveSuccess(String json);

    void onAnnouncementJsonStringSaveFailureUnauthorized();
}
