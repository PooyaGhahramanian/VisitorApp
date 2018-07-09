package ir.climaxweb.visitorapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VisitActivity extends AppCompatActivity implements LocationListener {
    private List<Agent> agentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AgentAdapter aAdapter;
    private Button SubmitVisitButton;
    private LocationManager locationManager;
    private Location LastLocation;
    Agent a;
    Agent agentToVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        SubmitVisitButton = (Button) findViewById(R.id.SubmitVisitButton);
        recyclerView = (RecyclerView) findViewById(R.id.agentRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        aAdapter = new AgentAdapter(agentList);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(aLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(aAdapter);
        ///


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                0, mLocationListener);
        ///
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Agent agent = agentList.get(position);
                agentToVisit = agent;
                Toast.makeText(getApplicationContext(), agent.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                SubmitVisitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProgressBar progressBar;
                        progressBar = (ProgressBar) findViewById(R.id.AgentsProgressBar);
                        progressBar.setVisibility(View.VISIBLE);
                       SubmitVisit submitVisit=new SubmitVisit();
                        submitVisit.execute();
                     progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareAgentData();
    }

    private void prepareAgentData() {
        getAgents getA = new getAgents();
        getA.execute();
        aAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationChanged(Location location) {
        LastLocation=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class SubmitVisit extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        String isSubmited;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONArray arr = new JSONArray(s);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        isSubmited=obj.getString("message");
                        Toast.makeText(getApplicationContext(), isSubmited, Toast.LENGTH_LONG).show();
                    }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        @Override
        protected String doInBackground(Void... voids) {

            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            int user_id = user.getId();
            String Lat=Double.toString(LastLocation.getLatitude());
            String Lng=Double.toString(LastLocation.getLongitude());
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("Visitor_id", Integer.toString(user_id));
            params.put("Agent_id",Integer.toString(agentToVisit.getId()));
            params.put("Lat",Lat);
            params.put("Lng",Lng);

            //returing the response

            return requestHandler.sendPostRequest(URLs.URL_VISIT, params);
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
           LastLocation=location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    class getAgents extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.AgentsProgressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

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
                    aAdapter.notifyDataSetChanged();
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
}