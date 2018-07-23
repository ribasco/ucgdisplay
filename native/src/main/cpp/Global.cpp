//
// Created by raffy on 7/4/18.
//

#include "Global.h"
#include <iostream>
#include <vector>

JavaVM *cachedJVM;

jclass clsGlcdNativeDriverException;
jclass clsGpioEventService;
jclass clsGpioEvent;
jmethodID midGpioEventService_onGpioEvent;
jmethodID midGpioEventCtr;

static vector<jclass> globalRefClasses;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;
    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    //START: Cache Class/methods
    JNI_MakeGlobal(env, CLS_GlcdNativeDriverException, clsGlcdNativeDriverException);
    JNI_MakeGlobal(env, CLS_GPIOEVENT, clsGpioEvent);
    JNI_MakeGlobal(env, CLS_GPIOEVENTSERVICE, clsGpioEventService);
    midGpioEventService_onGpioEvent = env->GetStaticMethodID(clsGpioEventService, "onGpioEvent", "(Lcom/ibasco/pidisplay/core/gpio/GpioEvent;)V");
    midGpioEventCtr = env->GetMethodID(clsGpioEvent, "<init>", "(IIILjava/lang/String;)V");
    //END

    InputDevManager_Load(env);
    U8g2Interface_Load(env);

    return JNI_VERSION;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    InputDevManager_UnLoad(env);
    U8g2Interface_UnLoad(env);

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
    //cachedJVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
}

void JNI_MakeGlobal(JNIEnv *env, const char *name, jclass &cls) {
    jclass tmp;
    tmp = env->FindClass(name);
    cls = (jclass) env->NewGlobalRef(tmp);
    globalRefClasses.emplace_back(cls);
    env->DeleteLocalRef(tmp);
}

void JNI_throwIOException(JNIEnv *env, string msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

void JNI_throwNativeDriverException(JNIEnv *env, string msg) {
    env->ThrowNew(clsGlcdNativeDriverException, msg.c_str());
}