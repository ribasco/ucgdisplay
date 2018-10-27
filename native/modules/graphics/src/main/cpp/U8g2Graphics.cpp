/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Graphics.cpp
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
#include <map>
#include <memory>
#include <iostream>
#include <cstdint>
#include <functional>
#include <utility>
#include <cstring>
#include <iomanip>
#include "U8g2Graphics.h"
#include "../../../../../include/Global.h"
#include "U8g2Hal.h"
#include "U8g2Utils.h"

#if defined( __linux__) && defined(__arm__)
#include <wiringPi.h>
#endif

using namespace std;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    //Load global references
    JNI_Load(jvm);

    JNIEnv *env;
    jvm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION);

    //Initialize Utils
    U8gUtils_Load(env);

    //Initialize HAL
    u8g2hal_Init();

    return JNI_VERSION;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNI_Unload(vm);
}

void set_font_flag(JNIEnv *env, jlong id, bool value) {
    shared_ptr<u8g2_info_t> info = u8g2util_GetDisplayDeviceInfo(static_cast<uintptr_t>(id));
    if (info == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Unable to set font flag. Device Info for address does not exist");
        return;
    }
    info.get()->flag_font = value;
}

bool get_font_flag(JNIEnv *env, jlong id) {
    shared_ptr<u8g2_info_t> info = u8g2util_GetDisplayDeviceInfo(static_cast<uintptr_t>(id));
    if (info == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Unable to set font flag. Device Info for address does not exist");
        return false;
    }
    return info.get()->flag_font;
}

bool check_validity(JNIEnv *env, jlong id) {
    if (u8g2util_GetDisplayDeviceInfo(static_cast<uintptr_t>(id)) == nullptr) {
        JNI_ThrowNativeLibraryException(env, string("Invalid Id specified (") + to_string(id) + string(")"));
        return false;
    }
    return true;
}

jlong Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setup(JNIEnv *env, jclass cls, jstring setupProc, jint commInt, jint commType, jint rotation, jint address, jbyteArray pin_config, jboolean emulation) {
    string setup_proc_name;

    if (setupProc != nullptr) {
        setup_proc_name = string(env->GetStringUTFChars(setupProc, nullptr));
    } else {
        JNI_ThrowNativeLibraryException(env, "Setup procedure name cannot be null");
        return -1;
    }

    //1. Setup procedure should not be empty
    if (setup_proc_name.empty()) {
        JNI_ThrowNativeLibraryException(env, "Setup procedure name cannot be empty");
        return -1;
    }

    //2. Verify pin mapping
    if (pin_config == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Pin map argument cannot be null");
        return -1;
    }
    jsize len = env->GetArrayLength(pin_config);
    if (len != 16) {
        JNI_ThrowNativeLibraryException(env, string("Pin map array should be exactly 16 of length (Actual: ") +
                                             to_string(len) + string(")"));
        return -1;
    }

    uint8_t tmp[len];
    JNI_CopyJByteArray(env, pin_config, tmp, len);

    //convert to struct
    auto *pinMap = reinterpret_cast<u8g2_pin_map_t *>(tmp);

    //3. Verify that the rotation number is within the allowed range
    if (rotation > 4)
        JNI_ThrowNativeLibraryException(env, string("Invalid rotation (") + to_string(rotation) + ")");

    //Get actual rotation value
    const u8g2_cb_t *_rotation = u8g2util_ToRotation(rotation);

    //4. Setup and Initialize the Display
    shared_ptr<u8g2_info_t> info = u8g2util_SetupAndInitDisplay(setup_proc_name, commInt, commType, address, _rotation, *pinMap, emulation);

    //5. Verify if display has been initialized successfully
    if (info == nullptr) {
        JNI_ThrowNativeLibraryException(env,
                                        string("Unable to initialize the display device. Please re-visit your configuration parameters and verify that they are correct"));
        return -1;
    }

    //6. Retrieve the pointer address
    auto addr = info->address();//(uintptr_t) info->u8g2.get();
    return addr;
}

//long id, int x, int y, int width, int height
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawBox(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawBox(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height));
}

//long id, int x, int y, int count, int height, byte[] bitmap
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawBitmap(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint count, jint height, jbyteArray bitmap) {
    if (!check_validity(env, id))
        return;
    if (bitmap == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Bitmap data cannot be null");
        return;
    }
    jsize len = env->GetArrayLength(bitmap);
    uint8_t tmp[len];
    JNI_CopyJByteArray(env, bitmap, tmp, len);
    u8g2_DrawBitmap(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(count), static_cast<u8g2_uint_t>(height), tmp);
}

//long id, int x, int y, int radius, int options
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawCircle(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint radius, jint options) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawCircle(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(radius), static_cast<uint8_t>(options));
}

//long id, int x, int y, int radius, int options
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawDisc(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint radius, jint options) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawDisc(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(radius), static_cast<uint8_t>(options));
}

//long id, int x, int y, int rx, int ry, int options
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawEllipse(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint rx, jint ry, jint options) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawEllipse(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(rx), static_cast<u8g2_uint_t>(ry), static_cast<uint8_t>(options));
}

//long id, int x, int y, int rx, int ry, int options
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawFilledEllipse(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint rx, jint ry, jint options) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawFilledEllipse(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(rx), static_cast<u8g2_uint_t>(ry), static_cast<uint8_t>(options));
}

//long id, int x, int y, int width, int height
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawFrame(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawFrame(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height));
}

//long id, int x, int y, short encoding
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawGlyph(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jshort encoding) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawGlyph(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<uint16_t>(encoding));
}

//long id, int x, int y, int width
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawHLine(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawHLine(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawVLine (JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawVLine(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width));
}

//long id, int x, int y, int x1, int y1
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawLine(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint x1, jint y1) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawLine(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(x1), static_cast<u8g2_uint_t>(y1));
}

//long id, int x, int y
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawPixel(JNIEnv *env, jclass cls, jlong id, jint x, jint y) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawPixel(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y));
}

//long id, int x, int y, int width, int height, int radius
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawRoundedBox(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jint radius) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawRBox(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), static_cast<u8g2_uint_t>(radius));
}

//long id, int x, int y, int width, int height, int radius
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawRoundedFrame(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jint radius) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawRFrame(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), static_cast<u8g2_uint_t>(radius));
}

//long id, int x, int y, String value
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawString(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jstring value) {
    if (!check_validity(env, id))
        return;
    if (!get_font_flag(env, id)) {
        JNI_ThrowNativeLibraryException(env, "A font needs to be assigned prior to calling this method");
        return;
    }
    const char *c = env->GetStringUTFChars(value, nullptr);
    u8g2_DrawStr(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), c);
}

//long id, int x0, int y0, int x1, int y1, int x2, int y2
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawTriangle(JNIEnv *env, jclass cls, jlong id, jint x0, jint y0, jint x1, jint y1, jint x2, jint y2) {
    if (!check_validity(env, id))
        return;
    u8g2_DrawTriangle(toU8g2(id), static_cast<int16_t>(x0), static_cast<int16_t>(y0), static_cast<int16_t>(x1), static_cast<int16_t>(y1), static_cast<int16_t>(x2), static_cast<int16_t>(y2));
}

//long id, int x, int y, int width, int height, byte[] data
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawXBM(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jint width, jint height, jbyteArray data) {
    if (!check_validity(env, id))
        return;
    jsize len = env->GetArrayLength(data);
    uint8_t tmp[len];
    JNI_CopyJByteArray(env, data, tmp, len);
    u8g2_DrawXBM(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), static_cast<u8g2_uint_t>(width), static_cast<u8g2_uint_t>(height), tmp);
}

//long id, int x, int y, String value
jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawUTF8(JNIEnv *env, jclass cls, jlong id, jint x, jint y, jstring value) {
    if (!check_validity(env, id))
        return -1;
    const char *c = env->GetStringUTFChars(value, nullptr);
    return u8g2_DrawUTF8(toU8g2(id), static_cast<u8g2_uint_t>(x), static_cast<u8g2_uint_t>(y), c);
}

//long id, String text
jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getUTF8Width(JNIEnv *env, jclass cls, jlong id, jstring text) {
    if (!check_validity(env, id))
        return -1;
    if (text == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Text cannot be null");
        return -1;
    }
    const char *c = env->GetStringUTFChars(text, nullptr);
    return u8g2_GetUTF8Width(toU8g2(id), c);
}

//long id, byte[] data
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFont__J_3B(JNIEnv *env, jclass cls, jlong id, jbyteArray data) {
    if (!check_validity(env, id))
        return;
    if (data == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Font data cannot be null");
        return;
    }
    jsize len = env->GetArrayLength(data);
    if (len <= 0) {
        JNI_ThrowNativeLibraryException(env, "Invalid font data");
        return;
    }
    uint8_t tmp[len];
    JNI_CopyJByteArray(env, data, tmp, len);
    u8g2_SetFont(toU8g2(id), tmp);
    set_font_flag(env, id, true);
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFont__JLjava_lang_String_2(JNIEnv *env, jclass cls, jlong id, jstring fontName) {
    if (!check_validity(env, id))
        return;
    if (fontName == nullptr) {
        JNI_ThrowNativeLibraryException(env, "Font key cannot be null");
        return;
    }
    string font = string(env->GetStringUTFChars(fontName, nullptr));
    uint8_t *fontData = u8g2hal_GetFontByName(font);
    if (fontData == nullptr) {
        JNI_ThrowNativeLibraryException(env, string("Unable to retrieve font data for: ") + font);
        return;
    }
    u8g2_SetFont(toU8g2(id), fontData);
    set_font_flag(env, id, true);
}

//long id, int mode
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontMode(JNIEnv *env, jclass cls, jlong id, jint mode) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontMode(toU8g2(id), static_cast<uint8_t>(mode));
}

//long id, int direction
void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontDirection(JNIEnv *env, jclass cls, jlong id, jint mode) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontDirection(toU8g2(id), static_cast<uint8_t>(mode));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosBaseline(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontPosBaseline(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosBottom(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontPosBottom(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosTop(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontPosTop(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosCenter(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontPosCenter(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightAll(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontRefHeightAll(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightExtendedText(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontRefHeightExtendedText(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightText(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFontRefHeightText(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFlipMode(JNIEnv *env, jclass cls, jlong id, jboolean enable) {
    if (!check_validity(env, id))
        return;
    u8g2_SetFlipMode(toU8g2(id), enable);
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setPowerSave(JNIEnv *env, jclass cls, jlong id, jboolean enable) {
    if (!check_validity(env, id))
        return;
    u8g2_SetPowerSave(toU8g2(id), enable);
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setDrawColor(JNIEnv *env, jclass cls, jlong id, jint color) {
    if (!check_validity(env, id))
        return;
    u8g2_SetDrawColor(toU8g2(id), static_cast<uint8_t>(color));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_initDisplay(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_InitDisplay(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_firstPage(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_FirstPage(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_nextPage(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_NextPage(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getAscent(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetAscent(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getDescent(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetDescent(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getMaxCharWidth(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetMaxCharWidth(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getMaxCharHeight(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetMaxCharHeight(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_sendBuffer(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_SendBuffer(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clearBuffer(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_ClearBuffer(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clearDisplay(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_ClearDisplay(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_begin(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    u8g2_t *u8g2 = toU8g2(id);
    u8g2_InitDisplay(u8g2);
    u8g2_ClearDisplay(u8g2);
    u8g2_SetPowerSave(u8g2, 0);
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getHeight(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetDisplayHeight(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getWidth(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetDisplayWidth(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clear(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return;
    //Process: home(); clearDisplay(); clearBuffer();
    u8g2_t *u8g2 = toU8g2(id);
    //home (not implemented here)
    u8g2_ClearDisplay(u8g2);
    u8g2_ClearBuffer(u8g2);
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setAutoPageClear(JNIEnv *env, jclass cls, jlong id, jint clear) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_SetAutoPageClear(toU8g2(id), clear);;
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setBitmapMode(JNIEnv *env, jclass cls, jlong id, jint mode) {
    if (!check_validity(env, id))
        return;
    u8g2_SetBitmapMode(toU8g2(id), static_cast<uint8_t>(mode));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setContrast(JNIEnv *env, jclass cls, jlong id, uint8_t value) {
    if (!check_validity(env, id))
        return;
    u8g2_SetContrast(toU8g2(id), value);
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setDisplayRotation(JNIEnv *env, jclass cls, jlong id, jint rotation) {
    if (!check_validity(env, id))
        return;
    u8g2_cb_t *_rotation = u8g2util_ToRotation(rotation);
    if (_rotation == nullptr)
        return;
    u8g2_SetDisplayRotation(toU8g2(id), _rotation);
}

//TODO: Still need to figure out how to pass this back to java
jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBuffer(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    uint8_t *buffer = u8g2_GetBufferPtr(toU8g2(id));
    return -1;
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferTileWidth(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetBufferTileWidth(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferTileHeight(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetBufferTileHeight(toU8g2(id));
}

void Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setBufferCurrTileRow(JNIEnv *env, jclass cls, jlong id, jint row) {
    if (!check_validity(env, id))
        return;
    u8g2_SetBufferCurrTileRow(toU8g2(id), static_cast<uint8_t>(row));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferCurrTileRow(JNIEnv *env, jclass cls, jlong id) {
    if (!check_validity(env, id))
        return -1;
    return u8g2_GetBufferCurrTileRow(toU8g2(id));
}

jint Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getStrWidth(JNIEnv *env, jclass cls, jlong id, jstring text) {
    if (!check_validity(env, id))
        return -1;
    const char *c = env->GetStringUTFChars(text, nullptr);
    return u8g2_GetStrWidth(toU8g2(id), c);
}
