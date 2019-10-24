/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: Global.h
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
#ifndef UCGDISP_GLOBAL_H
#define UCGDISP_GLOBAL_H

#include <jni.h>
#include <string>

//Comment USE_GPIOUSERSPACE to use the gpio sysfs version
#define USE_GPIOUSERSPACE

#ifdef USE_GPIOUSERSPACE
#define GPIOUS_CONSUMER "ucgdisplay"
#endif

// Global class/method signatures
#define CLS_IOEXCEPTION "java/io/IOException"
#define CLS_ARRAYLIST "java/util/ArrayList"
#define CLS_HASHMAP  "java/util/HashMap"
#define CLS_INTEGER "java/lang/Integer"
#define CLS_THREADGROUP "java/lang/ThreadGroup"
#define CLS_NativeLibraryException "com/ibasco/ucgdisplay/common/exceptions/NativeLibraryException"
#define CLS_U8g2EventDispatcher "com/ibasco/ucgdisplay/core/u8g2/U8g2EventDispatcher"
#define CLS_U8g2GpioEvent "com/ibasco/ucgdisplay/core/u8g2/U8g2GpioEvent"

#define CLS_INPUT_DEVICE "com/ibasco/ucgdisplay/core/input/InputDevice"
#define CLS_INPUT_EVENT_TYPE "com/ibasco/ucgdisplay/core/input/InputEventType"
#define CLS_INPUT_EVENT_CODE "com/ibasco/ucgdisplay/core/input/InputEventCode"
#define CLS_INPUT_DEVICE_MGR "com/ibasco/ucgdisplay/core/input/InputDeviceManager"
#define CLS_RAW_INPUT_EVENT "com/ibasco/ucgdisplay/core/input/RawInputEvent"
#define CLS_DEVICE_STATE_EVENT "com/ibasco/ucgdisplay/core/input/DeviceStateEvent"

#define FSIG_INPUTEVENTCODE_ABSDATA "Ljava/util/Map;"
#define MIDSIG_INPUTDEVICE_CTR "(Ljava/lang/String;Ljava/lang/String;[SLjava/lang/String;Ljava/util/List;)V"
#define MIDSIG_INPUTDEVMGR_DEVEVENTCB "(Lcom/ibasco/ucgdisplay/core/input/DeviceStateEvent;)V"
#define MIDSIG_INPUTEVENTTYPE_CTR1 "(Ljava/lang/String;ILjava/util/List;Z)V"
#define MIDSIG_INPUTEVENTCODE_CTR1 "(Ljava/lang/String;I)V"
#define MIDSIG_INPUTEVENTCODE_CTR2 "(Ljava/lang/String;II)V"
#define MIDSIG_INPUTDEVMGR_CALLBACK "(Lcom/ibasco/ucgdisplay/core/input/RawInputEvent;)V"
#define MIDSIG_RAWINPUTEVENT_CTR "(Lcom/ibasco/ucgdisplay/core/input/InputDevice;JIIILjava/lang/String;Ljava/lang/String;I)V"
#define MIDSIG_HASHMAP_PUT "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
#define MIDSIG_HASHMAP_CTR "()V"
#define MIDSIG_DEVSTATEVT_CTR "(Lcom/ibasco/ucgdisplay/core/input/InputDevice;Ljava/lang/String;)V"

#define JNI_VERSION JNI_VERSION_1_8

#define GETENV(e) cachedJVM->GetEnv((void **) &e, JNI_VERSION);

extern JavaVM *cachedJVM;

/**
 * Initialize global references
 *
 * @param jvm
 */
void JNI_Load(JavaVM *jvm);

/**
 * Unload/deinitialize global references
 * @param vm
 */
void JNI_Unload(JavaVM *vm);

/**
 * Throws a NativeLibraryException to java
 *
 * @param msg The exception message
 */
void JNI_ThrowNativeLibraryException(const std::string& msg);

/**
 * Throws a NativeLibraryException to java
 *
 * @param env JNIEnv instance
 * @param msg The exception message
 */
void JNI_ThrowNativeLibraryException(JNIEnv *env, std::string msg);

/**
 * Throws an IOException to java
 * @param env JNIEnv instance
 * @param msg The exception message
 */
void JNI_ThrowIOException(JNIEnv *env, std::string msg);

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

void JNI_CopyJIntArray(JNIEnv *env, jintArray arr, int *buffer, int length);

void InputDevManager_Load(JNIEnv *env);

void InputDevManager_UnLoad(JNIEnv *env);

/*
string getBinaryString(unsigned int u) {
    stringstream str;
    int t;
    for (t = 128; t > 0; t = t / 2) {
        if (u & t) str << "1 ";
        else str << "0 ";
    }
    return str.str();
}

std::string hexStr(unsigned char *data, int len) {
    std::stringstream ss;
    ss << std::hex;
    for (int i = 0; i < len; ++i)
        ss << std::setw(2) << std::setfill('0') << (int) data[i];
    return ss.str();
}
*/

#endif //UCGDISP_GLOBAL_H

