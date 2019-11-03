/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgCperSpiProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGCPERSPIPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGCPERSPIPROVIDER_H

#include <UcgSpiProvider.h>
#include <UcgCperipheryProvider.h>
#include <spi.h>

#define DEFAULT_SPI_SPEED 1000000

class UcgCperSpiProvider : public UcgSpiProvider {
public:
    explicit UcgCperSpiProvider(UcgIOProvider *provider);

    ~UcgCperSpiProvider() override;

    void open() override;

    int write(uint8_t *buffer, int count) override;

    void close() override;

    UcgCperipheryProvider *getProvider() override;

private:
    std::unique_ptr<cp_spi_t>  m_SPI;

    void _close();
};

#endif //UCGD_MOD_GRAPHICS_UCGCPERSPIPROVIDER_H
