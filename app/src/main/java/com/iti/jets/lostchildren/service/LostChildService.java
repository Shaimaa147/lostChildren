package com.iti.jets.lostchildren.service;

import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.pojos.User;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @Multipart
    @POST("imageUploade.json")
    Call<ResponseBody> uploadUserImage(
            @Part("email") RequestBody email,
            @Part("extension") RequestBody extension,
            @Part("userImage\"; filename=\"pp.png ") RequestBody userImage
    );

    @Multipart
    @POST("lostReport.json")
    Call<HashMap<String, String>> reportLost (@Part("child") RequestBody lostChild, @Part("email") RequestBody email,
                                              @Part("extension") RequestBody extension,
                                              @Part("image\"; filename=\"pp.png ") RequestBody image);

    @POST("foundReport.json")
    Call<HashMap<String, String>> reportFound (@Body FoundChild lost, @Query("email") String email,
                                               @Part("image") MultipartBody.Part image);

}
