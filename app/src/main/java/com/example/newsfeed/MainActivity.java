package com.example.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NewsClicked {

    private ArrayList<News> newsArray;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyleView = findViewById(R.id.recycleView);
        recyleView.setLayoutManager(new LinearLayoutManager(this));
        fetch_items();
        adapter = new NewsAdapter(this);
        recyleView.setAdapter(adapter);
    }

    private void fetch_items() {
        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=b945e514cd0a47d3834265dd873cf4f8";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            newsArray = new ArrayList<News>();
                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject article = articles.getJSONObject(i);
                                newsArray.add(new News(
                                        article.getString("title"),
                                        article.getString("author"),
                                        article.getString("urlToImage"),
                                        article.getString("url"))
                                );
                            }
                            adapter.update(newsArray);
                        } catch (JSONException e) {
                            Log.e("error", "No url field in JSON object");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error in network calls.");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onclickItem(News news) {

        int colorInt = Color.parseColor("#36475D");
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorInt)
                .build();

        String url = news.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setDefaultColorSchemeParams(defaultColors);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}

