package ru.tinkoff.school;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Dmitriy Tarasov
 */
public class ImageProcessingService extends IntentService {

    private static final String PATH_EXT = "path";
    private static final String HEIGHT_EXT = "height";
    private static final String WIDTH_EXT = "width";

    public static void start(Context context, String path, int imageWidth, int imageHeight) {
        Intent i = new Intent(context, ImageProcessingService.class);
        i.putExtra(PATH_EXT, path);
        i.putExtra(HEIGHT_EXT, imageHeight);
        i.putExtra(WIDTH_EXT, imageWidth);
        context.startService(i);
    }

    public ImageProcessingService() {
        super("ImageProcessor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String fileName = intent.getStringExtra(PATH_EXT);
        int width = intent.getExtras().getInt(WIDTH_EXT);
        int height = intent.getExtras().getInt(HEIGHT_EXT);
        Bitmap sampled = decodeSampledBitmapFromFile(fileName, width, height);
        Bitmap blackAndWhite = toBlackAndWhite(sampled);

        try {
            writeToFile(blackAndWhite);
        } catch (IOException e) {
            // just ignore in demo
            e.printStackTrace();
        }

        ImageProcessingBroadcastReceiver.notifyReceiver(this, blackAndWhite);
    }

    private void writeToFile(Bitmap bitmap) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Utils.getFullFileName(this));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private Bitmap toBlackAndWhite(Bitmap input) {
        int width, height;
        height = input.getHeight();
        width = input.getWidth();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));

        Canvas c = new Canvas(output);
        c.drawBitmap(input, 0, 0, paint);
        return output;
    }

    private Bitmap decodeSampledBitmapFromFile(String fileName, int width, int height) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
