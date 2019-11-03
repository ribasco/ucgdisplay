/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2Graphics.h
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics */

#ifndef _Included_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
#define _Included_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
#ifdef __cplusplus
extern "C" {
#endif
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R0
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R0 0L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R1
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R1 1L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R2
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R2 2L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R3
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_R3 3L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_MIRROR
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_ROTATION_MIRROR 4L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_BUS_HARDWARE
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_BUS_HARDWARE 0L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_BUS_SOFTWARE
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_BUS_SOFTWARE 1L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_4WSPI
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_4WSPI 1L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_3WSPI
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_3WSPI 2L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_6800
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_6800 4L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_8080
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_8080 8L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_I2C
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_I2C 16L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_ST7920SPI
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_ST7920SPI 32L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_UART
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_UART 64L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_KS0108
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_KS0108 128L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_SED1520
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_COM_SED1520 256L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_UPPER_RIGHT
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_UPPER_RIGHT 1L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_UPPER_LEFT
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_UPPER_LEFT 2L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_LOWER_LEFT
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_LOWER_LEFT 4L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_LOWER_RIGHT
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_LOWER_RIGHT 8L
#undef com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_ALL
#define com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_U8G2_DRAW_ALL 15L
/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setup
 * Signature: (Ljava/lang/String;III[ILjava/util/Map;ZLorg/slf4j/Logger;)J
 */
JNIEXPORT jlong JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setup
  (JNIEnv *, jclass, jstring, jint, jint, jint, jintArray, jobject, jboolean, jobject);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawBox
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawBox
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawBitmap
 * Signature: (JIIII[B)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawBitmap
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jbyteArray);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawCircle
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawCircle
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawDisc
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawDisc
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawEllipse
 * Signature: (JIIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawEllipse
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawFilledEllipse
 * Signature: (JIIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawFilledEllipse
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawFrame
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawFrame
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawGlyph
 * Signature: (JIIS)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawGlyph
  (JNIEnv *, jclass, jlong, jint, jint, jshort);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawHLine
 * Signature: (JIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawHLine
  (JNIEnv *, jclass, jlong, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawVLine
 * Signature: (JIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawVLine
  (JNIEnv *, jclass, jlong, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawLine
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawLine
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawPixel
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawPixel
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawRoundedBox
 * Signature: (JIIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawRoundedBox
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawRoundedFrame
 * Signature: (JIIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawRoundedFrame
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawString
 * Signature: (JIILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawString
  (JNIEnv *, jclass, jlong, jint, jint, jstring);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawTriangle
 * Signature: (JIIIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawTriangle
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawXBM
 * Signature: (JIIII[B)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawXBM
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jbyteArray);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    drawUTF8
 * Signature: (JIILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_drawUTF8
  (JNIEnv *, jclass, jlong, jint, jint, jstring);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getUTF8Width
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getUTF8Width
  (JNIEnv *, jclass, jlong, jstring);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFont
 * Signature: (J[B)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFont__J_3B
  (JNIEnv *, jclass, jlong, jbyteArray);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFont
 * Signature: (JLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFont__JLjava_lang_String_2
  (JNIEnv *, jclass, jlong, jstring);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontMode
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontDirection
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontDirection
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontPosBaseline
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosBaseline
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontPosBottom
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosBottom
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontPosTop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosTop
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontPosCenter
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontPosCenter
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontRefHeightAll
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightAll
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontRefHeightExtendedText
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightExtendedText
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFontRefHeightText
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFontRefHeightText
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setFlipMode
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setFlipMode
  (JNIEnv *, jclass, jlong, jboolean);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setPowerSave
 * Signature: (JZ)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setPowerSave
  (JNIEnv *, jclass, jlong, jboolean);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setDrawColor
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setDrawColor
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    initDisplay
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_initDisplay
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    firstPage
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_firstPage
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    nextPage
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_nextPage
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getAscent
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getAscent
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getDescent
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getDescent
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getMaxCharWidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getMaxCharWidth
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getMaxCharHeight
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getMaxCharHeight
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    sendBuffer
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_sendBuffer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    clearBuffer
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clearBuffer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    clearDisplay
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clearDisplay
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    begin
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_begin
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getHeight
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getHeight
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getWidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getWidth
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    clear
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_clear
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setAutoPageClear
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setAutoPageClear
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setBitmapMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setBitmapMode
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setContrast
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setContrast
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setDisplayRotation
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setDisplayRotation
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getBuffer
 * Signature: (J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBuffer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getBufferTileWidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferTileWidth
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getBufferTileHeight
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferTileHeight
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getBufferCurrTileRow
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getBufferCurrTileRow
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setBufferCurrTileRow
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setBufferCurrTileRow
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    getStrWidth
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_getStrWidth
  (JNIEnv *, jclass, jlong, jstring);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setClipWindow
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setClipWindow
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint);

/*
 * Class:     com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics
 * Method:    setMaxClipWindow
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ibasco_ucgdisplay_core_u8g2_U8g2Graphics_setMaxClipWindow
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
