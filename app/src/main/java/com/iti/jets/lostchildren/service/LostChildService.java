package com.iti.jets.lostchildren.service;

import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.pojos.SearchResult;
import com.iti.jets.lostchildren.pojos.SignInResponse;
import com.iti.jets.lostchildren.pojos.User;

import java.util.ArrayList;
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
    Call<SignInResponse> signIn (@Body HashMap<String, String> emailAndPassword);

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
   @GET("lostRetrieve.json")
   Call<ArrayList<LostChild>> retriveLost();
   @GET("foundRetrieve.json")
   Call<ArrayList<FoundChild>> retriveFound();

    @Multipart
    @POST("lostReport.json")
    Call<HashMap<String, String>> reportLost (@Part("lost") RequestBody lostChild,
                                              @Part("email") RequestBody email,
                                              @Part("image\"; filename=\"lost.png ") RequestBody image,
                                              @Part("extension") RequestBody extension);

    @Multipart
    @POST("foundReport.json")
    Call<HashMap<String, String>> reportFound (@Part("found") RequestBody foundChild,
                                               @Part("email") RequestBody email,
                                               @Part("image\"; filename=\"lost.png ") RequestBody image,
                                               @Part("extension") RequestBody extension);
    @Multipart
    @POST("search.json")
    Call<SearchResult> searchCild (@Part("firstName") RequestBody fName,@Part("lastName")RequestBody lName,@Part("gender") RequestBody gender ,@Part("userImage") RequestBody img);

}
