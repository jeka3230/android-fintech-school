package ru.tinkoff.school;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQ = 0;
    private static final int GALLERY_REQ = 1;
    private static final int PERMISSIONS_REQ = 2;

    private ImageProcessingBroadcastReceiver receiver;

    private ProgressBar mProgress;
    private ImageView mPreview;
    private static String mPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO задать лейаут активности из activity_main.xml
        setContentView(R.layout.activity_main);
        // TODO найти в лейауте нужные View по id
        mPreview = (ImageView) findViewById(R.id.preview);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        // TODO создать BroadcastReceiver
         receiver = new ImageProcessingBroadcastReceiver(mPreview, mProgress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver.register(this);
    }

    @Override
    protected void onPause() {
        // TODO разрегистрировать ресивер
        receiver.unregister(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQ) {
            startImageProcessing(Utils.getFullFileName(this));
        } else if (requestCode == GALLERY_REQ) {
            if (data != null) {
                Uri uri = data.getData();
                startImageProcessing(Utils.getRealPathFromUri(this, uri));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQ && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GalleryActivity.startForResult(this, GALLERY_REQ);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ImageProcessingBroadcastReceiver.PATH, mPath);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mPath = savedInstanceState.getString(ImageProcessingBroadcastReceiver.PATH);
        if (mPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            mPreview.setImageBitmap(bitmap);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onCapturePhotoClick(View v) {
        Intent cameraIntent = Utils.capturePhotoIntent(this);
        startActivityForResult(cameraIntent, CAMERA_REQ);
    }

    public void onChoosePhotoClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQ);
        } else {
            GalleryActivity.startForResult(this, GALLERY_REQ);
        }
    }

    public void onSendClick(View v) {
        String path = Utils.getFullFileName(this);
        startActivity(Utils.sendIntent(path));
    }

    private void startImageProcessing(String path) {
        mProgress.setVisibility(View.VISIBLE);
        mPreview.setVisibility(View.INVISIBLE);
        ImageProcessingService.start(this, path, mPreview.getWidth(), mPreview.getHeight());
    }

    public static void setImagePath(String path) {
        mPath = path;
    }
}
