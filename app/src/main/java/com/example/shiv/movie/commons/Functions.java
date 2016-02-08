package com.example.shiv.movie.commons;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.shiv.movie.R;
import com.example.shiv.movie.activity.SettingsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by shiv on 5/2/16.
 *
 * Common functions that are used across the application.
 */
public class Functions {

    public static void onItemSelectedApplicationMenu(MenuItem item, Context context){
        int itemId = item.getItemId();
        if(itemId == R.id.menu_application_settings_item){
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
        }
    }

    public static void inflateApplicationMenu(MenuInflater menuInflater, Menu menu){
        menuInflater.inflate(R.menu.menu_application, menu);
    }

    /**
     * Checks the internet connection of the device and returns true if internet connection is
     * available on the device
     *
     * @param context
     * @return
     */
    public static boolean isInternetEnabled(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Gets the content from a url as a string
     *
     * @param string
     * @return
     */
    public static String getStringFromURL(String string){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(string);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(Strings.HTTP_GET);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            if(stringBuffer.length() == 0){
                return null;
            }else{
                return stringBuffer.toString();
            }
        }catch (IOException e){
            Log.e(Strings.class.toString(), "Error connecting to URL : " + string, e);
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(Strings.class.toString(), "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
