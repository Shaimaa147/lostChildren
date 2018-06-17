package com.iti.jets.lostchildren.reporting;

import android.content.Intent;
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
import com.iti.jets.lostchildren.pojos.FoundChild;
import com.iti.jets.lostchildren.pojos.LostChild;
import com.iti.jets.lostchildren.service.LostChildServiceClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import static com.iti.jets.lostchildren.authorizing.SignUpFragment.*;
import static com.iti.jets.lostchildren.reporting.LostChildReportFragment.MOTHER_NAME;

import java.util.Calendar;

/**
 * Created by Shemo on 6/11/2018.
 */

public class FoundChildReportFragment extends Fragment implements ReportingInterface,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    public static final String FROMAGE = "Minimum age";
    public static final String TOAGE = "Maximum age";
    /*Place*/
    private static final String LOG_TAG = "FoundChildFragmment";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView currentLocation;
    private AutoCompleteTextView findingLocation;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW =
            new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    /*Place*/
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_child_report, container, false);
        service = LostChildServiceClient.getInstance();
        validator = Validator.getInstance();
        validator.setContext(getContext());

        childImgView = (ImageView) getActivity().findViewById(R.id.childImgView);
        uploadImgBtn = (ImageView) getActivity().findViewById(R.id.uploadImgBtn);
        firstNameWraapper = (TextInputLayout) getActivity().findViewById(R.id.firstNameWraapper);
        lastNameWrapper = (TextInputLayout) getActivity().findViewById(R.id.lastNameWrapper);
        motherNameWrapper = (TextInputLayout) getActivity().findViewById(R.id.motherNameWrapper);
        descWrapper = (TextInputLayout) getActivity().findViewById(R.id.descWrapper);
        fromAgeWrapper = (TextInputLayout) getActivity().findViewById(R.id.fromAgeWrapper);
        toAgeWrapper = (TextInputLayout) getActivity().findViewById(R.id.toAgeWrapper);

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

        genderRadioGroup = (RadioGroup) getActivity().findViewById(R.id.genderRadioGroup);
        maleRadioBtn = (RadioButton) getActivity().findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = (RadioButton) getActivity().findViewById(R.id.femaleRadioBtn);
        genderRadioGroup.check(maleRadioBtn.getId());

        dateBtn = (Button) getActivity().findViewById(R.id.dateBtn);
        reportBtn = (Button) getActivity().findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameWraapper.setError(validator.validateField(FIRST_NAME, firstNameWraapper));
                lastNameWrapper.setError(validator.validateField(LAST_NAME, lastNameWrapper));
                motherNameWrapper.setError(validator.validateField(MOTHER_NAME, motherNameWrapper));
                
                // add image
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

//                    service.reportFound(child, MainActivity.currentUser.getEmail(), );
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentLocation = (AutoCompleteTextView) getActivity().findViewById(R.id.currentLocation);
        findingLocation = (AutoCompleteTextView) getActivity().findViewById(R.id.foundLocation);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this).addConnectionCallbacks(this).build();
        currentLocation.setThreshold(3);
        findingLocation.setThreshold(3);
        currentLocation.setOnItemClickListener(mAutocompleteClickListener);
        findingLocation.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        currentLocation.setAdapter(mPlaceArrayAdapter);
        findingLocation.setAdapter(mPlaceArrayAdapter);
    }

    /*place*/
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getContext(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            if (attributions != null) {
                Log.i(LOG_TAG, "attributions: " + Html.fromHtml(attributions.toString()));
            }
        }
    };
    /*place*/

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
            Toast.makeText(getContext(), "Report has been saved successfully.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "Report saving failed ........", Toast.LENGTH_LONG).show();
        }

    }
}