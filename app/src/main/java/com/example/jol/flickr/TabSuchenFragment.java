package com.example.jol.flickr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;
import static com.example.jol.flickr.SearchMachine.FLICKR;

public class TabSuchenFragment extends Fragment {

    String searchText;

    EditText searchTextInput;

    TextView errorMessageSearchMachine;
    TextView noImageTextHolder;
    RecyclerView recyclerView;
    PhotoRecyclerViewAdapter adapter;

    AVLoadingIndicatorView loadingIndicatorView;

    ArrayList<PhotoData> allPhotos = new ArrayList<>();
    ArrayList<PhotoData> shortListPhotos = new ArrayList<>();

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

        loadingIndicatorView = view.findViewById(R.id.avi);

        searchTextInput = view.findViewById(R.id.search_input);
        if(searchTextInput!=null) {
            handleTextInputChange(view);
        }

        noImageTextHolder = view.findViewById(R.id.no_images);
        Button searchButton = view.findViewById(R.id.search_button);

        handleButtonClick(searchButton, view);
        return view;
    }

    private void handleTextInputChange(final View view) {
        searchTextInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0 && adapter != null) {
                    Log.d(TAG, "onTextChanged: empty");
                    shortListPhotos.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }
        });

        searchTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "onEditorAction: enter pressed");
                    performSearch(view);
                    return true;
                }
                return false;
            }
        });
    }

    private void handleButtonClick(Button searchButton, final View view) {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                performSearch(view);

            }
        });
    }

    private void performSearch(View view) {
        resetView();
        searchText = searchTextInput.getText().toString();
        KeyboardManager.hideKeyboard(getActivity());
        TextView errorText = view.findViewById(R.id.error_msg);
        if(!searchText.equals("")) {
            errorText.setVisibility(View.INVISIBLE);
            SearchMachine searchMachine = setSearchMachine(view);
            if(searchMachine != null)
                callApi(searchMachine);
        }
        else if (errorText != null) {
            errorText.setText(getString(com.example.jol.flickr.R.string.search_input_error_msg));
            errorText.setVisibility(View.VISIBLE);
        }
    }

    private SearchMachine setSearchMachine(View view) {
        SearchMachine searchMachine = null;
        Switch flickrSwitch = view.findViewById(R.id.simpleSwitchFlickr);
        Switch googleSwitch = view.findViewById(R.id.simpleSwitchGoogle);
        Switch gettySwitch = view.findViewById(R.id.simpleSwitchGetty);
        errorMessageSearchMachine = view.findViewById(R.id.error_msg_search_machines);
        if(!flickrSwitch.isChecked() && !googleSwitch.isChecked() && !gettySwitch.isChecked()) {
            AlertDialogManager.showAlert(getContext(), "Search Machine","turn on least one search engine");
        }
        else if(!flickrSwitch.isChecked()) {
            AlertDialogManager.showAlert(getContext(), "Search Machine","Please select Flickr as the other services are not available");
        }
        else if(flickrSwitch.isChecked()) {
            searchMachine = FLICKR;
            if(googleSwitch.isChecked() || gettySwitch.isChecked())
                errorMessageSearchMachine.setText(getString(R.string.search_machine_error));
        }
        return searchMachine;
    }

    private void resetView() {
        if (errorMessageSearchMachine != null)
            errorMessageSearchMachine.setText("");
        if(adapter != null) {
            shortListPhotos.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void callApi(SearchMachine searchMachine) {
        boolean hasConnection = InternetConnectivityManager.hasConnection(getContext());
        if(hasConnection) {
            makeCall(searchMachine);
        } else {
            AlertDialogManager.showAlert(getContext(), "NO INTERNET!","Please check your connection and try again");
        }

    }

    private void makeCall(SearchMachine searchMachine) {
        switch (searchMachine) {
            case FLICKR:
                FlickrRestResponseHandler responseHandler = new FlickrRestResponseHandler();
                try {
                    startLoader(true);
                    responseHandler.getFlickrImages(this.getContext(), searchText, new PhotosResponse() {
                        @Override
                        public void onResponseReceived(Photos allPhotos) {
                            Log.d("photos", String.valueOf(allPhotos));
                            startLoader(false);
                            if (allPhotos.photo != null && allPhotos.photo.size() > 0) {
                                populatePhotos(allPhotos.photo);
                            }
                            else {
                                if(noImageTextHolder != null) {
                                    noImageTextHolder.setText("No results");
                                }
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } catch (IOException e) {
                    startLoader(false);
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

    private void startLoader(boolean state) {
        if(loadingIndicatorView != null) {
            if (state) {
                loadingIndicatorView.show();
            } else {
                loadingIndicatorView.hide();
            }
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
        if(currentSize < allPhotos.size() - 9) {
            for(int i = currentSize; i< currentSize + 9; i++) {
                shortListPhotos.add(allPhotos.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
