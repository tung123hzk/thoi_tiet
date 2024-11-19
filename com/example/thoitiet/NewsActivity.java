package com.example.thoitiet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Button close = findViewById(R.id.close);
        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        recyclerViewNews.setAdapter(newsAdapter);
        close.setOnClickListener(v -> finish());
        // Gọi AsyncTask để lấy dữ liệu tin tức
      //new FetchNewsTask().execute("https://newsapi.org/v2/everything?q=Việt Nam&language=vi&apiKey=abdb8ccc4bef44b29f0770d8f2838b0a"); // Thay bằng URL thật
        new FetchNewsTask().execute("https://gnews.io/api/v4/search?q=Việt%20Nam&lang=vi&token=573beca102622fbba05b1aeb142b6957");
    }

    private class FetchNewsTask extends AsyncTask<String, Void, List<NewsItem>> {
        @Override
        protected List<NewsItem> doInBackground(String... urls) {
            String urlString = urls[0];
            List<NewsItem> newsItems = new ArrayList<>();
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    JSONObject jsonResponse = new JSONObject(result.toString());
                    Log.d("FetchNewsTask", "JSON Response: " + jsonResponse);
                    JSONArray jsonArray = jsonResponse.getJSONArray("articles");
                    if (jsonArray.length() == 0) {
                        Log.d("FetchNewsTask", "No articles found.");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            String imageUrl = jsonObject.optString("image"); // Sử dụng trường hình ảnh đúng

                            Log.d("FetchNewsTask", "Article Title: " + title + ", Image URL: " + imageUrl);
                            newsItems.add(new NewsItem(title, imageUrl));
                        }
                    }


                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                Log.e("NewsActivity", "Error fetching news", e);
                e.printStackTrace();
            }
            return newsItems;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            if (newsItems != null && !newsItems.isEmpty()) {
                newsList.clear();
                newsList.addAll(newsItems);
                newsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(NewsActivity.this, "Không có tin tức", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
