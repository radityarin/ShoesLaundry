package com.shoes.shoeslaundry.utils.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAhOdht70:APA91bFFKkC9htmo0rrJqUqW3HfKSWfxQzU2ctMLSQL4nQWNCiX_5xJxiaM1hm2fZDRkMCNkOVENeKPT_A_-s93d2UXRYXQbs-2YykqqabnGfar8LFLKsrd4w9lsPewa5emUipuwKWjI"
    })


    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
