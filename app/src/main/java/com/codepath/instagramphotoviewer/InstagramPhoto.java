package com.codepath.instagramphotoviewer;

import org.json.JSONException;
import org.json.JSONObject;

public class InstagramPhoto {

    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String ID = "id";
    private static final String CAPTION = "caption";
    private static final String TEXT = "text";
    private static final String TYPE = "type";
    private static final String LIKES = "likes";
    private static final String COUNT = "count";
    private static final String IMAGES = "images";
    private static final String STANDARD_RESOLUTION = "standard_resolution";
    private static final String URL = "url";
    private static final String HEIGHT = "height";
    private static final String PROFILE_PICTURE = "profile_picture";
    private static final String LOCATION = "location";
    private static final String NAME = "name";
    private static final String CREATED_TIME = "created_time";

    private String username;
    private long id;
    private String caption;
    private String type;
    private String imageUrl;
    private int imageHeight;
    private int likesCount;
    private String profilePicture;
    private String locationName;
    private long createdTime;

    public InstagramPhoto(JSONObject photoJson) throws JSONException {
        JSONObject userJson = photoJson.getJSONObject(USER);
        username = userJson.getString(USERNAME);
        id = userJson.getLong(ID);
        profilePicture = userJson.getString(PROFILE_PICTURE);

        if (!photoJson.isNull(CAPTION)) {
            caption = photoJson.getJSONObject(CAPTION).optString(TEXT);
        }

        type = photoJson.getString(TYPE);
        likesCount = photoJson.getJSONObject(LIKES).getInt(COUNT);
        createdTime = photoJson.getLong(CREATED_TIME);

        if (!photoJson.isNull(LOCATION)) {
            locationName = photoJson.getJSONObject(LOCATION).getString(NAME);
        }

        JSONObject imageJson = photoJson.getJSONObject(IMAGES).getJSONObject(STANDARD_RESOLUTION);
        imageUrl = imageJson.getString(URL);
        imageHeight = imageJson.getInt(HEIGHT);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLocationName() {
        return locationName;
    }

    public long getCreatedTime() {
        return createdTime * 1000;
    }
}
