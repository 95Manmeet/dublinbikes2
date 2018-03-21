package manmeet.training.com.dublinbikes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //TODO: Put all constants here:
    private GoogleMap mMap;
    final int REQUEST_CODE = 000;
    long MIN_TIME = 5000;
    float MIN_DISTANCE = 1000;
    final String DUBLIN_BIKE_URL = "https://api.jcdecaux.com/vls/v1/stations"; //App ID to use Dublin Bike URL
    final String APP_ID = "8f3aacc1264970aa54e3942f640091a1f35d1f7a ";


    //TODO: Set Location Provider below:
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;


    //TODO: Adding LocationManager and LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //TODO: By Adding onRequestPermissionsResult(), We can see what happen if we press allow or Denied button mentioned on screen

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            Log.d("Dublin Bikes", "Permission Granted..!!!");
        }

        else
        {
            Log.d("Dublin Bikes", "Permission Denied.");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //////////////////////////////////////

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                // Add a marker in Ireland and move the camera
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Smithfield").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));


                Log.d("Dublin Bikes", " onLocationChanged() callback received");

                //Adding parameters for dublin bikes......

                RequestParams params = new RequestParams();
                params.put("api key", APP_ID);
                networking(params);           //calling networking method here.....


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Log.d("Dublin Bikes", "onProviderDisabled() callback received");

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);


        // TODO: Setting the last location of user.....
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());



        mMap.clear(); // It will clear the last location of user.
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your last location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

        }

/////////////////////passing the parameters in networking method......

        private void networking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(DUBLIN_BIKE_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){  // if successfull give us response 200.
                Log.d("dublin bike", "Success! Json" + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){ // if failure show us toast message..
                Log.e("dublin bike", "Fail" + e.toString());
                Log.d("dublin bike", "status code" + statusCode);
                Toast.makeText(MapsActivity.this, "request failed", Toast.LENGTH_SHORT).show();
            }
        });
        }


    }

