package com.devrachit.ken.data.local.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    private val CONTEST_RATING_HISTOGRAM_KEY = stringPreferencesKey("contest_rating_histogram")
    private val json = Json { ignoreUnknownKeys = true }

    val contestRatingHistogram: Flow<ContestRatingHistogramResponse?> = context.dataStore.data
        .map { preferences ->
            preferences[CONTEST_RATING_HISTOGRAM_KEY]?.let {
                json.decodeFromString<ContestRatingHistogramResponse>(it)
            }
        }

    suspend fun saveContestRatingHistogram(histogramResponse: ContestRatingHistogramResponse) {
        context.dataStore.edit { preferences ->
            preferences[CONTEST_RATING_HISTOGRAM_KEY] = json.encodeToString(histogramResponse)
        }
    }

    suspend fun clearContestRatingHistogram() {
        context.dataStore.edit { preferences ->
            preferences.remove(CONTEST_RATING_HISTOGRAM_KEY)
        }
    }

    suspend fun readContestRatingHistogram(): ContestRatingHistogramResponse? {
        return context.dataStore.data
            .map { preferences ->
                preferences[CONTEST_RATING_HISTOGRAM_KEY]?.let {
                    json.decodeFromString<ContestRatingHistogramResponse>(it)
                }
            }.firstOrNull()
    }

}