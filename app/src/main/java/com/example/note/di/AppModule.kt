package com.example.note.di

import android.content.Context
import android.content.SharedPreferences
import com.example.note.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun providesSharedPref(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
    }
}