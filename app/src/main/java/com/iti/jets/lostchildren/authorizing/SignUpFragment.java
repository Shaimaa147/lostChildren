package com.iti.jets.lostchildren.authorizing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iti.jets.lostchildren.MainActivity;
import com.iti.jets.lostchildren.R;

import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.service.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Fadwa on 21/05/2018.
 */

public class SignUpFragment extends Fragment implements SignUpFragmentUpdate {

    private TextInputLayout firstNameLayout, lastNameLayout, emailLayout,
            passwordLayout, confirmPasswordLayout, addressLayout, phoneNumberLayout;
    private Button btnSignUp, btnGoToSignIn;
    private ImageView userImgView;
    private LostChildServiceClient service;
    private Validator validator;
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firstNameLayout = view.findViewById(R.id.txt_signup_fname);
        lastNameLayout = view.findViewById(R.id.txt_signup_lname);
        emailLayout = view.findViewById(R.id.txt_signup_email);
        passwordLayout = view.findViewById(R.id.txt_signup_password);
        confirmPasswordLayout = view.findViewById(R.id.txt_signup_confirmPassword);
        phoneNumberLayout = view.findViewById(R.id.txt_signup_phone);
        //userImgView = view.findViewById(R.id.userImgView);
        //TODO: Change address to Google Places
        addressLayout = view.findViewById(R.id.txt_signup_address);

        btnSignUp = view.findViewById(R.id.btn_signUp);
        btnGoToSignIn = view.findViewById(R.id.btn_goToSignIn);

        service = LostChildServiceClient.getInstance();
        validator = Validator.getInstance();
        validator.setContext(getContext());

        firstNameLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, firstNameLayout, FIRST_NAME);
            }
        });

        lastNameLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, lastNameLayout, LAST_NAME);
            }
        });

        emailLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, emailLayout, EMAIL);
            }
        });

        phoneNumberLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, phoneNumberLayout, PHONE);
            }
        });

        passwordLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, passwordLayout, PASSWORD);
            }
        });

        confirmPasswordLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, passwordLayout, confirmPasswordLayout);
            }
        });

        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment signInFragment = new SignInFragment();
                signInFragment.setArguments(getActivity().getIntent().getExtras());
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_home, signInFragment, HomeActivity.SIGN_IN_TAG).commit();
            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstNameLayout.setError(validator.validateField(FIRST_NAME, firstNameLayout));
                lastNameLayout.setError(validator.validateField(LAST_NAME, lastNameLayout));
                emailLayout.setError(validator.validateField(EMAIL, emailLayout));
                passwordLayout.setError(validator.validateField(PASSWORD, passwordLayout));
                confirmPasswordLayout.setError(validator.validateField(passwordLayout, confirmPasswordLayout));
                phoneNumberLayout.setError(validator.validateField(PHONE, phoneNumberLayout));

                if (firstNameLayout.getError() == "" && lastNameLayout.getError() == "" &&
                        emailLayout.getError() == "" && passwordLayout.getError() == "" &&
                        confirmPasswordLayout.getError() == "") {

                    User newUser = new User();
                    newUser.setFirstName(firstNameLayout.getEditText().getText().toString());
                    newUser.setLastName(lastNameLayout.getEditText().getText().toString());
                    newUser.setEmail(emailLayout.getEditText().getText().toString());
                    newUser.setPassword(passwordLayout.getEditText().getText().toString());
                    newUser.setPhone(phoneNumberLayout.getEditText().getText().toString());
                    newUser.setAddress(addressLayout.getEditText().getText().toString());

                    Toast.makeText(getContext(), "Singing Up....", Toast.LENGTH_LONG).show();
                    service.signUp(newUser);

                }
            }
        });
        return view;
    }

    void onTxtLayoutChange(boolean hasFocus, TextInputLayout inputLayout, String type) {

        if (type == EMAIL && !hasFocus) {
            String emailErrorMsg = validator.validateField(this, inputLayout);
            inputLayout.setError(emailErrorMsg);

        } else if (!hasFocus) {
            inputLayout.setError(validator.validateField(type, inputLayout));

        } else {
            inputLayout.setError("");
        }
    }

    //Password Confirmation Validation
    void onTxtLayoutChange(boolean hasFocus, TextInputLayout passwordLayout, TextInputLayout confirmPasswordLayout) {
        if (!hasFocus) {
            confirmPasswordLayout.setError(validator.validateField(passwordLayout, confirmPasswordLayout));
        } else {
            confirmPasswordLayout.setError("");
        }
    }

    @Override
    public void showDuplicatedEmailErrorMsg(boolean isDuplicated) {
        if (isDuplicated)
            emailLayout.setError(getContext().getString(R.string.duplicated_email));
        else
            emailLayout.setError("");
    }

    @Override
    public void uploadUserImage(User newUser) {


    }

    @Override
    public void redirectToMainActivity(String userJson) {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra(MainActivity.LOGGED_IN_USER_JSON, userJson);
        startActivity(i);
    }
}
