package inc.appscode0.landvaluationapplication.nearme;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.internal.zzd;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

import inc.appscode0.landvaluationapplication.MainActivity;
import inc.appscode0.landvaluationapplication.R;

public class SearchLocbyType extends AppCompatActivity implements LocationListener {
    public static LatLng ltln;
    final String TAG = "TAG";
    Double latitude;
    Double longtitude;
    String nearby;
    Location location;
    ListView listView;
    Button button;

String json_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportActionBar().setTitle("Nearby amenities!");

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(json_data.isEmpty())
                {
                    Toast.makeText(SearchLocbyType.this, "Locations are empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(SearchLocbyType.this, MainActivity.class);
                    intent.putExtra("json_data", json_data);
                    startActivity(intent);
                    SearchLocbyType.this.finish();
                }



            }
        });


        listView = (ListView) findViewById(R.id.list_item);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
                String place_type = map.get("type");
                String distance = map.get("distance");


            }
        });


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        } else {
            showGPSDisabledAlertToUser();
        }


        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            //Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            longtitude = location.getLongitude();
            latitude = location.getLatitude();

            String url = getUrl(latitude, longtitude, "");
            fetchdata(url);

            // Log.d(TAG, stlatitude+stlongitude);

        } catch (Exception a) {
            try {

                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                longtitude = location.getLongitude();
                latitude = location.getLatitude();

                String url = getUrl(latitude, longtitude, "");
                fetchdata(url);

            } catch (Exception ex) {
            }


        }


    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        SearchLocbyType.this.finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void return_cordinates(final String adress) {

        final ProgressDialog progressDialog = new ProgressDialog(SearchLocbyType.this);
        progressDialog.setMessage("Fetching coordinates for " + adress);
        progressDialog.setCancelable(false);
        progressDialog.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Geocoder geocoder = new Geocoder(SearchLocbyType.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(adress, 1);
                    if (addresses.size() > 0) {
                        latitude = addresses.get(0).getLatitude();
                        longtitude = addresses.get(0).getLongitude();
                        // Toast.makeText(this, String.valueOf(longitude).substring(0,4), Toast.LENGTH_SHORT).show();
                        String url = getUrl(latitude, longtitude, nearby);
                        progressDialog.dismiss();
                        fetchdata(url);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SearchLocbyType.this, "Location not found!", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + "10000");
        googlePlacesUrl.append("&type=school|hospital|university|park|museum|stadium");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCyG976e6EXwSj8BS2jrfmOym0CJ6OcsC8");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public void fetchdata(final String url_data) {
        class GetNearbyPlacesData extends AsyncTask<Void, Void, String> {
            String googlePlacesData;
            GoogleMap mMap;
            String url;
            ProgressDialog progressDialog = new ProgressDialog(SearchLocbyType.this);


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Searching nearby places");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Log.d("GetNearbyPlacesData", "doInBackground entered");
                    //  mMap = (GoogleMap) params[0];
                    url = url_data;
                    DownloadUrl downloadUrl = new DownloadUrl();
                    googlePlacesData = downloadUrl.readUrl(url);
                    Log.d("GooglePlacesReadTask", "doInBackground Exit");
                } catch (Exception e) {
                    Log.d("GooglePlacesReadTask", e.toString());
                }
                return googlePlacesData;
            }

            @Override
            protected void onPostExecute(String result) {
                //Toast.makeText(SearchLocbyType.this, result, Toast.LENGTH_SHORT).show();
                Log.d(TAG, result);
                //new AlertDialog.Builder(SearchLocbyType.this).setMessage(result).show();
                progressDialog.dismiss();
                Log.d("GooglePlacesReadTask", "onPostExecute Entered");
                List<HashMap<String, String>> nearbyPlacesList = null;
                DataParser dataParser = new DataParser();
                nearbyPlacesList = dataParser.parse(result);
                ShowNearbyPlaces(nearbyPlacesList);
                Log.d("GooglePlacesReadTask", "onPostExecute Exit");
            }

            private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList)
            {

                JSONArray jsonArray1 = new JSONArray();


                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < nearbyPlacesList.size(); i++)
                {
                    HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    String place_type= googlePlace.get("types");
                    String location="";
                    try
                    {
                         JSONArray jsonArray = new JSONArray(place_type);
                         location = jsonArray.getString(0);
                    } catch (JSONException e)
                    {
                        Toast.makeText(SearchLocbyType.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                    LatLng latLngA = new LatLng(latitude, longtitude);
                    LatLng latLngB = new LatLng(lat, lng);
                    Location locationA = new Location("point A");
                    locationA.setLatitude(latLngA.latitude);
                    locationA.setLongitude(latLngA.longitude);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(latLngB.latitude);
                    locationB.setLongitude(latLngB.longitude);
                    double distance = locationA.distanceTo(locationB);
                    HashMap<String, String> employees = new HashMap<>();
                    ltln = new LatLng(lat, lng);


                    JSONObject jsonObjOne = new JSONObject();
                    try
                    {
                        jsonObjOne.put("type", location);
                        jsonObjOne.put("distance", distance);
                        jsonArray1.put(jsonObjOne);

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                    employees.put("place_name", placeName);
                    employees.put("lat", String.valueOf(lat));
                    employees.put("lng", String.valueOf(lng));
                    employees.put("latlng", String.valueOf(lng) + "," + String.valueOf(lat));
                    employees.put("vicinity", vicinity);
                    employees.put("distance", String.valueOf(distance));
                    employees.put("type", location);
                    employees.put("json_data", String.valueOf(jsonArray1));
                    json_data=String.valueOf(jsonArray1);




//                    Intent intent = new Intent(SearchLocbyType.this, MainActivity.class);
//                    intent.putExtra("place_type", place_type);
//                    intent.putExtra("distance", distance);
//                    intent.putExtra("json_data", json_data);
//                    startActivity(intent);

                    list.add(employees);


                }


                ListAdapter adapter = new SimpleAdapter(SearchLocbyType.this, list, R.layout.listviewlist,
                        new String[]{
                                "place_name",
                                "latlng",
                                "type",
                                "distance"
                        }, new int[]{
                        R.id.textView5,
                        R.id.textView10,
                        R.id.textView9,
                        R.id.textView12});
                listView.setAdapter(adapter);

            }
        }
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute();


    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = (location.getLatitude());
        longtitude = location.getLongitude();
        Toast.makeText(this, String.valueOf(latitude) + " " + String.valueOf(longtitude), Toast.LENGTH_SHORT).show();
        String url = getUrl(latitude, longtitude, "");
        fetchdata(url);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(this, "Status chnaged", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Status chnaged", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Status chnaged", Toast.LENGTH_SHORT).show();
    }

}
