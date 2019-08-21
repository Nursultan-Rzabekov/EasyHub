package com.example.kotlindemogithubproject.RoomDao

import androidx.room.*


@Dao
interface AuthUserDao{
    @Query("SELECT * FROM AUTH_USER WHERE language_id = :id")
    suspend fun getLanguageById(id: Int): AuthUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguage(authUser: AuthUser)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLanguage(authUser: AuthUser)


    @Query("DELETE FROM AUTH_USER WHERE language_id = :id")
    suspend fun deleteLanguageByID(id:Int)

}
