package com.example.karan.imageselectandcrop;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Debug;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

  private static String TAG = "MainActivity";
  private static int REQUEST_SELECT_IMAGE = 1000;
  private static final int REQUEST_PERMISSIONS = 1002;

  private Button bLoadImage;
  private ImageView ivImagePreview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bLoadImage = findViewById(R.id.bLoadImage);
    ivImagePreview = findViewById(R.id.ivImagePreview);

    bLoadImage.setOnClickListener(this);

    checkForPermissions();
  }

  private void checkForPermissions() {
    // Assume thisActivity is the current activity
    int writeExternalStorage = ContextCompat.checkSelfPermission(this,
        permission.WRITE_EXTERNAL_STORAGE);
    int readExternalStorage = ContextCompat
        .checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE);

    if (writeExternalStorage == PackageManager.PERMISSION_DENIED
        || readExternalStorage == PackageManager.PERMISSION_DENIED) {
      ActivityCompat.requestPermissions(this,
          new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE},
          REQUEST_PERMISSIONS);
    }
  }

  private void onLoadButtonClick() {
    Log.d(TAG, "Load button clicked");
    Intent imgSelectionIntent = new Intent();
    imgSelectionIntent.setType("image/*");
    imgSelectionIntent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(imgSelectionIntent, "Select an image."),
        REQUEST_SELECT_IMAGE);
  }

  @Override
  public void onClick(View view) {
    Log.d(TAG, "On click called");
    int viewId = view.getId();
    switch (viewId) {
      case R.id.bLoadImage: {
        this.onLoadButtonClick();
      }
      break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
      if (data == null) {
        Log.e(TAG, "Image selection data was null");
        return;
      }
      Uri imgUri = data.getData();

      cropImage(imgUri);

    } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      Uri resultUri = result.getUri();
      Bitmap mBitmap = null;
      try {
        mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , resultUri);
      } catch (IOException e) {
        e.printStackTrace();
      }
      ivImagePreview.setImageBitmap(mBitmap);
    }
  }

  private void cropImage(Uri imgUri) {
    CropImage.activity(imgUri)
        .start(this);
  }

  // without library
//  private void cropImage(Uri imgUri) {
//    Intent cropIntent = new Intent("com.android.camera.action.CROP");
//    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    cropIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//    cropIntent.setDataAndType(imgUri, "image/*");
//    cropIntent.putExtra("crop", "true");
//    cropIntent.putExtra("aspectX", 4);
//    cropIntent.putExtra("aspectY", 5);
//    cropIntent.putExtra("outputX", 450);
//    cropIntent.putExtra("outputY", 563);
//    cropIntent.putExtra("scaleUpIfNeeded", true);
//    cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//    cropIntent.putExtra("return-data", true);
//    startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);
//  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    switch (requestCode) {
      case REQUEST_PERMISSIONS: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Log.d(TAG, "Permissions were granted");
        } else {
          Log.d(TAG, "Permissions were denied");
        }
        return;
      }
    }
  }
}