package com.outsourcing.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.outsourcing.data.dao.LocalJobDao
import com.outsourcing.data.entities.Job

@Database(entities = [Job::class], version = 1)
abstract class MainRoomDataBase: RoomDatabase() {
    abstract val localJobDao: LocalJobDao
}