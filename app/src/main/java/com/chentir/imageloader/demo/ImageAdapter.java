package com.chentir.imageloader.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.chentir.imageloader.R;
import com.chentir.imageloader.library.ImageLoader;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context context;
    private final String[] dataSet;

    public ImageAdapter(Context context, String[] dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        ViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView =
            (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance(context).load(holder.imageView, dataSet[position]);
        //Picasso.get().load(dataSet[position]).into(holder.imageView);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        ImageLoader.getInstance(context).cancelRequest(holder.imageView);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}