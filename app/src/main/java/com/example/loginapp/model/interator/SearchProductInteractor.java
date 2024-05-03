package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductDto;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductInteractor {

    private static final String TAG = SearchProductInteractor.class.getSimpleName();

    private SearchListener listener;

    private DatabaseReference searchHistoriesRef = Constant.searchHistoriesRef;

    private String uid;

    private ValueEventListener searchHistoriesValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                List<SearchHistory> searchHistories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    searchHistories.add(dataSnapshot.getValue(SearchHistory.class));
                if (listener != null) listener.getSearchHistories(searchHistories);
            } else {
                if (listener != null) listener.isSearchHistoriesEmpty();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void clearRef() {
        listener = null;
        searchHistoriesValueEventListener = null;
        uid = null;
        searchHistoriesRef = null;
    }

    public SearchProductInteractor(SearchListener listener) {
        this.listener = listener;
        uid = FirebaseAuth.getInstance().getUid();
    }

    public void searchProduct(String query) {
        Call<ProductDto> call = AppApiService.retrofit.searchProduct(query);

        call.enqueue(new Callback<ProductDto>() {
            @Override
            public void onResponse(@NonNull Call<ProductDto> call, @NonNull Response<ProductDto> response) {
                if (response.isSuccessful()) {
                    ProductDto productDto = response.body();
                    if (productDto != null && listener != null) {
                        listener.getProducts(productDto.getProducts());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDto> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void saveSearchHistory(String text) {
        if (uid != null)
            searchHistoriesRef.child(uid).child(text).setValue(new SearchHistory(text));
    }

    public void addSearchHistoriesValueEventListener() {
        if (uid != null)
            searchHistoriesRef.child(uid).addValueEventListener(searchHistoriesValueEventListener);
    }

    public void removeSearchHistoriesValueEventListener() {
        if (uid != null)
            searchHistoriesRef.child(uid).removeEventListener(searchHistoriesValueEventListener);
    }

    public void deleteSearchHistory(String key) {
        if (uid != null) searchHistoriesRef.child(uid).child(key).removeValue();
    }

    public void deleteAllSearchHistories() {
        if (uid != null) searchHistoriesRef.child(uid).removeValue();
    }
}
