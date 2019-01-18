package com.example.jol.flickr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private ArrayList<PhotoData> photoDataList;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;

    PhotoRecyclerViewAdapter(Context context, ArrayList<PhotoData> data, RecyclerView recyclerView) {
        this.mInflater = LayoutInflater.from(context);
        this.photoDataList = data;
        setHasStableIds(true);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.photo_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = constructURLForPhotoItem(photoDataList.get(position));
        if(holder.imgItem != null) {
            new DownloadPhotoTask(holder.imgItem).execute(url);
        }
    }

    @Override
    public int getItemCount() {
        return photoDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private String constructURLForPhotoItem(PhotoData item) {

        String farmId = item.farm;
        String serverId = item.server;
        String id = String.valueOf(item.id);
        String secret = item.secret;

        return "https://farm" + farmId +
                ".staticflickr.com/" +
                serverId + "/" +
                id +
                "_" +
                secret + ".jpg";

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        ViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }

}