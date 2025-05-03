package com.devrachit.ken.data.local.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

// Define DataStore at the package level to ensure there's only one instance
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class DataStoreRepository(private val context: Context) {

    // This companion object provides a thread-safe way of creating and accessing
    // a singleton instance of the DataStoreRepository. The INSTANCE variable is marked
    // with '@Volatile' to ensure changes made by one thread are immediately visible to
    // others. The 'getInstance' function uses double-checked locking with synchronization
    // to prevent multiple instances from being created in multithreaded environments,
    // thus ensuring only a single instance of DataStoreRepository exists throughout
    // the application's lifecycle.
    companion object {

        @Volatile
        private var INSTANCE: DataStoreRepository? = null

        fun getInstance(context: Context): DataStoreRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
    private val PRIMARY_USERNAME_KEY = stringPreferencesKey("primary_username")

    val primaryUsername: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PRIMARY_USERNAME_KEY]
        }

    suspend fun savePrimaryUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[PRIMARY_USERNAME_KEY] = username
        }
    }

    suspend fun clearPrimaryUsername() {
        context.dataStore.edit { preferences ->
            preferences.remove(PRIMARY_USERNAME_KEY)
        }
    }

    suspend fun readPrimaryUsername(): String? {
        return context.dataStore.data
            .map { preferences ->
                preferences[PRIMARY_USERNAME_KEY]
            }.firstOrNull()
    }
    


    private val Primary_Time_Key= stringPreferencesKey("primary_time_key")

    val primaryTime: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[Primary_Time_Key]
        }

    suspend fun savePrimaryTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[Primary_Time_Key] = time
        }
    }

    suspend fun clearPrimaryTime() {
        context.dataStore.edit { preferences ->
            preferences.remove(Primary_Time_Key)
        }
    }

    suspend fun readPrimaryTime(): String? {
        return context.dataStore.data
            .map { preferences ->
                preferences[Primary_Time_Key]
            }.firstOrNull()
    }

}