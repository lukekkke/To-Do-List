package com.example.todolist.ui

data class UiState(
    val listIsEmpty: Boolean = false,
    val showCreate: Boolean = false,
    val showRevise: Boolean = false,
    val showDelete: Boolean = false,
    val showTaskList: Boolean = true,
    val titleIsValid :Boolean = false,
    val createdDateIsValid :Boolean = false,
    val dueDateIsValid :Boolean = false,
    val addErrorMessage :String = "",
    val addError :Boolean = false,

)
