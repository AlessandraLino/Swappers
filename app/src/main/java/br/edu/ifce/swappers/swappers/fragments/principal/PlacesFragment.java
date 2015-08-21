package br.edu.ifce.swappers.swappers.fragments.principal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.edu.ifce.swappers.swappers.R;
import br.edu.ifce.swappers.swappers.activities.DetailPlaceActivity;
import br.edu.ifce.swappers.swappers.activities.MainActivity;
import br.edu.ifce.swappers.swappers.model.DistancePlaces;
import br.edu.ifce.swappers.swappers.model.Place;
import br.edu.ifce.swappers.swappers.util.AndroidUtils;
import br.edu.ifce.swappers.swappers.util.PlaceAsyncTask;
import br.edu.ifce.swappers.swappers.util.PlaceInterface;
import br.edu.ifce.swappers.swappers.util.SwappersToast;
import br.edu.ifce.swappers.swappers.webservice.PlaceService;


public class PlacesFragment extends Fragment implements GoogleMap.OnMarkerClickListener,PlaceInterface{

    static GoogleMap mapPlace;
    private final LatLng IFCE_FORTALEZA = new LatLng(-3.744197, -38.535877);
    MapView mapView;
    private Button findPlaceButton;
    private LatLng myPosition;
    private DistancePlaces distancePlaces =null;
    private List<Place> placesNear = new ArrayList<>();
    private int countPlace=0;
    private Listener listener = new Listener();

    public PlacesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        findPlaceButton = (Button) view.findViewById(R.id.find_near_place);
        findPlaceButton.setOnClickListener(findNearPlaceOnMap());

        verifyGpsAndWifi();

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapPlace = mapView.getMap();
        mapPlace.getUiSettings().setMyLocationButtonEnabled(true);
        mapPlace.getUiSettings().setMapToolbarEnabled(true);
        mapPlace.setMyLocationEnabled(true);
        mapPlace.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        MapsInitializer.initialize(this.getActivity());
        eventMarkers();

        return view;
    }

    private void verifyGpsAndWifi(){
        long timeUpdate = 3000;
        float distance = 0;

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distance, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distance, listener);

        Location locationUser = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(locationUser == null){
            Toast toast = SwappersToast.makeText(getActivity(), "Conecte o GPS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            //se o gps estiver desligado, o mapa abrirá em um local previamente definido.
            myPosition = IFCE_FORTALEZA;
        }
        else{
            myPosition = listener.getMyPosition(locationUser);

            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            String city = null;
            String state = null;
            try {
                addresses = gcd.getFromLocation(myPosition.latitude, myPosition.longitude, 1);
                if (addresses.size() > 0){
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(AndroidUtils.isNetworkAvailable(getActivity())) {
                PlaceAsyncTask task = new PlaceAsyncTask(getActivity(), this);
                task.execute(city, state);
            }
        }
    }

    public View.OnClickListener findNearPlaceOnMap(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AndroidUtils.isNetworkAvailable(getActivity()) && !placesNear.isEmpty()) {
                    if (countPlace > placesNear.size() - 1) {
                        countPlace = 0;
                    }
                    LatLng placeNow = new LatLng(placesNear.get(countPlace).getLatitude(), placesNear.get(countPlace).getLongitude());

                    setUpMap(placesNear.get(countPlace), placeNow);
                    showMarker(placeNow, mapPlace);
                    countPlace++;
                }else if(AndroidUtils.isNetworkAvailable(getActivity()) && placesNear.isEmpty()){
                    verifyGpsAndWifi();
                }else{
                    Toast toast = SwappersToast.makeText(getActivity(), "Verifique sua conexão!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        };
    }

    public void showMarker(LatLng position, GoogleMap googleMap){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
    }

    private void setUpMap(Place place, LatLng placeNow) {
        mapPlace.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14));
        mapPlace.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14));

        mapPlace.addMarker(new MarkerOptions().position(placeNow)
                .title(place.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void setUpMarkers(List<Place> placesCity){
        for(int i=0; i<placesCity.size(); i++){
            LatLng coordinate = new LatLng(placesCity.get(i).getLatitude(), placesCity.get(i).getLongitude());
            mapPlace.addMarker(new MarkerOptions().position(coordinate)
                    .title(placesCity.get(i).getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    private void eventMarkers(){
        mapPlace.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            private int tap = 0;
            private LatLng position_1 = new LatLng(0.0, 1.1);
            private LatLng position_2 = new LatLng(1.1, 0.0);

            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("onMarkerClick", String.valueOf(tap));
                tap = tap + 1;

                if (tap % 2 == 1 && !(position_1.equals(position_2))){
                    position_1 = marker.getPosition();
                } else {
                    position_2 = marker.getPosition();
                    if (position_1.equals(position_2)){
                        Intent detailPlaceActivityIntent = new Intent(getActivity(), DetailPlaceActivity.class);
                        startActivity(detailPlaceActivityIntent);
                    } else {
                        position_1 = marker.getPosition();
                    }
                }
                Log.i("onMarkerClick", String.valueOf(tap));

                return false;
            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void updatePlaceNear(List<Place> placeList) {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location locationUser = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(locationUser == null) {
            Toast toast = SwappersToast.makeText(getActivity(), "Conecte o GPS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else {
            Listener listenerUser = new Listener();
            LatLng myCurrentPosition = listenerUser.getMyPosition(locationUser);

            setUpMarkers(placeList);

            if(placeList != null) {
                distancePlaces = new DistancePlaces(placeList);
                placesNear = distancePlaces.calculateNearPlace(myCurrentPosition);

                LatLng placeNow = new LatLng(placesNear.get(countPlace).getLatitude(), placesNear.get(countPlace).getLongitude());

                setUpMap(placesNear.get(countPlace), placeNow);
                showMarker(placeNow, mapPlace);
                countPlace++;
            }else{
                Toast toast = SwappersToast.makeText(getActivity(), "Desculpe-nos! Ainda não há pontos de troca em sua cidade.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

    class Listener implements LocationListener {
        float latitudeUser;
        float longitudeUser;
        LatLng userPosition;
        private int changeZoom = 0;

        @Override
        public void onLocationChanged(Location location){
            latitudeUser = (float)location.getLatitude();
            longitudeUser = (float)location.getLongitude();
            userPosition = new LatLng(latitudeUser, longitudeUser);

            if (changeZoom < 1) {
                PlacesFragment.mapPlace.addMarker(new MarkerOptions().position(userPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_history)));
                PlacesFragment.mapPlace.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 16));
                PlacesFragment.mapPlace.animateCamera(CameraUpdateFactory.zoomTo(14), 500, null);
                changeZoom++;
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras){}

        @Override
        public void onProviderEnabled(String provider){}

        @Override
        public void onProviderDisabled(String provider){}

        public LatLng getMyPosition(Location location){
            float myPositionLatitude = (float) location.getLatitude();
            float myPositionLongitude = (float) location.getLongitude();
            LatLng myPosition = new LatLng(myPositionLatitude, myPositionLongitude);

            return myPosition;
        }
}
