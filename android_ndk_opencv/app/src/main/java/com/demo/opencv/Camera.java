package com.demo.opencv;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera {
    private static final String TAG = "TAG_CAREAM";
    private CameraManager mCameraManager;//摄像头管理器
    private Context context;
    private Handler childHandler, mainHandler;
    private String mCameraID; //摄像头Id 0 为后 1 为前
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    Camera(Context context) {
        this.context = context;
        mCameraID = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(context.getMainLooper());
        mImageReader = ImageReader.newInstance(1000, 1000, ImageFormat.JPEG,1);
//        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
//            @Override
//            public void onImageAvailable(ImageReader imageReader) {
//                Log.d(TAG,"onImageAvailable");
//                Image image = imageReader.acquireNextImage();
//                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                byte[] bytes = new byte[buffer.remaining()];
//                buffer.get(bytes);
//                Bitmap obitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
//
//                image.close();
//                close();
//            }
//        }, mainHandler);
    }

    @SuppressLint("MissingPermission")
    void open(SurfaceView surfaceView) {
        Log.d(TAG,"open");
        mSurfaceView = surfaceView;
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setFixedSize(1000, 1000);
        mSurfaceHolder.setKeepScreenOn(true);
        try {
            mCameraManager.openCamera(mCameraID, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    mCameraDevice = cameraDevice;
                    try {
                        CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
                        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                                mCameraCaptureSession = cameraCaptureSession;
                                try {
                                    CaptureRequest previewRequest = previewRequestBuilder.build();
                                    mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                                Log.e(TAG,"onConfigureFailed");
                            }
                        }, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    Log.e(TAG,"onDisconnected");
//                    cameraDevice.close();
//                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int i) {
                    Log.e(TAG,"onError");
                }
            }, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, String.valueOf(e));
        }
    }

//    void takePicture() {
    void takePicture(ImageReader.OnImageAvailableListener imageAvailableListener) {
        mImageReader.setOnImageAvailableListener(imageAvailableListener, mainHandler);
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自動對焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 曝光設定
//        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
//        // 获取手机方向
//        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
//        // 根据设备方向计算设置照片的方向
//            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 0);
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    void close() {
        Log.d(TAG,"close");
        if (mSurfaceView != null) {
            mSurfaceView = null;
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder = null;
        }
        if (childHandler != null) {
            childHandler.removeCallbacksAndMessages(null);
            childHandler = null;
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
            mainHandler = null;
        }
        if (null != mCameraManager) {
            mCameraManager = null;
        }
        if (null != mCameraCaptureSession) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }
}
