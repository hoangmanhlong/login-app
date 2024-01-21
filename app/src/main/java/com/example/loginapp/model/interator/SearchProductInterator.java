package com.example.loginapp.model.interator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginapp.data.remote.api.AppApiService;
import com.example.loginapp.model.entity.Product;
import com.example.loginapp.data.remote.api.dto.ProductResponse;
import com.example.loginapp.data.remote.service.Constant;
import com.example.loginapp.model.entity.SearchHistory;
import com.example.loginapp.model.listener.SearchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductInterator {
    private final String TAG = this.toString();
    private SearchListener listener;

    private final DatabaseReference searchHistoriesRef =
        FirebaseDatabase.getInstance().getReference().child(Constant.SEARCH_HISTORY_REF);

    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                    if (productResponse != null) {
                        Log.d(this.toString(), productResponse.getProducts().toString());
                        List<Product> products = productResponse.getProducts();
                        if (products.isEmpty()) {
                            listener.showProcessBar(false);
                            listener.onListEmpty(true);
                        } else {
                            listener.onListEmpty(false);
                            listener.onLoadProducts(products);
                            listener.showProcessBar(false);
                        }

                    } else {
                        listener.onLoadError("Load data fail");
                    }
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
//        DatabaseReference userSearchHistoryRef = searchHistoriesRef.child(currentUser.getUid());
//
//        // Tạo một tham chiếu mới sử dụng push() để có ID tự động
//        DatabaseReference newSearchHistoryRef = userSearchHistoryRef.push();
//
//        // Lấy ID mới tạo
//        String newSearchHistoryId = newSearchHistoryRef.getKey();
//
//        // Tạo một đối tượng SearchHistory với ID mới và thông tin khác
//        SearchHistory newSearchHistory = new SearchHistory(newSearchHistoryId, text);
//
//        // Lưu SearchHistory lên Firebase
//        newSearchHistoryRef.setValue(newSearchHistory);
    }

    public void getSearchHistories() {
        searchHistoriesRef.child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
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
        Log.d(TAG, "deleteSearchHistory: ");
        searchHistoriesRef.child(currentUser.getUid()).child(id).removeValue();
    }
}
