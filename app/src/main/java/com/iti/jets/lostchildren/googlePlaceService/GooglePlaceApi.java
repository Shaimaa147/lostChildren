package com.iti.jets.lostchildren.googlePlaceService;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Shemo on 6/18/2018.
 */

public class GooglePlaceApi implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, AdapterView.OnItemClickListener {

    private static GooglePlaceApi googlePlaceApi;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static Context context;
    private static final String LOG_TAG = "GOOglePlaceApiClass";

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW =
            new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    public PlaceArrayAdapter getmPlaceArrayAdapter() {
        return mPlaceArrayAdapter;
    }

    private GooglePlaceApi(Context context){
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Places.GEO_DATA_API)
                .enableAutoManage((AppCompatActivity) context, GOOGLE_API_CLIENT_ID, this).addConnectionCallbacks(this).build();
        mPlaceArrayAdapter = new PlaceArrayAdapter((AppCompatActivity) context, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);

    }

    public static synchronized GooglePlaceApi getInstance(Context _context) {
        if (googlePlaceApi == null) {
            context = _context;
            googlePlaceApi = new GooglePlaceApi(context);
        }
        return googlePlaceApi;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(context,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final  PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        Log.i(LOG_TAG, "Selected: " + item.description);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            if (attributions != null) {
                Log.i(LOG_TAG, "attributions: " + Html.fromHtml(attributions.toString()));
            }
        }
    };
}
