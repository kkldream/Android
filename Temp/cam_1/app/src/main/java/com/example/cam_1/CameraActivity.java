package com.example.cam_1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback{
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView1;
    Button button1;
    ImageView imageView1;
    Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button1=(Button)findViewById(R.id.button1);
        //在AndroidManifest.xml中設定或是用下面的setRequestedOrientation(0)設定也可以
        //0代表橫向、1代表縱向
        setRequestedOrientation(0);
        //設為横向顯示。因為攝影頭會自動翻轉90度，所以如果不横向顯示，看到的畫面就是翻轉的。

        surfaceView1=(SurfaceView)findViewById(R.id.surfaceView1);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        surfaceHolder=surfaceView1.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        button1.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {

                //自動對焦
                camera.autoFocus(afcb);

            }});

    }
    PictureCallback jpeg =new PictureCallback(){

        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
            //byte數组轉換成Bitmap
            imageView1.setImageBitmap(bmp);
            //拍下圖片顯示在下面的ImageView裡
            FileOutputStream fop;
            try {
                fop=new FileOutputStream("/sdcard/dd.jpg");
                //實例化FileOutputStream，參數是生成路徑
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
                //壓缩bitmap寫進outputStream 參數：輸出格式  輸出質量  目標OutputStream
                //格式可以為jpg,png,jpg不能存儲透明
                fop.close();
                System.out.println("拍照成功");
                //關閉流
            } catch (FileNotFoundException e) {

                e.printStackTrace();
                System.out.println("FileNotFoundException");

            } catch (IOException e) {

                e.printStackTrace();
                System.out.println("IOException");
            }
            camera.startPreview();
            //需要手動重新startPreview，否則停在拍下的瞬間
        }

    };

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    public void surfaceCreated(SurfaceHolder holder) {

        camera=Camera.open();
        try {

            Camera.Parameters parameters=camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPreviewSize(320, 220);
            camera.setParameters(parameters);
            //設置參數
            camera.setPreviewDisplay(surfaceHolder);
            //鏡頭的方向和手機相差90度，所以要轉向
            //camera.setDisplayOrientation(90);
            //攝影頭畫面顯示在Surface上
            camera.startPreview();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        System.out.println("surfaceDestroyed");
        camera.stopPreview();
        //關閉預覽
        camera.release();
        //
    }

    //自動對焦監聽式
    AutoFocusCallback afcb= new AutoFocusCallback(){

        public void onAutoFocus(boolean success, Camera camera) {

            if(success){
                //對焦成功才拍照
                camera.takePicture(null, null, jpeg);
            }
        }


    };
}