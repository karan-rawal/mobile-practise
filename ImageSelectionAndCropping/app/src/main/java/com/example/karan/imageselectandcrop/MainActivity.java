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

      Uri uri = data.getData();
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ivImagePreview.setImageBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
