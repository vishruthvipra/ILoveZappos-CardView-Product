package com.example.vishruthkrishnaprasad.ilovezappos;

/**
 * Created by vishruthkrishnaprasad on 7/2/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("brandName")
    @Expose
    public String brandName;
    @SerializedName("thumbnailImageUrl")
    @Expose
    public String thumbnailImageUrl;
    @SerializedName("productId")
    @Expose
    public int productId;
    @SerializedName("originalPrice")
    @Expose
    public String originalPrice;
    @SerializedName("styleId")
    @Expose
    public int styleId;
    @SerializedName("colorId")
    @Expose
    public int colorId;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("percentOff")
    @Expose
    public String percentOff;
    @SerializedName("productUrl")
    @Expose
    public String productUrl;
    @SerializedName("productName")
    @Expose
    public String productName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}