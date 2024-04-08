package pl.gs.fitnessapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepsDao {

    @Query("SELECT * FROM Steps WHERE username = :username")
    fun getSteps(username: String): Steps?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSteps(steps: Steps)

    @Update
    suspend fun updateSteps(steps: Steps)

    @Query("DELETE FROM steps WHERE username = :username")
    suspend fun deleteSteps(username: String)
}