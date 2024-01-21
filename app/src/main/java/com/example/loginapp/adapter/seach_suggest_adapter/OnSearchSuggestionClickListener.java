package com.example.loginapp.adapter.seach_suggest_adapter;

import com.example.loginapp.model.entity.SearchHistory;

public interface OnSearchSuggestionClickListener {
    void onClick(SearchHistory history);

    void deleteSearchHistory(SearchHistory history);
}
