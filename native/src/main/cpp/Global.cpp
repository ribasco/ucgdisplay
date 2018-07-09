//
// Created by raffy on 7/4/18.
//

#include "Global.h"
#include <iostream>

JavaVM *cachedJVM;

void throwIOException(JNIEnv *env, string msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cout << "JNI_Load" << endl;
    cachedJVM = jvm;
    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    InputDevManager_Load(env);
    U8g2Interface_Load(env);

    return JNI_VERSION;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    InputDevManager_UnLoad(env);
    U8g2Interface_UnLoad(env);
}