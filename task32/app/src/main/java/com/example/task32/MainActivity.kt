package com.example.task32

import Fraction
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.example.task32.ui.theme.Task32Theme
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.math.BigInteger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task32Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
//    var result: Fraction? by rememberSaveable {
//        mutableStateOf(null as Fraction?)
//    }
    var result_d: String? by rememberSaveable {
        mutableStateOf(null as String?)
    }
    var result_n: String? by rememberSaveable {
        mutableStateOf(null as String?)
    }
    val context = LocalContext.current;
    var sighnId: Int by rememberSaveable {
        mutableIntStateOf(0)
    }
    var numerator1: String by rememberSaveable {
        mutableStateOf("")
    }
    var numerator2: String by rememberSaveable {
        mutableStateOf("")
    }
    var denumerator1: String by rememberSaveable {
        mutableStateOf("")
    }
    var denumerator2: String by rememberSaveable {
        mutableStateOf("")
    }
    var alertMessage by rememberSaveable { mutableStateOf(null as String?)}
    val signs = listOf(R.string.devide, R.string.prod)
    Row(
        modifier = modifier
            .testTag("Tag:root")
    ) {
        Column(
            modifier = Modifier.weight(3f)
                .testTag("Tag:1-st fraction")
        ) {
            TextField(
                value = numerator1,
                modifier = Modifier
                    .testTag("Tag:1-st fraction numerator"),
                onValueChange = {
                    input: String ->
                    numerator1 = input
                }
            )
            Text(text = "----")
            TextField(value = denumerator1,
                modifier = Modifier
                    .testTag("Tag:1-st fraction denumerator"),
                onValueChange = {
                    input: String ->
                denumerator1 = input
            })

        }
        Button (
            onClick = { sighnId = (sighnId + 1) % signs.size },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f)
                .testTag("Tag:signButton")

        ) {
            Text(
                text = context.getString(signs[sighnId]),
                modifier = Modifier.testTag("Tag:signText")
            )
        }
        Column(
            modifier = Modifier.weight(3f)
                .testTag("Tag:2-st fraction")
        ) {
            TextField(value = numerator2,
                modifier = Modifier
                    .testTag("Tag:2-st fraction numerator"),

                onValueChange = {
                    input: String ->
                numerator2 = input
            })
            Text(text = "----")
            TextField(value = denumerator2,
                modifier = Modifier
                    .testTag("Tag:2-st fraction denumerator"),
                onValueChange = {input: String ->
                    denumerator2 = input
                }
            )

        }
        Button(
            onClick = {
                val numerator1I: BigInteger
                val dominator1I: BigInteger
                val numerator2I: BigInteger
                val dominator2I: BigInteger
                try {
                    numerator1I = BigInteger(numerator1)
                    dominator1I = BigInteger(denumerator1)
                    numerator2I = BigInteger(numerator2)
                    dominator2I = BigInteger(denumerator2)
                }
                catch (e: Exception) {
                    alertMessage = context.getString(R.string.numbers_must_contains_only_digits)
                    return@Button
                }
                if (dominator1I == BigInteger.ZERO || dominator2I == BigInteger.ZERO) {
                    alertMessage = context.getString(R.string.dominator_must_be_not_0)
                    return@Button
                }
                val fraction1 = Fraction(numerator1I, dominator1I)
                val fraction2 = Fraction(numerator2I, dominator2I)
                if (numerator2I == BigInteger.ZERO) {
                    alertMessage = context.getString(R.string.dominator_must_be_not_0)
                    return@Button
                }
                val result = when (signs[sighnId]) {
                    R.string.prod -> fraction1 * fraction2
                    R.string.devide -> fraction1 / fraction2
                    else -> {null}
                }
                result_n = result?.numerator?.toString()
                result_d = result?.denominator?.toString()
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f)
                .testTag("Tag:processOperation")
        ) {
            Text(text = stringResource(R.string.equal))
        }
        if (result_n != null && result_d != null) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
                    .testTag("Tag:resultNumber")
            ) {
                Text(text = result_n!!,
                        modifier = Modifier
                        .testTag("Tag:result fraction numerator")
                )
                Text(text = "----")
                Text(text = result_d!!,
                    modifier = Modifier
                        .testTag("Tag:result fraction denumerator")
                )
            }
        }
        if (alertMessage != null) {
            AlertMessage(
                alertMessage,
                onDismissRequest = {
                    alertMessage = null
                }
            )
        }

    }
}



@Composable
fun AlertMessage(alertMessage: String?, onDismissRequest: () -> Unit) {
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
                        .wrapContentSize(Alignment.Center)
                        .testTag("Tag:alert message text"),
                    textAlign = TextAlign.Center,
                )
            }
        },
        onDismissRequest = onDismissRequest
    )

}