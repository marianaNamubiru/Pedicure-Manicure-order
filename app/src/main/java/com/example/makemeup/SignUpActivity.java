package com.example.makemeup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 2222;
    File file0,file1,file2,file3,file4;
    ImageView imageView;
    SweetAlertDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
         //find the id reference
        imageView = findViewById(R.id.selectedImage);
    }
    //calling onclick
    public void imagePicker(View v){
        //Checking permission to read file storage of a user
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
            , PICK_FROM_GALLERY);
        } else {
        //method to launch image picker
        pickImage();
    }
    }

    private void pickImage() {
        //Calling image picker class
        ImagePicker.create(this)
                .multi()
                .limit(5)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            //get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            //or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

           //call sweetalert dialog success type
            SweetAlertDialog selectedDialog = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
            selectedDialog.getProgressHelper().setBarColor(Color.parseColor("A5DC86"));
            selectedDialog.setTitleText(images.size() + "images selected");
            selectedDialog.setContentText("The implementation works");
            selectedDialog.setCancelable(true);
            selectedDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    //dismiss running sweetalert dialog
                    sweetAlertDialog.dismissWithAnimation();
                    if (images.size() < 5) {
                        errorDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.ERROR_TYPE);
                        errorDialog.setTitleText("Make sure you pick 5 images");
                        errorDialog.setCancelable(true);
                        errorDialog.show();
                    } else {
                        //save path for images through file instance(submit)
                        file0 = new File(images.get(0).getPath());
                        file1 = new File(images.get(1).getPath());
                        file2 = new File(images.get(2).getPath());
                        file3 = new File(images.get(3).getPath());
                        file4 = new File(images.get(4).getPath());

                        Log.d("SignUpActivity", "file 0 is " + file0 + "file 1 is " + file1 + "file 2 is " + file2
                                + "file 3 is " + file3 + "file4 is " + file4);

                        //call glide to set image to my imageview
                        Glide
                                .with(SignUpActivity.this)
                                .load(file0)
                                .centerCrop()
                                .placeholder(R.drawable.ef_ic_camera_white)
                                .into(imageView);
                    }
                }
            });
            selectedDialog.show();

        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}