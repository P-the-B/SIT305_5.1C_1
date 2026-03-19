package com.example.sportsapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsapp.MainActivity;
import com.example.sportsapp.R;
import com.example.sportsapp.adapters.MatchAdapter;
import com.example.sportsapp.adapters.NewsAdapter;
import com.example.sportsapp.api.RetrofitClient;
import com.example.sportsapp.models.Match;
import com.example.sportsapp.models.News;
import com.example.sportsapp.models.NewsResponse;
import com.example.sportsapp.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// home screen = featured row + news grid
public class HomeFragment extends Fragment {

    private RecyclerView featuredRecycler;
    private RecyclerView newsRecycler;
    private EditText searchBar;
    private ImageButton bookmarksButton;

    private MatchAdapter matchAdapter;
    private NewsAdapter newsAdapter;

    private final List<Match> matchList = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();
    private final List<News> filteredNews = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bind views
        featuredRecycler = view.findViewById(R.id.featuredRecyclerView);
        newsRecycler = view.findViewById(R.id.newsRecyclerView);
        searchBar = view.findViewById(R.id.searchBar);
        bookmarksButton = view.findViewById(R.id.bookmarksButton);

        // top bookmarks icon -> open bookmark screen
        bookmarksButton.setOnClickListener(v ->
                ((MainActivity) requireActivity()).loadFragment(new BookmarkFragment(), true)
        );

        // featured matches row
        featuredRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        matchAdapter = new MatchAdapter(matchList, match -> {
            // reuse detail screen by mapping match -> news
            News featuredNews = new News(
                    match.getTitle(),
                    match.getCategory() + " featured match",
                    null
            );

            ((MainActivity) requireActivity())
                    .loadFragment(DetailFragment.newInstance(featuredNews), true);
        });

        featuredRecycler.setAdapter(matchAdapter);
        loadDummyMatches();

        // latest news grid (matches wireframe)
        newsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        newsAdapter = new NewsAdapter(filteredNews, news ->
                ((MainActivity) requireActivity())
                        .loadFragment(DetailFragment.newInstance(news), true),
                false
        );

        newsRecycler.setAdapter(newsAdapter);

        // live filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // enter/search key -> just filter current results, no newline
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterNews(searchBar.getText().toString());
                return true;
            }
            return false;
        });

        // initial news load
        fetchNews();
    }

    // static featured cards
    private void loadDummyMatches() {
        matchList.clear();

        matchList.add(new Match("Lions vs Tigers", "Football", R.drawable.s1));
        matchList.add(new Match("Heat vs Kings", "Basketball", R.drawable.s2));
        matchList.add(new Match("AUS vs IND", "Cricket", R.drawable.s3));

        matchAdapter.notifyDataSetChanged();
    }

    // news API load
    private void fetchNews() {

        RetrofitClient.getApiService()
                .getSportsNews("sports", "us", BuildConfig.NEWS_API_KEY)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsResponse> call,
                                           @NonNull Response<NewsResponse> response) {

                        newsList.clear();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().articles != null) {

                            newsList.addAll(response.body().articles);
                        }

                        filteredNews.clear();
                        filteredNews.addAll(newsList);
                        newsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsResponse> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    // local filter on loaded list
    private void filterNews(String text) {

        filteredNews.clear();

        String q = text == null ? "" : text.trim().toLowerCase();

        if (q.isEmpty()) {
            filteredNews.addAll(newsList);
        } else {
            for (News n : newsList) {
                if (n.title != null && n.title.toLowerCase().contains(q)) {
                    filteredNews.add(n);
                }
            }
        }

        newsAdapter.notifyDataSetChanged();
    }
}