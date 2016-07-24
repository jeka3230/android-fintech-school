package ru.tinkoff.school;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * @author Dmitriy Tarasov
 */
public class ImageProcessingBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION = "IMAGE_PROCESSING_READY_ACTION";
    private static final String BITMAP_EXT = "bitmap";
    public static final String PATH = "path";

    private final ImageView mPreview;
    private final ProgressBar mProgress;
    private final IntentFilter mFilter;

    public static void notifyReceiver(Context context, Bitmap bitmap, String path) {
        Intent i = new Intent(ACTION);
        i.putExtra(BITMAP_EXT, bitmap);
        i.putExtra(PATH, path);
        // TODO отправить Intent, чтобы на него среагировал BroadcastReceiver
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    public ImageProcessingBroadcastReceiver(ImageView preview, ProgressBar progress) {
        mPreview = preview;
        mProgress = progress;
        mFilter = new IntentFilter(ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bitmap bitmap = intent.getParcelableExtra(BITMAP_EXT);
        MainActivity.setImagePath(intent.getStringExtra(PATH));
        mPreview.setImageBitmap(bitmap);
        mPreview.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    // TODO найти место где можно применить данный метод
    public void register(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(this, mFilter);
    }

    // TODO найти место где можно применить данный метод
    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
