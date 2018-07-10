#include <map>
#include <memory>
#include <iostream>
#include <cstdint>
#include <functional>
#include <utility>

#include "U8g2Interface.h"
#include "Global.h"
#include "U8g2Hal.h"
#include "U8g2Utils.h"
#include <u8g2.h>
#include <wiringPi.h>
#include <cstring>
#include <iomanip>

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"
#pragma ide diagnostic ignored "OCUnusedGlobalDeclarationInspection"

using namespace std;

jclass clsGlcdNativeDriverException;

void U8g2Interface_Load(JNIEnv *env) {
    //Load Classes
    jclass lGlcdNativeDriverException;
    lGlcdNativeDriverException = env->FindClass(CLS_GlcdNativeDriverException);
    clsGlcdNativeDriverException = (jclass) env->NewGlobalRef(lGlcdNativeDriverException);
    env->DeleteLocalRef(lGlcdNativeDriverException);
    //Initialize HAL
    u8g2hal_init();
}

void U8g2Interface_UnLoad(JNIEnv *env) {
    env->DeleteLocalRef(clsGlcdNativeDriverException);
}

//u8g2_SetI2CAddress(&u8g2, adr)
/*u8g2_GetI2CAddress();
u8x8_SetPin(u8x8, U8X8_PIN_D0, d0);
u8x8_SetPin(u8x8, U8X8_PIN_D1, d1);
u8x8_SetPin(u8x8, U8X8_PIN_D2, d2);
u8x8_SetPin(u8x8, U8X8_PIN_D3, d3);
u8x8_SetPin(u8x8, U8X8_PIN_D4, d4);
u8x8_SetPin(u8x8, U8X8_PIN_D5, d5);
u8x8_SetPin(u8x8, U8X8_PIN_D6, d6);
u8x8_SetPin(u8x8, U8X8_PIN_D7, d7);
u8x8_SetPin(u8x8, U8X8_PIN_E, enable);
u8x8_SetPin(u8x8, U8X8_PIN_CS, cs);
u8x8_SetPin(u8x8, U8X8_PIN_DC, dc);
u8x8_SetPin(u8x8, U8X8_PIN_RESET, reset);*/
jlong Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setup(JNIEnv *env, jclass cls, jstring setupProc, jint commInt, jint commType, jint rotation, jint address, jbyteArray pin_config) {
    string setup_proc_name;
    if (setupProc != nullptr) {
        setup_proc_name = string(env->GetStringUTFChars(setupProc, nullptr));
    }

    //1. Setup procedure should not be empty
    if (setup_proc_name.empty()) {
        throwNativeDriverException(env, "Setup procedure name cannot be empty");
        return -1;
    }

    //2. Verify pin mapping
    jsize len = env->GetArrayLength(pin_config);
    if (len != 16) {
        throwNativeDriverException(env, string("Pin map array should be exactly 16 of length (Actual: ") + to_string(len) + string(")"));
        return -1;
    }

    uint8_t tmp[len];
    copyjByteArray(env, pin_config, tmp, len);

    //convert to struct
    auto *pinMap = reinterpret_cast<u8g2_pin_map_t *>(tmp);

    //3. Verify that the rotation number is within the allowed range
    if (rotation > 4)
        throwNativeDriverException(env, string("Invalid rotation (") + to_string(rotation) + ")");

    //Get actual rotation value
    const u8g2_cb_t *_rotation = U8G2_R0;
    if (rotation == 0) {
        _rotation = const_cast<u8g2_cb_t *>(U8G2_R0);
    } else if (rotation == 1) {
        _rotation = const_cast<u8g2_cb_t *>(U8G2_R1);
    } else if (rotation == 2) {
        _rotation = const_cast<u8g2_cb_t *>(U8G2_R2);
    } else if (rotation == 3) {
        _rotation = const_cast<u8g2_cb_t *>(U8G2_R3);
    } else if (rotation == 4) {
        _rotation = const_cast<u8g2_cb_t *>(U8G2_MIRROR);
    }

    //4. Setup and Initialize the Display
    shared_ptr<u8g2_info_t> info = setupAndInitDisplay(setup_proc_name, commInt, commType, _rotation, *pinMap);

    //5. Verify if display has been initialized successfully
    if (info == nullptr) {
        throwNativeDriverException(env, string("Unable to initialize the display device. It's possible that you have specified the wrong setup procedure"));
        return -1;
    }

    //6. Retrieve the pointer address
    auto addr = (uintptr_t) info->u8g2.get();

    cout << "Display successfully initialized (Address: " << to_string(addr) << ")" << endl;
    return addr;
}

//long id, int x, int y, int width, int height
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawBox(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height) {
    u8g2_DrawBox(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height));
}

//long id, int x, int y, int count, int height, byte[] bitmap
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawBitmap(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint count, jint height, jbyteArray bitmap) {
    jsize len = env->GetArrayLength(bitmap);
    uint8_t tmp[len];
    copyjByteArray(env, bitmap, tmp, len);
    u8g2_DrawBitmap(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(count), static_cast<u8g2_uint_t>(height), tmp);
}

//long id, int x, int y, int radius, int options
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawCircle(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint radius, jint options) {
    u8g2_DrawCircle(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(radius), static_cast<uint8_t>(options));
}

//long id, int x, int y, int radius, int options
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawDisc(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint radius, jint options) {
    u8g2_DrawDisc(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(radius), static_cast<uint8_t>(options));
}

//long id, int x, int y, int rx, int ry, int options
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawEllipse(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint rx, jint ry, jint options) {
    u8g2_DrawEllipse(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(rx), static_cast<u8g2_uint_t>(ry), static_cast<uint8_t>(options));
}

//long id, int x, int y, int rx, int ry, int options
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawFilledEllipse(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint rx, jint ry, jint options) {
    u8g2_DrawFilledEllipse(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(rx), static_cast<u8g2_uint_t>(ry), static_cast<uint8_t>(options));
}

//long id, int x, int y, int width, int height
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawFrame(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height) {
    u8g2_DrawFrame(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height));
}

//long id, int x, int y, short encoding
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawGlyph(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jshort encoding) {
    u8g2_DrawGlyph(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<uint16_t>(encoding));
}

//long id, int x, int y, int width
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawHLine(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width) {
    u8g2_DrawHLine(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width));
}

//long id, int x, int y, int x1, int y1
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawLine(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint x1, jint y1) {
    u8g2_DrawLine(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(x1), static_cast<u8g2_uint_t>(y1));
}

//long id, int x, int y
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawPixel(JNIEnv *env, jclass cls, jlong id, jint x, jint y) {
    u8g2_DrawPixel(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y));
}

//long id, int x, int y, int width, int height, int radius
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawRoundedBox(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jint radius) {
    u8g2_DrawRBox(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), static_cast<u8g2_uint_t>(radius));
}

//long id, int x, int y, int width, int height, int radius
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawRoundedFrame(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jint radius) {
    u8g2_DrawRFrame(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), static_cast<u8g2_uint_t>(radius));
}

//long id, int x, int y, String value
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawString(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jstring value) {
    const char *c = env->GetStringUTFChars(value, nullptr);
    u8g2_DrawStr(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), c);
}

//long id, int x0, int y0, int x1, int y1, int x2, int y2
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawTriangle(JNIEnv *env, jclass cls, jlong id, jint x0, jint y0, jint x1, jint y1, jint x2, jint y2) {
    u8g2_DrawTriangle(toU8g2(id), static_cast<int16_t>(x0), static_cast<int16_t>(y0), static_cast<int16_t>(x1), static_cast<int16_t>(y1), static_cast<int16_t>(x2), static_cast<int16_t>(y2));
}

//long id, int x, int y, int width, int height, byte[] data
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawXBM(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jbyteArray data) {
    if (data == nullptr)
        return;
    jsize len = env->GetArrayLength(data);
    uint8_t tmp[len];
    copyjByteArray(env, data, tmp, len);
    u8g2_DrawXBM(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), tmp);
}

//long id, int x, int y, String value
jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_drawUTF8(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jstring value) {
    if (value == nullptr)
        return -1;
    const char *c = env->GetStringUTFChars(value, nullptr);
    return u8g2_DrawUTF8(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), c);
}

//long id, String text
jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getUTF8Width(JNIEnv *env, jclass cls, jlong id, jstring text) {
    const char *c = env->GetStringUTFChars(text, nullptr);
    return u8g2_GetUTF8Width(toU8g2(id), c);
}

//long id, byte[] data
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFont__J_3B(JNIEnv *env, jclass cls, jlong id, jbyteArray data) {
    jsize len = env->GetArrayLength(data);
    if (len <= 0) {
        throwNativeDriverException(env, "Invalid font data");
        return;
    }
    uint8_t tmp[len];
    copyjByteArray(env, data, tmp, len);
    u8g2_SetFont(toU8g2(id), tmp);
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFont__JLjava_lang_String_2(JNIEnv *env, jclass cls, jlong id, jstring fontName) {
    if (fontName == nullptr) {
        throwNativeDriverException(env, "Font cannot be null");
        return;
    }
    string font = string(env->GetStringUTFChars(fontName, nullptr));
    uint8_t *fontData = u8g2hal_get_fontbyname(font);
    if (fontData == nullptr) {
        throwNativeDriverException(env, string("Unable to retrieve font data for: ") + font);
        return;
    }
    u8g2_SetFont(toU8g2(id), fontData);
}

//long id, int mode
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontMode(JNIEnv *env, jclass cls, jlong id, jint mode) {
    u8g2_SetFontMode(toU8g2(id), static_cast<uint8_t>(mode));
}

//long id, int direction
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontDirection(JNIEnv *env, jclass cls, jlong id, jint mode) {
    u8g2_SetFontDirection(toU8g2(id), static_cast<uint8_t>(mode));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontPosBaseline(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontPosBaseline(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontPosBottom(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontPosBottom(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontPosTop(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontPosTop(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontPosCenter(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontPosCenter(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontRefHeightAll(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontRefHeightAll(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontRefHeightExtendedText(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontRefHeightExtendedText(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFontRefHeightText(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SetFontRefHeightText(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setFlipMode(JNIEnv *env, jclass cls, jlong id, jboolean enable) {
    u8g2_SetFlipMode(toU8g2(id), enable);
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setPowerSave(JNIEnv *env, jclass cls, jlong id, jboolean enable) {
    u8g2_SetPowerSave(toU8g2(id), enable);
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setDrawColor(JNIEnv *env, jclass cls, jlong id, jint color) {
    u8g2_SetDrawColor(toU8g2(id), static_cast<uint8_t>(color));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_initDisplay(JNIEnv *env, jclass cls, jlong id) {
    u8g2_InitDisplay(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_firstPage(JNIEnv *env, jclass cls, jlong id) {
    u8g2_FirstPage(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_nextPage(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_NextPage(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getAscent(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetAscent(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getDescent(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetDescent(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getMaxCharWidth(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetMaxCharWidth(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getMaxCharHeight(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetMaxCharHeight(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_sendBuffer(JNIEnv *env, jclass cls, jlong id) {
    u8g2_SendBuffer(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_clearBuffer(JNIEnv *env, jclass cls, jlong id) {
    u8g2_ClearBuffer(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_clearDisplay(JNIEnv *env, jclass cls, jlong id) {
    u8g2_ClearDisplay(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_begin(JNIEnv *env, jclass cls, jlong id) {
    u8g2_t *u8g2 = toU8g2(id);
    u8g2_InitDisplay(u8g2);
    u8g2_ClearDisplay(u8g2);
    u8g2_SetPowerSave(u8g2, 0);
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getHeight(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetDisplayHeight(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getWidth(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetDisplayWidth(toU8g2(id));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_clear(JNIEnv *env, jclass cls, jlong id) {
    //Process: home(); clearDisplay(); clearBuffer();
    u8g2_t *u8g2 = toU8g2(id);
    //home (not implemented here)
    u8g2_ClearDisplay(u8g2);
    u8g2_ClearBuffer(u8g2);
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setAutoPageClear(JNIEnv *env, jclass cls, jlong id, jint clear) {
    return u8g2_SetAutoPageClear(toU8g2(id), clear);;
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setBitmapMode(JNIEnv *env, jclass cls, jlong id, jint mode) {
    u8g2_SetBitmapMode(toU8g2(id), static_cast<uint8_t>(mode));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setContrast(JNIEnv *env, jclass cls, jlong id, uint8_t value) {
    u8g2_SetContrast(toU8g2(id), value);
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setDisplayRotation(JNIEnv *env, jclass cls, jlong id, jint rotation) {
    u8g2_cb_t* _rotation = toRotation(rotation);
    if (_rotation == nullptr)
        return;
    u8g2_SetDisplayRotation(toU8g2(id), _rotation);
}

//TODO: Still need to figure out how to pass this back to java
jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getBuffer(JNIEnv *env, jclass cls, jlong id) {
    //return u8g2_GetBuf;
    uint8_t *buffer = u8g2_GetBufferPtr(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getBufferTileWidth(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetBufferTileWidth(toU8g2(id));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getBufferTileHeight(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetBufferTileHeight(toU8g2(id));
}

[[deprecated("Replaced by getBufferCurrTileRow")]]
jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getPageCurrTileRow(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetPageCurrTileRow(toU8g2(id));
}

[[deprecated("Replaced by setBufferCurrTileRow")]]
void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setPageCurrTileRow(JNIEnv *env, jclass cls, jlong id, jint row) {
    u8g2_SetBufferCurrTileRow(toU8g2(id), static_cast<uint8_t>(row));
}

void Java_com_ibasco_pidisplay_core_ui_U8g2Interface_setBufferCurrTileRow(JNIEnv *env, jclass cls, jlong id, jint row) {
    u8g2_SetBufferCurrTileRow(toU8g2(id), static_cast<uint8_t>(row));
}

jint Java_com_ibasco_pidisplay_core_ui_U8g2Interface_getBufferCurrTileRow(JNIEnv *env, jclass cls, jlong id) {
    return u8g2_GetBufferCurrTileRow(toU8g2(id));
}

#pragma clang diagnostic pop