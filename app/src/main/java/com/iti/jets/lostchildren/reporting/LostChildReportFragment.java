package com.iti.jets.lostchildren.reporting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.authorizing.Validator;
import com.iti.jets.lostchildren.googlePlaceService.GooglePlaceApi;
import com.iti.jets.lostchildren.homeScreen.MainActivity;
import com.iti.jets.lostchildren.image.ImageSelection;
import com.iti.jets.lostchildren.image.ImageUpload;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.service.LostChildServiceClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import static com.iti.jets.lostchildren.authorizing.SignUpFragment.*;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Shemo on 6/11/2018.
 */

public class LostChildReportFragment extends Fragment implements ReportingInterface,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    public static final String AGE = "Age";
    public static final String Reporter_PHONE = "Reporter Phone";
    public static final String MOTHER_NAME = "Mother name";
    public static final int maxChildAge = 99;
    /*Place*/
    private AutoCompleteTextView originalAddress;
    private AutoCompleteTextView lostLocation;
    /*Place*/
    private ImageView childImgView;
    private ImageView uploadImgBtn;
    private TextInputLayout firstNameWraapper;
    private TextInputLayout lastNameWrapper;
    private TextInputLayout motherNameWrapper;
    private TextInputLayout ageWrapper;
    private TextInputLayout descWrapper;
    private TextInputLayout phoneWrapper;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioBtn;
    private RadioButton femaleRadioBtn;
    private Button reportBtn;
    private Calendar calendar;
    private  Button dateBtn;
    private LostChildServiceClient service;
    private Validator validator;
    private GooglePlaceApi googleApiInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_child_report, container, false);

        originalAddress = (AutoCompleteTextView) view.findViewById(R.id.originalAddress);
        lostLocation = (AutoCompleteTextView) view.findViewById(R.id.lostLocation);
        googleApiInstance = GooglePlaceApi.getInstance(getContext());

        originalAddress.setThreshold(3);
        lostLocation.setThreshold(3);
        originalAddress.setOnItemClickListener(googleApiInstance);
        lostLocation.setOnItemClickListener(googleApiInstance);

        originalAddress.setAdapter(googleApiInstance.getmPlaceArrayAdapter());
        lostLocation.setAdapter(googleApiInstance.getmPlaceArrayAdapter());

        service = LostChildServiceClient.getInstance();
        validator = Validator.getInstance();
        validator.setContext(getContext());

        childImgView = (ImageView) view.findViewById(R.id.childImgView);
        uploadImgBtn = (ImageView) view.findViewById(R.id.uploadImgBtn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelection.ShowCustomDialog(getContext(),
                        (ViewGroup) getActivity().findViewById(R.id.lost_fragment_layout),childImgView);
            }
        });
        firstNameWraapper = (TextInputLayout) view.findViewById(R.id.firstNameWraapper);
        lastNameWrapper = (TextInputLayout) view.findViewById(R.id.lastNameWrapper);
        motherNameWrapper = (TextInputLayout) view.findViewById(R.id.motherNameWrapper);
        descWrapper = (TextInputLayout) view.findViewById(R.id.descWrapper);
        ageWrapper = (TextInputLayout) view.findViewById(R.id.ageWrapper);
        phoneWrapper = (TextInputLayout) view.findViewById(R.id.phoneWrapper);

        firstNameWraapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, firstNameWraapper, FIRST_NAME);
            }
        });
        lastNameWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, lastNameWrapper, LAST_NAME);
            }
        });
        motherNameWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, motherNameWrapper, MOTHER_NAME);
            }
        });
        ageWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, ageWrapper, AGE);
            }
        });
        phoneWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, phoneWrapper, Reporter_PHONE);
            }
        });

        genderRadioGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        maleRadioBtn = (RadioButton) view.findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = (RadioButton) view.findViewById(R.id.femaleRadioBtn);
        genderRadioGroup.check(maleRadioBtn.getId());

        dateBtn = (Button) view.findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        reportBtn = (Button) view.findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameWraapper.setError(validator.validateField(FIRST_NAME, firstNameWraapper));
                lastNameWrapper.setError(validator.validateField(LAST_NAME, lastNameWrapper));
                motherNameWrapper.setError(validator.validateField(MOTHER_NAME, motherNameWrapper));
                phoneWrapper.setError(validator.validateField(PHONE, phoneWrapper));

                // add image
                String imgPath = "/storage/emulated/0/pic.jpg";
                File imgFile = new File(imgPath);
                Uri imgUri = ImageUpload.getUriFromPath(imgPath, getActivity().getApplicationContext());
                //
                if (firstNameWraapper.getError() == "" && lastNameWrapper.getError() == "" &&
                        phoneWrapper.getError() == "" && ageWrapper.getError() == "") {
                    LostChild child = new LostChild();
                    child.setFirstName(firstNameWraapper.getEditText().getText().toString());
                    child.setLastName(lastNameWrapper.getEditText().getText().toString());
                    if(motherNameWrapper.getError() == "")
                        child.setMotherName(motherNameWrapper.getEditText().getText().toString());
                    child.setAge(Integer.parseInt(ageWrapper.getEditText().getText().toString()));
                    if(genderRadioGroup.getCheckedRadioButtonId() == maleRadioBtn.getId())
                        child.setGender("m");
                    else
                        child.setGender("f");
                    child.setPhone(phoneWrapper.getEditText().getText().toString());
                    if(!descWrapper.getEditText().getText().toString().equals(""))
                        child.setDescription(descWrapper.getEditText().getText().toString());
                    child.setLostDate(dateBtn.getText());
                    if(!lostLocation.getText().toString().equals(""))
                        child.setLostLocation(lostLocation.getText().toString());
                    if(!originalAddress.getText().toString().equals(""))
                        child.setOrginalAddress(originalAddress.getText().toString());
                    child.setReturned("no");
                    service.setContext(getActivity().getApplicationContext());
//                    service.setLostChildReportFragment(LostChildReportFragment.this);
                    service.reportLost(child, MainActivity.currentUser.getEmail(), imgFile, imgUri);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {

        super.onActivityResult(arg0, arg1, arg2);
        ImageSelection.customOnActivityResult(arg0, arg1, arg2, getContext(),childImgView);
    }


    public void setDate(View view) {
        calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                LostChildReportFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year,
                          int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateBtn.setText(date);
    }

    void onTxtLayoutChange(boolean hasFocus, TextInputLayout inputLayout, String type) {

        if (!hasFocus) {
            inputLayout.setError(validator.validateField(type, inputLayout));

        } else {
            inputLayout.setError("");
        }
    }

    @Override
    public void redirectToHome(Boolean didSuccess) {

        if(didSuccess) {
            Toast.makeText(getContext(), "Report has been saved successfully.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getContext(), "Report saving failed ........", Toast.LENGTH_LONG).show();
        }


    }
}
