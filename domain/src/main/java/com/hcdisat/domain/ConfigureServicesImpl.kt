package com.hcdisat.domain

import com.hcdisat.abstraction.networking.ConfigureServices
import com.hcdisat.dataaccess.ConfigureDataServices
import javax.inject.Inject

class ConfigureServicesImpl @Inject constructor(
    private val serviceConfigurator: ConfigureDataServices
) : ConfigureServices {
    override fun initializeServices() {
        serviceConfigurator.configure()
    }
}