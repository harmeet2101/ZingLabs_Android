package com.zing.activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.model.response.otpVerifyResponse.BasicInfo;
import com.zing.model.response.otpVerifyResponse.PreferenceInfo;
import com.zing.util.AppTypeface;
import com.zing.util.CommonUtils;
import com.zing.util.MyFileContentProvider;
import com.zing.util.SessionManagement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by savita on 4/4/18.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.ivAddImg)
    CircleImageView ivAddImg;
    @BindView(R.id.ivGallery)
    ImageView ivGallery;
    @BindView(R.id.btnNext)
    Button btnNext;
    int count = 0;
    @BindView(R.id.tvBack)
    TextView tvBackView;
    private static final int CAMERA_PERMISSION_CODE = 23;
    private static final int CAMERA_PERMISSION_CODE_GALLERY = 24;

    @BindView(R.id.tvAddPhoto)
    TextView tvAddPhoto;
    SessionManagement session;
    private String password, phone, from = "";
    private BasicInfo basicInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AppTypeface.getTypeFace(this);
        tvAddPhoto.setTypeface(AppTypeface.avenieNext_demibold);
        btnNext.setTypeface(AppTypeface.avenieNext_demibold);
        tvBackView.setTypeface(AppTypeface.avenieNext_demibold);
        session = new SessionManagement(this);

        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        from = getIntent().getStringExtra("from");
        if(getIntent().hasExtra("basicInfo"))
            basicInfo=(BasicInfo) getIntent().getSerializableExtra("basicInfo");
    }

    @OnClick({R.id.ivCamera, R.id.ivAddImg, R.id.ivGallery, R.id.btnNext,R.id.tvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCamera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSION_CODE);
                    } else {

                       openCamera();
                    }
                } else {

                   openCamera();
                }
                break;
            case R.id.ivAddImg:
                break;
            case R.id.ivGallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSION_CODE_GALLERY);
                    } else {
                        openGallery();
                    }
                } else {
                    openGallery();
                }
                break;
            case R.id.btnNext:
                if(imgBitmap!=null)
               {
                    Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("basicInfo",basicInfo);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }else{
                    CommonUtils.showSnackbar(tvAddPhoto, "Please select photo");
                }
                break;

            case R.id.tvBack:
                finish();
                break;
        }
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
        startActivityForResult(cameraIntent, 0);
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(galleryIntent, 1);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ivAddImg.setImageResource(R.drawable.ic_add);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case CAMERA_PERMISSION_CODE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the

                    openCamera();
                } else
                    CommonUtils.showSnackbar(tvAddPhoto, "Permission Denied");
                break;
            case  CAMERA_PERMISSION_CODE_GALLERY:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted! Do the
                    openGallery();
                } else
                    CommonUtils.showSnackbar(tvAddPhoto, "Permission Denied");
                break;
        }
    }



    Bitmap mBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == 0 && resultCode == RESULT_OK) {
            {

                File out = new File(getFilesDir(), "newImage.jpg");

                if(!out.exists()) {

                    Toast.makeText(getBaseContext(),

                            "Error while capturing image", Toast.LENGTH_LONG)

                            .show();

                    return;
                }
                mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());


                try {
                    ExifInterface exifObject = new ExifInterface(out.getAbsolutePath());
                    int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    mBitmap = CommonUtils.rotateBitmap(mBitmap,orientation);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File file = wrapper.getDir("Images",MODE_PRIVATE);
                file = new File(file, "profile_pic"+".jpg");
                OutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(),MoveScaleImageActivity.class);
                startActivityForResult(intent,2);

            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {


            Intent intent = new Intent(getApplicationContext(),MoveScaleImageActivity.class);
            intent.putExtra("filePath",getPathFromURI(data.getData()));
            intent.putExtra("isFormGallery",true);
            startActivityForResult(intent,2);
        } else if (requestCode == 2 && resultCode == RESULT_OK ) {



            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File directory = wrapper.getDir("Images", Context.MODE_PRIVATE);
            File mypath=new File(directory,"profile_pic.jpg");
            try {
                imgBitmap = BitmapFactory.decodeStream(new FileInputStream(mypath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
            ivAddImg.setImageBitmap(imgBitmap);
        }
    }

    private Bitmap imgBitmap;
    private String getPathFromURI(Uri contentUri) {


        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




}
