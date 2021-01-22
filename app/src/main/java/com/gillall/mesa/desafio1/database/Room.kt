package com.gillall.mesa.desafio1.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao {

    @Query("SELECT COUNT(*) FROM DatabaseNews")
    fun getCount(): Int

    @Query("select * from DatabaseNews")
    fun getAllNews(): LiveData<List<DatabaseNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( news: List<DatabaseNews>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll( news: List<DatabaseNewsUpdate>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSingle( news: DatabaseNews)
}

@Database(entities = [DatabaseNews::class, DatabaseNewsUpdate::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsDao: NewsDao
}

private lateinit var INSTANCE: NewsDatabase

fun getDatabase(context: Context): NewsDatabase {
    synchronized(NewsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    NewsDatabase::class.java,
                    "news").build()
        }
    }
    return INSTANCE
}