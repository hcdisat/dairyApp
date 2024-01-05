package com.hcdisat.abstraction.networking

interface ConfigureServices {
    fun initializeServices()
    fun configureRealm()
    fun initFirebase()
}