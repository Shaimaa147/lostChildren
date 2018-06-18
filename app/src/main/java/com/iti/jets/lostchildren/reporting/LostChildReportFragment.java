package com.iti.jets.lostchildren.reporting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.iti.jets.lostchildren.homeScreen.MainActivity;
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
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    public static final String AGE = "Age";
    public static final String Reporter_PHONE = "Reporter Phone";
    public static final String MOTHER_NAME = "Mother name";
    public static final int maxChildAge = 99;
    /*Place*/
    private static final String LOG_TAG = "LostChildFragmment";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView originalAddress;
    private AutoCompleteTextView lostLocation;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_child_report, container, false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        originalAddress = (AutoCompleteTextView) getActivity().findViewById(R.id.originalAddress);
        lostLocation = (AutoCompleteTextView) getActivity().findViewById(R.id.lostLocation);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this).addConnectionCallbacks(this).build();
        originalAddress.setThreshold(3);
        lostLocation.setThreshold(3);
        originalAddress.setOnItemClickListener(mAutocompleteClickListener);
        lostLocation.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        originalAddress.setAdapter(mPlaceArrayAdapter);
        lostLocation.setAdapter(mPlaceArrayAdapter);

        service = LostChildServiceClient.getInstance();
        validator = Validator.getInstance();
        validator.setContext(getContext());

        childImgView = (ImageView) getActivity().findViewById(R.id.childImgView);
        uploadImgBtn = (ImageView) getActivity().findViewById(R.id.uploadImgBtn);
        firstNameWraapper = (TextInputLayout) getActivity().findViewById(R.id.firstNameWraapper);
        lastNameWrapper = (TextInputLayout) getActivity().findViewById(R.id.lastNameWrapper);
        motherNameWrapper = (TextInputLayout) getActivity().findViewById(R.id.motherNameWrapper);
        descWrapper = (TextInputLayout) getActivity().findViewById(R.id.descWrapper);
        ageWrapper = (TextInputLayout) getActivity().findViewById(R.id.ageWrapper);
        phoneWrapper = (TextInputLayout) getActivity().findViewById(R.id.phoneWrapper);

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

        genderRadioGroup = (RadioGroup) getActivity().findViewById(R.id.genderRadioGroup);
        maleRadioBtn = (RadioButton) getActivity().findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = (RadioButton) getActivity().findViewById(R.id.femaleRadioBtn);
        genderRadioGroup.check(maleRadioBtn.getId());

        dateBtn = (Button) getActivity().findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        reportBtn = (Button) getActivity().findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameWraapper.setError(validator.validateField(FIRST_NAME, firstNameWraapper));
                lastNameWrapper.setError(validator.validateField(LAST_NAME, lastNameWrapper));
                motherNameWrapper.setError(validator.validateField(MOTHER_NAME, motherNameWrapper));
                phoneWrapper.setError(validator.validateField(PHONE, phoneWrapper));

                // add image
                String imgPath = "/storage/sdcard0/pictures/ww.jpg";
                File imgFile = new File(imgPath);
                Uri imgUri = getUriFromPath(imgPath, getActivity().getApplicationContext());
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
                    // date String ???
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

    private Uri getUriFromPath(String filePath, Context context) {
        long photoId;
        Uri photoUri = MediaStore.Images.Media.getContentUri("external");

        String[] projection = {MediaStore.Images.ImageColumns._ID};
        // TODO This will break if we have no matching item in the MediaStore.
        Cursor cursor = context.getContentResolver().query(photoUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[] { filePath }, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        photoId = cursor.getLong(columnIndex);

        cursor.close();
        return Uri.parse(photoUri.toString() + "/" + photoId);
    }
}
