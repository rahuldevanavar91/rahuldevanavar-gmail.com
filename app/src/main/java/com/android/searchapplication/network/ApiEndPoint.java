package com.android.searchapplication.network;


import com.android.searchapplication.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndPoint {

    @GET("api/v1/search")
    Call<SearchResponse> getSearchResponse(@Query("query") String query);
}
