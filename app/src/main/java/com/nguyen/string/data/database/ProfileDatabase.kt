package com.nguyen.string.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nguyen.string.util.DataConverter

@Database(entities = [Profile::class, ProfilePosts::class, ProfileItineraries::class, ProfileSavedPosts::class], version = 3, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao


    companion object{

        @Volatile
        private var instance: ProfileDatabase? = null

        fun getDatabase(context: Context): ProfileDatabase? {
            if (instance == null) {
                synchronized(ProfileDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ProfileDatabase::class.java, "profile_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance
        }

    }

}