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
//import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.FROMAGE;
//import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.TOAGE;
import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.FROMAGE;
import static com.iti.jets.lostchildren.reporting.FoundChildReportFragment.TOAGE;
import static com.iti.jets.lostchildren.reporting.LostChildReportFragment.*;

/**
 * Created by Fadwa on 20/05/2018.
 */

public class Validator {

    private Context context;
    private static Validator validator;
    private final static String MIN_LENGTH = "min";
    private final static String MAX_LENGTH = "max";

    public static final int minPasswordLength = 6;
    public static final int maxPasswordLength = 30;
    public static final int maxNameLength = 20;
    public static final int maxEmailLength = 100;
    public static final int maxAddressLength = 200;
    public static final int phoneLength = 11;
    public static final int maxAge = 99;

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
    private boolean validateEmailFormat(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateLength(String lengthTypeToCheck, String strToValidate, int length){

        if(lengthTypeToCheck.equals(MIN_LENGTH) && strToValidate.length() >= length)
            return true;
        else if(lengthTypeToCheck.equals(MAX_LENGTH) && strToValidate.length() <= length)
            return true;
        return false;
    }

    private String validateName(String type, String name) {
        String errorMsg = "";
        switch (type) {
            case FIRST_NAME:
                errorMsg = context.getString(R.string.fname);
                break;
            case LAST_NAME:
                errorMsg = context.getString(R.string.lname);
                break;
            case MOTHER_NAME:
                errorMsg = context.getString(R.string.mname);
                break;
        }

        if(!containsLettersOnly(name))
            errorMsg += context.getString(R.string.letters_only);
        else if(!validateLength(MAX_LENGTH, name, maxNameLength))
            errorMsg += context.getString(R.string.is_too_long);
        else
            errorMsg = "";

        return errorMsg;
    }

    private String validatePassword(String password){
        String errorMsg = "";
        if(password.contains(" "))
            errorMsg = context.getString(R.string.password) + context.getString(R.string.contains_spaces);
        else if(!validateLength(MIN_LENGTH, password, minPasswordLength))
            errorMsg = context.getString(R.string.short_password);
        else if(!validateLength(MAX_LENGTH, password, maxPasswordLength))
            errorMsg = context.getString(R.string.password) + context.getString(R.string.is_too_long);;
        return errorMsg;
    }

    private String validatePhone(String phone) {
        String errorMsg = "";
        if(!TextUtils.isDigitsOnly(phone)
                || phone.length() != phoneLength
                || phone.charAt(0) != '0'
                || phone.charAt(1)!= '1')
            errorMsg = context.getString(R.string.invalid_phone_number);
        return errorMsg;

    }

    //First Name, Last Name, Phone, Password, AGE Validation
    public String validateField(String strToValidateType, TextInputLayout layoutToValidate) {
        String errorMsg = "";
        String strToValidate = layoutToValidate.getEditText().getText().toString();

        if(strToValidate.isEmpty() && !strToValidateType.equals(PHONE) && !strToValidate.equals(MOTHER_NAME)){
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
            if(strToValidateType.equals(FIRST_NAME)
                    || strToValidateType.equals(LAST_NAME)
                    || strToValidateType.equals(MOTHER_NAME))
                errorMsg = validateName(strToValidateType, strToValidate);

            else if(strToValidateType == PASSWORD)
                errorMsg = validatePassword(strToValidate);

            else if((strToValidateType == PHONE || strToValidateType.equals(Reporter_PHONE)))
                errorMsg = validatePhone(strToValidate);

            else if((strToValidateType.equals(AGE) || strToValidateType.equals(FROMAGE) || strToValidateType.equals(TOAGE))
                    && !TextUtils.isDigitsOnly(strToValidate)
                    && Integer.parseInt(strToValidate) > 0
                    && Integer.parseInt(strToValidate) >= maxAge)
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
    public String validateField(TextInputLayout layoutToValidate) {
        String errorMsg = "";
        String strToValidate = layoutToValidate.getEditText().getText().toString().trim();

        if (strToValidate.isEmpty())
            errorMsg = context.getString(R.string.email) + context.getString(R.string.is_required);
        else if (!validateEmailFormat(strToValidate))
            errorMsg = context.getString(R.string.invalid_email);
        else if(validateLength(MAX_LENGTH, strToValidate, maxEmailLength)) ;
//        else {
//            LostChildServiceClient.getInstance().setSignUpFragment(signUpFragment);
//            LostChildServiceClient.getInstance().isEmailDuplicated(strToValidate);
//        }

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
