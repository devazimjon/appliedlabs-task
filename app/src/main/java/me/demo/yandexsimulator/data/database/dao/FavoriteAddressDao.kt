package me.demo.yandexsimulator.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.demo.yandexsimulator.data.database.model.FavoriteAddressEntity

@Dao
interface FavoriteAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookPage: FavoriteAddressEntity)

    @Delete
    suspend fun delete(bookPage: FavoriteAddressEntity)

    @Query("SELECT * FROM favorite_addresses")
    fun getAllAddresses(): Flow<List<FavoriteAddressEntity>>

    @Query("SELECT * FROM favorite_addresses")
    suspend fun getLast3Addresses(): List<FavoriteAddressEntity>
}