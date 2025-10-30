package com.example.weaterapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database abstract class for weather data.
 * @Database annotation defines entities and database version.
 * Room generates implementation at compile time.
 */
@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    /** Provides access to WeatherDao for database operations */
    abstract fun weatherDao(): WeatherDao

    companion object {
        /**
         * Volatile instance variable ensures visibility across threads.
         * Singleton pattern with double-check locking for thread safety.
         */
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        /**
         * Returns singleton database instance.
         * Creates database on first access using Room.databaseBuilder.
         * Thread-safe: uses synchronized block to prevent multiple instances.
         * 
         * @param context Application context (uses applicationContext to avoid leaks)
         * @return WeatherDatabase singleton instance
         */
        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

