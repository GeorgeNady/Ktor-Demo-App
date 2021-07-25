package com.george.ktorapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities = [],
//    version = 1
//)
////@TypeConverters(Converters::class)
//abstract class MyDatabase : RoomDatabase() {
//
//    abstract fun getArticleDao() : Dao
//
//    companion object {
//        @Volatile
//        private var instance: MyDatabase? = null
//        private val LOCK =  Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also {
//                instance = it
//            }
//        }
//
//        fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                MyDatabase::class.java,
//                "ktor_db.db"
//            ).build()
//    }
//}