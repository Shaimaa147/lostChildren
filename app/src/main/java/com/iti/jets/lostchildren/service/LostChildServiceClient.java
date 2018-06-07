package com.iti.jets.lostchildren.service;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import com.iti.jets.lostchildren.authorizing.AuthFragmentsHome;
import com.iti.jets.lostchildren.authorizing.SignUpFragmentUpdate;
import com.iti.jets.lostchildren.pojos.User;

import java.util.HashMap;
import java.util.Map;

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
    public static final String JSON_MSG_FAILED_MSG = "SUCCESS";

    private static LostChildServiceClient client;
    private LostChildService service;
    private SignUpFragmentUpdate signUpFragment;
    private AuthFragmentsHome signInFragment;

    private LostChildServiceClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    public void setSignInFragment(AuthFragmentsHome signInFragment) {
        this.signInFragment = signInFragment;
    }

    public void signIn (String email, String password) {
        final HashMap<String, String> userData = new HashMap<String, String>();
        userData.put("email", email);
        userData.put("password", password);

        service.signIn(userData).enqueue(new Callback<LogInDataDto>() {
            @Override
            public void onResponse(Call<LogInDataDto> call, Response<LogInDataDto> response) {
                if(response.code() == 200 && response.body() != null){
                    Log.i("signin", response.body().getStatus());
                    signInFragment.redirectToMainActivity(new Gson().toJson(userData));
                }
            }

            @Override
            public void onFailure(Call<LogInDataDto> call, Throwable t) {
                Log.i("signin", "inc " + t.toString());

            }
        });
    }

    //TODO: Handle network errors
    public void signUp (final User user) {

        service.signUp(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    String userJson = new Gson().toJson(response.body());
                    signUpFragment.redirectToMainActivity(userJson);
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
                if(response != null && response.code() == 200) {
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
}
