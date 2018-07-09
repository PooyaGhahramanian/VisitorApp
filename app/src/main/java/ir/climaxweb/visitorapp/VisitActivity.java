package ir.climaxweb.visitorapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VisitActivity extends AppCompatActivity {
    private List<Agent> agentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AgentAdapter aAdapter;
    //List<Agent> aList=new ArrayList<>();
    Agent a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        recyclerView = (RecyclerView) findViewById(R.id.agentRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        aAdapter = new AgentAdapter(agentList);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(aLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(aAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Agent agent = agentList.get(position);
                Toast.makeText(getApplicationContext(), agent.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intentBundle=new Intent(VisitActivity.this,ProductActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("agentId",agent.getId());
                intentBundle.putExtras(bundle);
                startActivity(intentBundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareAgentData();
    }

    private void prepareAgentData() {

        /*
        Agent agent = new Agent(10, "Ali", "Narmak, Tehran, Iran", 3.002, 4.003);
        agentList.add(agent);

        agent = new Agent(2, "Hadi", "Tehranpars, Tehran, Iran", 23.002, 5.003);
        agentList.add(agent);

        agent = new Agent(3, "Hossein", "Sazman Ab, Tehran, Iran", 4.002, 12.003);
        agentList.add(agent);
        */
        getAgents getA = new getAgents();
        getA.execute();

        aAdapter.notifyDataSetChanged();
    }

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