package com.zing.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zing.R;
import com.zing.adapter.CountryAdapter;
import com.zing.model.request.CompleteProfileRequest;
import com.zing.model.response.RegisterResponse.RegisterResponse;
import com.zing.model.response.countryListResponse.CountryResponse;
import com.zing.model.response.countryListResponse.Data;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.Constants;
import com.zing.util.NetworkUtils;
import com.zing.util.SessionManagement;
import com.zing.util.restClient.ApiClient;
import com.zing.util.restClient.ZinglabsApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

public class EditProfileFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.llHeading)
    LinearLayout llHeading;
    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;
    @BindView(R.id.tvEditPhoto)
    TextView tvEditPhoto;
    @BindView(R.id.tvFirstName)
    TextView tvFirstName;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvStreetAddress)
    TextView tvStreetAddress;
    @BindView(R.id.etStreetAddress)
    EditText etStreetAddress;
    @BindView(R.id.tvApt)
    TextView tvApt;
    @BindView(R.id.etApt)
    EditText etApt;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.tvZipCode)
    TextView tvZipCode;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.tvErrorDetail)
    TextView tvErrorDetail;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    Unbinder unbinder;
    @BindView(R.id.tvPassword)
    TextView tvPassword;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvSsn)
    TextView tvSsn;
    @BindView(R.id.etSsn)
    EditText etSsn;
    @BindView(R.id.tvDeletePhoto)
    TextView tvDeletePhoto;
    @BindView(R.id.tvChoosePhoto)
    TextView tvChoosePhoto;
    @BindView(R.id.tvTakeSelfie)
    TextView tvTakeSelfie;
    @BindView(R.id.tvCanceled)
    TextView tvCanceled;
    @BindView(R.id.rlPhotoOptions)
    LinearLayout rlPhotoOptions;
    @BindView(R.id.viewPhone)
    View viewPhone;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.tvCountryError)
    TextView tvCountryError;


    private String mParam1;
    private String mParam2;
    SessionManagement session;
    private ProgressDialog progressDialog;
    Animation slideUpAnimation, slideDownAnimation;
    private int GALLERY = 1, CAMERA = 2;

    ListPopupWindow selectCountry;
    Context mContext;
    CountryAdapter countryAdapter;
    List<Data> countryList = new ArrayList<>();
    int selectCountryPos;
    String countryId;
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_edit_profile, container, false );
        unbinder = ButterKnife.bind( this, view );
        mContext=getActivity();
        countryListApi();
        setListPopup();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity().findViewById( R.id.rvFooter )).setVisibility( View.GONE );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        AppTypeface.getTypeFace( getActivity() );
        tvEdit.setTypeface( AppTypeface.avenieNext_demibold );
        tvProfile.setTypeface( AppTypeface.avenieNext_demibold );
        tvEditPhoto.setTypeface( AppTypeface.avenieNext_demibold );
        tvStreetAddress.setTypeface( AppTypeface.avenieNext_demibold );
        tvApt.setTypeface( AppTypeface.avenieNext_demibold );
        tvFirstName.setTypeface( AppTypeface.avenieNext_demibold );
        tvLastName.setTypeface( AppTypeface.avenieNext_demibold );
        tvPhone.setTypeface( AppTypeface.avenieNext_demibold );
        tvPassword.setTypeface( AppTypeface.avenieNext_demibold );
        tvSsn.setTypeface( AppTypeface.avenieNext_demibold );

        etPassword.setTypeface( AppTypeface.avenieNext_medium );
        etPhone.setTypeface( AppTypeface.avenieNext_medium );
        etFirstName.setTypeface( AppTypeface.avenieNext_medium );
        etLastName.setTypeface( AppTypeface.avenieNext_medium );
        etStreetAddress.setTypeface( AppTypeface.avenieNext_medium );
        etApt.setTypeface( AppTypeface.avenieNext_medium );
        tvState.setTypeface( AppTypeface.avenieNext_demibold );
        etState.setTypeface( AppTypeface.avenieNext_medium );
        tvZipCode.setTypeface( AppTypeface.avenieNext_demibold );
        etZipCode.setTypeface( AppTypeface.avenieNext_medium );

        tvCountry.setTypeface( AppTypeface.avenieNext_demibold );
        etCountry.setTypeface( AppTypeface.avenieNext_medium );

        btnSave.setTypeface( AppTypeface.avenieNext_demibold );

        tvCancel.setTypeface( AppTypeface.avenieNext_medium );

        slideUpAnimation = AnimationUtils.loadAnimation( getActivity(), R.anim.bottom_up );
        slideDownAnimation = AnimationUtils.loadAnimation( getActivity(), R.anim.bottom_down );

        session = new SessionManagement( getActivity() );
        etFirstName.setText( session.getUserFirstName() );
        etLastName.setText( session.getUserLastName() );
        etPhone.setText( session.getUserPhone() );
        etApt.setText( session.getUserApt() );
        etState.setText( session.getUserState() );
        etZipCode.setText( session.getUserZip() );
        etStreetAddress.setText( session.getUserAddress() );
        etPassword.setText( session.getPassword() );
        imgString = session.getProfilePic();
        //etSsn.setText( session.getSsn() );

        etCountry.setText( session.getCountryName() );
        is_new_image = "0";
        Glide.with( getActivity() ).
                load( imgString )
                .apply( new RequestOptions()
                        .diskCacheStrategy( DiskCacheStrategy.NONE )
                        .dontAnimate()
                        .dontTransform().placeholder( R.drawable.ic_user ) )
                .into( civProfileImage );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    Fragment fragment;

    @OnClick({R.id.ivBack, R.id.tvEditPhoto, R.id.btnSave, R.id.tvCancel,
            R.id.tvDeletePhoto, R.id.tvChoosePhoto, R.id.tvTakeSelfie, R.id.tvCanceled,R.id.etCountry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                fragment = ProfileFragment.newInstance( "", "" );
                fragmentInterface.fragmentResult( fragment, "" );
                break;
            case R.id.tvEditPhoto:
                openDialog();
                break;
            case R.id.btnSave:
                if (etFirstName.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } else if (etLastName.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } else if (etPhone.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } else if (etPassword.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } else if (etStreetAddress.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } /*else if (etSsn.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                }*/ else if (etState.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
                } else if (etZipCode.getText().toString().isEmpty()) {
                    CommonUtils.showSnackbar( etFirstName, getActivity().getString( R.string.validate_emptyField ) );
//                    tvErrorDetail.setText(getActivity().getString(R.string.validate_emptyField));
//                    viewFirstName.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (NetworkUtils.isNetworkConnected( getActivity() ))
                    updateProfile();
                break;
            case R.id.tvCancel:
                fragment = ProfileFragment.newInstance( "", "" );
                fragmentInterface.fragmentResult( fragment, "" );
                break;
            case R.id.tvDeletePhoto:
                break;
            case R.id.tvChoosePhoto:
                choosePhotoFromGallary();
                break;
            case R.id.tvTakeSelfie:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission( getActivity(),
                            Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission( getActivity(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE )
                                    != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission( getActivity(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE )
                                    != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions( new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA );
                    } else {
                        takePhotoFromCamera();
                    }
                } else {
                    takePhotoFromCamera();
                }
                break;
            case R.id.tvCanceled:
                startSlideDownAnimation( rlPhotoOptions );
                break;
            case R.id.etCountry:
                selectCountry.show();
                break;
        }
    }

    private void openDialog() {
        startSlideUpAnimation( rlPhotoOptions );
    }

    public void startSlideUpAnimation(View view) {
        view.startAnimation( slideUpAnimation );
        rlPhotoOptions.setVisibility( View.VISIBLE );
    }

    public void startSlideDownAnimation(View view) {
        view.startAnimation( slideDownAnimation );
        rlPhotoOptions.setVisibility( View.GONE );
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent( Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI );

        startActivityForResult( galleryIntent, GALLERY );
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        startActivityForResult( intent, CAMERA );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( getActivity().getContentResolver(), contentURI );
                    loadImage( bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText( getActivity(), "Failed!", Toast.LENGTH_SHORT ).show();
                }
            }
        }

        if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get( "data" );

            Uri tempUri = CommonUtils.getImageUri(getActivity(), bitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File( CommonUtils.getRealPathFromURI(tempUri,getActivity()));
            int imageRotation = CommonUtils.getImageRotation(finalFile);

            if (imageRotation != 0)
                bitmap = CommonUtils.getBitmapRotatedByDegree(bitmap, imageRotation);

            loadImage( bitmap );
        }
    }

    private String imgString = "", is_new_image = "0";

    public void loadImage(Bitmap bitmap) {
        Glide.with( getActivity() ).
                load( bitmap )
                .apply( new RequestOptions()
                        .diskCacheStrategy( DiskCacheStrategy.NONE )
                        .dontAnimate()
                        .dontTransform().placeholder( R.drawable.ic_user ) )
                .into( civProfileImage );

//        civProfileImage.setImageBitmap(bitmap);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, baos2 );
        byte[] imageBytes2 = baos2.toByteArray();
        imgString = Base64.encodeToString( imageBytes2, Base64.DEFAULT );

        is_new_image = "1";
        startSlideDownAnimation( rlPhotoOptions );
    }

    private void updateProfile() {
        TimeZone timezone = TimeZone.getDefault();
        String timeZone = timezone.getID();

        progressDialog = CommonUtils.getProgressBar( getActivity() );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );
        try {

            CompleteProfileRequest completeProfileRequest = new CompleteProfileRequest();
            completeProfileRequest.setDataUpdated( "1" );
            completeProfileRequest.setPassword( etPassword.getText().toString() );
            completeProfileRequest.setFirstName( etFirstName.getText().toString() );
            completeProfileRequest.setLastName( etLastName.getText().toString() );
            completeProfileRequest.setProfileImage(Constants.imageType + imgString.replace( "\n", "" ) );
            completeProfileRequest.setApt( etApt.getText().toString() );
            completeProfileRequest.setPhone( etPhone.getText().toString() );
            //completeProfileRequest.setSsn( etSsn.getText().toString() );
            completeProfileRequest.setState( etState.getText().toString() );
            completeProfileRequest.setStreetAddress( etStreetAddress.getText().toString() );
            completeProfileRequest.setZipCode( etZipCode.getText().toString() );
            completeProfileRequest.setTimeZone( timeZone );
            completeProfileRequest.setIs_new_image( is_new_image );
            completeProfileRequest.setCountryId( countryList .get( selectCountryPos ).getCountryId());
            completeProfileRequest.setDeviceToken(session.getDeviceToken());
            Call<RegisterResponse> call = api.updateProfileApi( "Bearer " + session.getUserToken(),completeProfileRequest );
            call.enqueue( new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call,
                                       @NonNull Response<RegisterResponse> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {

                        try {
                            RegisterResponse registerResponse = response.body();
                            if (registerResponse != null && registerResponse.getResponse().getCode() == 200) {
                                session.setUserData( registerResponse.getResponse().getData().getUserId(),
                                        registerResponse.getResponse().getData().getFirstName(),
                                        registerResponse.getResponse().getData().getLastName(),
                                        registerResponse.getResponse().getData().getPhone(),
                                        registerResponse.getResponse().getData().getDataUpdated(),
                                        session.getUserToken(),
                                        registerResponse.getResponse().getData().getStatus(),
                                        registerResponse.getResponse().getData().getApt(),
                                        registerResponse.getResponse().getData().getStreetAddress(),
                                        registerResponse.getResponse().getData().getState(),
                                        registerResponse.getResponse().getData().getZipCode(),
                                        registerResponse.getResponse().getData().getSsn(),
                                        registerResponse.getResponse().getData().getProfilePic(),
                                        etPassword.getText().toString() ,
                                        registerResponse.getResponse().getData().getCountryName(),
                                        registerResponse.getResponse().getData().getCountryId()

                                );

                                fragment = ProfileFragment.newInstance( "", "" );
                                fragmentInterface.fragmentResult( fragment, "" );
                            } else {
                                CommonUtils.showSnackbar( etApt, registerResponse.getResponse().getMessage() );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showSnackbar( etApt, response.message() );
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showSnakBar( etApt, t.getMessage() );
                }
            } );
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void countryListApi() {
        progressDialog = CommonUtils.getProgressBar( mContext );
        ZinglabsApi api = ApiClient.getClient().create( ZinglabsApi.class );
        Call<CountryResponse> call = api.getCountryListApi();
        call.enqueue( new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call,
                                   @NonNull Response<CountryResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    CountryResponse countryResponse = response.body();

                    assert countryResponse != null;
                    countryList.addAll( countryResponse.getData() );
                    if (!session.getCountryName().isEmpty()) {
                        for (int i = 0; i < countryList.size(); i++) {
                            if (countryList.get( i ).getName().equalsIgnoreCase( session.getCountryName() )) {
                                selectCountryPos = i;
                                break;
                            }
                        }
                    }

                    if (countryAdapter != null)
                        countryAdapter.addAllData( countryList );


                } else {
//                        CommonUtils.showSnackbar(etApt, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
//                    CommonUtils.showSnakBar(etApt, t.getMessage());
            }
        } );


    }
    void setListPopup() {


        selectCountry = new ListPopupWindow( mContext );


        countryAdapter = new CountryAdapter( mContext );

        selectCountry.setAdapter( countryAdapter );
        selectCountry.setModal( true );
        selectCountry.setAnchorView( etCountry );
        selectCountry.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCountry.dismiss();
                etCountry.setText( countryList.get( position ).getName() );
                selectCountryPos = position;

            }
        } );


    }
}
