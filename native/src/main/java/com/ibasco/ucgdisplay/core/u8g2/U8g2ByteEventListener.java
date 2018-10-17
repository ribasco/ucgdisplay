package com.ibasco.ucgdisplay.core.u8g2;

@FunctionalInterface
public interface U8g2ByteEventListener {
    void onByteEvent(U8g2ByteEvent event);
}
