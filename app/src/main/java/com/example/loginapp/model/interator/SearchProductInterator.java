package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.listener.SearchListener;
import com.example.loginapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductInterator {

    private final String TAG = this.toString();

    private final SearchListener listener;

    private final DatabaseReference searchHistoriesRef = Constant.searchHistoriesRef;

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
                    } else listener.onLoadError("Load data fail");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }

    public void saveSearchHistory(String text) {
        searchHistoriesRef
                .child(currentUser.getUid())
                .child(text)
                .setValue(new SearchHistory(text));
    }

    public void getSearchHistories() {

        assert currentUser != null;
        Query query = searchHistoriesRef.child(currentUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.d(TAG, "onChildAdded: ");
                        listener.notifyItemAdded(snapshot.getValue(SearchHistory.class));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        listener.notifyItemChanged(snapshot.getValue(SearchHistory.class));
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
                if (!snapshot.exists()) listener.isSearchHistoriesEmpty();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteSearchHistory(String key) {
        searchHistoriesRef.child(currentUser.getUid()).child(key).removeValue();
    }

    public void deleteAllSearchHistories() {
        searchHistoriesRef.child(currentUser.getUid()).removeValue()
                .addOnCompleteListener(s -> listener.deleteSuccess(s.isSuccessful()));
    }
}
