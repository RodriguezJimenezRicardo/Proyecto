package com.example.proyecto.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {
    private val KEY_USER_ID = longPreferencesKey("user_id")

    val userIdFlow: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    suspend fun setUserId(userId: Long) {
        context.dataStore.edit { it[KEY_USER_ID] = userId }
    }

    suspend fun clear() {
        context.dataStore.edit { it.remove(KEY_USER_ID) }
    }
}
