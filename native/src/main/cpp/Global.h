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
//#define CLS_STRING "java/lang/String"

#define JNI_VERSION JNI_VERSION_1_8

extern JavaVM *cachedJVM;

void throwIOException(JNIEnv *env, string msg);

void InputDevManager_Load(JNIEnv *env);

void InputDevManager_UnLoad(JNIEnv *env);

void U8g2Interface_Load(JNIEnv *env);

void U8g2Interface_UnLoad(JNIEnv *env);

#endif //PIDISP_GLOBAL_H
