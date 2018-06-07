package com.iti.jets.lostchildren.authorizing;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iti.jets.lostchildren.R;

import com.iti.jets.lostchildren.service.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fadwa on 20/05/2018.
 */

public class SignInFragment extends Fragment implements AuthFragmentsHome {

    private Button btnSignIn, btnGoToSignUp;
    private TextView errorMsg;
    private TextInputLayout emailLayout, passwordLayout;
    private LostChildServiceClient service;
    private Validator validator;
    private LogInDataDto logInData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailLayout = view.findViewById(R.id.txt_signin_email);
        passwordLayout = view.findViewById(R.id.txt_signin_password);
        errorMsg = view.findViewById(R.id.errorMsg);
        btnGoToSignUp = view.findViewById(R.id.btn_goToSignUp);
        btnSignIn = view.findViewById(R.id.btn_signin);

        validator = Validator.getInstance();
        service = LostChildServiceClient.getInstance();
        logInData = new LogInDataDto();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();
                boolean isEmailValid = validator.validateEmailFormat(email);
                boolean isPasswordValid = validator.validateLength(password, HomeActivity.minPasswordLength);

                emailLayout.setError("");
                passwordLayout.setError("");

                if (isEmailValid && isPasswordValid) {
                    service.setSignInFragment(SignInFragment.this);
                    service.signIn(email, password);
                }
                else {
                    if (isPasswordValid)
                        passwordLayout.setError("");
                    else
                        passwordLayout.setError("Short password");

                    if (isEmailValid)
                        emailLayout.setError("");
                    else
                        emailLayout.setError("Invalid Email Format");
                }
            }
        });

        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();
                signUpFragment.setArguments(getActivity().getIntent().getExtras());
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_home, signUpFragment, HomeActivity.SIGN_UP_TAG).commit();
            }
        });

        return view;
    }

    @Override
    public void redirectToMainActivity(String userJson) {

    }
}
