package com.example.vishruthkrishnaprasad.ilovezappos;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.example.vishruthkrishnaprasad.ilovezappos.databinding.RowItemBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by vishruthkrishnaprasad on 1/2/17.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private RowItemBinding binding;
    private SharedPreferences sharedPreferences;

    public ProductViewHolder(View view, Context context) {
        super(view);

        binding = DataBindingUtil.bind(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // events when clicked
            }
        });

        try {
            // retrieve the thumbnailImageUrl from getRetrofitObject() and load using Picasso
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String thumbnailImageUrl = "No image";
            String rawThumbnailImageUrl = sharedPreferences.getString("thumbnailimage", thumbnailImageUrl);
            Picasso.with(context).load(rawThumbnailImageUrl).into(binding.imageView);

        } catch (Exception e) {
            Log.e("Image!", "Not able to load image" + e);
        }
    }

    public void bind(Result result) {
        binding.setResult(result);
    }
}
