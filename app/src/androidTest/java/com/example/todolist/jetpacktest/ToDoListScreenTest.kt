package com.example.jetpacktest

import TaskViewModel
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todolist.ToDoListScreen
import com.example.todolist.data.TaskDao
import com.example.todolist.data.TaskDatabase
import com.example.todolist.data.TaskRepository
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration

@RunWith(AndroidJUnit4::class)
class ToDoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var taskDao: TaskDao
    private lateinit var taskDatabase: TaskDatabase
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskViewModel: TaskViewModel

    @Before
    fun setup() {
        // Get the application context
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Create an in-memory Room database
        taskDatabase = Room.inMemoryDatabaseBuilder(
            context, TaskDatabase::class.java
        ).allowMainThreadQueries().build()

        // Get the TaskDao from the database
        taskDao = taskDatabase.taskDao()

        // Create the repository and ViewModel with the real TaskDao
        taskRepository = TaskRepository(taskDao)
        taskViewModel = TaskViewModel(taskRepository)
    }

    @After
    fun tearDown() {
        // Close the database after tests to prevent memory leaks
        taskDatabase.close()
    }

    @Test
    fun testAddTask() {
        // Set the Composable content with the real ViewModel
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }
        composeTestRule.waitForIdle()

        // Perform UI interactions and assertions
        composeTestRule.onNodeWithText("New Task").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.onNodeWithText("Buy groceries").assertExists()  // Check if task was added
    }
    @Test
    fun testDeleteTask() {
        // Add a task first
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }
        composeTestRule.waitForIdle()
        // Add a task
        composeTestRule.onNodeWithText("New Task").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.waitForIdle()
        // Delete the task
        composeTestRule.onNodeWithText("Buy groceries").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Delete Task").performClick()
        composeTestRule.waitForIdle()

        // Assert the task was deleted
        composeTestRule.onNodeWithText("Buy groceries").assertDoesNotExist()
    }
    @Test
    fun testMarkTaskAsDone() { //
        // Add a task first
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }

        composeTestRule.waitForIdle()
        // Add a task
        composeTestRule.onNodeWithText("New Task").performTextInput("Complete assignment")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Complete assignment").performClick()
        composeTestRule.onNodeWithText("Mark as Done").performClick()
        composeTestRule.onNodeWithText("Complete assignment").assertDoesNotExist()
    }

    @Test
    fun testFilterDoneTasks() {
        // Add a task and mark it as done
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }
        composeTestRule.waitForIdle()
        // Add a task and mark it as done
        composeTestRule.onNodeWithText("New Task").performTextInput("Complete assignment")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Complete assignment").performClick()
        composeTestRule.onNodeWithText("Mark as Done").performClick()
        composeTestRule.onNodeWithTag("filterButton").performClick()
        // Apply the filter to show only done tasks
        composeTestRule.onNodeWithTag("showOnlyDoneFilter").performClick()

        // Verify only the done task is visible
        composeTestRule.onNodeWithText("Complete assignment").assertExists()
    }
    @Test
    fun testShowAllTasks() {
        // Add two tasks: one marked as done, the other not
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }
        composeTestRule.waitForIdle()
        // Add a task
        composeTestRule.onNodeWithText("New Task").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Add Task").performClick()

        // Add another task and mark it as done
        composeTestRule.onNodeWithText("New Task").performTextInput("Complete assignment")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.onNodeWithText("Complete assignment").performClick()
        composeTestRule.onNodeWithText("Mark as Done").performClick()
        composeTestRule.onNodeWithTag("filterButton").performClick()


        // Apply the filter to show all tasks
        composeTestRule.onNodeWithTag("showAllFilter").performClick()

        // Verify both tasks are visible
        composeTestRule.onNodeWithText("Buy groceries").assertExists()
        composeTestRule.onNodeWithText("Complete assignment").assertExists()
    }
    @Test
    fun testShowAllExceptDone() {
        // Add two tasks: one marked as done, the other not
        composeTestRule.setContent {
            ToDoListScreen(viewModel = taskViewModel)
        }
        composeTestRule.waitForIdle()
        // Add a task
        composeTestRule.onNodeWithText("New Task").performTextInput("Buy groceries")
        composeTestRule.onNodeWithText("Add Task").performClick()

        // Add another task and mark it as done
        composeTestRule.onNodeWithText("New Task").performTextInput("Complete assignment")
        composeTestRule.onNodeWithText("Add Task").performClick()
        composeTestRule.onNodeWithText("Complete assignment").performClick()
        composeTestRule.onNodeWithText("Mark as Done").performClick()
        composeTestRule.onNodeWithTag("filterButton").performClick()



        // Apply the filter to show all except done tasks
        composeTestRule.onNodeWithTag("excludeDoneFilter").performClick()

        // Verify only the task that isn’t done is visible
        composeTestRule.onNodeWithText("Buy groceries").assertExists()
        composeTestRule.onNodeWithText("Complete assignment").assertDoesNotExist()
    }


}
