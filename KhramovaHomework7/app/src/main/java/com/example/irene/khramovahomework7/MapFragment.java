package com.example.irene.khramovahomework7;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    @BindView(R.id.fragmentBridgeInfo) View fragmentBridgeInfo;
    public static final String TAG_MAP = "Map";
    private static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private ArrayList<Bridge> mBridges = new ArrayList<>();
    private GoogleMap mMap;
    private OnBridgeInfoClick mOnBridgeInfoClick;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Marker mCurrLocationMarker = null;
    private Boolean mRequestingLocationUpdates = false;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        //TODO: getActivity() ?
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BridgeAdapter.OnItemClick) {
            mOnBridgeInfoClick = (OnBridgeInfoClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBridgeInfoClick");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //enableMyLocation();
        //TODO: перемещать камеру сразу в СПб?
        /*LatLng spb = new LatLng(59.93863, 30.31413);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(spb));*/

        addBridgesOnMap();

        mMap.setOnMarkerClickListener(marker -> {
            Bridge bridge = (Bridge) marker.getTag();
            if (bridge != null) {
                //TODO: вынести в класс?
                ImageView imageViewBridge = fragmentBridgeInfo.findViewById(R.id.imageViewBridge);
                ImageView imageViewBell = fragmentBridgeInfo.findViewById(R.id.imageViewBell);
                TextView textViewBridgeName = fragmentBridgeInfo.findViewById(R.id.textViewBridgeName);
                TextView textViewDivorceTime = fragmentBridgeInfo.findViewById(R.id.textViewDivorceTime);

                fragmentBridgeInfo.setBackgroundColor(getResources().getColor(R.color.white));
                textViewBridgeName.setText(bridge.getName());
                textViewDivorceTime.setText(DivorceUtil.getDivorceTime(bridge));
                imageViewBridge.setImageResource(DivorceUtil.getDivorceImgResId(bridge));
                //TODO:
                imageViewBell.setImageResource(R.drawable.ic_kolocol_off);

                fragmentBridgeInfo.setOnClickListener(view -> mOnBridgeInfoClick.onInfoClick(bridge));
            }
            return false;
        });

        mRequestingLocationUpdates = true;
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //enableMyLocation();
                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }
            } else {
                // Permission was denied
                mRequestingLocationUpdates = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationUpdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRequestingLocationUpdates) {
            if(checkPermissions()) {
                startLocationUpdates();
            } else {
                requestPermissions();
            }
        }

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopLocationUpdates();
    }

    public void setBridges(List<Bridge> bridges) {
        mBridges.clear();
        mBridges.addAll(bridges);

        addBridgesOnMap();
    }

    private void addBridgesOnMap() {
        if (mMap != null && !mBridges.isEmpty()) {
            for (Bridge bridge : mBridges) {
                LatLng bridgePosition = new LatLng(bridge.getLat(), bridge.getLng());
                mMap.addMarker(new MarkerOptions()
                        .position(bridgePosition)
                        .icon(BitmapDescriptorFactory.fromResource(DivorceUtil.getDivorceBitmapResId(bridge))))
                        .setTag(bridge);
            }
        }
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.setPosition(latLng);
            } else {
                mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
            }
        }
    }

    /*private void enableMyLocation() {
        if (checkPermissions()) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions();
        }
    }*/

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        //частота обновления
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        //максимальная частота обновления
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        //точность
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                updateUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), locationSettingsResponse -> {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                    updateUI();
                })
                .addOnFailureListener(getActivity(), e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                MapFragment.this.startIntentSenderForResult(resolvable.getResolution().getIntentSender(),
                                        REQUEST_CHECK_SETTINGS, null, 0,
                                        0, 0, null);
                            } catch (IntentSender.SendIntentException sie) {
                                //...
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Toast.makeText(MapFragment.this.getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }

                    MapFragment.this.updateUI();
                });
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private boolean checkPermissions() {
        int permissionState = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
    }

    interface OnBridgeInfoClick {
        void onInfoClick(Bridge bridge);
    }
}
