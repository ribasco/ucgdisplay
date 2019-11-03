/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UcgProviderBase.h
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
#ifndef UCGD_MOD_GRAPHICS_UCGPROVIDERBASE_H
#define UCGD_MOD_GRAPHICS_UCGPROVIDERBASE_H

#include <memory>
#include <utility>
#include <UcgIOProvider.h>

class UcgProviderBase {
public:
    explicit UcgProviderBase(UcgIOProvider *provider) : m_Provider(provider) {}

    virtual ~UcgProviderBase() = default;

    virtual UcgIOProvider *getProvider() {
        return m_Provider;
    }

protected:
    int getOptionValueInt(std::string key, int defaultValue = 0) {
        return this->getProvider()->getInfo()->getOptionInt(key, defaultValue);
    }

    std::string getOptionValueString(const std::string &key, std::string defaultValue = "") {
        return this->getProvider()->getInfo()->getOptionString(key, defaultValue);
    }

private:
    UcgIOProvider *m_Provider;
};

#endif
