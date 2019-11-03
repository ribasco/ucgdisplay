/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgPigpioProvider.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDER_H
#define UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDER_H

#include <utility>
#include <iostream>
#include <UcgIOProvider.h>

class PigpioProviderException : public std::runtime_error {
public:
    explicit PigpioProviderException(const std::string &arg) : runtime_error(arg) {}

    explicit PigpioProviderException(const char *string) : runtime_error(string) {}

    explicit PigpioProviderException(const runtime_error &error) : runtime_error(error) {}
};

class PigpioInitException : public PigpioProviderException {
public:
    explicit PigpioInitException(const std::string &msg) : PigpioProviderException(msg) {}
};

class UcgPigpioProvider : public UcgIOProvider {
public:
    explicit UcgPigpioProvider(const std::shared_ptr<u8g2_info_t> &info);

    ~UcgPigpioProvider() override;

    int getHandle() const;

    int getType();

private:
    int m_Handle;
    int m_Type;
};


#endif //UCGD_MOD_GRAPHICS_UCGPIGPIOPROVIDER_H
