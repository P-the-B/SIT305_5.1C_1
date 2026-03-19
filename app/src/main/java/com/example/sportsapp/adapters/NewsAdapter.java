package com.example.sportsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sportsapp.R;
import com.example.sportsapp.models.News;
import com.example.sportsapp.utils.BookmarkManager;

import java.util.List;

// adapter shared by the home screen, bookmark screen, and related stories list
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public interface OnItemClickListener {
        void onClick(News news);
    }

    private final List<News> newsList;
    private final OnItemClickListener listener;
    private final boolean isBookmarkScreen;

    public NewsAdapter(List<News> newsList,
                       OnItemClickListener listener,
                       boolean isBookmarkScreen) {
        this.newsList = newsList;
        this.listener = listener;
        this.isBookmarkScreen = isBookmarkScreen;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        News news = newsList.get(position);

        holder.title.setText(news.title);

        if (news.urlToImage != null && !news.urlToImage.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(news.urlToImage)
                    .placeholder(R.drawable.s1)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.s1);
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(news));

        if (isBookmarkScreen) {

            // on the bookmark screen the star is always filled — tap to remove
            holder.bookmarkBtn.setImageResource(android.R.drawable.btn_star_big_on);

            holder.bookmarkBtn.setOnClickListener(v -> {
                // use getAdapterPosition() so the index is always current,
                // even if earlier items have already been removed
                int currentPos = holder.getAdapterPosition();
                if (currentPos == RecyclerView.NO_ID) return;

                BookmarkManager.remove(news, holder.itemView.getContext());
                newsList.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, newsList.size());
            });

        } else {

            // on the home / related screen, show current saved state and allow toggling
            boolean isSaved = BookmarkManager.isBookmarked(news, holder.itemView.getContext());

            holder.bookmarkBtn.setImageResource(
                    isSaved
                            ? android.R.drawable.btn_star_big_on
                            : android.R.drawable.btn_star_big_off
            );

            holder.bookmarkBtn.setOnClickListener(v -> {
                boolean currentlySaved = BookmarkManager.isBookmarked(
                        news, holder.itemView.getContext()
                );

                if (currentlySaved) {
                    BookmarkManager.remove(news, holder.itemView.getContext());
                    holder.bookmarkBtn.setImageResource(android.R.drawable.btn_star_big_off);
                } else {
                    BookmarkManager.add(news, holder.itemView.getContext());
                    holder.bookmarkBtn.setImageResource(android.R.drawable.btn_star_big_on);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        ImageButton bookmarkBtn;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title       = itemView.findViewById(R.id.newsTitle);
            image       = itemView.findViewById(R.id.newsImage);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
        }
    }
}