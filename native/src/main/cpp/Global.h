//
// Created by raffy on 7/4/18.
//

#ifndef PIDISP_GLOBAL_H
#define PIDISP_GLOBAL_H

#include <jni.h>
#include <string>

using namespace std;

#define CLS_IOEXCEPTION "java/io/IOException"
#define CLS_ARRAYLIST "java/util/ArrayList"
#define CLS_HASHMAP  "java/util/HashMap"
#define CLS_INTEGER "java/lang/Integer"
#define CLS_THREADGROUP "java/lang/ThreadGroup"
#define CLS_GlcdNativeDriverException "com/ibasco/pidisplay/drivers/glcd/exceptions/GlcdNativeDriverException"
#define CLS_GPIOEVENTSERVICE "com/ibasco/pidisplay/core/gpio/GpioEventService"
#define CLS_GPIOEVENT "com/ibasco/pidisplay/core/gpio/GpioEvent"

#define JNI_VERSION JNI_VERSION_1_8

#define GETENV(e) cachedJVM->GetEnv((void **) &e, JNI_VERSION);

#if defined(__linux__) && (defined(__x86_64__) || defined(i386))
#define USE_EMULATOR
#endif

extern JavaVM *cachedJVM;

extern jclass clsGpioEvent;
extern jclass clsGpioEventService;
extern jclass clsGlcdNativeDriverException;
extern jmethodID midGpioEventService_onGpioEvent;
extern jmethodID midGpioEventCtr;

/**
 * Throws a native driver exception to java
 *
 * @param env The JNIEnv of the calling code
 * @param msg The exception message to be displayed
 */
void JNI_throwNativeDriverException(JNIEnv *env, string msg);

void JNI_throwIOException(JNIEnv *env, string msg);

void JNI_GetEnv(JNIEnv *env);

void JNI_MakeGlobal(JNIEnv *env, const char *name, jclass &cls);

void InputDevManager_Load(JNIEnv *env);

void InputDevManager_UnLoad(JNIEnv *env);

void U8g2Interface_Load(JNIEnv *env);

void U8g2Interface_UnLoad(JNIEnv *env);

#endif //PIDISP_GLOBAL_H
