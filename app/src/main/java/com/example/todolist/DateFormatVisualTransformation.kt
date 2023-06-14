package com.example.todolist

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
/*
class DateFormat(private val dateFormat: String) {
    fun filter(text: AnnotatedString): i {
        val trimmed = text.text.trim()
        val isValid = try {
            // 嘗試將輸入的文本轉換為指定的日期格式
            java.text.SimpleDateFormat(dateFormat).parse(trimmed.toString())
            true
        } catch (e: Exception) {
            false
        }

        // 若日期格式有效，則返回原始文本；否則返回空字串
        val transformedText = if (isValid) text else AnnotatedString("格式錯誤")
        return TransformedText(transformedText, OffsetMapping.Identity)
    }
}*/