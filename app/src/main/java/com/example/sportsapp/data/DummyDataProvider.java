package com.example.sportsapp.data;

import com.example.sportsapp.R;
import com.example.sportsapp.models.Match;
import com.example.sportsapp.models.News;

import java.util.ArrayList;
import java.util.List;

// Provides fallback + featured static data
public class DummyDataProvider {

    // ===== FEATURED MATCHES (horizontal) =====
    public static List<Match> getMatches() {

        List<Match> list = new ArrayList<>();

        // simple mock matches (used for horizontal scroll)
        list.add(new Match("Lions vs Tigers", "Football", R.drawable.s1));
        list.add(new Match("Heat vs Kings", "Basketball", R.drawable.s2));
        list.add(new Match("AUS vs IND", "Cricket", R.drawable.s3));

        return list;
    }

    // ===== NEWS FALLBACK (vertical) =====
    public static List<News> getNews() {

        List<News> list = new ArrayList<>();

        // fallback news (used if API fails)
        list.add(new News(
                "Big Football Clash",
                "Preview of upcoming match",
                "https://via.placeholder.com/300"
        ));

        list.add(new News(
                "Transfer News",
                "Latest player transfers",
                "https://via.placeholder.com/300"
        ));

        list.add(new News(
                "Match Highlights",
                "Top plays from last night",
                "https://via.placeholder.com/300"
        ));

        return list;
    }
}