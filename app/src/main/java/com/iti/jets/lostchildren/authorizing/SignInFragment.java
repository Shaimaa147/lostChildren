package com.iti.jets.lostchildren.authorizing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iti.jets.lostchildren.homeScreen.MainActivity;
import com.iti.jets.lostchildren.R;

import com.iti.jets.lostchildren.service.*;

/**
 * Created by Fadwa on 20/05/2018.
 */

public class SignInFragment extends Fragment implements SignInFragmentUpdate {

    private Button btnSignIn, btnGoToSignUp;
    private TextView errorMsg;
    private TextInputLayout emailLayout, passwordLayout;
    private LostChildServiceClient service;
    private Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailLayout = view.findViewById(R.id.txt_signin_email);
        passwordLayout = view.findViewById(R.id.txt_signin_password);
        errorMsg = view.findViewById(R.id.errorMsg);
        btnGoToSignUp = view.findViewById(R.id.btn_goToSignUp);
        btnSignIn = view.findViewById(R.id.btn_signin);
        errorMsg = view.findViewById(R.id.errorMsg);

        validator = Validator.getInstance();
        service = LostChildServiceClient.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLayout.setError(validator.validateField(emailLayout));
                passwordLayout.setError(validator.validateField(SignUpFragment.PASSWORD, passwordLayout));

                if (emailLayout.getError().toString().isEmpty() && passwordLayout.getError().toString().isEmpty()) {


//                if (isEmailValid && isPasswordValid) {
//                    service.setSignInFragment(SignInFragment.this);
//                    service.signIn(email, password);
//                }
//                else {
//                    if (isPasswordValid)
//                        passwordLayout.setError("");
//                    else
//                        passwordLayout.setError("Short password");
//
//                    if (isEmailValid)
//                        emailLayout.setError("");
//                    else
//                        emailLayout.setError("Invalid Email Format");
//                }

                    service.setSignInFragment(SignInFragment.this);
                    service.signIn(emailLayout.getEditText().getText().toString(),
                            passwordLayout.getEditText().getText().toString());
                }

            }
        });

        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();
                signUpFragment.setArguments(getActivity().getIntent().getExtras());
                getActivity()
                        .getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home, signUpFragment, HomeActivity.SIGN_UP_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void redirectToMainActivity(String userJson) {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtra(MainActivity.LOGGED_IN_USER_JSON, userJson);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void showInvalidEmailOrPasswordMsg(boolean invalidEmailOrPassword) {
        if(invalidEmailOrPassword)
            errorMsg.setVisibility(View.VISIBLE);
        else
            errorMsg.setVisibility(View.INVISIBLE);
    }
}
