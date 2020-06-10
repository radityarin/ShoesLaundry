package com.shoes.shoeslaundry.utils;

import com.shoes.shoeslaundry.data.model.Order;

public interface StatusCallback {
    void onSuccess(Order order);
    void onError(boolean failure);
}
