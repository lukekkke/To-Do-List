package com.example.todolist.ui



import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.todolist.TaskDatabase
import com.example.todolist.TaskViewModel
import com.example.todolist.data.TaskEntry
import com.example.todolist.ui.theme.Purple40
import kotlin.math.roundToInt

@Composable
fun TopBar(//上排新增按鍵
    modifier : Modifier = Modifier,
    browserViewModel: BrowserViewModel = viewModel()
){
    val uiState by browserViewModel.uiState.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFCCC2DC)),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(uiState.showCreate){
            Button(//按下後isCreate = True
                onClick = { browserViewModel.backToTaskList()},
                modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("取消")
            }
        }else{
            Button(//按下後isCreate = True
                onClick = { browserViewModel.changeToCreate()},
                modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("create")
            }
        }

    }
}
@Composable
fun BrowserUi(
    modifier : Modifier = Modifier,
    browserViewModel: BrowserViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel(),
){
    val browserUiState by browserViewModel.uiState.collectAsState()
    val sortedData by taskViewModel.database.taskEntryDao().getAllDataSortedByDate().collectAsState(initial = emptyList())
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if(sortedData.isNotEmpty() && browserUiState.showTaskList){
            LazyColumn{
                itemsIndexed(sortedData){index, id ->
                    Surface(
                        Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { }, color = Color.White){
                        Row(modifier = Modifier.fillMaxWidth()){
                            Column(
                                Modifier
                                    .padding(10.dp, 10.dp)
                                    .clip(RoundedCornerShape(10.dp))){
                                Text(id.title, Modifier.align(Alignment.CenterHorizontally))
                                Text(id.dueDate, Modifier.align(Alignment.CenterHorizontally))
                            }
                            Button(onClick = { taskViewModel.delete(sortedData[index]) }) {
                                Text("刪除")
                            }
                        }

                    }

                }
            }
        }
        if (browserUiState.showCreate) {
            CreateWindow(taskViewModel = taskViewModel,)
        }
    }
}

@Composable
fun CreateWindow(
    browserViewModel: BrowserViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel(),
){
    val browserUiState by browserViewModel.uiState.collectAsState()
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    val nowDate = browserViewModel.getCurrentDateAsString()
    var createdDateText by remember { mutableStateOf(nowDate) }
    val nextDate = browserViewModel.getNextDayAsString()
    var dueDateText by remember { mutableStateOf(nextDate) }
    var locationText by remember { mutableStateOf("") }
    AnimatedVisibility(
        visible = browserUiState.showCreate,
        enter = slideIn(initialOffset = { IntOffset(100,100) }),
        exit = slideOut(targetOffset = { IntOffset(-100,100) })
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ){
            EnterTask(
                browserViewModel,
                tit = titleText,
                des = descriptionText,
                cre = createdDateText,
                due = dueDateText,
                loc = locationText,
                onTitChange = { titleText = it},
                onCreChange = {createdDateText = it },
                onDesChange  = {descriptionText = it},
                onDueChange  = {dueDateText = it},
                onLocChange = {locationText = it }
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 10.dp)
                .weight(0.3f)
            ){
                if(browserUiState.addError){
                    Text(text = browserUiState.addErrorMessage, color = Color.Red, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if(!browserViewModel.checkTaskIsValid(titleText,descriptionText, createdDateText, dueDateText, locationText)){

                            taskViewModel.addEntry(titleText,descriptionText, createdDateText, dueDateText, locationText, browserViewModel.calculateSortdays(dueDateText))
                            browserViewModel.backToTaskList()
                        }
                    },
                    colors = buttonColors(Purple40)
                ){
                    Text("添加", color = Color.White)
                }
            }
        }

    }

}
@Composable
fun EnterTask(
    browserViewModel: BrowserViewModel = viewModel(),
    tit :String,
    des :String,
    cre :String,
    due :String,
    loc :String,
    onTitChange :(String) ->Unit,
    onDesChange :(String) ->Unit,
    onCreChange :(String) ->Unit,
    onDueChange :(String) ->Unit,
    onLocChange :(String) ->Unit
){
    val browserUiState by browserViewModel.uiState.collectAsState()

    Text(text = "Title", Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp))
    Text("ex: See a doctor.", Modifier.padding(start = 20.dp), color = Color(0xffb4b4b4), fontSize = 15.sp)
    BasicTextField(value = tit, onValueChange = onTitChange,
        Modifier
            .padding(20.dp, 5.dp, 20.dp, 5.dp)
            .border(width = 0.5.dp, color = Color.LightGray, shape = RectangleShape)
            .height(40.dp)
            .fillMaxWidth(0.5f),
        textStyle = TextStyle(fontSize = 15.sp),
        singleLine = true
    ){



        it()
    }


    Text(text = "Description", Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp))
    Text("ex: Take the Bus 123.", Modifier.padding(start = 20.dp),  color = Color(0xffb4b4b4), fontSize = 15.sp)
    BasicTextField(value = des, onValueChange = onDesChange ,
        Modifier
            .padding(20.dp, 5.dp, 20.dp, 5.dp)
            .border(width = 0.5.dp, color = Color.LightGray, shape = RectangleShape)
            .height(40.dp)
            .fillMaxWidth(0.5f),
        textStyle = TextStyle(fontSize = 15.sp),
        singleLine = true
    ){
        it()
    }


    Text(text = "CreatedDate", Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp))
    Text("ex: 2023/03/27.", Modifier.padding(start = 20.dp), color = Color(0xffb4b4b4), fontSize = 15.sp)
    BasicTextField(value = cre, onValueChange = onCreChange ,
        Modifier
            .padding(20.dp, 5.dp, 20.dp, 5.dp)
            .border(width = 0.5.dp, color = Color.LightGray, shape = RectangleShape)
            .height(40.dp)
            .fillMaxWidth(0.5f),
        textStyle = TextStyle(fontSize = 15.sp),
        singleLine = true
    ){
        it()
    }
    Text(text = "DueDate", Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp))
    Text("ex: 2023/03/28.", Modifier.padding(start = 20.dp), color = Color(0xffb4b4b4), fontSize = 15.sp)
    BasicTextField(value = due, onValueChange = onDueChange ,
        Modifier
            .padding(20.dp, 5.dp, 20.dp, 5.dp)
            .border(width = 0.5.dp, color = Color.LightGray, shape = RectangleShape)
            .height(40.dp)
            .fillMaxWidth(0.5f),
        textStyle = TextStyle(fontSize = 15.sp),
        singleLine = true
    ){
        it()
    }
    Text(text = "Location", Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp))
    Text("ex: 25.0174719, 121.3662922.", Modifier.padding(start = 20.dp), color = Color(0xffb4b4b4), fontSize = 15.sp)
    BasicTextField(value = loc, onValueChange = onLocChange ,
        Modifier
            .padding(20.dp, 5.dp, 20.dp, 5.dp)
            .border(width = 0.5.dp, color = Color.LightGray, shape = RectangleShape)
            .height(40.dp)
            .fillMaxWidth(0.5f),
        textStyle = TextStyle(fontSize = 15.sp),
        singleLine = true
    ){

        it()
    }


}