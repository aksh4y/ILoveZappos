package info.akshaysadarangani.ILoveZappos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import info.akshaysadarangani.ILoveZappos.R;
import info.akshaysadarangani.ILoveZappos.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private int rowLayout;
    private Context context;
    private static ProgressBar progressBar;


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productLayout;
        TextView brandName;
        TextView productName;
        ImageView thumbnail;
        TextView price;


        public ProductViewHolder(View v) {
            super(v);
            productLayout = (LinearLayout) v.findViewById(R.id.product_layout);
            productName = (TextView) v.findViewById(R.id.productName);
            brandName = (TextView) v.findViewById(R.id.brandName);
            price = (TextView) v.findViewById(R.id.price);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            progressBar = (ProgressBar) v.findViewById(R.id.progress);

        }
    }

    public ProductAdapter(List<Product> products, int rowLayout, Context context) {
        this.products = products;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        // Using Glide library for product thumbnails
        Glide
                .with(context)
                .load(products.get(0).getThumbnailURL())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnail);



        holder.productName.setText(products.get(0).getBrandName());
        holder.brandName.setText(products.get(0).getProductName());
        holder.price.setText(products.get(0).getPrice());

    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}