package com.niu.native_opencv;

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
import androidx.arch.core.util.Function;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera {
    private static final String TAG = "TAG_CAREAM";
    public static final int AE_MODE_OFF = CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH;
    public static final int AE_MODE_ON_AUTO_FLASH = CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH;
    private Context mContext;
    private CameraManager mCameraManager;
    private Handler childHandler, mainHandler;
    private ImageReader mimageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;
    private SurfaceHolder mSurfaceHolder;
    int cameraID;
    int aeMode;

    Camera(Context context) {
        mContext = context;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    void setSurfaceView(SurfaceView surfaceView) {
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setFixedSize(500, 1000);
        mSurfaceHolder.setKeepScreenOn(true);
    }

    @SuppressLint("MissingPermission")
    void open(int cameraid) {
        Log.d(TAG,"open");
        cameraID = cameraid;
        HandlerThread handlerThread = new HandlerThread("Camera");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(mContext.getMainLooper());
        mimageReader = ImageReader.newInstance(1000, 1000, ImageFormat.JPEG,1);
//        mimageReader.setOnImageAvailableListener(imageReader -> {
//            Log.d(TAG,"onImageAvailable");
//            Image image = imageReader.acquireNextImage();
//            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            Bitmap obitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
//
//            image.close();
//            close();
//        }, mainHandler);
        try {
            mCameraManager.openCamera(String.valueOf(cameraid), new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    mCameraDevice = cameraDevice;
                    try {
                        CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
                        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mimageReader.getSurface()), new CameraCaptureSession.StateCallback() {
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

    void setAeMode(int mode) {
        aeMode = mode;
    }

    void takePicture(Function function) {
        mimageReader.setOnImageAvailableListener(imageReader -> {
            Image image = imageReader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            Bitmap obitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            function.apply(obitmap);
            image.close();
        }, mainHandler);
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(mimageReader.getSurface());
            // 自動對焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 曝光設定
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, aeMode);
//        // 获取手机方向
//        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
//        // 根据设备方向计算设置照片的方向
//            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
//            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 0);
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
        if (mCameraCaptureSession != null) mCameraCaptureSession.close();
        if (mCameraDevice != null) mCameraDevice.close();
        if (mimageReader != null) mimageReader.close();
        if (childHandler != null) childHandler.removeCallbacksAndMessages(null);
        if (mainHandler != null) mainHandler.removeCallbacksAndMessages(null);
//        mCameraManager = null;
//        mSurfaceHolder = null;
        mCameraCaptureSession = null;
        mCameraDevice = null;
        mimageReader = null;
        childHandler = null;
        mainHandler = null;
    }

    void flip() {
        close();
        cameraID = (cameraID == CameraCharacteristics.LENS_FACING_FRONT) ?
                CameraCharacteristics.LENS_FACING_BACK : CameraCharacteristics.LENS_FACING_FRONT;
        open(cameraID);
    }
}
