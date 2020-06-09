package com.shoes.shoeslaundry.utils;

import com.shoes.shoeslaundry.data.model.Order;

public interface MessageCallback {
    void onSuccess(int newMessage);
    void onError(boolean failure);
}
