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
            }
        });
        return view;
    }

}
