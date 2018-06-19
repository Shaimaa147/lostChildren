package com.iti.jets.lostchildren.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import com.iti.jets.lostchildren.authorizing.SignInFragmentUpdate;
import com.iti.jets.lostchildren.authorizing.SignUpFragment;
import com.iti.jets.lostchildren.authorizing.SignUpFragmentUpdate;
import com.iti.jets.lostchildren.homeScreen.FragmentFound;
import com.iti.jets.lostchildren.homeScreen.FragmentLost;
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.pojos.SignInResponse;
import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.reporting.FoundChildReportFragment;
import com.iti.jets.lostchildren.reporting.LostChildReportFragment;
import java.io.File;
import java.util.ArrayList;
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


    private static final String serverIp = "10.0.1.85";


    public static final String BASE_URL = "http://" + serverIp + ":8084/LostChildren/rest/";
    public static final String JSON_MSG_STATUS = "status";
    public static final String JSON_MSG_FOUND_EMAIL = "FOUND";
    public static final String JSON_MSG_NOT_FOUND_EMAIL = "NOT_FOUND";
    public static final String JSON_MSG_SUCCESS = "SUCCESS";
    public static final String JSON_MSG_FAILED = "FAILED";
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static String getServerIp() {
        return serverIp;
    }

    private static LostChildServiceClient client;
    private LostChildService service;
    private SignUpFragmentUpdate signUpFragment;
    private SignInFragmentUpdate signInFragment;
    private FragmentLost lostFragment;
    private FragmentFound fragmentFound;

    public void setFragmentFound(FragmentFound fragmentFound) {
        this.fragmentFound = fragmentFound;
    }


    private LostChildReportFragment lostChildReportFragment;
    private FoundChildReportFragment foundChildReportFragment;
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

    public void setLostFragment(FragmentLost lostFragment) {
        this.lostFragment = lostFragment;
    }

    public void setSignInFragment(SignInFragmentUpdate signInFragment) {
        this.signInFragment = signInFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void signIn(String email, String password) {
        final HashMap<String, String> userData = new HashMap();
        userData.put(SignUpFragment.EMAIL, email);
        userData.put(SignUpFragment.PASSWORD, password);

        service.signIn(userData).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.code() == 200 && response.body() != null) {

                    if (response.body().getStatus().equals(JSON_MSG_SUCCESS)) {
                        signInFragment.showInvalidEmailOrPasswordMsg(false);
                        signInFragment.redirectToMainActivity(new Gson().toJson(response.body().getUser()));
                    }

                    else if (response.body().getStatus().equals(JSON_MSG_FAILED))
                        signInFragment.showInvalidEmailOrPasswordMsg(true);
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Log.i("signin", "inc " + t.toString());
            }
        });
    }

    public void setLostChildReportFragment(LostChildReportFragment lostChildReportFragment) {
        this.lostChildReportFragment = lostChildReportFragment;
    }

    public void setFoundChildReportFragment(FoundChildReportFragment foundChildReportFragment) {
        this.foundChildReportFragment = foundChildReportFragment;
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

    public void reportLost(final LostChild child, String email, File imgFile, Uri imgUri) {

        String lostChildJson = new Gson().toJson(child);
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, email);
        RequestBody childPart = RequestBody.create(JSON_MEDIA_TYPE, lostChildJson);
        RequestBody extensionPart = RequestBody.create(MultipartBody.FORM, context.getContentResolver().getType(imgUri));
        RequestBody imgPart = RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(imgUri)),
                imgFile);
//        RequestBody childPart = RequestBody.create(MediaType.parse("multipart/form-data"), new Gson().toJson(child));

        service.reportLost(childPart, emailPart, imgPart,extensionPart).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response != null && response.code() == 200) {
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_SUCCESS)) {
                        Log.i("LostReporting", "report lost status success");
                        lostChildReportFragment.redirectToHome(true);
                    }
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_FAILED)) {
                        Log.i("LostReporting", "report lost status failed");
                        lostChildReportFragment.redirectToHome(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.i("LostReporting", "failed " + t.toString());
            }
        });
    }
    public void retriveLosts(){

        service.retriveLost().enqueue(new Callback<ArrayList<LostChild>>() {

            @Override
            public void onResponse(Call<ArrayList<LostChild>> call, Response<ArrayList<LostChild>> response) {

                if (response.code() == 200 && response != null) {
                    lostFragment.updateList(response.body(),true);
                    Log.i("sec","callllled" + response.body().size());
                }

            }

            @Override
            public void onFailure(Call<ArrayList<LostChild>> call, Throwable t) {

            }
        });


    }
 public  void retriveFounds(){

        service.retriveFound().enqueue(new Callback<ArrayList<FoundChild>>() {
            @Override
            public void onResponse(Call<ArrayList<FoundChild>> call, Response<ArrayList<FoundChild>> response) {
                if (response.code() == 200 && response != null) {
                    Log.i("first","callllled" + response.body().size());
                     fragmentFound.updateList(response.body(), true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FoundChild>> call, Throwable t) {

            }
        });

   }

    public void reportFound(final FoundChild child, String email, File imgFile, Uri imgUri) {

        String foundChildJson = new Gson().toJson(child);
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, email);
        RequestBody childPart = RequestBody.create(JSON_MEDIA_TYPE, foundChildJson);
        RequestBody extensionPart = RequestBody.create(MultipartBody.FORM, context.getContentResolver().getType(imgUri));
        RequestBody imgPart = RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(imgUri)),
                imgFile);

        service.reportFound(childPart, emailPart, imgPart,extensionPart).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response != null && response.code() == 200) {
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_SUCCESS)) {
                        Log.i("LostReporting", "report found status success");
                        foundChildReportFragment.redirectToHome(true);
                    }
                    if (response.body().get(JSON_MSG_STATUS).equals(JSON_MSG_FAILED)) {
                        Log.i("LostReporting", "report found status failed");
                        foundChildReportFragment.redirectToHome(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.i("FoundReporting", "failed " + t.toString());
            }
        });
    }
//    public  void searchClidren(String firstName,String  lastName , String gender ,  File imgFile, Uri imgUri ){
//        RequestBody fnamePart = RequestBody.create(MultipartBody.FORM, firstName);
//        RequestBody  lastNamePart = RequestBody.create(JSON_MEDIA_TYPE, lastName);
//        RequestBody extensionPart = RequestBody.create(MultipartBody.FORM, context.getContentResolver().getType(imgUri));
//        RequestBody imgPart = RequestBody.create(
//                MediaType.parse(context.getContentResolver().getType(imgUri)),
//                imgFile);
//
//     service.searchCild(firstName,l).enqueue();
//
//    }
}
