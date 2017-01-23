package intranet.client.network.builder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import intranet.client.network.model.Announcement;

public class AnnouncementBuilder {
    private final Gson gson = new Gson();

    public List<Announcement> buildAnnouncementsFromJsonString(String jsonString) {
        Type listType = new TypeToken<List<Announcement>>() {}.getType();
        return gson.fromJson(jsonString, listType);
    }

    public Announcement buildAnnouncementFromJsonString(String jsonString) {
        return gson.fromJson(jsonString, Announcement.class);
    }
}
