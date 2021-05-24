package com.example.mychatapplication.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:Key=AAAAdDx9H6Q:APA91bEQ-RTQ-Cm46-6c083nkNRrOblcSL-Hi7ljFYimacY1imPP-Q3bxtuXibO7iaWO4SyWCCQAPv-JdgCW3DKuo6PCu_gZeKOs5i_fFSFubfCD2GLzqlI0lfVIqxgdb0R6x8qBsC1W"

    })
    @POST("from/send")
    Call<Response> sendNotification(@Body Sender body);
}
