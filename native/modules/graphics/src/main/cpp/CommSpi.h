/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: CommSpi.h
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

#ifndef UCGDISP_SPI_H
#define UCGDISP_SPI_H

#include <cstdint>

#ifdef __cplusplus
extern "C" {
#endif

#define SPI_DEV_MODE_0 0
#define SPI_DEV_MODE_1 1
#define SPI_DEV_MODE_2 2
#define SPI_DEV_MODE_3 3

/**
 * Get the current file descriptor for the specified SPI Channel
 * @param channel The spi channel (0 or 1)
 * @return The file descriptor
 */
int spi_get_fd(int channel);

/**
 * Writes a single byte to the SPI bus. This is a half-duplex operation.
 *
 * @param channel
 * @param data
 * @return
 */
int spi_write_byte(int channel, uint8_t data);

/**
 * Half-Duplex write operation. Chip-select is deactivated in this operation and you need to manually set/unset it yourself.
 *
 * @param channel
 * @param buffer
 * @param len
 * @return Returns 0 for successfull operation otherwise negative if an error occurs
 *
 * @see <a href="https://www.kernel.org/doc/Documentation/spi/spidev">SPI kernel documentation</a>
 */
int spi_write(int channel, uint8_t *buffer, unsigned int len);

/**
 * Half-Duplex read operation. Chip-select is deactivated in this operation and you need to manually set/unset it yourself.
 *
 * @param channel
 * @param buffer
 * @param len
 *
 * @return Returns 0 for successfull operation otherwise negative if an error occurs
 *
 * @see <a href="https://www.kernel.org/doc/Documentation/spi/spidev">SPI kernel documentation</a>
 */
int spi_read(int channel, uint8_t *buffer, unsigned int len);

/**
 * Full Duplex read/write operation. Received data overrides
 *
 * @param channel
 * @param buffer
 * @param len
 * @return
 */
int spi_transfer(int channel, uint8_t *buffer, int len);

/**
 * Initialize SPI Device
 *
 * @param channel
 * @param speed
 * @param mode
 * @return
 */
int spi_setup(int channel, int speed, int mode = 0);

#ifdef __cplusplus
}
#endif


#endif //UCGDISP_SPI_H
