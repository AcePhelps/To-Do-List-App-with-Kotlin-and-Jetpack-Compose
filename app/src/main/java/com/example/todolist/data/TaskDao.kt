package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao // This annotation marks the interface as a Data Access Object (DAO). It is used by Room to define methods that interact with the database.
interface TaskDao { // This is an interface named `TaskDao`. It defines methods for accessing and manipulating data in the "tasks" table.

    @Insert(onConflict = OnConflictStrategy.REPLACE) // This annotation marks the `insert` function as an insert operation. The `onConflict = OnConflictStrategy.REPLACE` argument tells Room to replace the existing row if there's a conflict (e.g., a row with the same primary key).
    suspend fun insert(task: Task) // This is a suspend function named `insert`. It inserts a `Task` object into the database. The `suspend` keyword means this function is designed to be called within a coroutine, making it non-blocking.

    @Query("SELECT * FROM tasks") // This annotation marks the `getAllTasks` function as a query operation. The query retrieves all rows from the "tasks" table.
    fun getAllTasks(): LiveData<List<Task>> // This function returns a `LiveData` object containing a list of `Task` objects. `LiveData` is an observable data holder, meaning the UI will automatically update when the data changes.

    @Delete // This annotation marks the `delete` function as a delete operation. It tells Room to delete the specified `Task` object from the database.
    suspend fun delete(task: Task) // This is a suspend function named `delete`. It deletes a specific `Task` object from the database. Like `insert`, this function is non-blocking and designed to be called within a coroutine.

    @Update // This annotation marks the `update` function as an update operation. It tells Room to update the specified `Task` object in the database.
    suspend fun update(task: Task) // This is a suspend function named `update`. It updates the values of an existing `Task` object in the database. This function is also non-blocking.

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId") // This annotation marks the `markDone` function as a query operation. It updates the `isCompleted` field of a specific task identified by `taskId`.
    suspend fun markDone(taskId: Int, isCompleted: Boolean) // This is a suspend function named `markDone`. It updates the `isCompleted` status of a task with the given `taskId` in the database. The function is non-blocking.
}
