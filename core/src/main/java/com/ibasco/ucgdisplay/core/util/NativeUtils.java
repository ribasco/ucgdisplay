package com.ibasco.ucgdisplay.core.util;

public class NativeUtils {
    public static final int _IOC_NONE = 0;
    public static final int _IOC_WRITE = 1;
    public static final int _IOC_READ = 2;

    static final int IOCPARM_MASK = 0x1fff;
    static final int IOC_VOID = 0x20000000;
    static final int IOC_OUT = 0x40000000;
    static final int IOC_IN = 0x80000000;
    static final int IOC_INOUT = (IOC_IN | IOC_OUT);
    static final int IOC_DIRMASK = 0xe0000000;

    public static int IOCPARM_LEN(int x) {
        return (((x) >> 16) & IOCPARM_MASK);
    }

    public static int IOCBASECMD(int x) {
        return ((x) & ~IOCPARM_MASK);
    }

    public static int IOCGROUP(int x) {
        return (((x) >> 8) & 0xff);
    }

    static public int sizeof(int x) {
        return 8; // does this work for 32 bit Linux too?
    }

    public static int _IOC(int inout, int group, int num, int len) {
        return ((inout << 30) | ((group) << 8) | (num) | ((len & IOCPARM_MASK) << 16));
    }

    public static int _IO(int g, int n) {
        return _IOC(IOC_VOID, (g), (n), 0);
    }

    public static int _IOR(int g, int n, int t) {
        return _IOC(IOC_OUT, (g), (n), sizeof(t));
    }

    public static int _IOW(int g, int n, int t) {
        return _IOC(IOC_IN, (g), (n), sizeof(t));
    }

    public static int _IOWR(int g, int n, int t) {
        return _IOC(IOC_INOUT, (g), (n), sizeof(t));
    }

    public static int EVIOCGVERSION() {
        return _IOR('E', 0x01, 4);
    }

    public static int EVIOCGNAME(int length) {
        return _IOC(_IOC_READ, 'E', 0x06, length);
    }
}


