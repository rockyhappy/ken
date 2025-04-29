package com.devrachit.ken.data.local.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.local.dao.LeetCodeUserProfileCalenderDao
import com.devrachit.ken.data.local.entity.LeetCodeConverters
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity

@Database(
    entities = [
        LeetCodeUserEntity::class,
        UserQuestionStatusEntity::class,
        UserProfileCalenderEntity::class
               ],
    version = 3,
    exportSchema = false
)
@TypeConverters(LeetCodeConverters::class)
abstract class KenDatabase : RoomDatabase() {

    abstract fun leetCodeUserDao(): LeetCodeUserDao
    abstract fun leetcodeUserProfileCalenderDao(): LeetCodeUserProfileCalenderDao

    companion object {
        @Volatile
        private var INSTANCE: KenDatabase? = null

        fun getDatabase(context: Context): KenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KenDatabase::class.java,
                    "ken_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}