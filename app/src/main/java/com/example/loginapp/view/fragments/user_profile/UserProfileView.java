package com.example.loginapp.view.fragments.user_profile;

import com.example.loginapp.model.entity.UserData;

public interface UserProfileView {

    void isLogged(boolean isLogged);

    void bindUserData(UserData userData);

    void bindNumberOfOrders(int numberOfProcessingOrder, int numberOfCompletedOrder, int numberOfCancelOrder, int numberOfReturnOrder);

}
