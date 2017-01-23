package intranet.client.android.delegates;

public interface CreateAnnouncementTaskDelegate {
    void onAnnouncementCreatedSuccess();
    void onAnnouncementCreatedUnauthorized();
    void onAnnouncementCreatedFailed();
}
