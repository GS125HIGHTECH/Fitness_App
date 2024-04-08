package pl.gs.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Steps")
data class Steps(
    @PrimaryKey val username: String,
    val stepsCount: Int
)