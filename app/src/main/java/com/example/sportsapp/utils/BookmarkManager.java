package com.example.sportsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sportsapp.models.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// handles saving and retrieving bookmarks locally via SharedPreferences
public class BookmarkManager {

    private static final String PREF_NAME = "bookmarks_pref";
    private static final String KEY = "bookmarks";

    public static List<News> getAll(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY, null);

        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<List<News>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    // returns true if a news item with the same title is already saved
    public static boolean isBookmarked(News news, Context context) {
        for (News n : getAll(context)) {
            if (n.title != null && n.title.equals(news.title)) return true;
        }
        return false;
    }

    public static void add(News news, Context context) {

        List<News> list = getAll(context);

        // prevent duplicates by title
        for (News n : list) {
            if (n.title != null && n.title.equals(news.title)) return;
        }

        list.add(news);
        save(list, context);
    }

    public static void remove(News news, Context context) {

        List<News> list = getAll(context);
        list.removeIf(n -> n.title != null && n.title.equals(news.title));
        save(list, context);
    }

    private static void save(List<News> list, Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY, new Gson().toJson(list)).apply();
    }
}