package com.devrachit.ken.data.remote.firebase

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRemoteConfigManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(36)
                .setFetchTimeoutInSeconds(10L)
                .build()
            setConfigSettingsAsync(configSettings)

            val defaults = mapOf(
                SHOW_DEVELOPER_MESSAGE to true,
                FORCE_PLAYSTORE_UPDATE to false,
                MINIMUM_REQUIRED_VERSION to getCurrentAppVersion(),
                LATEST_VERSION to getCurrentAppVersion(),
                PLAYSTORE_UPDATE_MESSAGE to "A new version is available. Please update to continue using Ken.",
                PLAYSTORE_UPDATE_URL to "https://play.google.com/store/apps/details?id=com.devrachit.ken"
            )
            setDefaultsAsync(defaults)
        }
    }

    /**
     * Get boolean value from Firebase Remote Config
     * @param key the remote config key
     * @param defaultValue default value if key doesn't exist
     * @return boolean value from remote config
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            remoteConfig.getBoolean(key)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * Get string value from Firebase Remote Config
     * @param key the remote config key
     * @param defaultValue default value if key doesn't exist
     * @return string value from remote config
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return try {
            remoteConfig.getString(key)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * Fetch remote config values
     */
    fun fetchConfig(): Boolean {
        return try {
            val result = remoteConfig.fetchAndActivate()
            result.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get current app version
     */
    fun getCurrentAppVersion(): String {
        return try {
            context.packageManager
                .getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        } catch (e: PackageManager.NameNotFoundException) {
            "1.0.0"
        }
    }

    /**
     * Check if app update is required based on remote config
     * @return true if update is required (force update - no later option)
     */
    fun isUpdateRequired(): Boolean {
        val currentVersion = getCurrentAppVersion()
        val minimumVersion = getString(MINIMUM_REQUIRED_VERSION, currentVersion)
        return isVersionLower(currentVersion, minimumVersion)
    }

    /**
     * Check if optional update should be shown
     * @return true if optional update should be shown (with "later" option)
     */
    fun shouldShowOptionalUpdate(): Boolean {
        val currentVersion = getCurrentAppVersion()
        val minimumVersion = getString(MINIMUM_REQUIRED_VERSION, currentVersion)
        val latestVersion = getLatestVersion()
        val forceUpdate = getBoolean(FORCE_PLAYSTORE_UPDATE, false)
        if (isVersionLower(currentVersion, minimumVersion)) {
            return false
        }
        if (compareVersions(currentVersion, latestVersion) >= 0) {
            return false
        }
        return forceUpdate
    }

    /**
     * Check if any update dialog should be shown at all
     * @return true if either forced or optional update should be shown
     */
    fun shouldShowAnyUpdate(): Boolean {
        return isUpdateRequired() || shouldShowOptionalUpdate()
    }

    /**
     * Get update type for UI decisions
     * @return "NONE", "FORCED", or "OPTIONAL"
     */
    fun getUpdateType(): String {
        val currentVersion = getCurrentAppVersion()
        val minimumVersion = getString(MINIMUM_REQUIRED_VERSION, currentVersion)
        val latestVersion = getLatestVersion()
        val forceUpdate = getBoolean(FORCE_PLAYSTORE_UPDATE, false)
        if (isVersionLower(currentVersion, minimumVersion)) {
            return "FORCED"
        }
        if (compareVersions(currentVersion, latestVersion) >= 0) {
            return "NONE"
        }
        return if (forceUpdate) "OPTIONAL" else "NONE"
    }

    /**
     * Get the latest available version from remote config
     */
    fun getLatestVersion(): String {
        return getString(LATEST_VERSION, getCurrentAppVersion())
    }

    /**
     * Get update message from remote config
     */
    fun getUpdateMessage(): String {
        return getString(PLAYSTORE_UPDATE_MESSAGE, "A new version is available. Please update to continue using Ken.")
    }

    /**
     * Get Play Store URL from remote config
     */
    fun getPlayStoreUrl(): String {
        return getString(PLAYSTORE_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.devrachit.ken")
    }

    /**
     * Compare two version strings
     * @param current current version (e.g., "1.2.3")
     * @param other other version (e.g., "1.3.0")
     * @return negative if current < other, 0 if equal, positive if current > other
     */
    private fun compareVersions(current: String, other: String): Int {
        return try {
            val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
            val otherParts = other.split(".").map { it.toIntOrNull() ?: 0 }

            val maxLength = maxOf(currentParts.size, otherParts.size)

            for (i in 0 until maxLength) {
                val currentPart = currentParts.getOrNull(i) ?: 0
                val otherPart = otherParts.getOrNull(i) ?: 0

                when {
                    currentPart < otherPart -> return -1
                    currentPart > otherPart -> return 1
                }
            }
            0
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Compare two version strings
     * @param current current version (e.g., "1.2.3")
     * @param minimum minimum required version (e.g., "1.3.0")
     * @return true if current version is lower than minimum
     */
    private fun isVersionLower(current: String, minimum: String): Boolean {
        return compareVersions(current, minimum) < 0
    }

    companion object {
        const val SHOW_DEVELOPER_MESSAGE = "show_developer_message"
        const val FORCE_PLAYSTORE_UPDATE = "force_playstore_update"
        const val MINIMUM_REQUIRED_VERSION = "minimum_required_version"
        const val LATEST_VERSION = "latest_version"
        const val PLAYSTORE_UPDATE_MESSAGE = "playstore_update_message"
        const val PLAYSTORE_UPDATE_URL = "playstore_update_url"
    }
}
