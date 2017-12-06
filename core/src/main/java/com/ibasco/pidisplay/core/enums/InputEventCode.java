package com.ibasco.pidisplay.core.enums;

import java.util.Arrays;

/**
 * Input Event Codes.
 *
 * Directly copied from input-event-codes.h header
 */
@SuppressWarnings("unused")
public enum InputEventCode {
    KEY_RESERVED(0),
    KEY_ESC(1),
    KEY_1(2, '1'),
    KEY_2(3, '2'),
    KEY_3(4, '3'),
    KEY_4(5, '4'),
    KEY_5(6, '5'),
    KEY_6(7, '6'),
    KEY_7(8, '7'),
    KEY_8(9, '8'),
    KEY_9(10, '9'),
    KEY_0(11, '0'),
    KEY_MINUS(12, '-'),
    KEY_EQUAL(13, '='),
    KEY_BACKSPACE(14),
    KEY_TAB(15, '\t'),
    KEY_Q(16, 'q'),
    KEY_W(17, 'w'),
    KEY_E(18, 'e'),
    KEY_R(19, 'r'),
    KEY_T(20, 't'),
    KEY_Y(21, 'y'),
    KEY_U(22, 'u'),
    KEY_I(23, 'i'),
    KEY_O(24, 'o'),
    KEY_P(25, 'p'),
    KEY_LEFTBRACE(26),
    KEY_RIGHTBRACE(27),
    KEY_ENTER(28),
    KEY_LEFTCTRL(29),
    KEY_A(30, 'a'),
    KEY_S(31, 's'),
    KEY_D(32, 'd'),
    KEY_F(33, 'f'),
    KEY_G(34, 'g'),
    KEY_H(35, 'h'),
    KEY_J(36, 'j'),
    KEY_K(37, 'k'),
    KEY_L(38, 'l'),
    KEY_SEMICOLON(39, ';'),
    KEY_APOSTROPHE(40, '\''),
    KEY_GRAVE(41),
    KEY_LEFTSHIFT(42),
    KEY_BACKSLASH(43, '\\'),
    KEY_Z(44, 'z'),
    KEY_X(45, 'x'),
    KEY_C(46, 'c'),
    KEY_V(47, 'v'),
    KEY_B(48, 'b'),
    KEY_N(49, 'n'),
    KEY_M(50, 'm'),
    KEY_COMMA(51, ','),
    KEY_DOT(52, '.'),
    KEY_SLASH(53, '/'),
    KEY_RIGHTSHIFT(54),
    KEY_KPASTERISK(55),
    KEY_LEFTALT(56),
    KEY_SPACE(57, ' '),
    KEY_CAPSLOCK(58),
    KEY_F1(59),
    KEY_F2(60),
    KEY_F3(61),
    KEY_F4(62),
    KEY_F5(63),
    KEY_F6(64),
    KEY_F7(65),
    KEY_F8(66),
    KEY_F9(67),
    KEY_F10(68),
    KEY_NUMLOCK(69),
    KEY_SCROLLLOCK(70),
    KEY_KP7(71),
    KEY_KP8(72),
    KEY_KP9(73),
    KEY_KPMINUS(74),
    KEY_KP4(75),
    KEY_KP5(76),
    KEY_KP6(77),
    KEY_KPPLUS(78),
    KEY_KP1(79),
    KEY_KP2(80),
    KEY_KP3(81),
    KEY_KP0(82),
    KEY_KPDOT(83),
    KEY_ZENKAKUHANKAKU(85),
    KEY_102ND(86),
    KEY_F11(87),
    KEY_F12(88),
    KEY_RO(89),
    KEY_KATAKANA(90),
    KEY_HIRAGANA(91),
    KEY_HENKAN(92),
    KEY_KATAKANAHIRAGANA(93),
    KEY_MUHENKAN(94),
    KEY_KPJPCOMMA(95),
    KEY_KPENTER(96),
    KEY_RIGHTCTRL(97),
    KEY_KPSLASH(98),
    KEY_SYSRQ(99),
    KEY_RIGHTALT(100),
    KEY_LINEFEED(101),
    KEY_HOME(102),
    KEY_UP(103),
    KEY_PAGEUP(104),
    KEY_LEFT(105),
    KEY_RIGHT(106),
    KEY_END(107),
    KEY_DOWN(108),
    KEY_PAGEDOWN(109),
    KEY_INSERT(110),
    KEY_DELETE(111),
    KEY_MACRO(112),
    KEY_MUTE(113),
    KEY_VOLUMEDOWN(114),
    KEY_VOLUMEUP(115),
    /**
     * SC System Power Down
     */
    KEY_POWER(116),
    KEY_KPEQUAL(117),
    KEY_KPPLUSMINUS(118),
    KEY_PAUSE(119),
    /**
     * AL Compiz Scale (Expose)
     */
    KEY_SCALE(120),
    KEY_KPCOMMA(121),
    KEY_HANGEUL(122),
    KEY_HANGUEL(KEY_HANGEUL.code),
    KEY_HANJA(123),
    KEY_YEN(124),
    KEY_LEFTMETA(125),
    KEY_RIGHTMETA(126),
    KEY_COMPOSE(127),
    /**
     * AC Stop
     */
    KEY_STOP(128),

    KEY_AGAIN(129),

    /**
     * AC Properties
     */
    KEY_PROPS(130),

    /**
     * AC Undo
     */
    KEY_UNDO(131),

    KEY_FRONT(132),

    /**
     * AC Copy
     */
    KEY_COPY(133),

    /**
     * AC Open
     */
    KEY_OPEN(134),

    /** AC Paste */
    KEY_PASTE(135),

    /** AC Search */
    KEY_FIND(136),

    /** AC Cut */
    KEY_CUT(137),

    /** AL Integrated Help Center */
    KEY_HELP(138),

    /** Menu (show menu) */
    KEY_MENU(139),

    /** AL Calculator */
    KEY_CALC(140),

    KEY_SETUP(141),

    /** SC System Sleep */
    KEY_SLEEP(142),

    /** System Wake Up */
    KEY_WAKEUP(143),

    /** AL Local Machine Browser */
    KEY_FILE(144),

    KEY_SENDFILE(145),

    KEY_DELETEFILE(146),

    KEY_XFER(147),

    KEY_PROG1(148),

    KEY_PROG2(149),

    /** AL Internet Browser */
    KEY_WWW(150),

    KEY_MSDOS(151),

    /** AL Terminal Lock/Screensaver */
    KEY_COFFEE(152),

    KEY_SCREENLOCK(KEY_COFFEE.code),

    /** Display orientation for e.g. tablets */
    KEY_ROTATE_DISPLAY(153),

    KEY_DIRECTION(KEY_ROTATE_DISPLAY.code),

    KEY_CYCLEWINDOWS(154),

    KEY_MAIL(155),

    /** AC Bookmarks */
    KEY_BOOKMARKS(156),

    KEY_COMPUTER(157),

    /** AC Back */
    KEY_BACK(158),

    /** AC Forward */
    KEY_FORWARD(159),

    KEY_CLOSECD(160),

    KEY_EJECTCD(161),

    KEY_EJECTCLOSECD(162),

    KEY_NEXTSONG(163),

    KEY_PLAYPAUSE(164),

    KEY_PREVIOUSSONG(165),

    KEY_STOPCD(166),

    KEY_RECORD(167),

    KEY_REWIND(168),

    /** Media Select Telephone */
    KEY_PHONE(169),

    KEY_ISO(170),

    /** AL Consumer Control Configuration */
    KEY_CONFIG(171),

    /** AC Home */
    KEY_HOMEPAGE(172),

    /** AC Refresh */
    KEY_REFRESH(173),

    /** AC Exit */
    KEY_EXIT(174),

    KEY_MOVE(175),

    KEY_EDIT(176),

    KEY_SCROLLUP(177),

    KEY_SCROLLDOWN(178),

    KEY_KPLEFTPAREN(179),

    KEY_KPRIGHTPAREN(180),

    /** AC New */
    KEY_NEW(181),

    /** AC Redo/Repeat */
    KEY_REDO(182),

    KEY_F13(183),

    KEY_F14(184),

    KEY_F15(185),

    KEY_F16(186),

    KEY_F17(187),

    KEY_F18(188),

    KEY_F19(189),

    KEY_F20(190),

    KEY_F21(191),

    KEY_F22(192),

    KEY_F23(193),

    KEY_F24(194),

    KEY_PLAYCD(200),

    KEY_PAUSECD(201),

    KEY_PROG3(202),

    KEY_PROG4(203),

    /** AL Dashboard */
    KEY_DASHBOARD(204),

    KEY_SUSPEND(205),

    /** AC Close */
    KEY_CLOSE(206),

    KEY_PLAY(207),

    KEY_FASTFORWARD(208),

    KEY_BASSBOOST(209),

    /** AC Print */
    KEY_PRINT(210),

    KEY_HP(211),

    KEY_CAMERA(212),

    KEY_SOUND(213),

    KEY_QUESTION(214),

    KEY_EMAIL(215),

    KEY_CHAT(216),

    KEY_SEARCH(217),

    KEY_CONNECT(218),

    /** AL Checkbook/Finance */
    KEY_FINANCE(219),

    KEY_SPORT(220),

    KEY_SHOP(221),

    KEY_ALTERASE(222),

    /** AC Cancel */
    KEY_CANCEL(223),

    KEY_BRIGHTNESSDOWN(224),

    KEY_BRIGHTNESSUP(225),

    KEY_MEDIA(226),

    /** Cycle between available video((   outputs (Monitor/LCD/TV-out/etc) */
    KEY_SWITCHVIDEOMODE(227),

    KEY_KBDILLUMTOGGLE(228),

    KEY_KBDILLUMDOWN(229),

    KEY_KBDILLUMUP(230),

    /** AC Send */
    KEY_SEND(231),

    /** AC Reply */
    KEY_REPLY(232),

    /** AC Forward Msg */
    KEY_FORWARDMAIL(233),

    /** AC Save */
    KEY_SAVE(234),

    KEY_DOCUMENTS(235),

    KEY_BATTERY(236),

    KEY_BLUETOOTH(237),

    KEY_WLAN(238),

    KEY_UWB(239),

    KEY_UNKNOWN(240),

    /** drive next video source */
    KEY_VIDEO_NEXT(241),

    /** drive previous video source */
    KEY_VIDEO_PREV(242),

    /** brightness up, after max is min */
    KEY_BRIGHTNESS_CYCLE(243),

    /** Set Auto Brightness: manual brightness control is off, rely on ambient */
    KEY_BRIGHTNESS_AUTO(244),

    KEY_BRIGHTNESS_ZERO(KEY_BRIGHTNESS_AUTO.code),

    /** display device to off state */
    KEY_DISPLAY_OFF(245),

    /** Wireless WAN (LTE, UMTS, GSM, etc.) */
    KEY_WWAN(246),

    KEY_WIMAX(KEY_WWAN.code),

    /** Key that controls all radios */
    KEY_RFKILL(247),

    /** Mute / unmute the microphone */
    KEY_MICMUTE(248),

    UNKNOWN(-1);

    private int code;

    private Character charCode;

    InputEventCode(int code) {
        this(code, null);
    }

    InputEventCode(int code, Character charCode) {
        this.code = code;
        this.charCode = charCode;
    }

    public Character getCharCode() {
        return charCode;
    }

    public int getCode() {
        return code;
    }

    public static InputEventCode toKeyCode(int value) {
        return Arrays.stream(InputEventCode.values()).filter(p -> p.getCode() == value)
                .findFirst()
                .orElse(InputEventCode.UNKNOWN.override(value));
    }

    /**
     * Overrides the code value of the current enumeration.
     *
     * Note: This is only meant to be used internally!
     *
     * @param code
     *         The code to override the value with
     *
     * @return The {@link InputEventCode} instance
     */
    InputEventCode override(int code) {
        this.code = code;
        return this;
    }

    /**
     * @return Returns a Hex Code representation of the code
     */
    public String toHexCode() {
        return Integer.toHexString(code & 0xffff);
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.code);
    }
}
