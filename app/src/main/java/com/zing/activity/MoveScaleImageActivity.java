package com.zing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zing.R;
import com.zing.base.BaseActivity;
import com.zing.util.AppTypeface;
import com.zing.util.SessionManagement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MoveScaleImageActivity extends BaseActivity {
    @BindView(R.id.tvMoveScale)
    TextView tvMoveScale;
    @BindView(R.id.ivMoveScale)
    ImageView ivMoveScale;
    @BindView(R.id.btnChoosePhoto)
    Button btnChoosePhoto;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    Bitmap mBitmap;
    private Bitmap cropBitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_scale_activity);
        ButterKnife.bind(this);


        File out = new File(getFilesDir(), "newImage.jpg");
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("isFormGallery",false);
        if(flag){
            String path=intent.getStringExtra("filePath");
            Bitmap selectedImage = BitmapFactory.decodeFile(path);
            ivMoveScale.setImageBitmap(selectedImage);
            cropImageView.setImageBitmap(selectedImage);

        }else {

            mBitmap= BitmapFactory.decodeFile(out.getAbsolutePath());

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("Images", Context.MODE_PRIVATE);
            File mypath=new File(directory,"profile_pic.jpg");
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(new FileInputStream(mypath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ivMoveScale.setImageBitmap(b);
            cropImageView.setImageBitmap(b);
        }

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                try {
                    cropBitmap = view.getCroppedImage();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    cropImageAndStartIntent();
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        AppTypeface.getTypeFace(this);
        tvMoveScale.setTypeface(AppTypeface.avenieNext_medium);
        btnChoosePhoto.setTypeface(AppTypeface.avenieNext_demibold);
        tvCancel.setTypeface(AppTypeface.avenieNext_demibold);

    }



    @OnClick({R.id.btnChoosePhoto, R.id.tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnChoosePhoto:
                cropImageView.getCroppedImageAsync();

                break;
            case R.id.tvCancel:
                onBackPressed();
                break;
        }
    }

    private void cropImageAndStartIntent(){


        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        file = new File(file, "profile_pic"+".jpg");
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cropBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }
}
