package ru.tinkoff.school;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author Dmitriy Tarasov
 */
public class Utils {

    private static final String FILE_NAME = "myImage";

    public static Intent capturePhotoIntent(Context context) {
        String fileName = getFullFileName(context);
        Uri uri = Uri.fromFile(new File(fileName));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Intent sendIntent(String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        return i;
    }

    public static String getFullFileName(Context context) {
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (filesDir != null) {
            String path = filesDir.getPath();
            return path + "/" + FILE_NAME;
        } else {
            return null;
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
