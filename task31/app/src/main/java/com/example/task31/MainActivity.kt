package com.example.task31

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.translation.Translator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.task31.ui.theme.Task31Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task31Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var num1 by rememberSaveable { mutableStateOf("")}
    var num2 by rememberSaveable { mutableStateOf("")}
    var numSum by rememberSaveable { mutableStateOf(null as String?)}
    var alertMessage by rememberSaveable { mutableStateOf(null as String?)}
    val context = LocalContext.current;

    Column (
        modifier = modifier
    ) {
        Text(
            text = context.getString(R.string.title1),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = num1,
                modifier = Modifier.weight(1f).padding(Dp(10f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = { num1 = it }
            )
            TextField(
                value = num2,
                modifier = Modifier.weight(1f).padding(Dp(10f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = { num2 = it }
            )
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                numSum = sumDigits(num1, num2) {
                    // on error
                    alertMessage = context.getString(R.string.digitwarn)
                }?.toString()
            }
        ){
            Text(text = context.getString(R.string.buttonText))
        }
        if (numSum != null) {
            Text(text = numSum.orEmpty())
        }
        Log.wtf("alertMessage", alertMessage)
        if (alertMessage != null) {
            Log.wtf("sgw", "go")
            Dialog(
                content = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                            .background(Color.Red),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = alertMessage.toString(),
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center,
                        )
                    }
                          },
                onDismissRequest = {
                alertMessage = null
            })
        }
    }
}

fun sumDigits(num1: String, num2: String, onError: () -> Unit): Int? {
    if (!num1.isDigitsOnly() or !num2.isDigitsOnly()) {
        onError()
        return null
    }
    try {
        return num1.toInt() + num2.toInt()
    } catch (e: Exception) {
        Log.i("sumDigits", e.toString())
        onError()
        return null
    }
}