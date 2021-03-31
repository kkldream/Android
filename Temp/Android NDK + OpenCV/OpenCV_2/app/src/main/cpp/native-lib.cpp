#include "com_demo_opencv_MainActivity.h"
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include <math.h>
using namespace cv;

extern "C" JNIEXPORT void
JNICALL Java_com_demo_opencv_MainActivity_getEdge(JNIEnv *env, jobject obj, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    Mat temp(info.height, info.width, CV_8UC4, pixels);
    Mat gray;
    cvtColor(temp, gray, COLOR_RGBA2GRAY);
    Canny(gray,gray,var*12,var*12*2);
    cvtColor(gray, temp, COLOR_GRAY2RGBA);
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demo_opencv_MainActivity_getTest(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 || info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat dstImage;
        Mat();
        /*
        kern = (Mat_<float>(3, 3) <<
                0.0625, 0.125, 0.0625,
                0.125, 0.25, 0.125,
                0.0625, 0.125, 0.125);
        kern = (Mat_<float>(3, 3) <<
                -1, -1, -1,
                -1, 8, -1,
                -1, -1, -1);
        kern = (Mat_<int>(3, 3) <<
                0, 1, 0,
                1, -4, 1,
                0, 1, 0);
        kern = (Mat_<int>(5, 5) <<
                0, 0, 1, 0, 0,
                0, 0, 1, 0, 0,
                1, 1, i, 1, 1,
                0, 0, 1, 0, 0,
                0, 0, 1, 0, 0);
//        int arr[] = {-1, -1, -1,
//                       -1, 8, -1,
//                       -1, -1, -1};
//        kern = (Mat_<float>(3, 3) << arr);
//        kern = (Mat_<float>(3, 3) <<
//                -1, -1, -1,
//                -1, 8, -1,
//                -1, -1, -1);

//        kern = (Mat_<int>(5, 5) <<
//                -1, -1, -1, -1, -1,
//                -1, -1, -1, -1, -1,
//                -1, -1, 24, -1, -1,
//                -1, -1, -1, -1, -1,
//                -1, -1, -1, -1, -1);
//        kern = Mat_<int>(5, 5);
//        for (int a = 0; a < n; a++) {
//            kern.
//            kern[n / 2] = n - 1;
//            if (a == n / 2) ;
//        }
        */
        Mat kern;
        kern = (Mat_<float>(5, 5) <<
                -0.125, -0.125, -0.125, -0.125, -0.125,
                -0.125, 0.25, 0.25, 0.25, -0.125,
                -0.125, 0.25, 1, 0.25, -0.125,
                -0.125, 0.25, 0.25, 0.25, -0.125,
                -0.125, -0.125, -0.125, -0.125, -0.125);
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
//        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        filter2D(temp,temp,-1,kern,Point(-1,-1));
//        cvtColor(gray, temp, COLOR_GRAY2RGBA);
        /*
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        Canny(gray, gray, 45, 75);
        cvtColor(gray, temp, COLOR_GRAY2RGBA);
        */
    } else {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGB2GRAY);
        Canny(gray, gray, 45, 75);
        cvtColor(gray, temp, COLOR_GRAY2RGB);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demo_opencv_MainActivity_getLight(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 || info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat dstImage;
        Mat();
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
//        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        filter2D(temp,temp,-1,var + 1,Point(-1,-1));
//        cvtColor(gray, temp, COLOR_GRAY2RGBA);
        /*
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        Canny(gray, gray, 45, 75);
        cvtColor(gray, temp, COLOR_GRAY2RGBA);
        */
    } else {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGB2GRAY);
        Canny(gray, gray, 45, 75);
        cvtColor(gray, temp, COLOR_GRAY2RGB);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demo_opencv_MainActivity_getBlurry(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    var *= 5;
    int n1 = 1 + var * 2;
    int n2 = n1 * n1;
    Mat kern(n1, n1, CV_32F);
    float *pData1;
    for (int i = 0; i < n1; i++) {
        pData1 = kern.ptr<float>(i);
        for (int j = 0; j < n1; j++) {
            if (i + j < var ||
                n1-i-1 + n1-j-1 < var ||
                n1-i-1 + j < var ||
                i + n1-j-1 < var ||
                (i == n1/2 && j == n1/2)) pData1[j] = 0;
            else pData1[j] = 1 / float(n2 / 2);
        }
    }
    Mat temp(info.height, info.width, CV_8UC4, pixels);
    filter2D(temp,temp,-1,kern,Point(-1,-1));
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demo_opencv_MainActivity_getEmbossing(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    int n1 = 1 + var * 2;
    int n2 = n1 * n1;
    Mat kern(n1, n1, CV_32F);
    float *pData1;
    for (int i = 0; i < n1; i++) {
        pData1 = kern.ptr<float>(i);
        for (int j = 0; j < n1; j++) {
            if (i + j < n1 - 1) pData1[j] = -1;
            else if (n1-i-1 + n1-j-1 < n1 - 1) pData1[j] = 1;
            else pData1[j] = 0;
        }
    }
//    Mat kern;
//    kern = (Mat_<float>(3, 3) <<
//            2, 0, 0,
//            0, -1, 0,
//            0, 0, -1);
//    Mat temp(info.height, info.width, CV_8UC4, pixels);
//    filter2D(temp,temp,-1,kern,Point(-1,-1));

    Mat temp(info.height, info.width, CV_8UC4, pixels);
    Mat gray;
    cvtColor(temp, gray, COLOR_RGBA2GRAY);
    filter2D(gray,gray,-1,kern,Point(-1,-1));
    cvtColor(gray, temp, COLOR_GRAY2RGBA);
    AndroidBitmap_unlockPixels(env, bitmap);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demo_opencv_MainActivity_getSharpness(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
    AndroidBitmapInfo info;
    void *pixels;

    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 || info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    /*
    int kern = (Mat_<char>(3, 3) << 0, -1 ,0,
            -1, 5, -1,
            0, -1, 0);
    Mat dstImage;
    filter2D(srcImage,dstImage,srcImage.depth(),kern);
     */

    Mat dstImage;
//        Mat();
    /*
    kern = (Mat_<float>(3, 3) <<
            0.0625, 0.125, 0.0625,
            0.125, 0.25, 0.125,
            0.0625, 0.125, 0.125);
    kern = (Mat_<float>(3, 3) <<
            -1, -1, -1,
            -1, 8, -1,
            -1, -1, -1);
    kern = (Mat_<int>(3, 3) <<
            0, 1, 0,
            1, -4, 1,
            0, 1, 0);
    kern = (Mat_<int>(5, 5) <<
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0,
            1, 1, i, 1, 1,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0);
//        int arr[] = {-1, -1, -1,
//                       -1, 8, -1,
//                       -1, -1, -1};
//        kern = (Mat_<float>(3, 3) << arr);
//        kern = (Mat_<float>(3, 3) <<
//                -1, -1, -1,
//                -1, 8, -1,
//                -1, -1, -1);

//        kern = (Mat_<int>(5, 5) <<
//                -1, -1, -1, -1, -1,
//                -1, -1, -1, -1, -1,
//                -1, -1, 24, -1, -1,
//                -1, -1, -1, -1, -1,
//                -1, -1, -1, -1, -1);
//        kern = Mat_<int>(5, 5);
//        for (int a = 0; a < n; a++) {
//            kern.
//            kern[n / 2] = n - 1;
//            if (a == n / 2) ;
//        }
    */
    int n1 = 1 + var * 2;
    int n2 = n1 * n1;
    Mat kern(n1, n1, CV_32F);
    float *pData1;
    for (int i = 0; i < n1; i++) {
        if (i == n1 / 2) {
            pData1 = kern.ptr<float>(i);
            for (int j = 0; j < n1; j++) {
                if (j == i) pData1[j] = n2;
                else pData1[j] = -1;
            }
        } else {
            pData1 = kern.ptr<float>(i);
            for (int j = 0; j < n1; j++) {
                pData1[j] = -1;
            }
        }
    }
    Mat temp(info.height, info.width, CV_8UC4, pixels);

    filter2D(temp,temp,-1,kern,Point(-1,-1));


//    Mat gray;
//    cvtColor(temp, gray, COLOR_RGBA2GRAY);
////    Canny(gray,gray,var*12,var*12*2);
////    filter2D(gray,gray,-1,kern,Point(-1,-1));
//    cvtColor(gray, temp, COLOR_GRAY2RGBA);
    /*
    Mat temp(info.height, info.width, CV_8UC4, pixels);
    Mat gray;
    cvtColor(temp, gray, COLOR_RGBA2GRAY);
    Canny(gray, gray, 45, 75);
    cvtColor(gray, temp, COLOR_GRAY2RGBA);
    */

    AndroidBitmap_unlockPixels(env, bitmap);
}