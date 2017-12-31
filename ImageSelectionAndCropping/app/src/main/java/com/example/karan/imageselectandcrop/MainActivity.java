package com.example.karan.imageselectandcrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements OnClickListener {

  private static String TAG = "MainActivity";
  private static int REQUEST_SELECT_IMAGE = 1000;
  private static int REQUEST_CROP_IMAGE = 1001;

  private Button bLoadImage;
  private ImageView ivImagePreview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bLoadImage = findViewById(R.id.bLoadImage);
    ivImagePreview = findViewById(R.id.ivImagePreview);

    bLoadImage.setOnClickListener(this);
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

      Intent cropIntent = new Intent("com.android.camera.action.CROP");
      cropIntent.setDataAndType(imgUri, "image/*");
      cropIntent.putExtra("crop", "true");
      cropIntent.putExtra("aspectX", 4);
      cropIntent.putExtra("aspectY", 5);
      cropIntent.putExtra("outputX", 450);
      cropIntent.putExtra("outputY", 563);
      cropIntent.putExtra("scaleUpIfNeeded", true);
      cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
      cropIntent.putExtra("return-data", true);
      startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);

    } else if (requestCode == REQUEST_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
      Bitmap mBitmap = (Bitmap) data.getExtras().get("data");
      ivImagePreview.setImageBitmap(mBitmap);
    }
  }
}