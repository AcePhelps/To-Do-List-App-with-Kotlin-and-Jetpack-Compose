package com.example.todolist.jetpacktest

import com.example.todolist.jetpacktest.pageViews.ToDoListPage

class ToDoListSteps(private val page : ToDoListPage) {


    //Given user want to add a new task
    fun userAddingNewTask(taskName: String){
        page.addNewTask(taskName)
    }

    //When user mark task As Done
    fun userMarkTaskAsDone(taskName: String){
        page.markTaskAsDone(taskName)
    }

    //Then Task is mark as done and color has changed to green
    fun verifyTaskIsDone(){
       page.assertTaskMarkedAsDone()
    }

    fun verifyTaskIsExist(taskName: String){
        page.assertTaskExists(taskName)
    }
    fun verifyAllDoneFilter(taskName: String){
        page.clickOnFilter()
        page.setFilterTo(ToDoListPage.TaskFilter.SHOW_ONLY_DONE)
        page.assertTaskExists(taskName)
    }

    fun userDeleteTask(taskName: String){
        page.deleteTask(taskName)
    }

    fun verifyTaskIsDeleted(taskName: String){
        page.assertTaskNotExists(taskName)
    }
}