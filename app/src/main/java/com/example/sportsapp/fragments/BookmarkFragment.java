package com.example.sportsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsapp.MainActivity;
import com.example.sportsapp.R;
import com.example.sportsapp.adapters.NewsAdapter;
import com.example.sportsapp.models.News;
import com.example.sportsapp.utils.BookmarkManager;

import java.util.List;

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView subheading;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        subheading   = view.findViewById(R.id.bookmarkSubheading);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadBookmarks();
    }

    private void loadBookmarks() {

        List<News> bookmarks = BookmarkManager.getAll(requireContext());

        updateSubheading(bookmarks.size());

        adapter = new NewsAdapter(
                bookmarks,
                news -> ((MainActivity) requireActivity())
                        .loadFragment(DetailFragment.newInstance(news), true),
                true
        );

        // when a star is tapped and an item is removed, update the subheading immediately
        adapter.setOnBookmarkRemovedListener(remainingCount ->
                updateSubheading(remainingCount)
        );

        recyclerView.setAdapter(adapter);
    }

    // updates the subheading text based on how many bookmarks remain
    private void updateSubheading(int count) {
        if (count == 0) {
            subheading.setText("No saved stories yet");
        } else if (count == 1) {
            subheading.setText("Showing 1 saved story");
        } else {
            subheading.setText("Showing " + count + " saved stories");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // reload when returning from detail screen in case a bookmark was toggled there
        if (adapter != null) {
            loadBookmarks();
        }
    }
}