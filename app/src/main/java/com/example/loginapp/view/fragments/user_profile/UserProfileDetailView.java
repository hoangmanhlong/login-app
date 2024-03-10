package com.example.loginapp.view.fragments.user_profile;

import com.example.loginapp.model.entity.UserData;

public interface UserProfileDetailView {

    void bindLanguageState(boolean isVietnamese);

    void bindUserData(UserData userData);

    void bindNumberOfOrders(int numberOfProcessingOrder, int numberOfCompletedOrder, int numberOfCancelOrder, int numberOfReturnOrder);

}
