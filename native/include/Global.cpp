/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: Global.cpp
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

#include <Global.h>
#include <iostream>
#include <vector>
#include <string>

#if defined(__linux__)
#include <execinfo.h> // for backtrace
#include <dlfcn.h>    // for dladdr
#include <cxxabi.h>   // for __cxa_demangle
#include <cstdio>
#include <cstdlib>
#include <sstream>
#endif

bool g_ShowExtraDebugInfo;
volatile sig_atomic_t g_SignalStatus;
JavaVM *g_CachedJVM;
jclass clsNativeLibraryException;
jclass clsSignalInterruptedException;

static std::vector<jobject> globalReferences;

void debug(const std::string& msg) {
#ifdef UCGD_DEBUG_LOG
    if (g_ShowExtraDebugInfo)
        std::cout << "[native] : " << msg << std::endl;
#endif
}

void JNI_Load(JavaVM *jvm) {
    g_CachedJVM = jvm;
    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
    JNI_MakeGlobal(env, CLS_NativeLibraryException, clsNativeLibraryException);
    JNI_MakeGlobal(env, CLS_SignalInterruptedException, clsSignalInterruptedException);
}

void JNI_Unload(JavaVM *vm) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);
    for (auto it : globalReferences) {
        env->DeleteGlobalRef(it);
    }
    globalReferences.clear();
}

void JNI_GetEnv(JNIEnv *env) {
    if (g_CachedJVM == nullptr) {
        std::cerr << "VM not yet cached" << std::endl;
        return;
    }
    g_CachedJVM->GetEnv((void **) &env, JNI_VERSION);
}

void JNI_MakeGlobal(JNIEnv *env, const char *name, jclass &cls) {
    jclass tmp;
    tmp = env->FindClass(name);
    cls = (jclass) env->NewGlobalRef(tmp);
    globalReferences.emplace_back(cls);
    env->DeleteLocalRef(tmp);
}

void JNI_MakeGlobal(JNIEnv *env, jobject &localObj, jobject &globalObj) {
    globalObj = env->NewGlobalRef(localObj);
    globalReferences.emplace_back(globalObj);
}

void JNI_ThrowIOException(JNIEnv *env, const std::string &msg) {
    jclass clsEx = env->FindClass(CLS_IOEXCEPTION);
    env->ThrowNew(clsEx, msg.c_str());
}

void JNI_ThrowNativeLibraryException(const std::string &msg) {
    JNIEnv *env;
    GETENV(env);
    env->ThrowNew(clsNativeLibraryException, msg.c_str());
}

void JNI_ThrowNativeLibraryException(JNIEnv *env, const std::string &msg) {
    env->ThrowNew(clsNativeLibraryException, msg.c_str());
}

void JNI_ThrowSignalInterruptedException(JNIEnv *env, const std::string &msg) {
    env->ThrowNew(clsSignalInterruptedException, msg.c_str());
}

void JNI_CopyJByteArray(JNIEnv *env, jbyteArray arr, uint8_t *buffer, int length) {
    if (length <= 0) {
        JNI_ThrowNativeLibraryException(env, "JNI_CopyJByteArray: Invalid array length");
        return;
    }
    if (buffer == nullptr) {
        JNI_ThrowNativeLibraryException(env, "JNI_CopyJByteArray: Buffer is null");
        return;
    }
    jbyte *body = env->GetByteArrayElements(arr, nullptr);
    for (int i = 0; i < length; i++) {
        buffer[i] = static_cast<uint8_t>(body[i]);
    }
    env->ReleaseByteArrayElements(arr, body, 0);
}

void JNI_CopyJIntArray(JNIEnv *env, jintArray arr, int *buffer, int length) {
    if (length <= 0) {
        JNI_ThrowNativeLibraryException(env, "JNI_CopyJIntArray: Invalid array length");
        return;
    }
    if (buffer == nullptr) {
        JNI_ThrowNativeLibraryException(env, "JNI_CopyJIntArray: Buffer is null");
        return;
    }
    jint *body = env->GetIntArrayElements(arr, nullptr);
    for (int i = 0; i < length; i++) {
        buffer[i] = static_cast<int>(body[i]);
    }
    env->ReleaseIntArrayElements(arr, body, 0);
}

// This function produces a stack backtrace with demangled function & method names.
// Reference: https://gist.github.com/fmela/591333
std::string Backtrace(int skip) {
#if defined(__linux__)
    void *callstack[128];
    const int nMaxFrames = sizeof(callstack) / sizeof(callstack[0]);
    char buf[1024];
    int nFrames = backtrace(callstack, nMaxFrames);
    char **symbols = backtrace_symbols(callstack, nFrames);

    std::ostringstream trace_buf;
    for (int i = skip; i < nFrames; i++) {
        printf("%s\n", symbols[i]);

        Dl_info info;
        if (dladdr(callstack[i], &info) && info.dli_sname) {
            char *demangled = nullptr;
            int status = -1;
            if (info.dli_sname[0] == '_')
                demangled = abi::__cxa_demangle(info.dli_sname, nullptr, 0, &status);
            snprintf(buf, sizeof(buf), "%-3d %*p %s + %zd\n",
                     i, int(2 + sizeof(void *) * 2), callstack[i],
                     status == 0 ? demangled :
                     info.dli_sname == 0 ? symbols[i] : info.dli_sname,
                     (char *) callstack[i] - (char *) info.dli_saddr);
            free(demangled);
        } else {
            snprintf(buf, sizeof(buf), "%-3d %*p %s\n",
                     i, int(2 + sizeof(void *) * 2), callstack[i], symbols[i]);
        }
        trace_buf << buf;
    }
    free(symbols);
    if (nFrames == nMaxFrames)
        trace_buf << "[truncated]\n";
    return trace_buf.str();
#else
    return std::string();
#endif
}
