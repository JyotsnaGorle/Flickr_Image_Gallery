package com.example.jol.flickr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class TabSuchenFragment extends Fragment {

    String searchText;
    String searchMachine;

    EditText searchTextInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_suchen_tab, container, false);
        TextView title = view.findViewById(R.id.title);
        title.setText(getString(R.string.suche_title));
        Button searchButton = view.findViewById(R.id.search_button);
        searchTextInput = view.findViewById(R.id.search_input);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                // check if text for search is there
                searchText = searchTextInput.getText().toString();
                if(searchText != "") {
                    Switch flickrSwitch = view.findViewById(R.id.simpleSwitchFlickr);
                    if(flickrSwitch.isChecked()) {
                        searchMachine = "flickr";
                    }
                }
                callApi();
            }
        });
        return view;
    }

    private void callApi() {
        FlickrRestResponseHandler responseHandler = new FlickrRestResponseHandler();
        try {
            responseHandler.getFlickrImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
