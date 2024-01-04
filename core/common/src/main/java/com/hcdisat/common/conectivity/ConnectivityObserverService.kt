package com.hcdisat.common.conectivity

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ConnectivityStatus { AVAILABLE, LOSING, LOST, UNAVAILABLE }

interface ConnectivityObserverService {
    fun observe(): Flow<ConnectivityStatus>
}

private class AppNetworkCallback(
    private val update: (ConnectivityStatus) -> Unit
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        update(ConnectivityStatus.AVAILABLE)
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        update(ConnectivityStatus.LOSING)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        update(ConnectivityStatus.LOST)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        update(ConnectivityStatus.UNAVAILABLE)
    }
}

class ConnectivityObserverServiceImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserverService {
    override fun observe(): Flow<ConnectivityStatus> = callbackFlow {
        val callback = AppNetworkCallback { launch { send(it) } }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}