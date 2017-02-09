package com.example.vishruthkrishnaprasad.ilovezappos;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.example.vishruthkrishnaprasad.ilovezappos.databinding.ActivityMainBinding;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/* We need the following imports in case we use AsyncTask instead of Retrofit
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
*/

/**
 * Created by vishruthkrishnaprasad on 31/1/17.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private RecyclerView recyclerView;
    Animation fabAddCart, fabCancel, fabFlubbergrow, fabFlubbershrink;
    private RecyclerViewAdapter adapter;
    SearchView searchView;
    MenuItem myActionMenuItem;
    ProgressDialog pdLoading;

    SharedPreferences sharedPreferences;
    Result result;

    // keeping track of animated floating action button
    static boolean isProductnull = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setSupportActionBar(activityMainBinding.toolbar);
        getSupportActionBar().setTitle("I Love Zappos");

        activityMainBinding.recyclerView.setAdapter(adapter);


        fabAddCart = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_add_cart);
        fabCancel = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_cancel);
        fabFlubbergrow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        fabFlubbershrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_adjust);

        // when user has not added item to the cart, hide the added to cart button
        activityMainBinding.fabCheck.hide();
        // disable added to cart button
        activityMainBinding.fabCheck.setClickable(false);

        activityMainBinding.fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* if no product has been searched, then the user has not chosen any item
                   hence, no item will not be added to cart
                   NOTE: adding item to cart is just a visual effect and has no database updates
                         in this app*/
                if (!isProductnull) {

                    // unhide added to cart button and make it clickable
                    activityMainBinding.fabCheck.show();
                    activityMainBinding.fabCheck.setClickable(true);

                    // respective animations
                    activityMainBinding.fabCheck.startAnimation(fabFlubbergrow);
                    activityMainBinding.fabCheck.startAnimation(fabFlubbershrink);

                    Toast.makeText(MainActivity.this, "Added to Cart! Click again to remove", Toast.LENGTH_SHORT).show();

                    // when user has added the item into the cart, hide the add to cart button
                    activityMainBinding.fabPlus.hide();
                    // disable add to cart button
                    activityMainBinding.fabPlus.setClickable(false);

                } else {
                    Toast.makeText(MainActivity.this, "No product available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // events that occur in fabPlus are reversed
        // No product available step is skipped here
        activityMainBinding.fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.fabPlus.show();
                activityMainBinding.fabPlus.setClickable(true);
                activityMainBinding.fabPlus.startAnimation(fabFlubbergrow);
                activityMainBinding.fabPlus.startAnimation(fabFlubbershrink);
                Toast.makeText(MainActivity.this, "Removed from Cart... Click again to add", Toast.LENGTH_SHORT).show();
                activityMainBinding.fabCheck.hide();
                activityMainBinding.fabCheck.setClickable(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*
                   if Retrofit is a requirement, (1) is executed
                   if AsyncTask is a requirement, (2) is executed
                 */

                // (1)
                getRetrofitObject(query);

                // (2)
                // new BackgroundTask(query).execute();
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    void getRetrofitObject(String searchQuery) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.zappos.com/Search?term=&key=b743e26728e16b81da139182bb2094357c31d331")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectAPI service = retrofit.create(RetrofitObjectAPI.class);

        // A dialog box for the user to know that data is being fetched from the remote database.
        pdLoading = new ProgressDialog(MainActivity.this);
        pdLoading.setTitle("Loading");
        pdLoading.setMessage("Loading data from URL...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        // the search query is appended to the url and then the Call is made
        Call<Product> call
                = service.getProductDetails("https://api.zappos.com/Search?term="
                + searchQuery
                + "&key=b743e26728e16b81da139182bb2094357c31d331");

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Response<Product> response, Retrofit retrofit) {
                ArrayList<Result> resultOfProducts = new ArrayList<>();
                try {
                    List<Result> resultList = null;
                    resultList = response.body().getResults();

                    /*
                    In case we need multiple results to be fetched, we use
                     for (int i = 0; i < resultList.size(); i++) {
                     */

                    result = new Result();

                    // get all the credentials one by one
                    result.setBrandName(resultList.get(0).getBrandName());
                    result.setThumbnailImageUrl(resultList.get(0).getThumbnailImageUrl());
                    result.setProductId(resultList.get(0).getProductId());
                    result.setOriginalPrice(resultList.get(0).getOriginalPrice());
                    result.setStyleId(resultList.get(0).getStyleId());
                    result.setColorId(resultList.get(0).getColorId());
                    result.setPrice(resultList.get(0).getPrice());
                    result.setPercentOff(resultList.get(0).getPercentOff());
                    result.setProductUrl(resultList.get(0).getProductUrl());
                    result.setProductName(resultList.get(0).getProductName());

                    // add to the array list and then populate the recycler view
                    resultOfProducts.add(result);
                    adapter = new RecyclerViewAdapter(resultOfProducts);
                    recyclerView.setAdapter(adapter);

                    // to load the image, we store the thumbnailImageUrl and then share it in the
                    // ProductViewHolder class
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    sharedPreferences.edit().putString("thumbnailimage", result.thumbnailImageUrl).apply();

                    // we close the for loop here
                    //}

                    // items searched is no more null
                    isProductnull = false;

                    // dismiss the dialog
                    pdLoading.dismiss();

                } catch (Exception e) {
                    Log.e("onResponse", "There is an error" + e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onResponse", "There is an error" + t);
            }
        });
    }

    /*
       The following exectues the Http request in case the requirement is by using AsnycTask
       private class BackgroundTask extends AsyncTask<String, String, String> {

           ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
           HttpURLConnection httpURLConnection;
           URL url = null;
           String searchQuery;

           public BackgroundTask(String searchQuery) {
               if (searchQuery == null)
                   searchQuery = " ";
               this.searchQuery = searchQuery;
           }

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               pdLoading.setTitle("Loading");
               pdLoading.setMessage("Loading data from URL...");
               pdLoading.setCancelable(false);
               pdLoading.show();
           }

           @Override
           protected String doInBackground(String... params) {
               try {
                   // Enter URL address where your php file resides
                   url = new URL("https://api.zappos.com/Search?term=" + searchQuery + "&key=b743e26728e16b81da139182bb2094357c31d331");
                   httpURLConnection = (HttpURLConnection) url.openConnection();
                   httpURLConnection.setRequestMethod("GET");
                   httpURLConnection.setRequestProperty("Content-length", "0");
                   httpURLConnection.setUseCaches(false);
                   httpURLConnection.setAllowUserInteraction(false);
                   httpURLConnection.connect();
                   int response_code = httpURLConnection.getResponseCode();

                   // Check if successful connection made
                   if (response_code == HttpURLConnection.HTTP_OK) {
                       // Read data sent from server
                       InputStream input = httpURLConnection.getInputStream();
                       BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                       StringBuilder result = new StringBuilder();
                       String line;

                       while ((line = reader.readLine()) != null) {
                           result.append(line);
                       }
                       reader.close();
                       return result.toString();
                   } else {
                       return ("Connection error");
                   }
               } catch (Exception e) {
                   Log.e("problem at io", "after response----->" + e);
                   return e.toString();
               } finally {
                   httpURLConnection.disconnect();
               }
           }

           @Override
           protected void onPostExecute(String result) {
               pdLoading.dismiss();
               resultOfProducts = new ArrayList<>();
               adapter = new RecyclerViewAdapter(resultOfProducts);
               recyclerView.setAdapter(adapter);
               pdLoading.dismiss();

               if (result.equals("no rows")) {
                   Toast.makeText(MainActivity.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
               } else {
                   try {
                       JSONObject json = new JSONObject(result);
                       JSONArray jArray = new JSONArray(json.getString("results"));

                       // Extract data from json and store into ArrayList as class objects
                       //for (int i = 0; i < jArray.length(); i++) {
                       JSONObject json_data = jArray.getJSONObject(0);
                       Result result1 = new Result();
                       result1.brandName = json_data.getString("brandName");
                       result1.thumbnailImageUrl = json_data.getString("thumbnailImageUrl");
                       result1.productId = json_data.getInt("productId");
                       result1.originalPrice = json_data.getString("originalPrice");
                       result1.styleId = json_data.getInt("styleId");
                       result1.colorId = json_data.getInt("colorId");
                       result1.price = json_data.getString("price");
                       result1.percentOff = json_data.getString("percentOff");
                       result1.productUrl = json_data.getString("productUrl");
                       result1.productName = json_data.getString("productName");
                       resultOfProducts.add(result1);

                       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                       sharedPreferences.edit().putString("thumbnailimage", result1.thumbnailImageUrl).apply();
                   // }
                       isProductnull = false;

                   } catch (Exception e) {
                       Log.e("json problem", "here------>" + e);
                   }
               }
           }
       }
    */

}


