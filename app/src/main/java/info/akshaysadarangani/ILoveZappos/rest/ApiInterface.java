package info.akshaysadarangani.ILoveZappos.rest;

import info.akshaysadarangani.ILoveZappos.model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("Search")
    Call<SearchResponse> getProductDetails(@Query("term") String overview, @Query("key") String apiKey, @Query("limit") int limit);
}