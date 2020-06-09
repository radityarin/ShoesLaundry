package com.shoes.shoeslaundry.utils;

import com.shoes.shoeslaundry.data.model.Order;

public interface NotifikasiCallback {
    void onSuccess(Order newOrder);
    void onError(boolean failure);
}
