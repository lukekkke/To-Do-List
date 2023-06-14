package com.example.todolist.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.todolist.ui.UiState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BrowserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    fun changeToCreate(){
        _uiState.update{
            it.copy(
                showCreate = true,
                showTaskList = false
            )
        }
    }
    fun backToTaskList(){
        _uiState.update{
            it.copy(
                showCreate = false,
                showRevise = false,
                titleIsValid = true,
                createdDateIsValid = true,
                dueDateIsValid = true,
                addErrorMessage = "",
                addError = false,
                showTaskList = true,
            )
        }
    }
    fun checkDateformat(text: String): Boolean {
        val trimmed = text.trim()
        Log.d("date", trimmed)
        val isValid = try {
            // 嘗試將輸入的文本轉換為指定的日期格式
            java.text.SimpleDateFormat("yyyy/MM/dd").parse(trimmed)
            true
        } catch (e: Exception) {
            false
        }
        if(isValid)Log.d("dateC", "isValid")
        else Log.d("dateC", "isNotValid")
        return isValid
    }
    fun calculateSortdays(dueDate: String): Long{
        val dateFormat = SimpleDateFormat("yyyy/MM/dd" )
        val sortDays = TimeUnit.DAYS.convert(dateFormat.parse(dueDate).time - dateFormat.parse("2023/6/13").time, TimeUnit.MILLISECONDS)
        Log.d("sortDays", sortDays.toString())
        return sortDays
    }
    fun getCurrentDateAsString(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(currentDate)
    }
    fun getNextDayAsString(): String {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val nextDay = calendar.time
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(nextDay)
    }
    fun checkTaskIsValid(title :String,description :String,  createdDate :String, dueDate :String, Location :String): Boolean{
        val titleText = title
        val descriptionText = description
        val createdDateText = createdDate
        val dueDateText = dueDate
        val LocationText = Location
        val titleIsValid = titleText.isNotEmpty()
        val createdDateIsValid = checkDateformat(createdDateText)
        val dueDateIsValid = checkDateformat(dueDateText)
        var addErrorMessage = "格式錯誤:"
        var addSuccess = true
        Log.d("Title", titleText)
        Log.d("CreatedDate", createdDateText)
        Log.d("DueDate", dueDateText)
        if(!titleIsValid){
            addErrorMessage += " Title"
            addSuccess = false
        }
        if(!createdDateIsValid){
            addErrorMessage += " CreatedDate"
            addSuccess = false
        }
        if(!dueDateIsValid){
            addErrorMessage += " DueDate"
            addSuccess = false
        }
        Log.d( "addSuccessVW", addErrorMessage)
        Log.d( "addSuccessVW", addSuccess.toString())
        _uiState.update {
            it.copy(
                titleIsValid = titleIsValid,
                createdDateIsValid = createdDateIsValid,
                dueDateIsValid = dueDateIsValid,
                addErrorMessage = addErrorMessage,
                addError = !addSuccess
            )
        }
        Log.d("USaddErrorVW", _uiState.value.addError.toString())
        return !addSuccess
    }


}