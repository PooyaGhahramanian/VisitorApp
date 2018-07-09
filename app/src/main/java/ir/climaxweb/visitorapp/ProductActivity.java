package ir.climaxweb.visitorapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    ArrayList<Product> productList=new ArrayList<>();
    Product p;
    int agentId;
    int sProductId;
    ArrayList<Integer> sProductsId;
    int sNum;
    ArrayList<Integer> sNums;
    String sProductsId_param;
    String sNums_param;

    private RecyclerView recyclerView;
    private ProductAdapter pAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sProductsId=new ArrayList<>();
        sNums=new ArrayList<>();

        Intent intentExtras=getIntent();
        Bundle bundleExtras=intentExtras.getExtras();
        if(!bundleExtras.isEmpty()){
            agentId=bundleExtras.getInt("agentId");
            Log.d("pooya, Selected Agent Id: ",Integer.toString(agentId));
        }
        Button btnSubmitOrder=(Button)findViewById(R.id.btnSubmitOrder);
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sProductsId.size()>0){
                    sProductsId_param=convertToParamFormat(sProductsId);
                    sNums_param=convertToParamFormat(sNums);
                    submitOrder sOrder=new submitOrder();
                    sOrder.execute();
                    //startActivity(new Intent(ProductActivity.this,ProfileActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "no products selected to order !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button btnCancelOrder=(Button)findViewById(R.id.btnCancelOrder);
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProductActivity.this,ProfileActivity.class));

            }
        });
        //AlertDialog


        //AlertDialog

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
                sProductId=product.getId();
                Toast.makeText(getApplicationContext(), product.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                showNpDialog();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareProductData();
    }
    private String convertToParamFormat(ArrayList<Integer> arr){
        String out="";
        for(int i=0;i<arr.size();i++){
            if(i==0){
                Integer.toString(arr.get(i));
            }
            else{

            }
            out=out+","+Integer.toString(arr.get(i));
        }
        return out;
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    public void showNpDialog()
    {

        final Dialog d = new Dialog(ProductActivity.this);
        d.setTitle("Select number of this product");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                sNum=np.getValue();
                sProductsId.add(sProductId);
                sNums.add(sNum);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


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
    class submitOrder extends AsyncTask<Void, Void, String> {

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
                if (s == "") {
                    Toast.makeText(getApplicationContext(), "empty order !", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject obj=new JSONObject(s);
                    Toast.makeText(getApplicationContext(), "Order successfully submitted !", Toast.LENGTH_SHORT).show();
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
            params.put("agent_id", Integer.toString(agentId));
            params.put("products_id", sProductsId_param);
            params.put("products_count", sNums_param);
            //returing the response

            return requestHandler.sendPostRequest(URLs.URL_ORDER, params);
            //return requestHandler.sendPostRequest("http://se.climaxweb.ir/api/login/?username="+username+"&password="+password);
        }
    }
}
