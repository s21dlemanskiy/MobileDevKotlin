package com.example.task33

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.task33.ui.theme.Task33Theme
import java.math.BigInteger

class MainActivity : ComponentActivity() {
    var onConect = {}
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Log.e("notify", "not granted")
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private var myService: MyService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyService.LocalBinder
            myService = binder.getService()
            Log.i("MyServiceInActivity", myService.toString())
            MinWindow()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myService = null
        }

    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        // Не останавливаем сервис, если он должен работать в фоне
    }
    override fun onStart() {
        super.onStart()
        bindService()
    }
    fun bindService(){
        val serviceIntent = Intent(this, MyService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestNotificationPermission()
        super.onCreate(savedInstanceState)

    }
    fun MinWindow() {
        enableEdgeToEdge()
        setContent {
            Task33Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Count(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun Count(name: String, modifier: Modifier = Modifier) {
        var fibNumber: BigInteger? by rememberSaveable {
            mutableStateOf(myService?.getState())
        }
        var nums: Int by rememberSaveable {
            mutableIntStateOf(12)
        }
        var endTask: Boolean by rememberSaveable {
            mutableStateOf(myService?.isFinished() ?: false)
        }
//        onConect = {
//            run{
//                Log.INFO(1)
//                fibNumber = myService?.getState()
//                endTask = myService?.isFinished() ?: false
//            }
//        }
        myService?.onUpdate = { fib, end ->
            run {
//                if (endTask.not() && end) {
//                    val result = sendNotification("ready")
//                    Log.i("notify", if(result) {
//                        "success"
//                    }else {
//                        "error"
//                    })
//                }
                endTask = end
                fibNumber = BigInteger(fib.toString().length.toString())
            }
        }
        Column {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            TextField (
                value = nums.toString(),
                onValueChange = { nums = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 12}
            )
            Row {
                Button(
                    onClick = { onClick1(nums) },
                    modifier = modifier

                ) {
                    Text(text = "push me")
                }
                Button(onClick = {
                    myService?.stopCalculation()
                },
                    modifier = modifier
                ) {
                    Text(text = "stop")
                }
                Button(
                    onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, myService?.getState().toString())
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, "null11")
                    startActivity(shareIntent)
                },
                    modifier = modifier
                ) {
                    Text(text = "share")
                }
                Button(
                    onClick = {

                        fibNumber = myService?.getState()
                        endTask = myService?.isFinished() ?: false

                    },
                    modifier = modifier

                ) {
                    Text(text = "refresh")
                }
            }
            Row() {
                Text(
                    text = "count of nums in last digit:",
                    modifier = modifier
                )
                Text(
                    text = fibNumber.toString(),
                    modifier = modifier
                )
            }
        }
    }





    private fun onClick1(i: Int) {
//        val intent = Intent(context, MyService::class.java)
//        if (myService?.calcExists() != true) {
//            myService?.startService(intent)
//        }
        myService?.calculate(i)
    }

}
