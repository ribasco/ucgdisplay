/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Input
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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

#ifndef PID_NATIVE_INPUTDEVHELPER_H
#define PID_NATIVE_INPUTDEVHELPER_H

#include <jni.h>
#include <cstdio>
#include <cstdint>
#include <cstring>
#include <fcntl.h>
#include <unistd.h>
#include <cstdio>
#include <cstdlib>
#include <dirent.h>
#include <cerrno>
#include <getopt.h>
#include <cctype>
#include <csignal>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <map>
#include <iostream>
#include <utility>
#include <vector>
#include <thread>
#include <future>
#include <algorithm>
#include <sys/stat.h>
#include <sys/poll.h>
#include <linux/uinput.h>
#include <linux/version.h>
#include <linux/input.h>

using namespace std;

#define DEV_INPUT_EVENT "/dev/input"
#define EVENT_DEV_NAME "event"

#define NAME_ELEMENT(element) #element
#define BITS_PER_LONG (sizeof(long) * 8)
#define NBITS(x) ((((x)-1)/BITS_PER_LONG)+1)
#define OFF(x)  ((x)%BITS_PER_LONG)
#define BIT(x)  (1UL<<OFF(x))
#define LONG(x) ((x)/BITS_PER_LONG)
#define test_bit(bit, array)    ((array[LONG(bit)] >> OFF(bit)) & 1)

static const map<int, string> events = {
        {EV_SYN,       NAME_ELEMENT(EV_SYN)},
        {EV_KEY,       NAME_ELEMENT(EV_KEY)},
        {EV_REL,       NAME_ELEMENT(EV_REL)},
        {EV_ABS,       NAME_ELEMENT(EV_ABS)},
        {EV_MSC,       NAME_ELEMENT(EV_MSC)},
        {EV_LED,       NAME_ELEMENT(EV_LED)},
        {EV_SND,       NAME_ELEMENT(EV_SND)},
        {EV_REP,       NAME_ELEMENT(EV_REP)},
        {EV_FF,        NAME_ELEMENT(EV_FF)},
        {EV_PWR,       NAME_ELEMENT(EV_PWR)},
        {EV_FF_STATUS, NAME_ELEMENT(EV_FF_STATUS)},
        {EV_SW,        NAME_ELEMENT(EV_SW)}
};

//Returns the maximum value of the event, default = -1
static const map<int, int> maxval = {
        {EV_SYN,       SYN_MAX},
        {EV_KEY,       KEY_MAX},
        {EV_REL,       REL_MAX},
        {EV_ABS,       ABS_MAX},
        {EV_MSC,       MSC_MAX},
        {EV_SW,        SW_MAX},
        {EV_LED,       LED_MAX},
        {EV_SND,       SND_MAX},
        {EV_REP,       REP_MAX},
        {EV_FF,        FF_MAX},
        {EV_FF_STATUS, FF_STATUS_MAX}
};

static const map<int, string> keys = {
        {KEY_RESERVED,                 NAME_ELEMENT(KEY_RESERVED)},
        {KEY_ESC,                      NAME_ELEMENT(KEY_ESC)},
        {KEY_1,                        NAME_ELEMENT(KEY_1)},
        {KEY_2,                        NAME_ELEMENT(KEY_2)},
        {KEY_3,                        NAME_ELEMENT(KEY_3)},
        {KEY_4,                        NAME_ELEMENT(KEY_4)},
        {KEY_5,                        NAME_ELEMENT(KEY_5)},
        {KEY_6,                        NAME_ELEMENT(KEY_6)},
        {KEY_7,                        NAME_ELEMENT(KEY_7)},
        {KEY_8,                        NAME_ELEMENT(KEY_8)},
        {KEY_9,                        NAME_ELEMENT(KEY_9)},
        {KEY_0,                        NAME_ELEMENT(KEY_0)},
        {KEY_MINUS,                    NAME_ELEMENT(KEY_MINUS)},
        {KEY_EQUAL,                    NAME_ELEMENT(KEY_EQUAL)},
        {KEY_BACKSPACE,                NAME_ELEMENT(KEY_BACKSPACE)},
        {KEY_TAB,                      NAME_ELEMENT(KEY_TAB)},
        {KEY_Q,                        NAME_ELEMENT(KEY_Q)},
        {KEY_W,                        NAME_ELEMENT(KEY_W)},
        {KEY_E,                        NAME_ELEMENT(KEY_E)},
        {KEY_R,                        NAME_ELEMENT(KEY_R)},
        {KEY_T,                        NAME_ELEMENT(KEY_T)},
        {KEY_Y,                        NAME_ELEMENT(KEY_Y)},
        {KEY_U,                        NAME_ELEMENT(KEY_U)},
        {KEY_I,                        NAME_ELEMENT(KEY_I)},
        {KEY_O,                        NAME_ELEMENT(KEY_O)},
        {KEY_P,                        NAME_ELEMENT(KEY_P)},
        {KEY_LEFTBRACE,                NAME_ELEMENT(KEY_LEFTBRACE)},
        {KEY_RIGHTBRACE,               NAME_ELEMENT(KEY_RIGHTBRACE)},
        {KEY_ENTER,                    NAME_ELEMENT(KEY_ENTER)},
        {KEY_LEFTCTRL,                 NAME_ELEMENT(KEY_LEFTCTRL)},
        {KEY_A,                        NAME_ELEMENT(KEY_A)},
        {KEY_S,                        NAME_ELEMENT(KEY_S)},
        {KEY_D,                        NAME_ELEMENT(KEY_D)},
        {KEY_F,                        NAME_ELEMENT(KEY_F)},
        {KEY_G,                        NAME_ELEMENT(KEY_G)},
        {KEY_H,                        NAME_ELEMENT(KEY_H)},
        {KEY_J,                        NAME_ELEMENT(KEY_J)},
        {KEY_K,                        NAME_ELEMENT(KEY_K)},
        {KEY_L,                        NAME_ELEMENT(KEY_L)},
        {KEY_SEMICOLON,                NAME_ELEMENT(KEY_SEMICOLON)},
        {KEY_APOSTROPHE,               NAME_ELEMENT(KEY_APOSTROPHE)},
        {KEY_GRAVE,                    NAME_ELEMENT(KEY_GRAVE)},
        {KEY_LEFTSHIFT,                NAME_ELEMENT(KEY_LEFTSHIFT)},
        {KEY_BACKSLASH,                NAME_ELEMENT(KEY_BACKSLASH)},
        {KEY_Z,                        NAME_ELEMENT(KEY_Z)},
        {KEY_X,                        NAME_ELEMENT(KEY_X)},
        {KEY_C,                        NAME_ELEMENT(KEY_C)},
        {KEY_V,                        NAME_ELEMENT(KEY_V)},
        {KEY_B,                        NAME_ELEMENT(KEY_B)},
        {KEY_N,                        NAME_ELEMENT(KEY_N)},
        {KEY_M,                        NAME_ELEMENT(KEY_M)},
        {KEY_COMMA,                    NAME_ELEMENT(KEY_COMMA)},
        {KEY_DOT,                      NAME_ELEMENT(KEY_DOT)},
        {KEY_SLASH,                    NAME_ELEMENT(KEY_SLASH)},
        {KEY_RIGHTSHIFT,               NAME_ELEMENT(KEY_RIGHTSHIFT)},
        {KEY_KPASTERISK,               NAME_ELEMENT(KEY_KPASTERISK)},
        {KEY_LEFTALT,                  NAME_ELEMENT(KEY_LEFTALT)},
        {KEY_SPACE,                    NAME_ELEMENT(KEY_SPACE)},
        {KEY_CAPSLOCK,                 NAME_ELEMENT(KEY_CAPSLOCK)},
        {KEY_F1,                       NAME_ELEMENT(KEY_F1)},
        {KEY_F2,                       NAME_ELEMENT(KEY_F2)},
        {KEY_F3,                       NAME_ELEMENT(KEY_F3)},
        {KEY_F4,                       NAME_ELEMENT(KEY_F4)},
        {KEY_F5,                       NAME_ELEMENT(KEY_F5)},
        {KEY_F6,                       NAME_ELEMENT(KEY_F6)},
        {KEY_F7,                       NAME_ELEMENT(KEY_F7)},
        {KEY_F8,                       NAME_ELEMENT(KEY_F8)},
        {KEY_F9,                       NAME_ELEMENT(KEY_F9)},
        {KEY_F10,                      NAME_ELEMENT(KEY_F10)},
        {KEY_NUMLOCK,                  NAME_ELEMENT(KEY_NUMLOCK)},
        {KEY_SCROLLLOCK,               NAME_ELEMENT(KEY_SCROLLLOCK)},
        {KEY_KP7,                      NAME_ELEMENT(KEY_KP7)},
        {KEY_KP8,                      NAME_ELEMENT(KEY_KP8)},
        {KEY_KP9,                      NAME_ELEMENT(KEY_KP9)},
        {KEY_KPMINUS,                  NAME_ELEMENT(KEY_KPMINUS)},
        {KEY_KP4,                      NAME_ELEMENT(KEY_KP4)},
        {KEY_KP5,                      NAME_ELEMENT(KEY_KP5)},
        {KEY_KP6,                      NAME_ELEMENT(KEY_KP6)},
        {KEY_KPPLUS,                   NAME_ELEMENT(KEY_KPPLUS)},
        {KEY_KP1,                      NAME_ELEMENT(KEY_KP1)},
        {KEY_KP2,                      NAME_ELEMENT(KEY_KP2)},
        {KEY_KP3,                      NAME_ELEMENT(KEY_KP3)},
        {KEY_KP0,                      NAME_ELEMENT(KEY_KP0)},
        {KEY_KPDOT,                    NAME_ELEMENT(KEY_KPDOT)},
        {KEY_ZENKAKUHANKAKU,           NAME_ELEMENT(KEY_ZENKAKUHANKAKU)},
        {KEY_102ND,                    NAME_ELEMENT(KEY_102ND)},
        {KEY_F11,                      NAME_ELEMENT(KEY_F11)},
        {KEY_F12,                      NAME_ELEMENT(KEY_F12)},
        {KEY_RO,                       NAME_ELEMENT(KEY_RO)},
        {KEY_KATAKANA,                 NAME_ELEMENT(KEY_KATAKANA)},
        {KEY_HIRAGANA,                 NAME_ELEMENT(KEY_HIRAGANA)},
        {KEY_HENKAN,                   NAME_ELEMENT(KEY_HENKAN)},
        {KEY_KATAKANAHIRAGANA,         NAME_ELEMENT(KEY_KATAKANAHIRAGANA)},
        {KEY_MUHENKAN,                 NAME_ELEMENT(KEY_MUHENKAN)},
        {KEY_KPJPCOMMA,                NAME_ELEMENT(KEY_KPJPCOMMA)},
        {KEY_KPENTER,                  NAME_ELEMENT(KEY_KPENTER)},
        {KEY_RIGHTCTRL,                NAME_ELEMENT(KEY_RIGHTCTRL)},
        {KEY_KPSLASH,                  NAME_ELEMENT(KEY_KPSLASH)},
        {KEY_SYSRQ,                    NAME_ELEMENT(KEY_SYSRQ)},
        {KEY_RIGHTALT,                 NAME_ELEMENT(KEY_RIGHTALT)},
        {KEY_LINEFEED,                 NAME_ELEMENT(KEY_LINEFEED)},
        {KEY_HOME,                     NAME_ELEMENT(KEY_HOME)},
        {KEY_UP,                       NAME_ELEMENT(KEY_UP)},
        {KEY_PAGEUP,                   NAME_ELEMENT(KEY_PAGEUP)},
        {KEY_LEFT,                     NAME_ELEMENT(KEY_LEFT)},
        {KEY_RIGHT,                    NAME_ELEMENT(KEY_RIGHT)},
        {KEY_END,                      NAME_ELEMENT(KEY_END)},
        {KEY_DOWN,                     NAME_ELEMENT(KEY_DOWN)},
        {KEY_PAGEDOWN,                 NAME_ELEMENT(KEY_PAGEDOWN)},
        {KEY_INSERT,                   NAME_ELEMENT(KEY_INSERT)},
        {KEY_DELETE,                   NAME_ELEMENT(KEY_DELETE)},
        {KEY_MACRO,                    NAME_ELEMENT(KEY_MACRO)},
        {KEY_MUTE,                     NAME_ELEMENT(KEY_MUTE)},
        {KEY_VOLUMEDOWN,               NAME_ELEMENT(KEY_VOLUMEDOWN)},
        {KEY_VOLUMEUP,                 NAME_ELEMENT(KEY_VOLUMEUP)},
        {KEY_POWER,                    NAME_ELEMENT(KEY_POWER)},
        {KEY_KPEQUAL,                  NAME_ELEMENT(KEY_KPEQUAL)},
        {KEY_KPPLUSMINUS,              NAME_ELEMENT(KEY_KPPLUSMINUS)},
        {KEY_PAUSE,                    NAME_ELEMENT(KEY_PAUSE)},
        {KEY_KPCOMMA,                  NAME_ELEMENT(KEY_KPCOMMA)},
        {KEY_HANGUEL,                  NAME_ELEMENT(KEY_HANGUEL)},
        {KEY_HANJA,                    NAME_ELEMENT(KEY_HANJA)},
        {KEY_YEN,                      NAME_ELEMENT(KEY_YEN)},
        {KEY_LEFTMETA,                 NAME_ELEMENT(KEY_LEFTMETA)},
        {KEY_RIGHTMETA,                NAME_ELEMENT(KEY_RIGHTMETA)},
        {KEY_COMPOSE,                  NAME_ELEMENT(KEY_COMPOSE)},
        {KEY_STOP,                     NAME_ELEMENT(KEY_STOP)},
        {KEY_AGAIN,                    NAME_ELEMENT(KEY_AGAIN)},
        {KEY_PROPS,                    NAME_ELEMENT(KEY_PROPS)},
        {KEY_UNDO,                     NAME_ELEMENT(KEY_UNDO)},
        {KEY_FRONT,                    NAME_ELEMENT(KEY_FRONT)},
        {KEY_COPY,                     NAME_ELEMENT(KEY_COPY)},
        {KEY_OPEN,                     NAME_ELEMENT(KEY_OPEN)},
        {KEY_PASTE,                    NAME_ELEMENT(KEY_PASTE)},
        {KEY_FIND,                     NAME_ELEMENT(KEY_FIND)},
        {KEY_CUT,                      NAME_ELEMENT(KEY_CUT)},
        {KEY_HELP,                     NAME_ELEMENT(KEY_HELP)},
        {KEY_MENU,                     NAME_ELEMENT(KEY_MENU)},
        {KEY_CALC,                     NAME_ELEMENT(KEY_CALC)},
        {KEY_SETUP,                    NAME_ELEMENT(KEY_SETUP)},
        {KEY_SLEEP,                    NAME_ELEMENT(KEY_SLEEP)},
        {KEY_WAKEUP,                   NAME_ELEMENT(KEY_WAKEUP)},
        {KEY_FILE,                     NAME_ELEMENT(KEY_FILE)},
        {KEY_SENDFILE,                 NAME_ELEMENT(KEY_SENDFILE)},
        {KEY_DELETEFILE,               NAME_ELEMENT(KEY_DELETEFILE)},
        {KEY_XFER,                     NAME_ELEMENT(KEY_XFER)},
        {KEY_PROG1,                    NAME_ELEMENT(KEY_PROG1)},
        {KEY_PROG2,                    NAME_ELEMENT(KEY_PROG2)},
        {KEY_WWW,                      NAME_ELEMENT(KEY_WWW)},
        {KEY_MSDOS,                    NAME_ELEMENT(KEY_MSDOS)},
        {KEY_COFFEE,                   NAME_ELEMENT(KEY_COFFEE)},
        {KEY_DIRECTION,                NAME_ELEMENT(KEY_DIRECTION)},
        {KEY_CYCLEWINDOWS,             NAME_ELEMENT(KEY_CYCLEWINDOWS)},
        {KEY_MAIL,                     NAME_ELEMENT(KEY_MAIL)},
        {KEY_BOOKMARKS,                NAME_ELEMENT(KEY_BOOKMARKS)},
        {KEY_COMPUTER,                 NAME_ELEMENT(KEY_COMPUTER)},
        {KEY_BACK,                     NAME_ELEMENT(KEY_BACK)},
        {KEY_FORWARD,                  NAME_ELEMENT(KEY_FORWARD)},
        {KEY_CLOSECD,                  NAME_ELEMENT(KEY_CLOSECD)},
        {KEY_EJECTCD,                  NAME_ELEMENT(KEY_EJECTCD)},
        {KEY_EJECTCLOSECD,             NAME_ELEMENT(KEY_EJECTCLOSECD)},
        {KEY_NEXTSONG,                 NAME_ELEMENT(KEY_NEXTSONG)},
        {KEY_PLAYPAUSE,                NAME_ELEMENT(KEY_PLAYPAUSE)},
        {KEY_PREVIOUSSONG,             NAME_ELEMENT(KEY_PREVIOUSSONG)},
        {KEY_STOPCD,                   NAME_ELEMENT(KEY_STOPCD)},
        {KEY_RECORD,                   NAME_ELEMENT(KEY_RECORD)},
        {KEY_REWIND,                   NAME_ELEMENT(KEY_REWIND)},
        {KEY_PHONE,                    NAME_ELEMENT(KEY_PHONE)},
        {KEY_ISO,                      NAME_ELEMENT(KEY_ISO)},
        {KEY_CONFIG,                   NAME_ELEMENT(KEY_CONFIG)},
        {KEY_HOMEPAGE,                 NAME_ELEMENT(KEY_HOMEPAGE)},
        {KEY_REFRESH,                  NAME_ELEMENT(KEY_REFRESH)},
        {KEY_EXIT,                     NAME_ELEMENT(KEY_EXIT)},
        {KEY_MOVE,                     NAME_ELEMENT(KEY_MOVE)},
        {KEY_EDIT,                     NAME_ELEMENT(KEY_EDIT)},
        {KEY_SCROLLUP,                 NAME_ELEMENT(KEY_SCROLLUP)},
        {KEY_SCROLLDOWN,               NAME_ELEMENT(KEY_SCROLLDOWN)},
        {KEY_KPLEFTPAREN,              NAME_ELEMENT(KEY_KPLEFTPAREN)},
        {KEY_KPRIGHTPAREN,             NAME_ELEMENT(KEY_KPRIGHTPAREN)},
        {KEY_F13,                      NAME_ELEMENT(KEY_F13)},
        {KEY_F14,                      NAME_ELEMENT(KEY_F14)},
        {KEY_F15,                      NAME_ELEMENT(KEY_F15)},
        {KEY_F16,                      NAME_ELEMENT(KEY_F16)},
        {KEY_F17,                      NAME_ELEMENT(KEY_F17)},
        {KEY_F18,                      NAME_ELEMENT(KEY_F18)},
        {KEY_F19,                      NAME_ELEMENT(KEY_F19)},
        {KEY_F20,                      NAME_ELEMENT(KEY_F20)},
        {KEY_F21,                      NAME_ELEMENT(KEY_F21)},
        {KEY_F22,                      NAME_ELEMENT(KEY_F22)},
        {KEY_F23,                      NAME_ELEMENT(KEY_F23)},
        {KEY_F24,                      NAME_ELEMENT(KEY_F24)},
        {KEY_PLAYCD,                   NAME_ELEMENT(KEY_PLAYCD)},
        {KEY_PAUSECD,                  NAME_ELEMENT(KEY_PAUSECD)},
        {KEY_PROG3,                    NAME_ELEMENT(KEY_PROG3)},
        {KEY_PROG4,                    NAME_ELEMENT(KEY_PROG4)},
        {KEY_SUSPEND,                  NAME_ELEMENT(KEY_SUSPEND)},
        {KEY_CLOSE,                    NAME_ELEMENT(KEY_CLOSE)},
        {KEY_PLAY,                     NAME_ELEMENT(KEY_PLAY)},
        {KEY_FASTFORWARD,              NAME_ELEMENT(KEY_FASTFORWARD)},
        {KEY_BASSBOOST,                NAME_ELEMENT(KEY_BASSBOOST)},
        {KEY_PRINT,                    NAME_ELEMENT(KEY_PRINT)},
        {KEY_HP,                       NAME_ELEMENT(KEY_HP)},
        {KEY_CAMERA,                   NAME_ELEMENT(KEY_CAMERA)},
        {KEY_SOUND,                    NAME_ELEMENT(KEY_SOUND)},
        {KEY_QUESTION,                 NAME_ELEMENT(KEY_QUESTION)},
        {KEY_EMAIL,                    NAME_ELEMENT(KEY_EMAIL)},
        {KEY_CHAT,                     NAME_ELEMENT(KEY_CHAT)},
        {KEY_SEARCH,                   NAME_ELEMENT(KEY_SEARCH)},
        {KEY_CONNECT,                  NAME_ELEMENT(KEY_CONNECT)},
        {KEY_FINANCE,                  NAME_ELEMENT(KEY_FINANCE)},
        {KEY_SPORT,                    NAME_ELEMENT(KEY_SPORT)},
        {KEY_SHOP,                     NAME_ELEMENT(KEY_SHOP)},
        {KEY_ALTERASE,                 NAME_ELEMENT(KEY_ALTERASE)},
        {KEY_CANCEL,                   NAME_ELEMENT(KEY_CANCEL)},
        {KEY_BRIGHTNESSDOWN,           NAME_ELEMENT(KEY_BRIGHTNESSDOWN)},
        {KEY_BRIGHTNESSUP,             NAME_ELEMENT(KEY_BRIGHTNESSUP)},
        {KEY_MEDIA,                    NAME_ELEMENT(KEY_MEDIA)},
        {KEY_UNKNOWN,                  NAME_ELEMENT(KEY_UNKNOWN)},
        {KEY_OK,                       NAME_ELEMENT(KEY_OK)},
        {KEY_SELECT,                   NAME_ELEMENT(KEY_SELECT)},
        {KEY_GOTO,                     NAME_ELEMENT(KEY_GOTO)},
        {KEY_CLEAR,                    NAME_ELEMENT(KEY_CLEAR)},
        {KEY_POWER2,                   NAME_ELEMENT(KEY_POWER2)},
        {KEY_OPTION,                   NAME_ELEMENT(KEY_OPTION)},
        {KEY_INFO,                     NAME_ELEMENT(KEY_INFO)},
        {KEY_TIME,                     NAME_ELEMENT(KEY_TIME)},
        {KEY_VENDOR,                   NAME_ELEMENT(KEY_VENDOR)},
        {KEY_ARCHIVE,                  NAME_ELEMENT(KEY_ARCHIVE)},
        {KEY_PROGRAM,                  NAME_ELEMENT(KEY_PROGRAM)},
        {KEY_CHANNEL,                  NAME_ELEMENT(KEY_CHANNEL)},
        {KEY_FAVORITES,                NAME_ELEMENT(KEY_FAVORITES)},
        {KEY_EPG,                      NAME_ELEMENT(KEY_EPG)},
        {KEY_PVR,                      NAME_ELEMENT(KEY_PVR)},
        {KEY_MHP,                      NAME_ELEMENT(KEY_MHP)},
        {KEY_LANGUAGE,                 NAME_ELEMENT(KEY_LANGUAGE)},
        {KEY_TITLE,                    NAME_ELEMENT(KEY_TITLE)},
        {KEY_SUBTITLE,                 NAME_ELEMENT(KEY_SUBTITLE)},
        {KEY_ANGLE,                    NAME_ELEMENT(KEY_ANGLE)},
        {KEY_ZOOM,                     NAME_ELEMENT(KEY_ZOOM)},
        {KEY_MODE,                     NAME_ELEMENT(KEY_MODE)},
        {KEY_KEYBOARD,                 NAME_ELEMENT(KEY_KEYBOARD)},
        {KEY_SCREEN,                   NAME_ELEMENT(KEY_SCREEN)},
        {KEY_PC,                       NAME_ELEMENT(KEY_PC)},
        {KEY_TV,                       NAME_ELEMENT(KEY_TV)},
        {KEY_TV2,                      NAME_ELEMENT(KEY_TV2)},
        {KEY_VCR,                      NAME_ELEMENT(KEY_VCR)},
        {KEY_VCR2,                     NAME_ELEMENT(KEY_VCR2)},
        {KEY_SAT,                      NAME_ELEMENT(KEY_SAT)},
        {KEY_SAT2,                     NAME_ELEMENT(KEY_SAT2)},
        {KEY_CD,                       NAME_ELEMENT(KEY_CD)},
        {KEY_TAPE,                     NAME_ELEMENT(KEY_TAPE)},
        {KEY_RADIO,                    NAME_ELEMENT(KEY_RADIO)},
        {KEY_TUNER,                    NAME_ELEMENT(KEY_TUNER)},
        {KEY_PLAYER,                   NAME_ELEMENT(KEY_PLAYER)},
        {KEY_TEXT,                     NAME_ELEMENT(KEY_TEXT)},
        {KEY_DVD,                      NAME_ELEMENT(KEY_DVD)},
        {KEY_AUX,                      NAME_ELEMENT(KEY_AUX)},
        {KEY_MP3,                      NAME_ELEMENT(KEY_MP3)},
        {KEY_AUDIO,                    NAME_ELEMENT(KEY_AUDIO)},
        {KEY_VIDEO,                    NAME_ELEMENT(KEY_VIDEO)},
        {KEY_DIRECTORY,                NAME_ELEMENT(KEY_DIRECTORY)},
        {KEY_LIST,                     NAME_ELEMENT(KEY_LIST)},
        {KEY_MEMO,                     NAME_ELEMENT(KEY_MEMO)},
        {KEY_CALENDAR,                 NAME_ELEMENT(KEY_CALENDAR)},
        {KEY_RED,                      NAME_ELEMENT(KEY_RED)},
        {KEY_GREEN,                    NAME_ELEMENT(KEY_GREEN)},
        {KEY_YELLOW,                   NAME_ELEMENT(KEY_YELLOW)},
        {KEY_BLUE,                     NAME_ELEMENT(KEY_BLUE)},
        {KEY_CHANNELUP,                NAME_ELEMENT(KEY_CHANNELUP)},
        {KEY_CHANNELDOWN,              NAME_ELEMENT(KEY_CHANNELDOWN)},
        {KEY_FIRST,                    NAME_ELEMENT(KEY_FIRST)},
        {KEY_LAST,                     NAME_ELEMENT(KEY_LAST)},
        {KEY_AB,                       NAME_ELEMENT(KEY_AB)},
        {KEY_NEXT,                     NAME_ELEMENT(KEY_NEXT)},
        {KEY_RESTART,                  NAME_ELEMENT(KEY_RESTART)},
        {KEY_SLOW,                     NAME_ELEMENT(KEY_SLOW)},
        {KEY_SHUFFLE,                  NAME_ELEMENT(KEY_SHUFFLE)},
        {KEY_BREAK,                    NAME_ELEMENT(KEY_BREAK)},
        {KEY_PREVIOUS,                 NAME_ELEMENT(KEY_PREVIOUS)},
        {KEY_DIGITS,                   NAME_ELEMENT(KEY_DIGITS)},
        {KEY_TEEN,                     NAME_ELEMENT(KEY_TEEN)},
        {KEY_TWEN,                     NAME_ELEMENT(KEY_TWEN)},
        {KEY_DEL_EOL,                  NAME_ELEMENT(KEY_DEL_EOL)},
        {KEY_DEL_EOS,                  NAME_ELEMENT(KEY_DEL_EOS)},
        {KEY_INS_LINE,                 NAME_ELEMENT(KEY_INS_LINE)},
        {KEY_DEL_LINE,                 NAME_ELEMENT(KEY_DEL_LINE)},
        {KEY_VIDEOPHONE,               NAME_ELEMENT(KEY_VIDEOPHONE)},
        {KEY_GAMES,                    NAME_ELEMENT(KEY_GAMES)},
        {KEY_ZOOMIN,                   NAME_ELEMENT(KEY_ZOOMIN)},
        {KEY_ZOOMOUT,                  NAME_ELEMENT(KEY_ZOOMOUT)},
        {KEY_ZOOMRESET,                NAME_ELEMENT(KEY_ZOOMRESET)},
        {KEY_WORDPROCESSOR,            NAME_ELEMENT(KEY_WORDPROCESSOR)},
        {KEY_EDITOR,                   NAME_ELEMENT(KEY_EDITOR)},
        {KEY_SPREADSHEET,              NAME_ELEMENT(KEY_SPREADSHEET)},
        {KEY_GRAPHICSEDITOR,           NAME_ELEMENT(KEY_GRAPHICSEDITOR)},
        {KEY_PRESENTATION,             NAME_ELEMENT(KEY_PRESENTATION)},
        {KEY_DATABASE,                 NAME_ELEMENT(KEY_DATABASE)},
        {KEY_NEWS,                     NAME_ELEMENT(KEY_NEWS)},
        {KEY_VOICEMAIL,                NAME_ELEMENT(KEY_VOICEMAIL)},
        {KEY_ADDRESSBOOK,              NAME_ELEMENT(KEY_ADDRESSBOOK)},
        {KEY_MESSENGER,                NAME_ELEMENT(KEY_MESSENGER)},
        {KEY_DISPLAYTOGGLE,            NAME_ELEMENT(KEY_DISPLAYTOGGLE)},
        {KEY_SPELLCHECK,               NAME_ELEMENT(KEY_SPELLCHECK)},
        {KEY_LOGOFF,                   NAME_ELEMENT(KEY_LOGOFF)},
        {KEY_DOLLAR,                   NAME_ELEMENT(KEY_DOLLAR)},
        {KEY_EURO,                     NAME_ELEMENT(KEY_EURO)},
        {KEY_FRAMEBACK,                NAME_ELEMENT(KEY_FRAMEBACK)},
        {KEY_FRAMEFORWARD,             NAME_ELEMENT(KEY_FRAMEFORWARD)},
        {KEY_CONTEXT_MENU,             NAME_ELEMENT(KEY_CONTEXT_MENU)},
        {KEY_MEDIA_REPEAT,             NAME_ELEMENT(KEY_MEDIA_REPEAT)},
        {KEY_10CHANNELSUP,             NAME_ELEMENT(KEY_10CHANNELSUP)},
        {KEY_10CHANNELSDOWN,           NAME_ELEMENT(KEY_10CHANNELSDOWN)},
        {KEY_IMAGES,                   NAME_ELEMENT(KEY_IMAGES)},
        {KEY_DEL_EOL,                  NAME_ELEMENT(KEY_DEL_EOL)},
        {KEY_DEL_EOS,                  NAME_ELEMENT(KEY_DEL_EOS)},
        {KEY_INS_LINE,                 NAME_ELEMENT(KEY_INS_LINE)},
        {KEY_DEL_LINE,                 NAME_ELEMENT(KEY_DEL_LINE)},
        {KEY_FN,                       NAME_ELEMENT(KEY_FN)},
        {KEY_FN_ESC,                   NAME_ELEMENT(KEY_FN_ESC)},
        {KEY_FN_F1,                    NAME_ELEMENT(KEY_FN_F1)},
        {KEY_FN_F2,                    NAME_ELEMENT(KEY_FN_F2)},
        {KEY_FN_F3,                    NAME_ELEMENT(KEY_FN_F3)},
        {KEY_FN_F4,                    NAME_ELEMENT(KEY_FN_F4)},
        {KEY_FN_F5,                    NAME_ELEMENT(KEY_FN_F5)},
        {KEY_FN_F6,                    NAME_ELEMENT(KEY_FN_F6)},
        {KEY_FN_F7,                    NAME_ELEMENT(KEY_FN_F7)},
        {KEY_FN_F8,                    NAME_ELEMENT(KEY_FN_F8)},
        {KEY_FN_F9,                    NAME_ELEMENT(KEY_FN_F9)},
        {KEY_FN_F10,                   NAME_ELEMENT(KEY_FN_F10)},
        {KEY_FN_F11,                   NAME_ELEMENT(KEY_FN_F11)},
        {KEY_FN_F12,                   NAME_ELEMENT(KEY_FN_F12)},
        {KEY_FN_1,                     NAME_ELEMENT(KEY_FN_1)},
        {KEY_FN_2,                     NAME_ELEMENT(KEY_FN_2)},
        {KEY_FN_D,                     NAME_ELEMENT(KEY_FN_D)},
        {KEY_FN_E,                     NAME_ELEMENT(KEY_FN_E)},
        {KEY_FN_F,                     NAME_ELEMENT(KEY_FN_F)},
        {KEY_FN_S,                     NAME_ELEMENT(KEY_FN_S)},
        {KEY_FN_B,                     NAME_ELEMENT(KEY_FN_B)},
        {KEY_BRL_DOT1,                 NAME_ELEMENT(KEY_BRL_DOT1)},
        {KEY_BRL_DOT2,                 NAME_ELEMENT(KEY_BRL_DOT2)},
        {KEY_BRL_DOT3,                 NAME_ELEMENT(KEY_BRL_DOT3)},
        {KEY_BRL_DOT4,                 NAME_ELEMENT(KEY_BRL_DOT4)},
        {KEY_BRL_DOT5,                 NAME_ELEMENT(KEY_BRL_DOT5)},
        {KEY_BRL_DOT6,                 NAME_ELEMENT(KEY_BRL_DOT6)},
        {KEY_BRL_DOT7,                 NAME_ELEMENT(KEY_BRL_DOT7)},
        {KEY_BRL_DOT8,                 NAME_ELEMENT(KEY_BRL_DOT8)},
        {KEY_BRL_DOT9,                 NAME_ELEMENT(KEY_BRL_DOT9)},
        {KEY_BRL_DOT10,                NAME_ELEMENT(KEY_BRL_DOT10)},
        {KEY_NUMERIC_0,                NAME_ELEMENT(KEY_NUMERIC_0)},
        {KEY_NUMERIC_1,                NAME_ELEMENT(KEY_NUMERIC_1)},
        {KEY_NUMERIC_2,                NAME_ELEMENT(KEY_NUMERIC_2)},
        {KEY_NUMERIC_3,                NAME_ELEMENT(KEY_NUMERIC_3)},
        {KEY_NUMERIC_4,                NAME_ELEMENT(KEY_NUMERIC_4)},
        {KEY_NUMERIC_5,                NAME_ELEMENT(KEY_NUMERIC_5)},
        {KEY_NUMERIC_6,                NAME_ELEMENT(KEY_NUMERIC_6)},
        {KEY_NUMERIC_7,                NAME_ELEMENT(KEY_NUMERIC_7)},
        {KEY_NUMERIC_8,                NAME_ELEMENT(KEY_NUMERIC_8)},
        {KEY_NUMERIC_9,                NAME_ELEMENT(KEY_NUMERIC_9)},
        {KEY_NUMERIC_STAR,             NAME_ELEMENT(KEY_NUMERIC_STAR)},
        {KEY_NUMERIC_POUND,            NAME_ELEMENT(KEY_NUMERIC_POUND)},
#ifdef KEY_NUMERIC_11
        {KEY_NUMERIC_11,               NAME_ELEMENT(KEY_NUMERIC_11)},
        {KEY_NUMERIC_12,               NAME_ELEMENT(KEY_NUMERIC_12)},
#endif
        {KEY_BATTERY,                  NAME_ELEMENT(KEY_BATTERY)},
        {KEY_BLUETOOTH,                NAME_ELEMENT(KEY_BLUETOOTH)},
        {KEY_BRIGHTNESS_CYCLE,         NAME_ELEMENT(KEY_BRIGHTNESS_CYCLE)},
        {KEY_BRIGHTNESS_ZERO,          NAME_ELEMENT(KEY_BRIGHTNESS_ZERO)},
        {KEY_DASHBOARD,                NAME_ELEMENT(KEY_DASHBOARD)},
        {KEY_DISPLAY_OFF,              NAME_ELEMENT(KEY_DISPLAY_OFF)},
        {KEY_DOCUMENTS,                NAME_ELEMENT(KEY_DOCUMENTS)},
        {KEY_FORWARDMAIL,              NAME_ELEMENT(KEY_FORWARDMAIL)},
        {KEY_NEW,                      NAME_ELEMENT(KEY_NEW)},
        {KEY_KBDILLUMDOWN,             NAME_ELEMENT(KEY_KBDILLUMDOWN)},
        {KEY_KBDILLUMUP,               NAME_ELEMENT(KEY_KBDILLUMUP)},
        {KEY_KBDILLUMTOGGLE,           NAME_ELEMENT(KEY_KBDILLUMTOGGLE)},
        {KEY_REDO,                     NAME_ELEMENT(KEY_REDO)},
        {KEY_REPLY,                    NAME_ELEMENT(KEY_REPLY)},
        {KEY_SAVE,                     NAME_ELEMENT(KEY_SAVE)},
        {KEY_SCALE,                    NAME_ELEMENT(KEY_SCALE)},
        {KEY_SEND,                     NAME_ELEMENT(KEY_SEND)},
        {KEY_SCREENLOCK,               NAME_ELEMENT(KEY_SCREENLOCK)},
        {KEY_SWITCHVIDEOMODE,          NAME_ELEMENT(KEY_SWITCHVIDEOMODE)},
        {KEY_UWB,                      NAME_ELEMENT(KEY_UWB)},
        {KEY_VIDEO_NEXT,               NAME_ELEMENT(KEY_VIDEO_NEXT)},
        {KEY_VIDEO_PREV,               NAME_ELEMENT(KEY_VIDEO_PREV)},
        {KEY_WIMAX,                    NAME_ELEMENT(KEY_WIMAX)},
        {KEY_WLAN,                     NAME_ELEMENT(KEY_WLAN)},
        {KEY_RFKILL,                   NAME_ELEMENT(KEY_RFKILL)},
        {KEY_MICMUTE,                  NAME_ELEMENT(KEY_MICMUTE)},
        {KEY_CAMERA_FOCUS,             NAME_ELEMENT(KEY_CAMERA_FOCUS)},
        {KEY_WPS_BUTTON,               NAME_ELEMENT(KEY_WPS_BUTTON)},
        {KEY_TOUCHPAD_TOGGLE,          NAME_ELEMENT(KEY_TOUCHPAD_TOGGLE)},
        {KEY_TOUCHPAD_ON,              NAME_ELEMENT(KEY_TOUCHPAD_ON)},
        {KEY_TOUCHPAD_OFF,             NAME_ELEMENT(KEY_TOUCHPAD_OFF)},
        {KEY_CAMERA_ZOOMIN,            NAME_ELEMENT(KEY_CAMERA_ZOOMIN)},
        {KEY_CAMERA_ZOOMOUT,           NAME_ELEMENT(KEY_CAMERA_ZOOMOUT)},
        {KEY_CAMERA_UP,                NAME_ELEMENT(KEY_CAMERA_UP)},
        {KEY_CAMERA_DOWN,              NAME_ELEMENT(KEY_CAMERA_DOWN)},
        {KEY_CAMERA_LEFT,              NAME_ELEMENT(KEY_CAMERA_LEFT)},
        {KEY_CAMERA_RIGHT,             NAME_ELEMENT(KEY_CAMERA_RIGHT)},
        {KEY_ATTENDANT_ON,             NAME_ELEMENT(KEY_ATTENDANT_ON)},
        {KEY_ATTENDANT_OFF,            NAME_ELEMENT(KEY_ATTENDANT_OFF)},
        {KEY_ATTENDANT_TOGGLE,         NAME_ELEMENT(KEY_ATTENDANT_TOGGLE)},
        {KEY_LIGHTS_TOGGLE,            NAME_ELEMENT(KEY_LIGHTS_TOGGLE)},
        {BTN_0,                        NAME_ELEMENT(BTN_0)},
        {BTN_1,                        NAME_ELEMENT(BTN_1)},
        {BTN_2,                        NAME_ELEMENT(BTN_2)},
        {BTN_3,                        NAME_ELEMENT(BTN_3)},
        {BTN_4,                        NAME_ELEMENT(BTN_4)},
        {BTN_5,                        NAME_ELEMENT(BTN_5)},
        {BTN_6,                        NAME_ELEMENT(BTN_6)},
        {BTN_7,                        NAME_ELEMENT(BTN_7)},
        {BTN_8,                        NAME_ELEMENT(BTN_8)},
        {BTN_9,                        NAME_ELEMENT(BTN_9)},
        {BTN_LEFT,                     NAME_ELEMENT(BTN_LEFT)},
        {BTN_RIGHT,                    NAME_ELEMENT(BTN_RIGHT)},
        {BTN_MIDDLE,                   NAME_ELEMENT(BTN_MIDDLE)},
        {BTN_SIDE,                     NAME_ELEMENT(BTN_SIDE)},
        {BTN_EXTRA,                    NAME_ELEMENT(BTN_EXTRA)},
        {BTN_FORWARD,                  NAME_ELEMENT(BTN_FORWARD)},
        {BTN_BACK,                     NAME_ELEMENT(BTN_BACK)},
        {BTN_TASK,                     NAME_ELEMENT(BTN_TASK)},
        {BTN_TRIGGER,                  NAME_ELEMENT(BTN_TRIGGER)},
        {BTN_THUMB,                    NAME_ELEMENT(BTN_THUMB)},
        {BTN_THUMB2,                   NAME_ELEMENT(BTN_THUMB2)},
        {BTN_TOP,                      NAME_ELEMENT(BTN_TOP)},
        {BTN_TOP2,                     NAME_ELEMENT(BTN_TOP2)},
        {BTN_PINKIE,                   NAME_ELEMENT(BTN_PINKIE)},
        {BTN_BASE,                     NAME_ELEMENT(BTN_BASE)},
        {BTN_BASE2,                    NAME_ELEMENT(BTN_BASE2)},
        {BTN_BASE3,                    NAME_ELEMENT(BTN_BASE3)},
        {BTN_BASE4,                    NAME_ELEMENT(BTN_BASE4)},
        {BTN_BASE5,                    NAME_ELEMENT(BTN_BASE5)},
        {BTN_BASE6,                    NAME_ELEMENT(BTN_BASE6)},
        {BTN_DEAD,                     NAME_ELEMENT(BTN_DEAD)},
        {BTN_C,                        NAME_ELEMENT(BTN_C)},
        {BTN_SOUTH,                    NAME_ELEMENT(BTN_SOUTH)},
        {BTN_EAST,                     NAME_ELEMENT(BTN_EAST)},
        {BTN_NORTH,                    NAME_ELEMENT(BTN_NORTH)},
        {BTN_WEST,                     NAME_ELEMENT(BTN_WEST)},
        {BTN_A,                        NAME_ELEMENT(BTN_A)},
        {BTN_B,                        NAME_ELEMENT(BTN_B)},
        {BTN_X,                        NAME_ELEMENT(BTN_X)},
        {BTN_Y,                        NAME_ELEMENT(BTN_Y)},
        {BTN_Z,                        NAME_ELEMENT(BTN_Z)},
        {BTN_TL,                       NAME_ELEMENT(BTN_TL)},
        {BTN_TR,                       NAME_ELEMENT(BTN_TR)},
        {BTN_TL2,                      NAME_ELEMENT(BTN_TL2)},
        {BTN_TR2,                      NAME_ELEMENT(BTN_TR2)},
        {BTN_SELECT,                   NAME_ELEMENT(BTN_SELECT)},
        {BTN_START,                    NAME_ELEMENT(BTN_START)},
        {BTN_MODE,                     NAME_ELEMENT(BTN_MODE)},
        {BTN_THUMBL,                   NAME_ELEMENT(BTN_THUMBL)},
        {BTN_THUMBR,                   NAME_ELEMENT(BTN_THUMBR)},
        {BTN_TOOL_PEN,                 NAME_ELEMENT(BTN_TOOL_PEN)},
        {BTN_TOOL_RUBBER,              NAME_ELEMENT(BTN_TOOL_RUBBER)},
        {BTN_TOOL_BRUSH,               NAME_ELEMENT(BTN_TOOL_BRUSH)},
        {BTN_TOOL_PENCIL,              NAME_ELEMENT(BTN_TOOL_PENCIL)},
        {BTN_TOOL_AIRBRUSH,            NAME_ELEMENT(BTN_TOOL_AIRBRUSH)},
        {BTN_TOOL_FINGER,              NAME_ELEMENT(BTN_TOOL_FINGER)},
        {BTN_TOOL_MOUSE,               NAME_ELEMENT(BTN_TOOL_MOUSE)},
        {BTN_TOOL_LENS,                NAME_ELEMENT(BTN_TOOL_LENS)},
        {BTN_TOUCH,                    NAME_ELEMENT(BTN_TOUCH)},
        {BTN_STYLUS,                   NAME_ELEMENT(BTN_STYLUS)},
        {BTN_STYLUS2,                  NAME_ELEMENT(BTN_STYLUS2)},
        {BTN_TOOL_DOUBLETAP,           NAME_ELEMENT(BTN_TOOL_DOUBLETAP)},
        {BTN_TOOL_TRIPLETAP,           NAME_ELEMENT(BTN_TOOL_TRIPLETAP)},
        {BTN_TOOL_QUADTAP,             NAME_ELEMENT(BTN_TOOL_QUADTAP)},
        {BTN_GEAR_DOWN,                NAME_ELEMENT(BTN_GEAR_DOWN)},
        {BTN_GEAR_UP,                  NAME_ELEMENT(BTN_GEAR_UP)},
        {BTN_DPAD_UP,                  NAME_ELEMENT(BTN_DPAD_UP)},
        {BTN_DPAD_DOWN,                NAME_ELEMENT(BTN_DPAD_DOWN)},
        {BTN_DPAD_LEFT,                NAME_ELEMENT(BTN_DPAD_LEFT)},
        {BTN_DPAD_RIGHT,               NAME_ELEMENT(BTN_DPAD_RIGHT)},
        {KEY_ALS_TOGGLE,               NAME_ELEMENT(KEY_ALS_TOGGLE)},
#if LINUX_VERSION_CODE >= KERNEL_VERSION(4, 15, 0)
        {KEY_BUTTONCONFIG,             NAME_ELEMENT(KEY_BUTTONCONFIG)},
        {KEY_TASKMANAGER,              NAME_ELEMENT(KEY_TASKMANAGER)},
        {KEY_JOURNAL,                  NAME_ELEMENT(KEY_JOURNAL)},
        {KEY_CONTROLPANEL,             NAME_ELEMENT(KEY_CONTROLPANEL)},
        {KEY_APPSELECT,                NAME_ELEMENT(KEY_APPSELECT)},
        {KEY_SCREENSAVER,              NAME_ELEMENT(KEY_SCREENSAVER)},
        {KEY_VOICECOMMAND,             NAME_ELEMENT(KEY_VOICECOMMAND)},
        {KEY_ASSISTANT,                NAME_ELEMENT(KEY_ASSISTANT)},
        {KEY_BRIGHTNESS_MIN,           NAME_ELEMENT(KEY_BRIGHTNESS_MIN)},
        {KEY_BRIGHTNESS_MAX,           NAME_ELEMENT(KEY_BRIGHTNESS_MAX)},
        {KEY_KBDINPUTASSIST_PREV,      NAME_ELEMENT(KEY_KBDINPUTASSIST_PREV)},
        {KEY_KBDINPUTASSIST_NEXT,      NAME_ELEMENT(KEY_KBDINPUTASSIST_NEXT)},
        {KEY_KBDINPUTASSIST_PREVGROUP, NAME_ELEMENT(KEY_KBDINPUTASSIST_PREVGROUP)},
        {KEY_KBDINPUTASSIST_NEXTGROUP, NAME_ELEMENT(KEY_KBDINPUTASSIST_NEXTGROUP)},
        {KEY_KBDINPUTASSIST_ACCEPT,    NAME_ELEMENT(KEY_KBDINPUTASSIST_ACCEPT)},
        {KEY_KBDINPUTASSIST_CANCEL,    NAME_ELEMENT(KEY_KBDINPUTASSIST_CANCEL)},
        {KEY_RIGHT_UP,                 NAME_ELEMENT(KEY_RIGHT_UP)},
        {KEY_RIGHT_DOWN,               NAME_ELEMENT(KEY_RIGHT_DOWN)},
        {KEY_LEFT_UP,                  NAME_ELEMENT(KEY_LEFT_UP)},
        {KEY_LEFT_DOWN,                NAME_ELEMENT(KEY_LEFT_DOWN)},
        {KEY_ROOT_MENU,                NAME_ELEMENT(KEY_ROOT_MENU)},
        {KEY_MEDIA_TOP_MENU,           NAME_ELEMENT(KEY_MEDIA_TOP_MENU)},
        {KEY_AUDIO_DESC,               NAME_ELEMENT(KEY_AUDIO_DESC)},
        {KEY_3D_MODE,                  NAME_ELEMENT(KEY_3D_MODE)},
        {KEY_NEXT_FAVORITE,            NAME_ELEMENT(KEY_NEXT_FAVORITE)},
        {KEY_STOP_RECORD,              NAME_ELEMENT(KEY_STOP_RECORD)},
        {KEY_PAUSE_RECORD,             NAME_ELEMENT(KEY_PAUSE_RECORD)},
        {KEY_VOD,                      NAME_ELEMENT(KEY_VOD)},
        {KEY_UNMUTE,                   NAME_ELEMENT(KEY_UNMUTE)},
        {KEY_FASTREVERSE,              NAME_ELEMENT(KEY_FASTREVERSE)},
        {KEY_SLOWREVERSE,              NAME_ELEMENT(KEY_SLOWREVERSE)},
        {KEY_DATA,                     NAME_ELEMENT(KEY_DATA)},
#endif
#if LINUX_VERSION_CODE >= KERNEL_VERSION(4,15,0)
        {KEY_ONSCREEN_KEYBOARD,        NAME_ELEMENT(KEY_ONSCREEN_KEYBOARD)},
#endif
        {BTN_TRIGGER_HAPPY1,           NAME_ELEMENT(BTN_TRIGGER_HAPPY1)},
        {BTN_TRIGGER_HAPPY2,           NAME_ELEMENT(BTN_TRIGGER_HAPPY2)},
        {BTN_TRIGGER_HAPPY3,           NAME_ELEMENT(BTN_TRIGGER_HAPPY3)},
        {BTN_TRIGGER_HAPPY4,           NAME_ELEMENT(BTN_TRIGGER_HAPPY4)},
        {BTN_TRIGGER_HAPPY5,           NAME_ELEMENT(BTN_TRIGGER_HAPPY5)},
        {BTN_TRIGGER_HAPPY6,           NAME_ELEMENT(BTN_TRIGGER_HAPPY6)},
        {BTN_TRIGGER_HAPPY7,           NAME_ELEMENT(BTN_TRIGGER_HAPPY7)},
        {BTN_TRIGGER_HAPPY8,           NAME_ELEMENT(BTN_TRIGGER_HAPPY8)},
        {BTN_TRIGGER_HAPPY9,           NAME_ELEMENT(BTN_TRIGGER_HAPPY9)},
        {BTN_TRIGGER_HAPPY10,          NAME_ELEMENT(BTN_TRIGGER_HAPPY10)},
        {BTN_TRIGGER_HAPPY11,          NAME_ELEMENT(BTN_TRIGGER_HAPPY11)},
        {BTN_TRIGGER_HAPPY12,          NAME_ELEMENT(BTN_TRIGGER_HAPPY12)},
        {BTN_TRIGGER_HAPPY13,          NAME_ELEMENT(BTN_TRIGGER_HAPPY13)},
        {BTN_TRIGGER_HAPPY14,          NAME_ELEMENT(BTN_TRIGGER_HAPPY14)},
        {BTN_TRIGGER_HAPPY15,          NAME_ELEMENT(BTN_TRIGGER_HAPPY15)},
        {BTN_TRIGGER_HAPPY16,          NAME_ELEMENT(BTN_TRIGGER_HAPPY16)},
        {BTN_TRIGGER_HAPPY17,          NAME_ELEMENT(BTN_TRIGGER_HAPPY17)},
        {BTN_TRIGGER_HAPPY18,          NAME_ELEMENT(BTN_TRIGGER_HAPPY18)},
        {BTN_TRIGGER_HAPPY19,          NAME_ELEMENT(BTN_TRIGGER_HAPPY19)},
        {BTN_TRIGGER_HAPPY20,          NAME_ELEMENT(BTN_TRIGGER_HAPPY20)},
        {BTN_TRIGGER_HAPPY21,          NAME_ELEMENT(BTN_TRIGGER_HAPPY21)},
        {BTN_TRIGGER_HAPPY22,          NAME_ELEMENT(BTN_TRIGGER_HAPPY22)},
        {BTN_TRIGGER_HAPPY23,          NAME_ELEMENT(BTN_TRIGGER_HAPPY23)},
        {BTN_TRIGGER_HAPPY24,          NAME_ELEMENT(BTN_TRIGGER_HAPPY24)},
        {BTN_TRIGGER_HAPPY25,          NAME_ELEMENT(BTN_TRIGGER_HAPPY25)},
        {BTN_TRIGGER_HAPPY26,          NAME_ELEMENT(BTN_TRIGGER_HAPPY26)},
        {BTN_TRIGGER_HAPPY27,          NAME_ELEMENT(BTN_TRIGGER_HAPPY27)},
        {BTN_TRIGGER_HAPPY28,          NAME_ELEMENT(BTN_TRIGGER_HAPPY28)},
        {BTN_TRIGGER_HAPPY29,          NAME_ELEMENT(BTN_TRIGGER_HAPPY29)},
        {BTN_TRIGGER_HAPPY30,          NAME_ELEMENT(BTN_TRIGGER_HAPPY30)},
        {BTN_TRIGGER_HAPPY31,          NAME_ELEMENT(BTN_TRIGGER_HAPPY31)},
        {BTN_TRIGGER_HAPPY32,          NAME_ELEMENT(BTN_TRIGGER_HAPPY32)},
        {BTN_TRIGGER_HAPPY33,          NAME_ELEMENT(BTN_TRIGGER_HAPPY33)},
        {BTN_TRIGGER_HAPPY34,          NAME_ELEMENT(BTN_TRIGGER_HAPPY34)},
        {BTN_TRIGGER_HAPPY35,          NAME_ELEMENT(BTN_TRIGGER_HAPPY35)},
        {BTN_TRIGGER_HAPPY36,          NAME_ELEMENT(BTN_TRIGGER_HAPPY36)},
        {BTN_TRIGGER_HAPPY37,          NAME_ELEMENT(BTN_TRIGGER_HAPPY37)},
        {BTN_TRIGGER_HAPPY38,          NAME_ELEMENT(BTN_TRIGGER_HAPPY38)},
        {BTN_TRIGGER_HAPPY39,          NAME_ELEMENT(BTN_TRIGGER_HAPPY39)},
        {BTN_TRIGGER_HAPPY40,          NAME_ELEMENT(BTN_TRIGGER_HAPPY40)},
        {BTN_TOOL_QUINTTAP,            NAME_ELEMENT(BTN_TOOL_QUINTTAP)}

};

static const vector<string> absval = {"Value", "Min  ", "Max  ", "Fuzz ", "Flat ", "Resolution "
};

static const map<int, string> relatives = {
        {REL_X,      NAME_ELEMENT(REL_X)
        },
        {REL_Y,      NAME_ELEMENT(REL_Y)
        },
        {REL_Z,      NAME_ELEMENT(REL_Z)
        },
        {REL_RX,     NAME_ELEMENT(REL_RX)
        },
        {REL_RY,     NAME_ELEMENT(REL_RY)
        },
        {REL_RZ,     NAME_ELEMENT(REL_RZ)
        },
        {REL_HWHEEL, NAME_ELEMENT(REL_HWHEEL)
        },
        {REL_DIAL,   NAME_ELEMENT(REL_DIAL)
        },
        {REL_WHEEL,  NAME_ELEMENT(REL_WHEEL)
        },
        {REL_MISC,   NAME_ELEMENT(REL_MISC)
        }
};

static const map<int, string> absolutes = {
        {ABS_X,              NAME_ELEMENT(ABS_X)},
        {ABS_Y,              NAME_ELEMENT(ABS_Y)},
        {ABS_Z,              NAME_ELEMENT(ABS_Z)},
        {ABS_RX,             NAME_ELEMENT(ABS_RX)},
        {ABS_RY,             NAME_ELEMENT(ABS_RY)},
        {ABS_RZ,             NAME_ELEMENT(ABS_RZ)},
        {ABS_THROTTLE,       NAME_ELEMENT(ABS_THROTTLE)},
        {ABS_RUDDER,         NAME_ELEMENT(ABS_RUDDER)},
        {ABS_WHEEL,          NAME_ELEMENT(ABS_WHEEL)},
        {ABS_GAS,            NAME_ELEMENT(ABS_GAS)},
        {ABS_BRAKE,          NAME_ELEMENT(ABS_BRAKE)},
        {ABS_HAT0X,          NAME_ELEMENT(ABS_HAT0X)},
        {ABS_HAT0Y,          NAME_ELEMENT(ABS_HAT0Y)},
        {ABS_HAT1X,          NAME_ELEMENT(ABS_HAT1X)},
        {ABS_HAT1Y,          NAME_ELEMENT(ABS_HAT1Y)},
        {ABS_HAT2X,          NAME_ELEMENT(ABS_HAT2X)},
        {ABS_HAT2Y,          NAME_ELEMENT(ABS_HAT2Y)},
        {ABS_HAT3X,          NAME_ELEMENT(ABS_HAT3X)},
        {ABS_HAT3Y,          NAME_ELEMENT(ABS_HAT3Y)},
        {ABS_PRESSURE,       NAME_ELEMENT(ABS_PRESSURE)},
        {ABS_DISTANCE,       NAME_ELEMENT(ABS_DISTANCE)},
        {ABS_TILT_X,         NAME_ELEMENT(ABS_TILT_X)},
        {ABS_TILT_Y,         NAME_ELEMENT(ABS_TILT_Y)},
        {ABS_TOOL_WIDTH,     NAME_ELEMENT(ABS_TOOL_WIDTH)},
        {ABS_VOLUME,         NAME_ELEMENT(ABS_VOLUME)},
        {ABS_MISC,           NAME_ELEMENT(ABS_MISC)},
        {ABS_MT_TOUCH_MAJOR, NAME_ELEMENT(ABS_MT_TOUCH_MAJOR)},
        {ABS_MT_TOUCH_MINOR, NAME_ELEMENT(ABS_MT_TOUCH_MINOR)},
        {ABS_MT_WIDTH_MAJOR, NAME_ELEMENT(ABS_MT_WIDTH_MAJOR)},
        {ABS_MT_WIDTH_MINOR, NAME_ELEMENT(ABS_MT_WIDTH_MINOR)},
        {ABS_MT_ORIENTATION, NAME_ELEMENT(ABS_MT_ORIENTATION)},
        {ABS_MT_POSITION_X,  NAME_ELEMENT(ABS_MT_POSITION_X)},
        {ABS_MT_POSITION_Y,  NAME_ELEMENT(ABS_MT_POSITION_Y)},
        {ABS_MT_TOOL_TYPE,   NAME_ELEMENT(ABS_MT_TOOL_TYPE)},
        {ABS_MT_BLOB_ID,     NAME_ELEMENT(ABS_MT_BLOB_ID)},
        {ABS_MT_TRACKING_ID, NAME_ELEMENT(ABS_MT_TRACKING_ID)},
        {ABS_MT_PRESSURE,    NAME_ELEMENT(ABS_MT_PRESSURE)},
        {ABS_MT_SLOT,        NAME_ELEMENT(ABS_MT_SLOT)},
        {ABS_MT_TOOL_X,      NAME_ELEMENT(ABS_MT_TOOL_X)},
        {ABS_MT_TOOL_Y,      NAME_ELEMENT(ABS_MT_TOOL_Y)},
        {ABS_MT_DISTANCE,    NAME_ELEMENT(ABS_MT_DISTANCE)}
};

static const map<int, string> misc = {
        {MSC_SERIAL,    NAME_ELEMENT(MSC_SERIAL)
        },
        {MSC_PULSELED,  NAME_ELEMENT(MSC_PULSELED)
        },
        {MSC_GESTURE,   NAME_ELEMENT(MSC_GESTURE)
        },
        {MSC_RAW,       NAME_ELEMENT(MSC_RAW)
        },
        {MSC_SCAN,      NAME_ELEMENT(MSC_SCAN)
        },
        {MSC_TIMESTAMP, NAME_ELEMENT(MSC_TIMESTAMP)
        }
};

static const map<int, string> leds = {
        {LED_NUML,     NAME_ELEMENT(LED_NUML)},
        {LED_CAPSL,    NAME_ELEMENT(LED_CAPSL)},
        {LED_SCROLLL,  NAME_ELEMENT(LED_SCROLLL)},
        {LED_COMPOSE,  NAME_ELEMENT(LED_COMPOSE)},
        {LED_KANA,     NAME_ELEMENT(LED_KANA)},
        {LED_SLEEP,    NAME_ELEMENT(LED_SLEEP)},
        {LED_SUSPEND,  NAME_ELEMENT(LED_SUSPEND)},
        {LED_MUTE,     NAME_ELEMENT(LED_MUTE)},
        {LED_MISC,     NAME_ELEMENT(LED_MISC)},
        {LED_MAIL,     NAME_ELEMENT(LED_MAIL)},
        {LED_CHARGING, NAME_ELEMENT(LED_CHARGING)}
};

static const map<int, string> repeats = {
        {REP_DELAY,  NAME_ELEMENT(REP_DELAY)},
        {REP_PERIOD, NAME_ELEMENT(REP_PERIOD)}
};

static const map<int, string> sounds = {
        {SND_CLICK, NAME_ELEMENT(SND_CLICK)
        },
        {SND_BELL,  NAME_ELEMENT(SND_BELL)
        },
        {SND_TONE,  NAME_ELEMENT(SND_TONE)
        }
};

static const map<int, string> syns = {
        {SYN_REPORT,    NAME_ELEMENT(SYN_REPORT)},
        {SYN_CONFIG,    NAME_ELEMENT(SYN_CONFIG)},
        {SYN_MT_REPORT, NAME_ELEMENT(SYN_MT_REPORT)},
        {SYN_DROPPED,   NAME_ELEMENT(SYN_DROPPED)}
};

static const map<int, string> switches = {
        {SW_TABLET_MODE,          NAME_ELEMENT(SW_TABLET_MODE)},
        {SW_HEADPHONE_INSERT,     NAME_ELEMENT(SW_HEADPHONE_INSERT)},
        {SW_RFKILL_ALL,           NAME_ELEMENT(SW_RFKILL_ALL)},
        {SW_MICROPHONE_INSERT,    NAME_ELEMENT(SW_MICROPHONE_INSERT)},
        {SW_DOCK,                 NAME_ELEMENT(SW_DOCK)},
        {SW_LINEOUT_INSERT,       NAME_ELEMENT(SW_LINEOUT_INSERT)},
        {SW_JACK_PHYSICAL_INSERT, NAME_ELEMENT(SW_JACK_PHYSICAL_INSERT)},
        {SW_VIDEOOUT_INSERT,      NAME_ELEMENT(SW_VIDEOOUT_INSERT)},
        {SW_CAMERA_LENS_COVER,    NAME_ELEMENT(SW_CAMERA_LENS_COVER)},
        {SW_KEYPAD_SLIDE,         NAME_ELEMENT(SW_KEYPAD_SLIDE)},
        {SW_FRONT_PROXIMITY,      NAME_ELEMENT(SW_FRONT_PROXIMITY)},
        {SW_ROTATE_LOCK,          NAME_ELEMENT(SW_ROTATE_LOCK)},
        {SW_LINEIN_INSERT,        NAME_ELEMENT(SW_LINEIN_INSERT)},
        {SW_MUTE_DEVICE,          NAME_ELEMENT(SW_MUTE_DEVICE)},
#ifdef SW_PEN_INSERTED
        {SW_PEN_INSERTED,         NAME_ELEMENT(SW_PEN_INSERTED)}
#endif
};

static const map<int, string> force = {
        {FF_RUMBLE,     NAME_ELEMENT(FF_RUMBLE)
        },
        {FF_PERIODIC,   NAME_ELEMENT(FF_PERIODIC)
        },
        {FF_CONSTANT,   NAME_ELEMENT(FF_CONSTANT)
        },
        {FF_SPRING,     NAME_ELEMENT(FF_SPRING)
        },
        {FF_FRICTION,   NAME_ELEMENT(FF_FRICTION)
        },
        {FF_DAMPER,     NAME_ELEMENT(FF_DAMPER)
        },
        {FF_INERTIA,    NAME_ELEMENT(FF_INERTIA)
        },
        {FF_RAMP,       NAME_ELEMENT(FF_RAMP)
        },
        {FF_SQUARE,     NAME_ELEMENT(FF_SQUARE)
        },
        {FF_TRIANGLE,   NAME_ELEMENT(FF_TRIANGLE)
        },
        {FF_SINE,       NAME_ELEMENT(FF_SINE)
        },
        {FF_SAW_UP,     NAME_ELEMENT(FF_SAW_UP)
        },
        {FF_SAW_DOWN,   NAME_ELEMENT(FF_SAW_DOWN)
        },
        {FF_CUSTOM,     NAME_ELEMENT(FF_CUSTOM)
        },
        {FF_GAIN,       NAME_ELEMENT(FF_GAIN)
        },
        {FF_AUTOCENTER, NAME_ELEMENT(FF_AUTOCENTER)
        }
};

static const map<int, string> forcestatus = {
        {FF_STATUS_STOPPED, NAME_ELEMENT(FF_STATUS_STOPPED)
        },
        {FF_STATUS_PLAYING, NAME_ELEMENT(FF_STATUS_PLAYING)
        }
};

static const map<int, map<int, string>> names = {
        {EV_SYN,       syns
        },
        {EV_KEY,       keys
        },
        {EV_REL,       relatives
        },
        {EV_ABS,       absolutes
        },
        {EV_MSC,       misc
        },
        {EV_LED,       leds
        },
        {EV_SND,       sounds
        },
        {EV_REP,       repeats
        },
        {EV_SW,        switches
        },
        {EV_FF,        force
        },
        {EV_FF_STATUS, forcestatus
        }
};

uint8_t HasEventType(int fd, int type);

uint8_t HasKeyEvents(int device_fd);

uint8_t HasSpecificKey(int device_fd, unsigned int key);

int get_state(int fd, unsigned int type, unsigned long *array, size_t size);

string typname(unsigned int type);

string codename(int type, int code);

int listInputDevices(vector<string> &entries);

void JNI_throwIOException(JNIEnv *env, string msg);

int is_valid_fd(int fd);

bool file_exists(const string &name);

string defaultVal(const char *value);

bool is_readable(const string &path);

#endif //PID_NATIVE_INPUTDEVHELPER_H
