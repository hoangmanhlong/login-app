package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductInterator {

    private final String TAG = this.toString();

    private SearchListener listener;

    private DatabaseReference searchHistoriesRef = Constant.searchHistoriesRef;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private ValueEventListener searchHistoriesValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                List<SearchHistory> searchHistories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    searchHistories.add(dataSnapshot.getValue(SearchHistory.class));
                listener.getSearchHistories(searchHistories);
            } else {
                listener.isSearchHistoriesEmpty();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void clearRef() {
        listener = null;
        searchHistoriesValueEventListener = null;
        currentUser = null;
        searchHistoriesRef = null;
    }

    public SearchProductInterator(SearchListener listener) {
        this.listener = listener;
    }

    public void searchProduct(String query) {
        Call<ProductResponse> call = AppApiService.retrofit.searchProduct(query);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        listener.getProducts(productResponse.getProducts());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " +  t.getMessage());
            }
        });
    }

    public void saveSearchHistory(String text) {
        searchHistoriesRef
                .child(currentUser.getUid())
                .child(text)
                .setValue(new SearchHistory(text));
    }

    public void addSearchHistoriesValueEventListener() {
        searchHistoriesRef.child(currentUser.getUid()).addValueEventListener(searchHistoriesValueEventListener);
    }

    public void removeSearchHistoriesValueEventListener() {
        searchHistoriesRef.child(currentUser.getUid()).removeEventListener(searchHistoriesValueEventListener);
    }

    public void deleteSearchHistory(String key) {
        searchHistoriesRef.child(currentUser.getUid()).child(key).removeValue();
    }

    public void deleteAllSearchHistories() {
        searchHistoriesRef.child(currentUser.getUid()).removeValue()
                .addOnCompleteListener(s -> listener.deleteSuccess(s.isSuccessful()));
    }
}
