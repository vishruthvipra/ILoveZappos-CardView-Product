package com.example.vishruthkrishnaprasad.ilovezappos;

/**
 * Created by vishruthkrishnaprasad on 31/1/17.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("originalTerm")
    @Expose
    public Object originalTerm;
    @SerializedName("currentResultCount")
    @Expose
    public String currentResultCount;
    @SerializedName("totalResultCount")
    @Expose
    public String totalResultCount;
    @SerializedName("term")
    @Expose
    public String term;
    @SerializedName("results")
    @Expose
    public ArrayList<Result> results;
    @SerializedName("statusCode")
    @Expose
    public String statusCode;

    // created getters and setters for future use

    public Object getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(Object originalTerm) {
        this.originalTerm = originalTerm;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}