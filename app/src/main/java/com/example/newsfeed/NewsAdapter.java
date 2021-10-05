package com.example.newsfeed;


import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> newsArray;
    private NewsClicked listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView author;
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.author);
            image = (ImageView) view.findViewById(R.id.image);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public ImageView getImage() {
            return image;
        }

    }

    public NewsAdapter(NewsClicked listener) {
        newsArray = new ArrayList<News>();
        this.listener = listener;
    }

    public void update(ArrayList<News> newsArray){
        this.newsArray.clear();
        this.newsArray.addAll(newsArray);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onclickItem(newsArray.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTitle().setText(newsArray.get(position).getTitle());
        String author = newsArray.get(position).getAuthor();
        if(author == "null")
            author = "";
        viewHolder.getAuthor().setText(author);
        ImageView image = viewHolder.getImage();
        Glide.with(image.getContext()).load(newsArray.get(position).getImageUrl()).listener(
                new RequestListener<Drawable>() {

                    public boolean onException(Exception e, String model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i("error", "Exception in loading image");
                        image.setImageResource(R.drawable.no_image_found);
                        return false;
                    }


                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i("error", "Error in loading image");
                        image.setImageResource(R.drawable.no_image_found);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }
        ).into(image);
    }

    @Override
    public int getItemCount() {
        return newsArray.size();
    }
}

interface NewsClicked
{
    void onclickItem(News newsArray);
}
