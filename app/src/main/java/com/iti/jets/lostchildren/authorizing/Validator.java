package com.iti.jets.lostchildren.authorizing;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;

import com.iti.jets.lostchildren.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iti.jets.lostchildren.service.LostChildServiceClient;

import static com.iti.jets.lostchildren.authorizing.SignUpFragment.*;
import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.FROMAGE;
import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.TOAGE;
import static com.iti.jets.lostchildren.reporting.LostChildReportFragment.*;

/**
 * Created by Fadwa on 20/05/2018.
 */

public class Validator {

    private Context context;
    private static Validator validator;

    private Validator() {}

    public static synchronized Validator getInstance() {
        if (validator == null)
            validator = new Validator();
        return validator;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //TODO: Double Check The Pattern
    public boolean validateEmailFormat(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateLength(String strToValidate, int minLength){
        if(strToValidate.length() >= minLength)
            return true;
        return false;
    }

    //First Name, Last Name, Phone, Password,AGE Validation
    //TODO: Validate Max Length (DataBase)
    public String validateField(String strToValidateType, TextInputLayout layoutToValidate) {
        String errorMsg = "";
        //TODO: Handle Spaces
        String strToValidate = layoutToValidate.getEditText().getText().toString().trim();

        if(strToValidate.isEmpty() && !strToValidateType.equals(PHONE) ){
            switch (strToValidateType){
                case FIRST_NAME:
                    errorMsg = context.getString(R.string.fname);
                    break;
                case LAST_NAME:
                    errorMsg = context.getString(R.string.lname);
                    break;
                case PASSWORD:
                    errorMsg = context.getString(R.string.password);
                    break;
                case AGE:
                    errorMsg = context.getString(R.string.age);
                    break;
                case Reporter_PHONE:
                    errorMsg = context.getString(R.string.reporter_phone);
                    break;
            }
            errorMsg += context.getString(R.string.is_required);
        }

        else {
            if(strToValidateType == FIRST_NAME  && !containsLettersOnly(strToValidate))
                errorMsg = context.getString(R.string.fname) + context.getString(R.string.letters_only);

            else if(strToValidateType == LAST_NAME);

            else if(strToValidateType == MOTHER_NAME  && !containsLettersOnly(strToValidate))
                errorMsg = context.getString(R.string.mname) + context.getString(R.string.letters_only);

            else if(strToValidateType == PASSWORD && !validateLength(strToValidate, HomeActivity.minPasswordLength))
                errorMsg = context.getString(R.string.short_password);

            else if((strToValidateType == PHONE || strToValidateType.equals(Reporter_PHONE))
                    && !TextUtils.isDigitsOnly(strToValidate))
                errorMsg = context.getString(R.string.invalid_phone_number);

            else if((strToValidateType.equals(AGE) || strToValidateType.equals(FROMAGE) || strToValidateType.equals(TOAGE))
                    && !TextUtils.isDigitsOnly(strToValidate) && Integer.parseInt(strToValidate) > 0)
                errorMsg = context.getString(R.string.invalid_age);
        }
        return errorMsg;
    }

    //Password Confirmation Validation
    public String validateField(TextInputLayout passwordLayout, TextInputLayout confirmPasswordLayout) {
        String errorMsg = "";
        if(!passwordLayout.getEditText().getText().toString().equals(confirmPasswordLayout.getEditText().getText().toString()))
            errorMsg = context.getString(R.string.invalid_password_confirmation);
        return errorMsg;
    }


    //Email Validation
    //TODO: Validate Max Length (DataBase)
    public String validateField(SignUpFragment signUpFragment, TextInputLayout layoutToValidate) {
        String errorMsg = "";
        String strToValidate = layoutToValidate.getEditText().getText().toString().trim();

        if (strToValidate.isEmpty())
            errorMsg = context.getString(R.string.email) + context.getString(R.string.is_required);
        else if (!validateEmailFormat(strToValidate))
            errorMsg = context.getString(R.string.invalid_email);
        else {
            LostChildServiceClient.getInstance().setSignUpFragment(signUpFragment);
            LostChildServiceClient.getInstance().isEmailDuplicated(strToValidate);
        }

        return errorMsg;
    }

    private boolean containsLettersOnly(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
