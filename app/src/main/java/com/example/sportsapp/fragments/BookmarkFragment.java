package com.example.sportsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

// bookmark screen
public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadBookmarks();
    }

    private void loadBookmarks() {

        List<News> bookmarks = BookmarkManager.getAll(requireContext());

        adapter = new NewsAdapter(
                bookmarks,
                news -> ((MainActivity) requireActivity())
                        .loadFragment(DetailFragment.newInstance(news), true),
                true
        );

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            loadBookmarks();
        }
    }
}