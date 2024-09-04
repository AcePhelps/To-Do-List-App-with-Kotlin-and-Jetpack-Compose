import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    // The `TaskViewModel` class extends `ViewModel` and takes a `TaskRepository` as a parameter.
    // The ViewModel's responsibility is to interact with the repository and provide data to the UI.
    // It survives configuration changes, such as screen rotations, ensuring data is not lost.

    val allTasks: LiveData<List<Task>> = repository.allTasks
    // `allTasks` is a LiveData object that holds a list of `Task` objects.
    // It is initialized with the LiveData from the repository's `allTasks` property.
    // The UI observes this LiveData to automatically update when the data changes.

    fun insert(task: Task) = viewModelScope.launch {
        // The `insert` function takes a `Task` object as a parameter.
        // It uses the `viewModelScope.launch` to launch a coroutine, ensuring that the database operation is performed asynchronously.
        repository.insert(task)
        // The function calls `insert` on the repository to add the task to the database.
    }

    fun delete(task: Task) = viewModelScope.launch {
        // The `delete` function takes a `Task` object as a parameter.
        // It launches a coroutine to delete the task from the database asynchronously.
        repository.delete(task)
        // The function calls `delete` on the repository to remove the task from the database.
    }

    fun update(task: Task) = viewModelScope.launch {
        // The `update` function takes a `Task` object as a parameter.
        // It launches a coroutine to update the task in the database asynchronously.
        repository.update(task)
        // The function calls `update` on the repository to modify the task in the database.
    }

    fun markDone(task: Task) = viewModelScope.launch {
        // The `markDone` function takes a `Task` object as a parameter.
        // It launches a coroutine to update the task's `isCompleted` status in the database asynchronously.
        repository.markDone(task.id, true)
        // The function calls `markDone` on the repository, passing the task's ID and setting `isCompleted` to `true`.
    }
}
