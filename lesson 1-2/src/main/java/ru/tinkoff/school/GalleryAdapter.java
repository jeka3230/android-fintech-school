package ru.tinkoff.school;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import static android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.Media.BUCKET_ID;
import static android.provider.MediaStore.Images.Media.DATA;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.Images.Media._ID;
import static android.provider.MediaStore.Images.Thumbnails.MINI_KIND;

/**
 * @author Dmitriy Tarasov
 */
public class GalleryAdapter extends CursorAdapter {

    public GalleryAdapter(Context context) {
        super(context, getGalleryCursor(context), true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.item_gallery, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(_ID));
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), id, MINI_KIND, null);

        ImageView iv = (ImageView) view.findViewById(R.id.image);
        iv.setImageBitmap(bitmap);
    }

    private static Cursor getGalleryCursor(Context context) {
        Uri uri = EXTERNAL_CONTENT_URI;
        String[] projection = {_ID, BUCKET_ID, BUCKET_DISPLAY_NAME, DATA};
        return context.getContentResolver().query(uri, projection, null, null, null);
    }
}
