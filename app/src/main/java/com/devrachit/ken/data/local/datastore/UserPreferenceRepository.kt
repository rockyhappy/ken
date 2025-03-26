package com.devrachit.ken.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreRepository(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

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
}