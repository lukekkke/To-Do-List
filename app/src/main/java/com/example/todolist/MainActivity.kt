package com.example.todolist


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.todolist.ui.*
import com.example.todolist.ui.theme.ToDoListTheme


class MainActivity : ComponentActivity() {
    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_database"
        ).build()
        val taskViewModel = TaskViewModel(database)


        setContent {
            ToDoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    Column(){
                        TopBar()

                        BrowserUi(taskViewModel = taskViewModel)
                    }

                }
            }
        }
    }


}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoListTheme {

    }
}