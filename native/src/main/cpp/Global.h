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
#define CLS_U8g2EventDispatcher "com/ibasco/pidisplay/core/u8g2/U8g2EventDispatcher"
#define CLS_U8g2GpioEvent "com/ibasco/pidisplay/core/u8g2/U8g2GpioEvent"

#define JNI_VERSION JNI_VERSION_1_8

#define GETENV(e) cachedJVM->GetEnv((void **) &e, JNI_VERSION);

#if defined(__linux__) && (defined(__x86_64__) || defined(i386))
#define USE_EMULATOR
#endif

extern JavaVM *cachedJVM;
extern jclass clsU8g2GpioEvent;
extern jclass clsU8g2EventDispatcher;
extern jclass clsGlcdNativeDriverException;
extern jmethodID midU8g2EventDispatcher_onGpioEvent;
extern jmethodID midU8g2EventDispatcher_onByteEvent;
extern jmethodID midU8g2GpioEventCtr;

/**
 * Throws a GlcdNativeDriverException to java
 *
 * @param env JNIEnv instance
 * @param msg The exception message
 */
void JNI_ThrowNativeDriverException(JNIEnv *env, string msg);

/**
 * Throws an IOException to java
 * @param env JNIEnv instance
 * @param msg The exception message
 */
void JNI_ThrowIOException(JNIEnv *env, string msg);

/**
 * Creates a JNIEnv instance from the cached JVM
 * @param env The env variable to assign
 */
void JNI_GetEnv(JNIEnv *env);

/**
 * Convert a local reference variable to global
 * @param env JNIEnv instance
 * @param name Fully qualified class name
 * @param cls The jclass variable where the global reference will be stored
 */
void JNI_MakeGlobal(JNIEnv *env, const char *name, jclass &cls);

/**
 * Copy jByteArray to a byte buffer
 *
 * @param env JNIEnv instance
 * @param arr  The source jbyteArray
 * @param buffer  The destination buffer
 * @param length The number of bytes to copy
 */
void JNI_CopyJByteArray(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length);

/**
 * Fires a GpioEvent to the attached listeners
 *
 * @param env JNIEnv instance
 * @param msg The u8g2 message code
 * @param value The u8g2 messave value
 */
void JNI_FireGpioEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value);

/**
 * Fires a ByteEvent to the attached listeners
 *
 * @param env JNIEnv instance
 * @param value The data associated with the event
 */
void JNI_FireByteEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value);

void InputDevManager_Load(JNIEnv *env);

void InputDevManager_UnLoad(JNIEnv *env);

void U8g2Graphics_Load(JNIEnv *env);

void U8g2Graphics_UnLoad(JNIEnv *env);

#endif //PIDISP_GLOBAL_H
