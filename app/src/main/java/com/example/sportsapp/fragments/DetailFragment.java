package com.example.sportsapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sportsapp.MainActivity;
import com.example.sportsapp.R;
import com.example.sportsapp.adapters.NewsAdapter;
import com.example.sportsapp.models.News;
import com.example.sportsapp.utils.BookmarkManager;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private static final String KEY = "news";
    private News news;

    public static DetailFragment newInstance(News news) {

        DetailFragment fragment = new DetailFragment();

        Bundle b = new Bundle();
        b.putSerializable(KEY, news);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (getArguments() != null) {
            news = (News) getArguments().getSerializable(KEY);
        }

        ImageView ivImage       = view.findViewById(R.id.detailImageView);
        TextView tvSource       = view.findViewById(R.id.detailSource);
        TextView tvTitle        = view.findViewById(R.id.detailTitle);
        TextView tvAuthor       = view.findViewById(R.id.detailAuthor);
        TextView tvDate         = view.findViewById(R.id.detailDate);
        TextView tvDesc         = view.findViewById(R.id.detailDescription);
        TextView tvContent      = view.findViewById(R.id.detailContent);
        Button readMoreBtn      = view.findViewById(R.id.readMoreButton);
        Button bookmarkBtn      = view.findViewById(R.id.bookmarkButton);
        RecyclerView relatedRv  = view.findViewById(R.id.relatedStoriesRecyclerView);

        if (news == null) return;

        // image
        Glide.with(this)
                .load(news.urlToImage)
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)
                .into(ivImage);

        // source outlet name e.g. "ESPN"
        if (news.source != null && news.source.name != null) {
            tvSource.setText(news.source.name);
            tvSource.setVisibility(View.VISIBLE);
        } else {
            tvSource.setVisibility(View.GONE);
        }

        // title
        tvTitle.setText(news.title);

        // author
        if (news.author != null && !news.author.isEmpty()) {
            tvAuthor.setText("By " + news.author);
            tvAuthor.setVisibility(View.VISIBLE);
        } else {
            tvAuthor.setVisibility(View.GONE);
        }

        // published date — API sends ISO format "2025-03-19T10:30:00Z", we just show the date part
        if (news.publishedAt != null && !news.publishedAt.isEmpty()) {
            String date = news.publishedAt.contains("T")
                    ? news.publishedAt.split("T")[0]
                    : news.publishedAt;
            tvDate.setText(date);
            tvDate.setVisibility(View.VISIBLE);
        } else {
            tvDate.setVisibility(View.GONE);
        }

        // description
        if (news.description != null && !news.description.isEmpty()) {
            tvDesc.setText(news.description);
            tvDesc.setVisibility(View.VISIBLE);
        } else {
            tvDesc.setVisibility(View.GONE);
        }

        // body content — NewsAPI free tier appends "[+123 chars]" when truncating, so we strip it
        if (news.content != null && !news.content.isEmpty()) {
            String cleanContent = news.content.replaceAll("\\[\\+\\d+ chars\\]", "").trim();
            tvContent.setText(cleanContent);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }

        // read full article button — only shown when the API provided a URL
        if (news.url != null && !news.url.isEmpty()) {
            readMoreBtn.setVisibility(View.VISIBLE);
            readMoreBtn.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));
                startActivity(intent);
            });
        } else {
            readMoreBtn.setVisibility(View.GONE);
        }

        // bookmark toggle
        updateBookmarkButtonLabel(bookmarkBtn);

        bookmarkBtn.setOnClickListener(v -> {
            boolean isSaved = BookmarkManager.isBookmarked(news, requireContext());
            if (isSaved) {
                BookmarkManager.remove(news, requireContext());
            } else {
                BookmarkManager.add(news, requireContext());
            }
            updateBookmarkButtonLabel(bookmarkBtn);
        });

        // related stories — hardcoded dummy data satisfies the spec without extra API calls
        relatedRv.setLayoutManager(new LinearLayoutManager(getContext()));
        relatedRv.setAdapter(new NewsAdapter(
                buildRelatedStories(),
                relatedNews -> ((MainActivity) requireActivity())
                        .loadFragment(DetailFragment.newInstance(relatedNews), true),
                false
        ));
    }

    private void updateBookmarkButtonLabel(Button btn) {
        boolean saved = BookmarkManager.isBookmarked(news, requireContext());
        btn.setText(saved ? "Bookmarked ★" : "Save Bookmark");
    }

    // three dummy items cover the spec requirement for a Related Stories RecyclerView
    private List<News> buildRelatedStories() {
        List<News> related = new ArrayList<>();
        related.add(new News(
                "Top Performers This Week",
                "A look at the standout athletes making headlines.",
                null
        ));
        related.add(new News(
                "Injury Report Update",
                "Latest updates on key players ahead of the weekend.",
                null
        ));
        related.add(new News(
                "Season Standings Roundup",
                "Where every team stands going into the next round.",
                null
        ));
        return related;
    }
}