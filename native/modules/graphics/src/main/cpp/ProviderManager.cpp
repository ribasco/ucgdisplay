/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: ProviderManager.cpp
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
#include "ProviderManager.h"

#include <UcgIOProvider.h>
#include <Utils.h>

ProviderManager::ProviderManager() : log(ServiceLocator::getInstance().getLogger()) {};

ProviderManager::~ProviderManager() = default;

auto ProviderManager::registerProvider(std::unique_ptr<UcgIOProvider> provider, bool system) -> bool {
    if (provider == nullptr)
        return false;
    std::string &name = provider->getName();
    if (name.empty()) {
        log.error("{}() : Unable to register provider. The provided name is empty", std::string(__func__));
        throw std::runtime_error(std::string(__func__) + std::string("(): Provider name cannot be empty"));
    }
    if (isRegistered(name)) {
        log.warn("Provider {} has already been registered. Skipping.", name);
        return false;
    }
    auto it = m_Providers.insert(std::make_pair(provider->getName(), std::move(provider)));
    auto& prov = it.first->second;
    if (prov->isProvided() || isInstalled(prov)) {
        prov->markAvailable();
    } else {
        prov->markUnavailable();
    }
    log.debug("{}() : Registered provider '{}' (Installed: {})", std::string(__func__), name, prov->isAvailable() ? "yes" : "no");
    return it.second;
}

auto ProviderManager::isRegistered(const std::string &name) -> bool {
    try {
        std::shared_ptr<UcgIOProvider> &a = getProvider(name);
        return a->getName() == name;
    } catch (ProviderNotFoundException &e) {
        return false;
    }
}

auto ProviderManager::getProvider(const std::string &name) -> std::shared_ptr<UcgIOProvider> & {
    auto it = this->m_Providers.find(name);
    if (it != m_Providers.end())
        return it->second;
    throw ProviderNotFoundException("Provider " + name + " not found");
}

auto ProviderManager::getAllProviders() -> const std::map<std::string, std::shared_ptr<UcgIOProvider>> & {
    return m_Providers;
}

auto ProviderManager::release() -> void {
    //Close all initialized providers
    for (auto const& [key, val] : m_Providers) {
        if (val->isInitialized()) {
            log.debug("release() : Releasing all provider's resources: {}", std::string(val->getName()));
            val->close();
        }
    }
}

#if (defined(__arm__) || defined(__aarch64__)) && defined(__linux__)
auto ProviderManager::getProvider(const std::shared_ptr<ucgd_t> &context) -> std::shared_ptr<UcgIOProvider> & {
    std::string defaultProvider = context->getOptionString(OPT_PROVIDER);
    if (defaultProvider.empty() || !isInstalled(defaultProvider)) {
        log.warn("get_default_provider() : Provider not specified or is not install on your system. Falling back to default system provider '{}'", PROVIDER_DEFAULT);
        defaultProvider = PROVIDER_DEFAULT;
    }
    return getProvider(defaultProvider);
}

auto ProviderManager::isInstalled(std::shared_ptr<UcgIOProvider> &provider) -> bool {
    return isInstalled(provider->getName());
}

auto ProviderManager::isInstalled(const std::string &providerName) -> bool {
    if (providerName.empty()) {
        log.warn("isInstalled() : Provider name is empty");
        return false;
    }
    try {
        std::shared_ptr<UcgIOProvider> & p = getProvider(providerName);
        if (p->isProvided())
            return true;
        if (p->getLibraryName().empty()) {
            log.warn("isInstalled() : Missing library name for provider '{}'", p->getName());
            return false;
        }
        return Utils::isLibraryLoaded(p->getLibraryName());
    } catch (ProviderNotFoundException& e) {
        return false;
    }
}

auto ProviderManager::initializeProvider(const std::string &name, const std::shared_ptr<ucgd_t>& context) -> void {
    auto provider = getProvider(name);
    if (!provider->isInitialized()) {
        log.debug("Initializing provider '{}'", name);
        provider->initialize(context);
    } else {
        log.warn("Provider '{}' has already been initialized", name);
    }
}

#endif

