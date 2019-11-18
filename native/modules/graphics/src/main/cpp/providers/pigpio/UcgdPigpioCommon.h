#ifndef UCGD_MOD_GRAPHICS_UCGDPIGPIOCOMMON_H
#define UCGD_MOD_GRAPHICS_UCGDPIGPIOCOMMON_H

#include <string>
#include <pigpio.h>
#include <UcgdGpioPeripheral.h>

class UcgdPigpioCommon {
public:
    static inline std::string getErrorMsg(int val) {
        std::string reason;
        switch (val) {
            case PI_BAD_I2C_BUS:
                reason = "Bad I2C Bus";
                break;
            case PI_BAD_I2C_ADDR:
                reason = "Bad I2C Address";
                break;
            case PI_BAD_FLAGS:
                reason = "Bad I2C open flags";
                break;
            case PI_NO_HANDLE:
                reason = "No handle available";
                break;
            case PI_I2C_OPEN_FAILED: {
                reason = "Can't open I2C device";
                break;
            }
            case PI_BAD_HANDLE: {
                reason = "Unknown handle";
                break;
            }
            case PI_BAD_PARAM: {
                reason = "Bad i2c/spi/ser parameter";
                break;
            }
            case PI_I2C_WRITE_FAILED: {
                reason = "I2C write failed";
                break;
            }
            default: {
                reason = "Unknown error";
                break;
            }
        }
        return reason;
    }

    static UcgdGpioPeripheral::GpioMode pigpioToGpioMode(int mode) {
        switch (mode) {
            case PI_INPUT: {
                return UcgdGpioPeripheral::GpioMode::MODE_INPUT;
            }
            case PI_OUTPUT: {
                return UcgdGpioPeripheral::GpioMode::MODE_OUTPUT;
            }
            case PI_ALT0: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT0;
            }
            case PI_ALT1: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT1;
            }
            case PI_ALT2: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT2;
            }
            case PI_ALT3: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT3;
            }
            case PI_ALT4: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT4;
            }
            case PI_ALT5: {
                return UcgdGpioPeripheral::GpioMode::MODE_ALT5;
            }
            default: {
                std::stringstream ss;
                ss << "modeToInt() : Unable to perform mode conversion. Reason: mode is invalid or not supported: " << std::to_string(mode);
                throw GpioModeException(ss.str());
            }
        }
    }

    static unsigned int gpioModeToPigpio(UcgdGpioPeripheral::GpioMode mode) {
        switch (mode) {
            case UcgdGpioPeripheral::GpioMode::MODE_INPUT: {
                return PI_INPUT;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_OUTPUT: {
                return PI_OUTPUT;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT0: {
                return PI_ALT0;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT1: {
                return PI_ALT1;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT2: {
                return PI_ALT2;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT3: {
                return PI_ALT3;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT4: {
                return PI_ALT4;
            }
            case UcgdGpioPeripheral::GpioMode::MODE_ALT5: {
                return PI_ALT5;
            }
            default: {
                std::stringstream ss;
                ss << "modeToInt() : Unable to perform mode conversion. Reason: mode is invalid or not supported: " << std::to_string(mode);
                throw GpioModeException(ss.str());
            }
        }
    }
};


#endif //UCGD_MOD_GRAPHICS_UCGDPIGPIOCOMMON_H
