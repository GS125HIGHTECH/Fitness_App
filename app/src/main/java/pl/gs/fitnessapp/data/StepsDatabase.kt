package pl.gs.fitnessapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Steps::class], version = 1, exportSchema = false)

abstract class StepsDatabase : RoomDatabase() {
    abstract val dao: StepsDao
}