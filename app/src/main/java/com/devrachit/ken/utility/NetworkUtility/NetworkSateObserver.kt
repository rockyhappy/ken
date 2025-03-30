package com.devrachit.ken.utility.NetworkUtility


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkStateObserver(private val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkState = MutableLiveData<Boolean>()

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkState.postValue(true)
            }

            override fun onLost(network: Network) {
                networkState.postValue(false)
            }
        })

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val activeNetwork = connectivityManager.activeNetworkInfo
                networkState.postValue(activeNetwork?.isConnected == true)
            }
        }, intentFilter)
    }

    fun getNetworkState(): LiveData<Boolean> = networkState

    fun checkNetworkState() {
        val activeNetwork = connectivityManager.activeNetworkInfo
        networkState.postValue(activeNetwork?.isConnected == true)
    }
}