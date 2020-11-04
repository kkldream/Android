//
// Created by kk693 on 2020/4/25.
//
#include <jni.h>
#include <android/log.h>

#include <string.h>
#include <stdio.h>
#include <android/bitmap.h>
#include <assert.h>
#include <opencv2/core/core.hpp>
#include <opencv2/core/mat.hpp>

#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#ifndef LOG
#define LOG_TAG "imgprocess"
#define ALOGD(...) \
            __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__);
#define ALOGE(...) \
            __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__);
#define ALOGV(...) \
            __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__);
#endif LOG


#ifndef NELEM
# define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif

// 若这里不加namespace cv，则下面所有openCV的数据类型都要加cv::限定
using namespace cv;

// 传入一个bitmap对象，传出一个包含边缘信息的bitmap对象
// 注意前两个参数是JNIEnv* env, jobject thiz
// JNIEnv标识当前NDK环境的对象指针,可以通过该参数访问NDK中的内置成员，
// jobject表示调用当前NDK方法的Java对象，可以用该参数值访问调用该方法的Java对象成员。
jobject getEdge(JNIEnv* env, jobject thiz, jobject bitmap){

    AndroidBitmapInfo bitmapInfo;
    uint32_t* storedBitmapPixels = NULL;
    int pixelsCount;
    int ret = -1;

    // 读取bitmap基本信息
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        return NULL;
    }
    ALOGD("width:%d height:%d stride:%d", bitmapInfo.width, bitmapInfo.height, bitmapInfo.stride);

    // 这里只处理RGBA_888类型的bitmap
    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return NULL;
    }

    // 提取像素值
    void* bitmapPixels = NULL;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0) {
        return NULL;
    }

    // 生成openCV Mat矩阵
    Mat srcMat(Size(bitmapInfo.width, bitmapInfo.height), CV_8UC4);
    pixelsCount = bitmapInfo.height * bitmapInfo.width;
    memcpy(srcMat.data, bitmapPixels, sizeof(uint32_t) * pixelsCount);
    AndroidBitmap_unlockPixels(env, bitmap);

    // 处理求边缘得到desMat
    Mat desMat;
    Canny(srcMat, desMat, 30.0, 90.0, 3, true);

    // 通过JAVA层的Bitmap类，新建一个bitmap对象
    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass, "valueOf", "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass, valueOfBitmapConfigFunction, configName);
    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls, createBitmapFunction, desMat.cols, desMat.rows, bitmapConfig);

    // 获取新bitmap的data数据指针
    bitmapPixels = NULL;
    if ((ret = AndroidBitmap_lockPixels(env, newBitmap, &bitmapPixels)) < 0) {
        return NULL;
    }

    // 将Mat数据写入bitmap
    uint32_t* newBitmapPixels = (uint32_t*)bitmapPixels;
    pixelsCount = srcMat.cols * desMat.rows;
    for (size_t i = 0; i < pixelsCount; i++) {
        memset(&(newBitmapPixels[i]), srcMat.data[i], 3);
    }

    AndroidBitmap_unlockPixels(env, newBitmap);
    return newBitmap;
}



// native函数所在的类
static const char *classPathName = "test/hc/cvtest/MainActivity";

//
static JNINativeMethod gMethods[] = {
        {"getEdge", "(Ljava/lang/Object;)Ljava/lang/Object;", (void*)getEdge},
};

int registerNativeMethods(JNIEnv* env, const char* className,
                          JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
        ALOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        ALOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

int register_jni_methods(JNIEnv* env)
{
    return registerNativeMethods(env, classPathName, gMethods, NELEM(gMethods));
}

jint JNI_OnLoad(JavaVM* vm, void*)
{
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        ALOGE("ERROR: GetEnv failed\n");
        goto bail;
    }
    assert(env != NULL);

    if (register_jni_methods(env) < 0) {
        ALOGE("ERROR: native registration failed\n");
        goto bail;
    }

    ALOGE("SUCCESS: native registration successed\n");
    result = JNI_VERSION_1_4;

    bail:
    return result;
}
