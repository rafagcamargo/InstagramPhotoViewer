package com.codepath.instagramphotoviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PhotosActivity extends AppCompatActivity {

    private static final String TAG = PhotosActivity.class.getSimpleName();

    private static final String CLIENT_ID = "7465880729f848948f27aa098bcf615e";

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter photosAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photos = new ArrayList<>();

        photosAdapter = new InstagramPhotosAdapter(this, photos);

        StickyListHeadersListView lvPhotos = (StickyListHeadersListView) findViewById(R.id.lvPhotos);

        lvPhotos.setAdapter(photosAdapter);

        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        /*
        - GET /media/popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        - Response:
        - Type: { "data" => [x] => "type" } ("image" or "video")
        - URL: { "data" => [x] => "images" => "standard_resolution" => "url" }
        - Caption: { "data" => [x] => "caption" => "text" }
        - Author Name: { "data" => [x] => "user" => "username" }
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                photosAdapter.clear();
                try {
                    JSONArray photosJson = response.getJSONArray("data");
                    for (int i = 0; i < photosJson.length(); i++) {
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto(photoJson);
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing photos.", e);
                }

                // callback
                photosAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });

    }

}
