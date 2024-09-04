package com.example.todolist.data

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {
    // This is a class named `TaskRepository` that takes a `TaskDao` object as a constructor parameter.
    // The `TaskDao` is the Data Access Object that provides methods to interact with the database.
    // The repository's role is to manage the data operations and provide a clean API for the rest of the app to interact with the data.

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()
    // `allTasks` is a LiveData object that holds a list of `Task` objects.
    // It is initialized by calling `getAllTasks()` from the `TaskDao`.
    // This LiveData is observed by the UI to automatically update when the data changes.

    suspend fun insert(task: Task) {
        // This is a suspend function named `insert` that takes a `Task` object as a parameter.
        // The function is marked as `suspend`, meaning it must be called from a coroutine or another suspend function.
        // The purpose of this function is to insert a new task into the database.
        taskDao.insert(task)
        // The actual insertion of the task is delegated to the `insert` method of `TaskDao`.
    }

    suspend fun delete(task: Task) {
        // This is a suspend function named `delete` that takes a `Task` object as a parameter.
        // It deletes the specified task from the database.
        taskDao.delete(task)
        // The actual deletion of the task is delegated to the `delete` method of `TaskDao`.
    }

    suspend fun update(task: Task) {
        // This is a suspend function named `update` that takes a `Task` object as a parameter.
        // It updates the specified task in the database.
        taskDao.update(task)
        // The actual update of the task is delegated to the `update` method of `TaskDao`.
    }

    suspend fun markDone(taskId: Int, isCompleted: Boolean) {
        // This is a suspend function named `markDone` that takes a task ID and a Boolean `isCompleted` as parameters.
        // It updates the completion status of a task identified by the task ID in the database.
        taskDao.markDone(taskId, isCompleted)
        // The actual marking of the task as done is delegated to the `markDone` method of `TaskDao`.
    }
}

