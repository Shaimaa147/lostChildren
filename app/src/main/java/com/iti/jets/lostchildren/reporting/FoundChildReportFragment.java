package com.iti.jets.lostchildren.reporting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
//import com.iti.jets.lostchildren.MainActivity;
import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.authorizing.Validator;
import com.iti.jets.lostchildren.googlePlaceService.GooglePlaceApi;
import com.iti.jets.lostchildren.googlePlaceService.PlaceArrayAdapter;
import com.iti.jets.lostchildren.homeScreen.MainActivity;
import com.iti.jets.lostchildren.image.ImageUpload;
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.service.LostChildServiceClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import static com.iti.jets.lostchildren.authorizing.SignUpFragment.*;
import static com.iti.jets.lostchildren.reporting.LostChildReportFragment.MOTHER_NAME;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Shemo on 6/11/2018.
 */


public class FoundChildReportFragment extends Fragment implements ReportingInterface,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    public static final String FROMAGE = "Minimum age";
    public static final String TOAGE = "Maximum age";
    private AutoCompleteTextView currentLocation;
    private AutoCompleteTextView findingLocation;
    private ImageView childImgView;
    private ImageView uploadImgBtn;
    private TextInputLayout firstNameWraapper;
    private TextInputLayout lastNameWrapper;
    private TextInputLayout motherNameWrapper;
    private TextInputLayout fromAgeWrapper;
    private TextInputLayout toAgeWrapper;
    private TextInputLayout descWrapper;
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
        View view = inflater.inflate(R.layout.fragment_found_child_report, container, false);

        currentLocation = (AutoCompleteTextView) view.findViewById(R.id.currentLocation);
        findingLocation = (AutoCompleteTextView) view.findViewById(R.id.foundLocation);

        googleApiInstance = GooglePlaceApi.getInstance(getContext());
        currentLocation.setThreshold(3);
        findingLocation.setThreshold(3);
        currentLocation.setOnItemClickListener(googleApiInstance);
        findingLocation.setOnItemClickListener(googleApiInstance);
        currentLocation.setAdapter(googleApiInstance.getmPlaceArrayAdapter());
        findingLocation.setAdapter(googleApiInstance.getmPlaceArrayAdapter());

        service = LostChildServiceClient.getInstance();
        validator = Validator.getInstance();
        validator.setContext(getContext());

        childImgView = (ImageView) view.findViewById(R.id.childImgView);
        uploadImgBtn = (ImageView) view.findViewById(R.id.uploadImgBtn);
        firstNameWraapper = (TextInputLayout) view.findViewById(R.id.firstNameWraapper);
        lastNameWrapper = (TextInputLayout) view.findViewById(R.id.lastNameWrapper);
        motherNameWrapper = (TextInputLayout) view.findViewById(R.id.motherNameWrapper);
        descWrapper = (TextInputLayout) view.findViewById(R.id.descWrapper);
        fromAgeWrapper = (TextInputLayout) view.findViewById(R.id.fromAgeWrapper);
        toAgeWrapper = (TextInputLayout) view.findViewById(R.id.toAgeWrapper);

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
        fromAgeWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, fromAgeWrapper, FROMAGE);
            }
        });
        toAgeWrapper.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onTxtLayoutChange(hasFocus, toAgeWrapper, TOAGE);
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

                // add image
                String imgPath = "/storage/emulated/0/pic.jpg";
                File imgFile = new File(imgPath);
                Uri imgUri = ImageUpload.getUriFromPath(imgPath, getActivity().getApplicationContext());
                //
                if (firstNameWraapper.getError() == "" && lastNameWrapper.getError() == "" &&
                        fromAgeWrapper.getError() == "" && toAgeWrapper.getError() == "") {
                    FoundChild child = new FoundChild();
                    child.setFirstName(firstNameWraapper.getEditText().getText().toString());
                    child.setLastName(lastNameWrapper.getEditText().getText().toString());
                    if(motherNameWrapper.getError() == "")
                        child.setMotherName(motherNameWrapper.getEditText().getText().toString());
                    child.setFromAge(Integer.parseInt(fromAgeWrapper.getEditText().getText().toString()));
                    child.setToAge(Integer.parseInt(toAgeWrapper.getEditText().getText().toString()));
                    if(genderRadioGroup.getCheckedRadioButtonId() == maleRadioBtn.getId())
                        child.setGender("m");
                    else
                        child.setGender("f");
                    if(!descWrapper.getEditText().getText().toString().equals(""))
                        child.setDescription(descWrapper.getEditText().getText().toString());
                    // date String ???
                    child.setFoundDate(dateBtn.getText());
                    if(!findingLocation.getText().toString().equals(""))
                        child.setFoundLocation(findingLocation.getText().toString());
                    if(!currentLocation.getText().toString().equals(""))
                        child.setCurrentLocation(currentLocation.getText().toString());
                    child.setReturned("no");
                    service.setContext(getActivity().getApplicationContext());
                    service.reportFound(child, MainActivity.currentUser.getEmail(), imgFile, imgUri);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public void setDate(View view) {
        calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FoundChildReportFragment.this,
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
//            Toast.makeText(getContext(), "Report has been saved successfully.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getContext(), "Report saving failed ........", Toast.LENGTH_LONG).show();
        }

    }

}
