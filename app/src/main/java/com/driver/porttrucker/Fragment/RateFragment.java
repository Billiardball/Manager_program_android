package com.driver.porttrucker.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;
import com.driver.porttrucker.CustomView.CustomMapView;
import com.driver.porttrucker.Utils.CalculateDistanceTime;
import com.driver.porttrucker.Utils.LogUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.driver.porttrucker.Base.BaseFragment;
import com.driver.porttrucker.R;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RateFragment extends BaseFragment implements OnMapReadyCallback, DirectionCallback {

    View fragment;
    CustomMapView mapView;

    RateFragment instance;

    PlacesAutocompleteTextView txvPickupAddress, txvDropAddress;
    TextView txvDistance, txvTime, txvPrice, txvFinalPrice;
    Button btnSearch;
    EditText edtTolls;

    GoogleMap mGoogleMap;
    LatLng fromPlace, toPlace;
    private String[] colors = {"#06B5FA", "#06B5FA", "#06B5FA"};
    int price;

    public RateFragment() { }

    public static RateFragment newInstance(){
        RateFragment newInstance = new RateFragment();
        return  newInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.frm_rate, parent, false);

        txvFinalPrice = (TextView)fragment.findViewById(R.id.txvFinalPrice);

        edtTolls = (EditText)fragment.findViewById(R.id.edtTolls);
        edtTolls.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                int tolls = Integer.valueOf(edtTolls.getText().toString().trim());
                txvFinalPrice.setText("$ " + String.valueOf(price + tolls));
            }
        });

        txvPrice = (TextView)fragment.findViewById(R.id.txvPrice);

        btnSearch = (Button)fragment.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDirection();
            }
        });

        txvDistance = (TextView)fragment.findViewById(R.id.txvDistance);
        txvTime = (TextView)fragment.findViewById(R.id.txvTime);

        txvPickupAddress = (PlacesAutocompleteTextView)fragment.findViewById(R.id.txvPickupAddress);
        txvPickupAddress.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                txvPickupAddress.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        fromPlace = new LatLng(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);
                        if (txvDropAddress.length()> 0){
                            showDirection();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showToast("Can't find location info");
                    }
                });
            }
        });

        txvDropAddress = (PlacesAutocompleteTextView)fragment.findViewById(R.id.txvDropAddress);
        txvDropAddress.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                txvDropAddress.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        toPlace = new LatLng(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);
                        if (txvPickupAddress.length() > 0){
                            showDirection();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showToast("Can't find location info");
                    }
                });
            }
        });

        mapView = (CustomMapView)fragment.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng myLocation = new LatLng(Common.lat, Common.lng);

        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions().position(myLocation));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));
    }

    private void showDirection(){
        GoogleDirection.withServerKey(Constant.MAP_API_KEY)
                .from(fromPlace)
                .to(toPlace)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            mGoogleMap.addMarker(new MarkerOptions().position(fromPlace));
            mGoogleMap.addMarker(new MarkerOptions().position(toPlace));

            Route route = direction.getRouteList().get(0);
            String color = colors[0 % colors.length];
            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            mGoogleMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, Color.parseColor(color)));

            setCameraWithCoordinationBounds(direction.getRouteList().get(0));

            getDistanceTime();
        }
    }

    private void getDistanceTime() {
        CalculateDistanceTime distance_task = new CalculateDistanceTime(context);

        distance_task.getDirectionsUrl(fromPlace, toPlace);

        distance_task.setLoadListener(new CalculateDistanceTime.taskCompleteListener() {
            @Override
            public void taskCompleted(String[] time_distance) {
                txvTime.setText(time_distance[1]);
                txvDistance.setText(time_distance[0]);

                price = Integer.valueOf(time_distance[0].replaceAll("\\D+","")) * 5;
                txvPrice.setText("$ " + String.valueOf(price));

                hideSoftKeyBoard();
            }
        });
    }
/*
    private String extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }*/

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
