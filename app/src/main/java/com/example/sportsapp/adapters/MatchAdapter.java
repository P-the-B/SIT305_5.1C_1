package com.example.sportsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsapp.R;
import com.example.sportsapp.models.Match;

import java.util.List;

// adapter for horizontal featured matches
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matchList;
    private OnItemClickListener listener;

    // click interface
    public interface OnItemClickListener {
        void onItemClick(Match match);
    }

    public MatchAdapter(List<Match> matchList, OnItemClickListener listener) {
        this.matchList = matchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);

        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {

        Match match = matchList.get(position);

        holder.tvTitle.setText(match.getTitle());
        holder.ivImage.setImageResource(match.getImage());

        // 🔴 FIX: click binding
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(match);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivImage;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.matchTitle);
            ivImage = itemView.findViewById(R.id.matchImage);
        }
    }
}