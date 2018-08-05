package com.example.irene.khramovahomework7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG_MAP = "Map";
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private ArrayList<Bridge> mBridges = new ArrayList<>();
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        //ButterKnife.bind(this, rootView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        enableMyLocation();

        /*LatLng spb = new LatLng(59.93863, 30.31413);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(spb));*/

        addBridgesOnMap();

        mMap.setOnMarkerClickListener(marker -> {
            Bridge bridge = (Bridge) marker.getTag();
            if(bridge != null) {
                //TODO: fragment with info
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    public void setBridges(List<Bridge> bridges) {
        mBridges.clear();
        mBridges.addAll(bridges);

        addBridgesOnMap();
    }

    private void addBridgesOnMap() {
        if(mMap != null && !mBridges.isEmpty()) {
            for (Bridge bridge : mBridges) {
                LatLng bridgePosition = new LatLng(bridge.getLat(), bridge.getLng());
                mMap.addMarker(new MarkerOptions()
                        .position(bridgePosition)
                        .icon(BitmapDescriptorFactory.fromResource(DivorceUtil.getDivorceBitmapResId(bridge))))
                        .setTag(bridge);
            }
        }
    }

    private void enableMyLocation() {
        //TODO: change marker icon
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }
}
