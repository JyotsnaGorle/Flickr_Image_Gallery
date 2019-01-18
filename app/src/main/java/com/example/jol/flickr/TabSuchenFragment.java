package com.example.jol.flickr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class TabSuchenFragment extends Fragment {

    String searchText;
    SearchMachine searchMachine;
    EditText searchTextInput;
    GridView gridView;

    PhotosResponse photosResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_suchen_tab, container, false);
        return initializeViewElements(view);
    }

    private View initializeViewElements(final View view) {
        TextView title = view.findViewById(R.id.title);
        title.setText(getString(R.string.suche_title));
        gridView = view.findViewById(R.id.photo_grid_item);

        searchTextInput = view.findViewById(R.id.search_input);
        Button searchButton = view.findViewById(R.id.search_button);

        handleButtonClick(searchButton, view);
        return view;
    }

    private void handleButtonClick(Button searchButton, final View view) {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                searchText = searchTextInput.getText().toString();
                if(!searchText.equals("")) {
                    Switch flickrSwitch = view.findViewById(R.id.simpleSwitchFlickr);
                    if(flickrSwitch.isChecked()) {
                        searchMachine = SearchMachine.FLICKR;
                    }
                    callApi();
                }
            }
        });
    }

    private void callApi() {
        switch (searchMachine) {
            case FLICKR:
                FlickrRestResponseHandler responseHandler = new FlickrRestResponseHandler();
                try {
                    responseHandler.getFlickrImages(this.getContext(), searchText, new PhotosResponse() {
                        @Override
                        public void onResponseReceived(Photos allPhotos) {
                            Log.d("photos", String.valueOf(allPhotos));
                            if (allPhotos.photo != null && allPhotos.photo.size() > 0) {
                                populatePhotos(allPhotos);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case GOOGLE:
                break;
            case GETTY:
                break;
            default:
                break;
        }

    }

    private void populatePhotos(Photos allPhotos) {
        FragmentActivity fragmentActivity = this.getActivity();
        if(fragmentActivity != null) {
            PhotoGridAdapter adapter = new PhotoGridAdapter(fragmentActivity, allPhotos.photo);
            gridView.setAdapter(adapter);
        }
    }
}
