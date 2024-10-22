package com.betan.betanstoktakip.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private const val SHARED_PREF_NAME = "com.betan.betanstoktakip.prefs"

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return try {
            EncryptedSharedPreferences.create(
                app,
                SHARED_PREF_NAME,
                createMasterKey(app),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (_: Exception) {
            app.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE,
            )
        }
    }

    private fun createMasterKey(context: Context): MasterKey {
        val spec =
            KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            }.build()

        return MasterKey.Builder(context).apply {
            setKeyGenParameterSpec(spec)
        }.build()
    }
}
