package com.example.jol.flickr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class TabSuchenFragment extends Fragment {

    String searchText;
    SearchMachine searchMachine;
    EditText searchTextInput;

    RecyclerView recyclerView;
    PhotoRecyclerViewAdapter adapter;

    ArrayList<PhotoData> allPhotos = new ArrayList<PhotoData>();
    ArrayList<PhotoData> shortListPhotos = new ArrayList<PhotoData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_suchen_tab, container, false);
        return initializeViewElements(view);
    }

    private View initializeViewElements(final View view) {
        TextView title = view.findViewById(R.id.title);
        title.setText(getString(R.string.suche_title));

        recyclerView = view.findViewById(R.id.recyclerView);
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
                else {
                    resetView(view);
                }

            }
        });
    }

    private void resetView(View view) {
        TextView errorText = view.findViewById(R.id.error_msg);
        errorText.setText(getString(com.example.jol.flickr.R.string.search_input_error_msg));
        if(adapter != null) {
            shortListPhotos.clear();
            adapter.notifyDataSetChanged();
        }
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
                                populatePhotos(allPhotos.photo);
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

    private void populatePhotos(ArrayList<PhotoData> allPhotos) {

        FragmentActivity fragmentActivity = this.getActivity();
        if(fragmentActivity != null) {

            int numberOfColumns = 3;

            recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), numberOfColumns));

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1))
                        onScrolledToBottom();
                }
            });
            if(allPhotos != null && allPhotos.size() > 0) {
                this.allPhotos = allPhotos;
                for(int i = 0; i<9; i++) {
                    shortListPhotos.add(allPhotos.get(i));
                }
            }

            adapter = new PhotoRecyclerViewAdapter(this.getContext(), shortListPhotos, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    private void onScrolledToBottom() {
        int currentSize = shortListPhotos.size();
        if(currentSize < allPhotos.size()) {
            for(int i = currentSize; i< currentSize + 9; i++) {
                shortListPhotos.add(allPhotos.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
