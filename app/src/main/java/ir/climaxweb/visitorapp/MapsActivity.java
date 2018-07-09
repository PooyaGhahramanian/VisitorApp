package ir.climaxweb.visitorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wiadevelopers.com.directionlib.DirectionCallback;
import wiadevelopers.com.directionlib.GoogleDirection;
import wiadevelopers.com.directionlib.model.Direction;
import wiadevelopers.com.directionlib.model.RouteInfo;

public class MapsActivity extends RuntimePermissionsActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int Location_Permision_Request_Code = 1;
    private LatLng mylatlang;
    public Button Agreq, pathhreq ;
    private boolean flag  = false ;
    private ArrayList<LatLng> latLngsha = new ArrayList<>();
    private ArrayList<RouteInfo> routeInfos = new ArrayList<>();
    private  Polyline mypolyline ;
    private LocationManager locationManager;
    LatLng tehran = new LatLng(35.720775, 51.415228);


    Agent a;
    private List<Agent> agentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Agreq    = (Button)findViewById(R.id.btnAgentReq);
        pathhreq = (Button)findViewById(R.id.btnPathReq);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tehran, 13), 3000, null );

        MapsActivity.super.requestAppPermissions(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}
                , Location_Permision_Request_Code);

    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {

        if (requestCode == Location_Permision_Request_Code)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);

//mreza
              locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
              Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
              getmyLocation(location);
//mreza

       Agreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agentList=new ArrayList<Agent>();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tehran, 11), 3000, null );
                getAgents getA=new getAgents();
                getA.execute();


            }
        });
       pathhreq.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   direction(latLngsha);
                   Toast.makeText(MapsActivity.this, "please wait ", Toast.LENGTH_SHORT).show();
                   latLngsha.clear();
               }catch (Exception e){
                   Toast.makeText(getBaseContext(),"Bad Requset", Toast.LENGTH_LONG).show();
               }
           }
       });

    }

    @Override
    public void onPermissionsDeny(int requestCode) {

        finish();

    }
    class getAgents extends AsyncTask<Void, Void, String> {

        //ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar = (ProgressBar) findViewById(R.id.AgentsProgressBar);
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);

            try {
                if (s == "") {
                    Toast.makeText(getApplicationContext(), "no agents available !", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray arr = new JSONArray(s);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(), Integer.toString(obj.getInt("Id")), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                        a = new Agent(
                                obj.getInt("Id"),
                                obj.getString("Name"),
                                obj.getString("Address"),
                                obj.getDouble("Lat"),
                                obj.getDouble("Lng")
                        );
                        agentList.add(a);
                        Toast.makeText(getApplicationContext(), Integer.toString(agentList.size()), Toast.LENGTH_SHORT).show();
                        Log.d("pooya", Integer.toString(agentList.size()));
                    }
                    //Toast.makeText(getApplicationContext(), Integer.toString(aList.get(0).getId())+" , "+Integer.toString(aList.get(1).getId()), Toast.LENGTH_SHORT).show();
                    latLngsha = getAgentLocation();
                    if (!(latLngsha.isEmpty()))
                    {
                        Toast.makeText(MapsActivity.this,"Done", Toast.LENGTH_SHORT).show();
                        flag =true;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            int user_id = user.getId();
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("visitor_id", Integer.toString(user_id));
            //returing the response

            return requestHandler.sendPostRequest(URLs.URL_AGENTS, params);
            //return requestHandler.sendPostRequest("http://se.climaxweb.ir/api/login/?username="+username+"&password="+password);
        }
    }
    public ArrayList getAgentLocation()
    {
        latLngsha.add(mylatlang);
        for(int i=0; i<agentList.size(); i++){

            a=agentList.get(i);
            LatLng a1 = new LatLng(a.getLat(),a.getLng());
            latLngsha.add(a1);

            Marker ma1 = mMap.addMarker(new MarkerOptions().position(a1).title("agent id : " + a.getId()));
            ir.climaxweb.visitorapp.MapUtils.animateMarker(ma1, 1000, new BounceInterpolator());
        }
//                LatLng a1 = new LatLng(35.732976, 51.211866);//chit
//                LatLng a2 = new LatLng(35.711642, 51.394203);//lalah
//                LatLng a3 = new LatLng(35.738404, 51.582984);//tajrish
//                LatLng a4 = new LatLng(35.798304, 51.653247);//tajrish
//                LatLng a5 = new LatLng(35.668304, 51.343247);//tajrish
//                LatLng a6 = new LatLng(35.708304, 51.413247);//tajrish
//
//                latLngsha.add(a1);
//                latLngsha.add(a2);
//                latLngsha.add(a3);
//                latLngsha.add(a4);
//                latLngsha.add(a5);
//                latLngsha.add(a6);
//
//                Marker ma1 = mMap.addMarker(new MarkerOptions().position(a1).title("agent id : " + latLngsha.indexOf(a1)));
//                Marker ma2 = mMap.addMarker(new MarkerOptions().position(a2).title("agent id : " + latLngsha.indexOf(a2)));
//                Marker ma3 = mMap.addMarker(new MarkerOptions().position(a3).title("agent id : " + latLngsha.indexOf(a3)));
//                Marker ma4 = mMap.addMarker(new MarkerOptions().position(a4).title("agent id : " + latLngsha.indexOf(a4)));
//                Marker ma5 = mMap.addMarker(new MarkerOptions().position(a5).title("agent id : " + latLngsha.indexOf(a5)));
//                Marker ma6 = mMap.addMarker(new MarkerOptions().position(a6).title("agent id : " + latLngsha.indexOf(a6)));
//
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma4, 1000,new BounceInterpolator());
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma5, 1000, new BounceInterpolator());
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma6, 1000, new BounceInterpolator());
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma1, 1000, new BounceInterpolator());
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma2, 1000, new BounceInterpolator());
//                ir.climaxweb.visitorapp.MapUtils.animateMarker(ma3, 1000, new BounceInterpolator());
                return latLngsha;



    }
    public void getmyLocation(Location location)
    {

        mylatlang = new LatLng(location.getLatitude(),location.getLongitude());

    }



    public void direction(ArrayList list)
    {

        GoogleDirection.withServerKey("AIzaSyCNoAIHLEKmSGyteAikNeUJLeVIfam6SjY")
                .from((LatLng) list.get(0))
                .to((LatLng) list.get(1))
                .optimizeWaypoints(true)
                .waypoints(latLngsha)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String s) {

                        if (direction.isOK())
                        {
                            Toast.makeText(MapsActivity.this,"Complete", Toast.LENGTH_LONG).show();
                            routeInfos = direction.getRouteInfo(getApplicationContext(),4);

                            for (int i=0 ; i < routeInfos.size() ;i++)
                            {
                              mypolyline = mypolyline = mMap.addPolyline(routeInfos.get(i).getPolylineOptions());
                                mypolyline.setColor(Color.RED);
                            }

                        }
                        else
                            Toast.makeText(MapsActivity.this,"Error,try later", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onDirectionFailure(Throwable throwable) {


                        Toast.makeText(MapsActivity.this,"Connection ERROR", Toast.LENGTH_LONG).show();
                    }
                });



    }
}


