#include "Log.h"
#include <Global.h>

static jobject getLogObject(JNIEnv *env, jobject source) {
    assert(source);

    auto class_ = env->GetObjectClass(source);
    assert(class_);

    auto logField = env->GetStaticFieldID(class_, "log", "Lorg/slf4j/Logger;");
    assert(logField);

    auto logObject = env->GetStaticObjectField(class_, logField);
    assert(logObject);

    return logObject;
}

static jmethodID getMethod(JNIEnv *env, jobject log, const std::string &method) {
    auto methodId = env->GetMethodID(env->GetObjectClass(log), method.c_str(), "(Ljava/lang/String;[Ljava/lang/Object;)V");
    assert(methodId);
    return methodId;
}

static JNIEnv *getEnv() {
    JNIEnv *env;
    GETENV(env);
    return env;
}

Log::Log(jobject source) :
        env(getEnv()),
        object(source),
        infoMethod(getMethod(env, object, "info")),
        debugMethod(getMethod(env, object, "debug")),
        warnMethod(getMethod(env, object, "warn")),
        errorMethod(getMethod(env, object, "error")) {

}

Log::~Log() = default;
