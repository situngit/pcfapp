package com.situn.pcfapp.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>


    @Query("SELECT * FROM user WHERE id LIKE :id ")
    fun findUserWithId(id: Int): List<User>

    @Query("SELECT * FROM user WHERE id LIKE :id ")
    fun findUserWithIdUnique(id: Int): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun delete(user: User)
}