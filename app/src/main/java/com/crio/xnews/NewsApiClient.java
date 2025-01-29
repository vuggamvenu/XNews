package com.crio.xnews;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

public class NewsApiClient {

    private static final String API_URL = "https://newsapi.org/v2/everything";
    private static final String API_KEY = "0afc92dd045b4aa0bf79cdf3e5444301";

// TODO: CRIO_TASK_MODULE_PROJECT
// Utilize the Okhttp3 library to send a request to the News API, including the provided query, language, and sortBy parameters.
// Ensure that the Gradle dependency for Okhttp3 is included in build.gradle.
// Parse the JSON response using NewsParser.
// If the "query" parameter is empty, an IllegalArgumentException is thrown. 
// If there is an error during the API request or response parsing, IOException is thrown.

    public List<NewsArticle> fetchNewsArticles(String query, String language, String sortBy) throws IOException {
        String url;
        try{
            url = buildUrl(query, language, sortBy);
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Query parameter must not be empty");
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute()){
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                List<NewsArticle> articles = NewsParser.parse(responseBody);
                return articles;
            } else {
                throw new IOException("Failed to fetch news: " + response.message());
            }
        }catch(Exception e){
            throw new IOException("Error occured during API call or response parsing");
        }
    }

// TODO: CRIO_TASK_MODULE_PROJECT
// Construct the URL required to make a request to the News API and use this in above method.
// Refer to https://newsapi.org/docs/endpoints/everything for guidance on URL construction.
// The "query" parameter is mandatory and must not be empty. 
// If the "query" parameter is empty, throw IllegalArgumentException with message "Query parameter must not be empty".
// The "language" and "sortBy" parameters are optional and will be included in the URL if they are non-empty.

    private String buildUrl(String query, String language, String sortBy) {
        //sample URL
        // https://newsapi.org/v2/everything?q=apple&language=fr&sortBy=popularity&apiKey=0afc92dd045b4aa0bf79cdf3e5444301
        if(null==query || query.equals("")){
            throw new IllegalArgumentException("Query parameter must not be empty");
        }

        StringBuilder url=new StringBuilder(API_URL+"?q="+query);

        if(null!=language && !language.equals("")){
            url.append("&language=").append(language);
        }

        if(null!=sortBy && !sortBy.equals("")){
            url.append("&sortBy=").append(sortBy);
        }
        
        url.append("&apikey=").append(API_KEY);
        return url.toString();
    //   return "";
    }
}