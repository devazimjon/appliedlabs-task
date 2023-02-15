package me.demo.yandexsimulator.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.demo.yandexsimulator.data.database.AppDatabase.Companion.DATABASE_VERSION
import me.demo.yandexsimulator.data.database.dao.FavoriteAddressDao
import me.demo.yandexsimulator.data.database.model.FavoriteAddressEntity

@Database(
    entities = [
        FavoriteAddressEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val favoriteAddressDao: FavoriteAddressDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME: String = "app_database"
        const val DATABASE_VERSION: Int = 2
    }
}