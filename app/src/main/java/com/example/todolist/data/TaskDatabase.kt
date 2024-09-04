package com.example.todolist.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Task::class], version = 1, exportSchema = false)
// This annotation marks the class as a Room database. It specifies the entities (tables) associated with the database, the database version, and whether to export the database schema for version control.
abstract class TaskDatabase : RoomDatabase() {
// This is an abstract class named `TaskDatabase` that extends `RoomDatabase`. `RoomDatabase` is a base class for Room's database implementation. The abstract class provides the connection to the SQLite database and is the main entry point for database interactions.

    abstract fun taskDao(): TaskDao
    // This abstract function provides an instance of the `TaskDao` interface. Room will generate the implementation for this DAO. It's used to access the methods defined in `TaskDao` to interact with the database.

    companion object {
        // The `companion object` is a singleton object associated with the `TaskDatabase` class. It's used to ensure that there is only one instance of the database throughout the application.

        @Volatile
        private var INSTANCE: TaskDatabase? = null
        // `@Volatile` ensures that changes to `INSTANCE` are visible to all threads immediately. `INSTANCE` holds the single instance of `TaskDatabase`. It is initially set to `null` until the database is created.

        fun getDatabase(context: Context): TaskDatabase {
            // This function returns the single instance of `TaskDatabase`. It takes a `Context` as a parameter, which is necessary for creating the database.

            return INSTANCE ?: synchronized(this) {
                // The `synchronized` block ensures that the database instance is created only once, even if multiple threads try to access it simultaneously.

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                // `Room.databaseBuilder` is a method that creates the Room database.
                // It takes three parameters: the application context, the `TaskDatabase` class, and the name of the database ("task_database").
                // `.build()` finalizes the creation of the database instance.

                INSTANCE = instance
                // The newly created database instance is assigned to `INSTANCE`.

                instance
                // The instance is returned by the function.
            }
        }
    }
}


