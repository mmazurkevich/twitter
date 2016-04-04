package com.example.infinity.twitter.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.infinity.twitter.R;
import com.example.infinity.twitter.ui.activity.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by infinity on 23.09.15.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback{

    private com.google.android.gms.maps.MapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getResources().getString(R.string.location));
        mapFragment = ((com.google.android.gms.maps.MapFragment)((MainActivity)getActivity()).getFragmentManager().findFragmentById(R.id.map_one));
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onDestroy() {
        if(mapFragment != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng pointOne = new LatLng(59.938314, 30.317745);
        LatLng pointTwo = new LatLng(59.927140, 30.309078);
        LatLng pointThree = new LatLng(59.935422, 30.337641);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pointOne, 13));
        map.addMarker(new MarkerOptions()
                .title("First")
                .position(pointOne));
        map.addMarker(new MarkerOptions()
                .title("Second")
                .snippet("Hi")
                .position(pointTwo));
        map.addMarker(new MarkerOptions()
                .title("Third")
                .snippet("Nice to meet you")
                .position(pointThree));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                switch (marker.getTitle()) {
                    case "First":
                        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity)getActivity());
                        builder.setTitle("AlertDialog")
                                .setMessage("A dialog is a small window that")
                                .setCancelable(false)
                                .setNegativeButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    case "Second":
                        Intent intent = new Intent().setAction(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://developers.google.com/"));
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

}
