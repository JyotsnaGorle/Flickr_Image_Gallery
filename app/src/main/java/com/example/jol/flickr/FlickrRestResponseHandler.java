package com.example.jol.flickr;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class FlickrRestResponseHandler {
    public void getFlickrImages() throws IOException {
        RequestParams params = new RequestParams();
        params.put("method", "flickr.photos.search");
        params.put("api_key", "766733a614a9c15a873ac2b130ef2db7");
        params.put("format", "json");
        params.put("text", "cat");
        params.put("nojsoncallback", "1");

        FlickrRestClient.get("",params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
