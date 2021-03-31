#include "com_demo_opencv_MainActivity.h"
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C" JNIEXPORT void
JNICALL Java_com_demo_opencv_MainActivity_getEdge(JNIEnv *env, jobject obj, jobject bitmap, jint edge) {
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
        int n1 = 1 + edge * 2;
        int n2 = n1 * n1;
        Mat kern(n1, n1, CV_32F);
        float *pData1;
        for (int i = 0; i < n1; i++) {
            if (i == n1 / 2) {
                pData1 = kern.ptr<float>(i);
                for (int j = 0; j < n1; j++) {
                    if (j == i) pData1[j] = n2 - 1;
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
        Mat gray;
        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        filter2D(gray,gray,-1,kern,Point(-1,-1));
        cvtColor(gray, temp, COLOR_GRAY2RGBA);
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
Java_com_demo_opencv_MainActivity_getLight(JNIEnv *env, jobject thiz, jobject bitmap, jint light) {
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
        filter2D(temp,temp,-1,light,Point(-1,-1));
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