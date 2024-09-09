package com.example.todolist

import TaskViewModel
import TaskViewModelFactory
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDatabase
import com.example.todolist.data.TaskRepository
import com.example.todolist.ui.theme.ToDoListTheme

class MainActivity : ComponentActivity() {
    // Initialize the TaskViewModel using a ViewModelFactory
    private val taskViewModel: TaskViewModel by viewModels {
        val repository = TaskRepository(TaskDatabase.getDatabase(this).taskDao())
        TaskViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                // Set the content of the activity to the Composable function
                ToDoListScreen(viewModel = taskViewModel)
            }
        }
    }
}


@Composable
fun ToDoListScreen(viewModel: TaskViewModel) {
    var taskName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var currentFilter by remember { mutableStateOf("Show All") }
    val allTasks by viewModel.allTasks.observeAsState(initial = emptyList())
    val filteredTasks = when (currentFilter) {
        "Show All" -> allTasks
        "Show Only Done" -> allTasks.filter { it.isCompleted }
        "Exclude Done" -> allTasks.filter { !it.isCompleted }
        else -> allTasks
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = taskName,
                    onValueChange = {
                        taskName = it
                        showError = taskName.length <= 1
                    },
                    label = { Text("New Task") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (showError) {
                    Text(
                        text = "Task name should be more than 1 symbol",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)  // Space between TextField and error message
                    )
                } else {
                    Spacer(modifier = Modifier.height(20.dp))  // Reserve space when there's no error
                }
            }
            FilterMenu(onFilterSelected = { filter ->
                currentFilter = filter
            })
        }

        Button(
            onClick = {
                if (taskName.length > 1) {
                    viewModel.insert(Task(title = taskName))
                    taskName = ""
                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !showError
        ) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredTasks) { task ->
                TaskItem(
                    task = task,
                    onTaskAction = { action ->
                        when (action) {
                            is TaskAction.Update -> viewModel.update(task.copy(title = action.updatedName))
                            TaskAction.Delete -> viewModel.delete(task)
                            TaskAction.MarkDone -> viewModel.markDone(task)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FilterMenu(onFilterSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Filter",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp)
                .testTag("filterButton")
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Show All") },
                onClick = {
                    onFilterSelected("Show All")
                    expanded = false
                },
                modifier = Modifier.testTag("showAllFilter")

            )
            DropdownMenuItem(
                text = { Text("Show Only Done") },
                onClick = {
                    onFilterSelected("Show Only Done")
                    expanded = false
                },
                modifier = Modifier.testTag("showOnlyDoneFilter")
            )
            DropdownMenuItem(
                text = { Text("Exclude Done") },
                onClick = {
                    onFilterSelected("Exclude Done")
                    expanded = false
                },
                modifier = Modifier.testTag("excludeDoneFilter")
            )
        }
    }
}



@Composable
fun TaskItem(task: Task, onTaskAction: (TaskAction) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var updatedTaskName by remember { mutableStateOf(TextFieldValue(task.title)) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Task Options") },
            text = {
                Column {
                    Button(onClick = {
                        showDialog = false
                        onTaskAction(TaskAction.MarkDone)

                    }) {
                        Text("Mark as Done")
                    }
                    Button(onClick = {
                        showDialog = false
                        onTaskAction(TaskAction.Delete)
                    }) {
                        Text("Delete Task")
                    }
                    TextField(
                        value = updatedTaskName,
                        onValueChange = {
                            updatedTaskName = it
                                        showError = it.text.length<=1
                                        },
                        label = { Text("Update Task Name") }
                    )
                    if (showError){
                        Text(text = "Task name should be more than 1 symbol",
                        color = Color.Red
                        )
                    }
                    Button(
                        onClick = {
                            if(!showError){
                                showDialog = false
                                onTaskAction(TaskAction.Update(updatedName = updatedTaskName.text))
                            }

                    },
                        enabled = !showError
                    ) {
                        Text("Update Task")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    val taskColor = if (task.isCompleted) Color.Green else Color.White
    val semanticsModifier = Modifier.semantics {
        contentDescription = if (task.isCompleted) "Task Done" else "Task Not Done"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(taskColor)
            .then(semanticsModifier)
            .clickable { showDialog = true }
            .padding(16.dp)
    ) {
        Text(task.title, modifier = Modifier.weight(1f))
    }
}

@Composable
fun InputWithValidation() {
    var input by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = input,
            onValueChange = {
                input = it
                showError = input.length <= 1
            },
            label = { Text("Enter something") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (showError) {
            Text(
                text = "Input should be more than 1 symbol",
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Do something */ }) {
            Text("Submit")
        }
    }
}

@Composable
fun PreviewInputWithValidation() {
    InputWithValidation()
}



// Define the actions for the task
sealed class TaskAction {
    object Delete : TaskAction()
    object MarkDone : TaskAction()
    data class Update(val updatedName: String) : TaskAction()
}
