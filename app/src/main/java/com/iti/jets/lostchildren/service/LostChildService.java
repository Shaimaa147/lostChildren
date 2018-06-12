package com.iti.jets.lostchildren.service;

import com.iti.jets.lostchildren.pojos.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Fadwa on 21/05/2018.
 */

public interface LostChildService {

    @POST("login.json")
    Call<LogInDataDto> signIn (@Body HashMap<String, String> emailAndPassword);

    @POST("register.json")
    Call<User> signUp (@Body User user);

    @Headers("Content-Type: application/json")
    @GET("emailCheck.json")
    Call<HashMap<String, String>> isEmailDuplicated(@Query("email") String email);

//    @Multipart
//    @POST("imageUploade.json")
//    void uploadImage(@Part("userImage") ,@Part("email"));

}