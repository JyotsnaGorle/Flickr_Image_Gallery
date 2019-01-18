package com.example.jol.flickr;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class FlickrRestResponseHandler {
    private Photos allPhotos;

    public void getFlickrImages(Context context, String searchText, final PhotosResponse photosResponse) throws IOException {
        RequestParams params = new RequestParams();
        params.put("method", "flickr.photos.search");
        params.put("api_key", context.getString(R.string.api_key));
        params.put("format", "json");
        params.put("text", searchText);
        params.put("nojsoncallback", "1");


        FlickrRestClient.get("",params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String status = String.valueOf(response.get("stat"));
                    if (!("ok".equals(status))) {
                        return;
                    }
                    Photos allPhotos = new Gson().fromJson(response.get("photos").toString(),Photos.class);
                    photosResponse.onResponseReceived(allPhotos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("response failure", String.valueOf(statusCode));
            }
        });
    }
}
