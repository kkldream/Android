package com.niu.native_opencv;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Header {
    public static String saveBitmapToStorage(Bitmap bitmap) {
//        Bitmap temp_bitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
//        Bitmap temp_bitmap = Bitmap.createBitmap(bitmap);
//        Bitmap bitmap = Bitmap.createBitmap(bitmap);
        File sdCard = Environment.getExternalStorageDirectory();
        File filePath = new File(sdCard,"Pictures");
        if (!filePath.exists()) filePath.mkdir();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(filePath, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            Bitmap ratio_bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            ratio_bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();
    }

    public static Bitmap imageViewToBitmap(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
//        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
