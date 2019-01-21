package com.example.jol.flickr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.NetworkInfo;

public class InternetConnectivityManager {
    public static boolean hasConnection(Context context) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission")
        NetworkInfo wifiNetwork = cm.getNetworkInfo(android.net.ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }
}
