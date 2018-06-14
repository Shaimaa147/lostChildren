package com.iti.jets.lostchildren.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import com.iti.jets.lostchildren.authorizing.SignInFragmentUpdate;
import com.iti.jets.lostchildren.authorizing.SignUpFragment;
import com.iti.jets.lostchildren.authorizing.SignUpFragmentUpdate;
import com.iti.jets.lostchildren.pojos.User;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fadwa on 03/06/2018.
 */

public class LostChildServiceClient {

    
    private static final String serverIp = "192.168.1.4";
    public static final String BASE_URL = "http://" + serverIp + ":8084/LostChildren/rest/";
    public static final String JSON_MSG_STATUS = "status";
    public static final String JSON_MSG_FOUND_EMAIL = "FOUND";
    public static final String JSON_MSG_NOT_FOUND_EMAIL = "NOT_FOUND";
    public static final String JSON_MSG_SUCCESS = "SUCCESS";
    public static final String JSON_MSG_FAILED = "FAILED";

    private static LostChildServiceClient client;
    private LostChildService service;
    private SignUpFragmentUpdate signUpFragment;
    private SignInFragmentUpdate signInFragment;
    private Context context;

    private LostChildServiceClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()).create()).build();
        service = retrofit.create(LostChildService.class);
    }

    public static synchronized LostChildServiceClient getInstance() {
        if (client == null)
            client = new LostChildServiceClient();
        return client;
    }

    public void setSignUpFragment(SignUpFragmentUpdate signUpFragment) {
        this.signUpFragment = signUpFragment;
    }

    public void setSignInFragment(SignInFragmentUpdate signInFragment) {
        this.signInFragment = signInFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void signIn(String email, String password) {
        final HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(SignUpFragment.EMAIL, email);
        userData.put(SignUpFragment.PASSWORD, password);

        service.signIn(userData).enqueue(new Callback<LogInDataDto>() {
            @Override
            public void onResponse(Call<LogInDataDto> call, Response<LogInDataDto> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.i("signin status", response.body().getStatus());

                    if (response.body().getStatus().equals(JSON_MSG_SUCCESS)) {
                        signInFragment.showInvalidEmailOrPasswordMsg(false);
                        signInFragment.redirectToMainActivity(new Gson().toJson(userData));
                    }

                    //TODO: Handle When Wrong Email or Password
                    else if (response.body().getStatus().equals(JSON_MSG_FAILED))
                        signInFragment.showInvalidEmailOrPasswordMsg(true);
                }
            }

            @Override
            public void onFailure(Call<LogInDataDto> call, Throwable t) {
                Log.i("signin", "inc " + t.toString());
            }
        });
    }

    //TODO: Handle network errors
    public void signUp(final User user) {

        service.signUp(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200 && response != null) {
                    if (response.body().getEmail().equals(JSON_MSG_FOUND_EMAIL))
                        signUpFragment.showDuplicatedEmailErrorMsg(true);
                    else
                        signUpFragment.uploadUserImage(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("signup", t.toString());

            }
        });
    }

    //TODO: Handle network errors
    public void isEmailDuplicated(String email) {
        service.isEmailDuplicated(email).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response != null && response.code() == 200) {
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_FOUND_EMAIL))
                        signUpFragment.showDuplicatedEmailErrorMsg(true);
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_NOT_FOUND_EMAIL))
                        signUpFragment.showDuplicatedEmailErrorMsg(false);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.i("Email", "failed " + t.toString());
            }
        });
    }

    public void uploadUserImageToServer(final User newUser, File imgFile, Uri imgUri) {

        Log.i("img", context.getContentResolver().getType(imgUri));

        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, newUser.getEmail());
        RequestBody extensionPart = RequestBody.create(MultipartBody.FORM, context.getContentResolver().getType(imgUri));

        RequestBody imgPart = RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(imgUri)),
                imgFile);

        //MultipartBody.Part img = MultipartBody.Part.createFormData("image", imgFile.getName(), imgPart);

        service.uploadUserImage(emailPart, extensionPart, imgPart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("img", response.code() + "");
                Log.i("img", response.body().toString() + "");

                if (response.code() == 200 && response.body() != null) {
                    signUpFragment.redirectToMainActivity(new Gson().toJson(newUser));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("img", "Failed " + t.toString());

            }
        });
    }

}
