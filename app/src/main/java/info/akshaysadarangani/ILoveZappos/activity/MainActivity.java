package info.akshaysadarangani.ILoveZappos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import java.util.List;
import info.akshaysadarangani.ILoveZappos.R;
import info.akshaysadarangani.ILoveZappos.adapter.ProductAdapter;
import info.akshaysadarangani.ILoveZappos.model.Product;
import info.akshaysadarangani.ILoveZappos.model.SearchResponse;
import info.akshaysadarangani.ILoveZappos.rest.ApiClient;
import info.akshaysadarangani.ILoveZappos.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Akshay on 2/7/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Zappos API key
    private final static String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";

    //private static final String TAG = MainActivity.class.getSimpleName();  //for debugging
    private TextView item_count;
    private FloatingActionButton expand, buy, share;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward,
            wiggle, slideUp, slideDown;
    private Boolean isFabOpen = false;
    private Boolean disableFab = false;
    private Boolean addedToCart = false;
    private Boolean hideBuy = false;
    private String shareURL = "";
    private LinearLayout container;
    private ShimmerTextView purchase;
    private Shimmer shimmer;

    ProgressDialog mProgressDialog;
    List<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid API", Toast.LENGTH_LONG).show();
            return;
        }

        shimmer = new Shimmer();
        container = (LinearLayout) findViewById(R.id.container);
        expand = (FloatingActionButton) findViewById(R.id.btn_expand);
        share = (FloatingActionButton) findViewById(R.id.btn_share);
        buy = (FloatingActionButton) findViewById(R.id.btn_buy);
        purchase = (ShimmerTextView) findViewById(R.id.purchase);
        item_count = (TextView) findViewById(R.id.item_count);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        wiggle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
        expand.setOnClickListener(this);
        buy.setOnClickListener(this);
        share.setOnClickListener(this);

        // Progress bar

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        String searchTerm = getIntent().getStringExtra("SEARCH_QUERY");

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // Request call

        Call<SearchResponse> call = apiService.getProductDetails(searchTerm, API_KEY, 1);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                int statusCode = response.code();
                products = response.body().getResults();
                //recyclerView.setAdapter(new ProductAdapter(products, R.layout.product_layout, getApplicationContext()));
                recyclerView.setAdapter(new ProductAdapter(products));
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (products.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No results!", Toast.LENGTH_LONG).show();
                    disableFab = true;
                } else
                    shareURL = products.get(0).getProductURL();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // failed
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "No results!", Toast.LENGTH_LONG).show();
                // Log.e(TAG, t.toString()); //for debug
            }
        });

        // Animations
        slideUp = new TranslateAnimation(0, 0, recyclerView.getHeight(), -1600);
        slideUp.setDuration(500);
        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                purchase.setVisibility(View.VISIBLE);
                shimmer.start(purchase);
                container.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        slideDown = new TranslateAnimation(0, 0, -1600, recyclerView.getHeight());
        slideDown.setDuration(500);
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                container.setVisibility(View.VISIBLE);
                purchase.setVisibility(View.GONE);
                shimmer.cancel();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.cart_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        item_count = (TextView) notifCount.findViewById(R.id.item_count);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cart:
                Toast.makeText(getApplicationContext(), "Cart!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_expand:
                if(!disableFab)
                    animateFAB();
                break;
            case R.id.btn_buy:
                if(!addedToCart) {
                    buy.startAnimation(wiggle);
                    addedToCart = true;
                    hideBuy = true;
                    container.startAnimation(slideUp);
                    animateFAB();
                    item_count.setText("1");
                    Toast.makeText(getApplicationContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                }
                else {
                    buy.startAnimation(wiggle);
                    addedToCart = false;
                    hideBuy = true;
                    container.startAnimation(slideDown);
                    item_count.setText("");
                    animateFAB();
                    Toast.makeText(getApplicationContext(), "Removed from cart!", Toast.LENGTH_SHORT).show();
                }

                    break;
            case R.id.btn_share:
                shareProduct(v);
                //Log.d("FAB", "Share"); //debug
                break;
            case R.id.container:
                Toast.makeText(getApplicationContext(), "Buy pressed", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void shareProduct(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Check out this awesome product:\n" + shareURL;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public void animateFAB(){

        if(isFabOpen){
            expand.startAnimation(rotate_backward);
            share.startAnimation(fab_close);
            if(!hideBuy)
                buy.startAnimation(fab_close);
            share.setClickable(false);
            buy.setClickable(false);
            isFabOpen = false;
            //Log.d("FAB", "close"); //for debug
        } else {

            expand.startAnimation(rotate_forward);
            share.startAnimation(fab_open);
            if(!addedToCart)
                buy.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_add_shopping_cart));
            else
                buy.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_remove_shopping_cart));
            hideBuy = false;
            buy.startAnimation(fab_open);
            share.setClickable(true);
            buy.setClickable(true);
            isFabOpen = true;
            // Log.d("FAB","open"); //for debug
        }
    }
}
