package com.example.cam_6;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    Button button;
    ImageView imageView;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private SurfaceHolder mSurfaceHolder;
    private Handler childHandler, mainHandler;
    String CameraID = Integer.toString(CameraCharacteristics.LENS_FACING_FRONT);//摄像头Id 0 为后  1 为前
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        Log.i("test", "-------------------------");
        Log.i("test", "開始 onCreate");
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        // mSurfaceView添加回调
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
                Log.i("test", "SurfaceView 創建");
                try {
                    initCamera2();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
                // 释放Camera资源
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    MainActivity.this.mCameraDevice = null;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "按下按鍵 onClick");
                if (mCameraDevice == null) return;
                // 创建拍照需要的CaptureRequest.Builder
                final CaptureRequest.Builder captureRequestBuilder;
                try {
                    captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                    // 将imageReader的surface作为CaptureRequest.Builder的目标
                    captureRequestBuilder.addTarget(mImageReader.getSurface());
                    // 自动对焦
//                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    // 自动曝光
//                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                    // 获取手机方向
//                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    // 根据设备方向计算设置照片的方向
//                    captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                    //拍照
                    CaptureRequest mCaptureRequest = captureRequestBuilder.build();
                    mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
//                    initCamera2();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void initCamera2() throws CameraAccessException {
        Log.i("test", "初始化 initCamera2");
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG,5);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onImageAvailable(ImageReader reader) {
                Log.i("test", "顯示 onImageAvailable");
//                mCameraDevice.close();
//                surfaceView.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
                // 拿到拍照照片数据
                Image image = reader.acquireLatestImage();
//                Image image = reader.acquireNextImage();
                Log.i("test", "acquireNextImage");
//                mImageReader.close();
//                Log.i("test", "close");
//                mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG,1);
//                Log.i("test", "ImageReader");
//                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
                image.close();
//                reader.close();
            }
        }, mainHandler);
        //获取摄像头管理
        CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //創建攝像頭監聽
        CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {//打开摄像头
                Log.i("test", "創建攝像頭監聽 onOpened");
                mCameraDevice = camera;
                //开启预览
                try {
                    // 创建预览需要的CaptureRequest.Builder
                    final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    // 将SurfaceView的surface作为CaptureRequest.Builder的目标
                    previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
                    // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
                    mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback()
                    {
                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            Log.i("test", "顯示預覽 onConfigured");
                            if (null == mCameraDevice) return;
                            // 当摄像头已经准备好时，开始显示预览
                            mCameraCaptureSession = cameraCaptureSession;
                            try {
//                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);// 自动对焦
//                                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);// 打开闪光灯
                                // 显示预览
                                CaptureRequest previewRequest = previewRequestBuilder.build();
                                mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(MainActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                        }
                    }, childHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onDisconnected(CameraDevice camera) {//关闭摄像头
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    MainActivity.this.mCameraDevice = null;
                }
            }
            @Override
            public void onError(CameraDevice camera, int error) {//发生错误
                Toast.makeText(MainActivity.this, "攝像頭開啟失敗", Toast.LENGTH_SHORT).show();
            }
        };
        mCameraManager.openCamera(CameraID, stateCallback, mainHandler);
    }
}
