package com.demo.opencv;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    int light,edge,blurry,embossing,sharpness;

    TextView textView;
    Button button1,button2,button3,button4;
    SeekBar seekBar1,seekBar2,seekBar3,seekBar4,seekBar5;
    static {//加载so库
        System.loadLibrary("native-lib");
//        System.loadLibrary("t1");
    }
    //获得Canny边缘
    native void getLight(Object bitmap, int var);
    native void getEdge(Object bitmap, int var);
    native void getBlurry(Object bitmap, int var);
    native void getEmbossing(Object bitmap, int var);
    native void getSharpness(Object bitmap, int var);
    native void getTest(Object bitmap, int var);

    Bitmap bitmap,obitmap;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageView iv_show;
    private CameraManager mCameraManager;//摄像头管理器
    private Handler childHandler, mainHandler;
    private String mCameraID; //摄像头Id 0 为后 1 为前
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Log.i("TAG","-------------------onCreate-------------------");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar6);
        iv_show = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        mSurfaceView = findViewById(R.id.surfaceView);
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后摄像头


        camera = new Camera(this);


        obitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edge = progress;
                setTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light = progress;
                setTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurry = progress;
                setTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                embossing = progress;
                setTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharpness = progress;
                setTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setTextView() {
        textView.setText("參數調整：");
        if (light > 0) textView.setText(textView.getText().toString() + "\n亮度：" + light);
        if (sharpness > 0) textView.setText(textView.getText().toString() + "\n銳化：" + sharpness);
        if (blurry > 0) textView.setText(textView.getText().toString() + "\n模糊：" + blurry);
        if (embossing > 0) textView.setText(textView.getText().toString() + "\n浮雕：" + embossing);
        if (edge > 0) textView.setText(textView.getText().toString() + "\n邊緣：" + edge);
//        textView.setText(textView.getText().toString() + "\n亮度：" + (light == 0 ? "無調整" : "" + light));
//        textView.setText(textView.getText().toString() + "\n銳化：" + (sharpness == 0 ? "無調整" : "" + sharpness));
//        textView.setText(textView.getText().toString() + "\n模糊：" + (blurry == 0 ? "無調整" : "" + blurry));
//        textView.setText(textView.getText().toString() + "\n浮雕：" + (embossing == 0 ? "無調整" : "" + embossing));
//        textView.setText(textView.getText().toString() + "\n邊緣：" + (edge == 0 ? "關閉" : "" + edge));
    }

    /**
     * 初始化
     */
//    private void initVIew() {
//        Log.i("TAG","initVIew");
//        //mSurfaceView
//        mSurfaceView = findViewById(R.id.surfaceView);
//        Log.i("TAG","findViewById");
////        mSurfaceView.getHolder().setFixedSize(300*3, 400*3);
//        mSurfaceView.getHolder().setFixedSize(1000, 1000);
//        mSurfaceHolder = mSurfaceView.getHolder();
//        Log.i("TAG","getHolder");
//        mSurfaceHolder.setKeepScreenOn(true);
//        // mSurfaceView添加回调
//        initCamera2();
//        /*
//        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
//                // 初始化Camera
//                initCamera2();
//                Log.i("TAG","initCamera2");
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
//                // 释放Camera资源
//                if (null != mCameraDevice) {
//                    mCameraDevice.close();
//                    MainActivity.this.mCameraDevice = null;
//                }
//            }
//        });
//         */
//    }

    /**
     * 初始化Camera2
     */
//    @SuppressLint("MissingPermission")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void initCamera2() {
//        HandlerThread handlerThread = new HandlerThread("Camera2");
//        handlerThread.start();
//        childHandler = new Handler(handlerThread.getLooper());
//        mainHandler = new Handler(getMainLooper());
//        mImageReader = ImageReader.newInstance(1000, 1000, ImageFormat.JPEG,1);
//        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
//            @Override
//            public void onImageAvailable(ImageReader reader) {
//                Log.d("TAG","onImageAvailable");
////                mCameraDevice.close();
////                mSurfaceView.setVisibility(View.GONE);
////                iv_show.setVisibility(View.VISIBLE);
//                // 拿到拍照照片数据
//                Image image = reader.acquireNextImage();
//                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                byte[] bytes = new byte[buffer.remaining()];
//                buffer.get(bytes);//由缓冲区存入字节数组
//                obitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                if (obitmap != null) {
//                    iv_show.setImageBitmap(obitmap);
//                }
//                image.close();
//                button1.setText("相機");
//                button2.setText("原圖");
//                button3.setText("轉換");
//                button4.setVisibility(View.VISIBLE);
//                iv_show.setVisibility(View.VISIBLE);
//                cameraClose();
//            }
//        }, mainHandler);
//        //获取摄像头管理
//        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            //打开摄像头
//            mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 摄像头创建监听
     */
//    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
//        @Override
//        public void onOpened(CameraDevice camera) {//打开摄像头
//            mCameraDevice = camera;
//            //开启预览
//            takePreview();
//        }
//
//        @Override
//        public void onDisconnected(CameraDevice camera) {//关闭摄像头
//            if (null != mCameraDevice) {
//                mCameraDevice.close();
//                MainActivity.this.mCameraDevice = null;
//            }
//        }
//
//        @Override
//        public void onError(CameraDevice camera, int error) {//发生错误
//            Toast.makeText(MainActivity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
//        }
//    };

    /**
     * 开始预览
     */
//    private void takePreview() {
//        Log.d("TAG","takePreview");
//        try {
//            // 创建预览需要的CaptureRequest.Builder
//            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
//            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
//            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
//            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
//            {
//                @Override
//                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
//                    if (null == mCameraDevice) return;
//                    // 当摄像头已经准备好时，开始显示预览
//                    mCameraCaptureSession = cameraCaptureSession;
//                    try {
////                        // 自动对焦
////                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
////                        // 打开闪光灯
////                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                        // 显示预览
//                        CaptureRequest previewRequest = previewRequestBuilder.build();
//                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
//                    Toast.makeText(MainActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
//                }
//            }, childHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 拍照
     */
//    private void takePicture() {
//        Log.d("TAG","takePicture");
//        if (mCameraDevice == null) return;
//        // 创建拍照需要的CaptureRequest.Builder
//        final CaptureRequest.Builder captureRequestBuilder;
//        try {
//            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//            // 将imageReader的surface作为CaptureRequest.Builder的目标
//            captureRequestBuilder.addTarget(mImageReader.getSurface());
////            // 自动对焦
////            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
////            // 自动曝光
////            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//            // 获取手机方向
////            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
//            // 根据设备方向计算设置照片的方向
//            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
//            //拍照
//            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
//            mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
//            Log.d("TAG","takePicture end");
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
    public void button1(View view) {
        if (button1.getText().toString().equals("相機")) {
            button1.setText("拍照");
            button2.setText("翻轉");
            button3.setText("返回");
            button4.setVisibility(View.GONE);
//            initVIew();
            camera.open(mSurfaceView);
            iv_show.setVisibility(View.INVISIBLE);
//            mSurfaceView.getChildAt(0).setVisibility(View.INVISIBLE);
//            mSurfaceView.setVisibility(View.VISIBLE);
//            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
//            CaptureRequest previewRequest = previewRequestBuilder.build();
//            mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
        } else if (button1.getText().toString().equals("拍照")) {
            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                iv_show.setScaleX(1);
            } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                iv_show.setScaleX(-1);
            }
//            takePicture();
            camera.takePicture(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Log.d("TAG","onImageAvailable");
                    Image image = imageReader.acquireNextImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    Bitmap obitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                    image.close();
                }
            });
//            mCameraCaptureSession.stopRepeating();
//            mSurfaceView.setVisibility(View.INVISIBLE);
        }
    }
    boolean isObitmap = true;
    int lensFacing = CameraCharacteristics.LENS_FACING_FRONT;
    public void button2(View view) {
        if (button2.getText().toString().equals("翻轉")) {
            if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                mCameraID = "" + CameraCharacteristics.LENS_FACING_BACK;
//                cameraClose();
//                initVIew();
                camera.close();
                camera.open(mSurfaceView);
                lensFacing = CameraCharacteristics.LENS_FACING_BACK;
            } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                lensFacing = CameraCharacteristics.LENS_FACING_FRONT;
                mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
//                cameraClose();
//                initVIew();
                camera.close();
                camera.open(mSurfaceView);
            }
        } else if (button2.getText().toString().equals("原圖")) {
            iv_show.setImageBitmap(obitmap);
            isObitmap = true;
        }
    }
    public void button3(View view) {
        if (button3.getText().toString().equals("返回")) {
            button1.setText("相機");
            button2.setText("原圖");
            button3.setText("轉換");
            button4.setVisibility(View.VISIBLE);
            iv_show.setVisibility(View.VISIBLE);
//            cameraClose();
            camera.close();
        } else if (button3.getText().toString().equals("轉換")) {
            bitmap = obitmap.copy(Bitmap.Config.ARGB_8888,true);
//            getEdge(bitmap, edge);
            if (light > 0) getLight(bitmap, light);
            if (sharpness > 0) getSharpness(bitmap, sharpness);
            if (blurry > 0) getBlurry(bitmap, blurry);
            if (embossing > 0) getEmbossing(bitmap, embossing);
            if (edge > 0) getEdge(bitmap, edge);
            iv_show.setImageBitmap(bitmap);
            isObitmap = false;
        }
    }
    public void button4(View view) {
//        File filePath = new File("/storage/emulated/0/Pictures","opencv");
        File filePath = getExternalFilesDir("Pictures");
        if (!filePath.exists()) filePath.mkdir();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(filePath,fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            if (isObitmap) {
                obitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(this,"原圖儲存至" + file,Toast.LENGTH_SHORT).show();
            }
            else {
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(this,"轉換儲存至" + file,Toast.LENGTH_SHORT).show();
            }
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private void cameraClose() {
//        if (mSurfaceView != null) {
//            mSurfaceView = null;
//        }
//        if (mSurfaceHolder != null) {
//            mSurfaceHolder = null;
//        }
//        if (childHandler != null) {
//            childHandler.removeCallbacksAndMessages(null);
//            childHandler = null;
//        }
//        if (mainHandler != null) {
//            mainHandler.removeCallbacksAndMessages(null);
//            mainHandler = null;
//        }
//        if (null != mCameraManager) {
//            mCameraManager = null;
//        }
//        if (null != mCameraCaptureSession) {
//            mCameraCaptureSession.close();
//            mCameraCaptureSession = null;
//        }
//        if (null != mCameraDevice) {
//            mCameraDevice.close();
//            mCameraDevice = null;
//        }
//        if (null != mImageReader) {
//            mImageReader.close();
//            mImageReader = null;
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraDevice != null) {
//            cameraClose();
            camera.close();
            Toast.makeText(this,"釋放鏡頭",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button button = findViewById(R.id.button1);
        if (button.getText().toString().equals("拍照")) {
//            initVIew();
            camera.open(mSurfaceView);
            Toast.makeText(this,"重啟鏡頭",Toast.LENGTH_SHORT).show();
        }
    }

}