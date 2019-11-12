/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: ProviderManager.h
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
#ifndef UCGD_MOD_GRAPHICS_PROVIDERMANAGER_H
#define UCGD_MOD_GRAPHICS_PROVIDERMANAGER_H

#include <memory>
#include <map>

//Forward declarations
class UcgIOProvider;

struct ucgd_t;

class Log;

class ProviderNotFoundException : public std::runtime_error {
public:
    explicit ProviderNotFoundException(const std::string &arg) : runtime_error(arg) {};

    explicit ProviderNotFoundException(const char *string) : runtime_error(string) {}

    explicit ProviderNotFoundException(const runtime_error &error) : runtime_error(error) {}
};

class ProviderManager {
public:
    explicit ProviderManager();

    ~ProviderManager();

    auto registerProvider(std::unique_ptr<UcgIOProvider> provider, bool system = false) -> bool;

    auto getProvider(const std::string &name) -> std::shared_ptr<UcgIOProvider> &;

    auto isRegistered(const std::string &name) -> bool;

    auto getAllProviders() -> const std::map<std::string, std::shared_ptr<UcgIOProvider>>&;

    auto release() -> void;

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
    auto initializeProvider(const std::string& name, const std::shared_ptr<ucgd_t>& context = nullptr) -> void;

    auto getProvider(const std::shared_ptr<ucgd_t>& context) -> std::shared_ptr<UcgIOProvider>&;

    auto isInstalled(const std::string& providerName) -> bool;

    auto isInstalled(std::shared_ptr<UcgIOProvider>&) -> bool;
#endif

private:
    std::map<std::string, std::shared_ptr<UcgIOProvider>> m_Providers;
    Log &log;
};

#endif //UCGD_MOD_GRAPHICS_PROVIDERMANAGER_H
