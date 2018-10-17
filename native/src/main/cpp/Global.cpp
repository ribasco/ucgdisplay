//
// Created by raffy on 7/4/18.
//

#include "Global.h"
#include "U8g2Hal.h"
#include <iostream>
#include <vector>

JavaVM *cachedJVM;

jclass clsNativeLibraryException;
jclass clsU8g2EventDispatcher;
jclass clsU8g2GpioEvent;
jmethodID midU8g2EventDispatcher_onGpioEvent;
jmethodID midU8g2EventDispatcher_onByteEvent;
jmethodID midU8g2GpioEventCtr;

static vector<jclass> globalRefClasses;

#pragma clang diagnostic push
#pragma ide diagnostic ignored "OCUnusedGlobalDeclarationInspection"
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;
    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    //START: Cache Class/methods
    JNI_MakeGlobal(env, CLS_NativeLibraryException, clsNativeLibraryException);
    JNI_MakeGlobal(env, CLS_U8g2GpioEvent, clsU8g2GpioEvent);
    JNI_MakeGlobal(env, CLS_U8g2EventDispatcher, clsU8g2EventDispatcher);
    midU8g2EventDispatcher_onGpioEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onGpioEvent", "(JII)V");
    midU8g2EventDispatcher_onByteEvent = env->GetStaticMethodID(clsU8g2EventDispatcher, "onByteEvent", "(JII)V");
    midU8g2GpioEventCtr = env->GetMethodID(clsU8g2GpioEvent, "<init>", "(II)V");
    //END

#ifdef __linux__
    InputDevManager_Load(env);
#endif
    U8g2Graphics_Load(env);

    return JNI_VERSION;
}
#pragma clang diagnostic pop

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

#ifdef __linux__
    InputDevManager_UnLoad(env);
#endif
    U8g2Graphics_UnLoad(env);

    for (auto it : globalRefClasses) {
        env->DeleteGlobalRef(it);
    }

    globalRefClasses.clear();
}

void JNI_GetEnv(JNIEnv *env) {
    if (cachedJVM == nullptr) {
        cerr << "VM not yet cached" << endl;
        return;
    }
    cachedJVM->GetEnv((void **) &env, JNI_VERSION);
}

void JNI_MakeGlobal(JNIEnv *env, const char *name, jclass &cls) {
    jclass tmp;
    tmp = env->FindClass(name);
    cls = (jclass) env->NewGlobalRef(tmp);
    globalRefClasses.emplace_back(cls);
    env->DeleteLocalRef(tmp);
}

void JNI_ThrowIOException(JNIEnv *env, string msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

void JNI_ThrowNativeLibraryException(JNIEnv *env, string msg) {
    env->ThrowNew(clsNativeLibraryException, msg.c_str());
}

void JNI_CopyJByteArray(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length) {
    if (length <= 0) {
        JNI_ThrowNativeLibraryException(env, "Invalid array length");
        return;
    }
    if (buffer == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Buffer is null");
        return;
    }
    jbyte *body = env->GetByteArrayElements(arr, nullptr);
    for (int i = 0; i < length; i++) {
        buffer[i] = static_cast<uint8_t>(body[i]);
    }
    env->ReleaseByteArrayElements(arr, body, 0);
}

void JNI_FireGpioEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onGpioEvent, (jlong) id,  msg, value);
}

void JNI_FireByteEvent(JNIEnv *env, uintptr_t id, uint8_t msg, uint8_t value) {
    env->CallStaticVoidMethod(clsU8g2EventDispatcher, midU8g2EventDispatcher_onByteEvent, (jlong) id, msg, value);
}
