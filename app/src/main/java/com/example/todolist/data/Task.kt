package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks") // This annotation marks this class as an Entity, representing a table in the Room database. The table will be named "tasks".
data class Task(              // This is a Kotlin data class named `Task`. Each instance of this class represents a row in the "tasks" table.
    @PrimaryKey(autoGenerate = true) // This annotation marks the `id` property as the primary key of the table. The `autoGenerate = true` argument tells Room to automatically generate a unique ID for each new row.
    val id: Int = 0,                // This is the `id` property, which is an integer and acts as the unique identifier for each task. It defaults to 0, but in practice, Room will generate and assign a unique value when a new Task is inserted.

    val title: String,              // This is the `title` property, which holds the name or description of the task. It is of type `String` and must be provided when creating a new `Task` object.

    val isCompleted: Boolean = false // This is the `isCompleted` property, which is a Boolean indicating whether the task has been completed or not. It defaults to `false`, meaning that tasks are considered incomplete by default.
)

