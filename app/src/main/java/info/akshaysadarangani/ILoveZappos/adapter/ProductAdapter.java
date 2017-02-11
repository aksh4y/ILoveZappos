package info.akshaysadarangani.ILoveZappos.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import info.akshaysadarangani.ILoveZappos.R;
import info.akshaysadarangani.ILoveZappos.databinding.ProductLayoutBinding;
import info.akshaysadarangani.ILoveZappos.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ProductLayoutBinding binding;

        public ProductViewHolder(ProductLayoutBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
        }

        public void bindConnection(Product product) {
            binding.setProduct(product);
        }
    }


    public ProductAdapter(List<Product> _products) {
        products = _products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        ProductLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.product_layout, parent, false);
        return new ProductViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        holder.bindConnection(products.get(position));
    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}