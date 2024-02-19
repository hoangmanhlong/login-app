package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductInterator {

    private final String TAG = this.toString();

    private final SearchListener listener;

    private final DatabaseReference searchHistoriesRef = Constant.searchHistoriesRef;

    private final FirebaseUser currentUser = Constant.currentUser;

    private final Query searchSuggestionQuery = searchHistoriesRef.child(currentUser.getUid());

    public SearchProductInterator(SearchListener listener) {
        this.listener = listener;
    }

    public void searchProduct(String query) {
        Call<ProductResponse> call = AppApiService.retrofit.searchProduct(query);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null)
                        listener.getProducts(productResponse.getProducts());
                    else listener.onLoadError("Load data fail");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void saveSearchHistory(String text) {
        searchHistoriesRef.child(currentUser.getUid()).child(text).setValue(new SearchHistory(text));
    }

    public void getSearchHistories() {

        searchSuggestionQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listener.notifyItemAdded(snapshot.getValue(SearchHistory.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                listener.notifyItemRemoved(snapshot.getValue(SearchHistory.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteSearchHistory(String id) {
        searchHistoriesRef.child(currentUser.getUid()).child(id).removeValue();
    }

    public void getCategories() {
        Call<List<String>> call = AppApiService.retrofit.getCategories();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    listener.getCategories(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }
}
