package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.core.u8g2.U8g2ByteEvent;
import com.ibasco.ucgdisplay.core.u8g2.U8g2ByteEventListener;
import com.ibasco.ucgdisplay.core.u8g2.U8g2GpioEvent;
import com.ibasco.ucgdisplay.core.u8g2.U8g2GpioEventListener;

/**
 * Interface for handling U8g2 display events.
 *
 * @author Rafael Ibasco
 */
public interface GlcdDriverEventHandler extends U8g2ByteEventListener, U8g2GpioEventListener {
    @Override
    void onByteEvent(U8g2ByteEvent event);

    @Override
    default void onGpioEvent(U8g2GpioEvent event) {
        //optional only
    }
}
