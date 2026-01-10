package com.vaspit.simplesmsforwarder.forwarding.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vaspit.simplesmsforwarder.forwarding.data.model.SmsEntity

@Dao
interface SmsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(entity: SmsEntity): Long

    @Query("SELECT * FROM sms WHERE isSent = 0 ORDER BY timestamp ASC LIMIT :limit")
    suspend fun getUnsent(limit: Int = 10): List<SmsEntity>

    @Query("UPDATE sms SET isSent = 1 WHERE id = :id")
    suspend fun markSent(id: Long)
}
