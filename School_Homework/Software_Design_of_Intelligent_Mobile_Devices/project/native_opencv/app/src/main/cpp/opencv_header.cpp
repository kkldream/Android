#include <jni.h>
#include <string>
//#include <android/bitmap.h>
//#include <opencv2/opencv.hpp>
//#include <math.h>
//using namespace cv;

extern "C" JNIEXPORT jstring JNICALL
Java_com_niu_native_1opencv_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

//extern "C" JNIEXPORT void JNICALL
//Java_com_niu_native_1opencv_MainActivity_getTest(JNIEnv *env, jobject thiz, jobject bitmap, jint var) {
//    AndroidBitmapInfo info;
//    void *pixels;
//    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
//    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 || info.format == ANDROID_BITMAP_FORMAT_RGB_565);
//    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
//    CV_Assert(pixels);
//    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//        Mat dstImage;
//        Mat kern;
//        kern = (Mat_<float>(5, 5) <<
//                                  -0.125, -0.125, -0.125, -0.125, -0.125,
//                -0.125, 0.25, 0.25, 0.25, -0.125,
//                -0.125, 0.25, 1, 0.25, -0.125,
//                -0.125, 0.25, 0.25, 0.25, -0.125,
//                -0.125, -0.125, -0.125, -0.125, -0.125);
//        Mat temp(info.height, info.width, CV_8UC4, pixels);
//        Mat gray;
////        cvtColor(temp, gray, COLOR_RGBA2GRAY);
//        filter2D(temp,temp,-1,kern,Point(-1,-1));
////        cvtColor(gray, temp, COLOR_GRAY2RGBA);
//        /*
//        Mat temp(info.height, info.width, CV_8UC4, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGBA2GRAY);
//        Canny(gray, gray, 45, 75);
//        cvtColor(gray, temp, COLOR_GRAY2RGBA);
//        */
//    } else {
//        Mat temp(info.height, info.width, CV_8UC2, pixels);
//        Mat gray;
//        cvtColor(temp, gray, COLOR_RGB2GRAY);
//        Canny(gray, gray, 45, 75);
//        cvtColor(gray, temp, COLOR_GRAY2RGB);
//    }
//    AndroidBitmap_unlockPixels(env, bitmap);
//}