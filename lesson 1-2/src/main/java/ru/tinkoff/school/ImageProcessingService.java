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
import android.util.Log;

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
        Log.d("params", fileName + " " + width + " " + height);
        Bitmap sampled = Utils.decodeSampledBitmapFromFile(fileName, width, height);
        Bitmap blackAndWhite = toBlackAndWhite(sampled);

        try {
            writeToFile(blackAndWhite);
        } catch (IOException e) {
            // just ignore in demo
            e.printStackTrace();
        }

        ImageProcessingBroadcastReceiver.notifyReceiver(this, blackAndWhite, Utils.getFullFileName(this));
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
}
