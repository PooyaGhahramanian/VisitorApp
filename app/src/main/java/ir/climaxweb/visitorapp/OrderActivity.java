package ir.climaxweb.visitorapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.climaxweb.visitorapp.Product;

public class OrderActivity extends AppCompatActivity {


    ArrayList<Product> pList=new ArrayList<>();
    Product p;
    ArrayList<Agent> aList=new ArrayList<>();
    Agent a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findViewById(R.id.btnTsetProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts getP = new getProducts();
                getP.execute();
            }
        });
        findViewById(R.id.btnTsetAgents).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAgents getA = new getAgents();
                getA.execute();
            }
        });
    }

    class getProducts extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.productsProgressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

            try {
                if(s==""){
                    Toast.makeText(getApplicationContext(), "no products available !", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONArray arr=new JSONArray(s);
                    for (int i=0;i<arr.length();i++){
                        JSONObject obj = arr.getJSONObject(i);
                        Toast.makeText(getApplicationContext(), Integer.toString(obj.getInt("Id")), Toast.LENGTH_SHORT).show();
                        p=new Product(
                                obj.getInt("Id"),
                                obj.getString("Name"),
                                obj.getBoolean("In_stock"),
                                obj.getDouble("Price"),
                                obj.getDouble("Sale_price"),
                                obj.getString("Description"),
                                obj.getString("Picture")
                        );
                        pList.add(p);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();

            //returing the response

            return requestHandler.sendPostRequest(URLs.URL_PRODUCTS, params);
            //return requestHandler.sendPostRequest("http://se.climaxweb.ir/api/login/?username="+username+"&password="+password);
        }
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
                if(s==""){
                    Toast.makeText(getApplicationContext(), "no agents available !", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONArray arr=new JSONArray(s);
                    for (int i=0;i<arr.length();i++){
                        JSONObject obj = arr.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(), Integer.toString(obj.getInt("Id")), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                        a=new Agent(
                                obj.getInt("Id"),
                                obj.getString("Name"),
                                obj.getString("Address"),
                                obj.getDouble("Lat"),
                                obj.getDouble("Lng")
                        );
                        aList.add(a);
                        Toast.makeText(getApplicationContext(), Integer.toString(aList.size()), Toast.LENGTH_SHORT).show();
                        Log.d("pooya",Integer.toString(aList.size()));
                    }
                    //Toast.makeText(getApplicationContext(), Integer.toString(aList.get(0).getId())+" , "+Integer.toString(aList.get(1).getId()), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            int user_id=user.getId();
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("visitor_id",Integer.toString(user_id));
            //returing the response

            return requestHandler.sendPostRequest(URLs.URL_AGENTS, params);
            //return requestHandler.sendPostRequest("http://se.climaxweb.ir/api/login/?username="+username+"&password="+password);
        }
    }
}
