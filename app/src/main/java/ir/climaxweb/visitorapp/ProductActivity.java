package ir.climaxweb.visitorapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductActivity extends AppCompatActivity{

    ArrayList<Product> productList=new ArrayList<>();
    Product p;
    private RecyclerView recyclerView;
    private ProductAdapter pAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        pAdapter = new ProductAdapter(productList);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Product product = productList.get(position);
                Toast.makeText(getApplicationContext(), product.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareProductData();
    }
    private void prepareProductData() {

        /*
        Agent agent = new Agent(10, "Ali", "Narmak, Tehran, Iran", 3.002, 4.003);
        agentList.add(agent);

        agent = new Agent(2, "Hadi", "Tehranpars, Tehran, Iran", 23.002, 5.003);
        agentList.add(agent);

        agent = new Agent(3, "Hossein", "Sazman Ab, Tehran, Iran", 4.002, 12.003);
        agentList.add(agent);
        */
        ProductActivity.getProducts getP = new getProducts();
        getP.execute();

        pAdapter.notifyDataSetChanged();
    }

    class getProducts extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) findViewById(R.id.ProductProgressBar);
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
                        productList.add(p);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pAdapter.notifyDataSetChanged();
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
}
