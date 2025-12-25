package com.outsourcing.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.outsourcing.domain.entities.Job

@Database(entities = [Job::class], version = 1)
class MainRoomDataBase {

}