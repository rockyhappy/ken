package com.devrachit.ken

import android.app.Application
import android.content.Intent
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.widget.WidgetUpdateReceiver
import com.intuit.sdp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class KenApplication : Application() {

    @Inject
    lateinit var localRepository: LeetcodeLocalRepository
    
    @Inject
    lateinit var cachePolicy: CachePolicy

    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Clean expired cache on application start
//        CoroutineScope(Dispatchers.IO).launch {
//            val expiryTimeMillis = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)
//            localRepository.cleanExpiredCache(expiryTimeMillis)
//        }
        
        // Update all widgets when app starts
        updateAllWidgets()
    }
    
    override fun onTerminate() {
        updateAllWidgets()
        super.onTerminate()
    }
    private fun updateAllWidgets() {
        // Send broadcasts to update each widget type
        val updateIntent = Intent(this, WidgetUpdateReceiver::class.java).apply {
            action = "com.devrachit.ken.ACTION_UPDATE_WIDGET"
        }
        sendBroadcast(updateIntent)
    }
}