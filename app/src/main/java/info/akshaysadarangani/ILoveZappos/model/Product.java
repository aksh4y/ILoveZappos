package info.akshaysadarangani.ILoveZappos.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("thumbnailImageUrl")
    private String thumbnailURL;
    @SerializedName("brandName")
    private String brandName;
    @SerializedName("productName")
    private String productName;
    @SerializedName("price")
    private String price;
    @SerializedName("productUrl")
    private String productURL;

    public Product(String thumbnailURL, String brandName, String productName, String price, String productURL) {
        this.thumbnailURL = thumbnailURL;
        this.brandName = brandName;
        this.productName = productName;
        this.price = price;
        this.productURL = productURL;
    }


    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) { this.productName = productName; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }
}
