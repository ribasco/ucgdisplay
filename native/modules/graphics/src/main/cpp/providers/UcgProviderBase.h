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
#include <ServiceLocator.h>

class UcgProviderBase {
public:
    explicit UcgProviderBase(UcgIOProvider *provider) : m_Provider(provider), log(ServiceLocator::getInstance().getLogger()) {}

    virtual ~UcgProviderBase();

    virtual UcgIOProvider *getProvider();

    virtual void open(const std::shared_ptr<ucgd_t> &context) {};

    virtual void close(const std::shared_ptr<ucgd_t> &context) {};

    auto getDevices() -> const std::vector<std::shared_ptr<ucgd_t>> &;

    auto registerDevice(const std::shared_ptr<ucgd_t> &context) -> void;

    auto close() -> void;

protected:
    Log log;

private:
    UcgIOProvider *m_Provider;

    std::vector<std::shared_ptr<ucgd_t>> m_OpenDevices;
};

#endif
