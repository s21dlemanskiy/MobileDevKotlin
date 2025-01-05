package com.example.task33

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.math.BigInteger
import kotlin.concurrent.Volatile

class MyService : Service() {
    @Volatile
    var running: Boolean = false
    var thread1: Thread? = null
    @Volatile
    var fibNumbers: Pair<BigInteger, BigInteger>? = null
    private val binder = LocalBinder()
    public var onUpdate: (BigInteger, Boolean) -> Unit = {fib, end -> Log.e("onUpdate", "default onUpdate function is used")}
    private val list = mutableListOf<BigInteger>()

//    fun getOnUpdate(): (BigInteger, Boolean) -> Unit {
//        return onUpdate
//    }
//    fun setOnUpdate(function: (BigInteger, Boolean) -> Unit){
//        onUpdate = function
//    }

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    override fun onBind(p0: Intent?): IBinder? {
//        return Binder()
//        return null
        return binder
    }

    fun getList(): List<BigInteger> {
        return list
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        this.calculate(12, { a, b -> })
        return super.onStartCommand(intent, flags, startId)
    }

    fun fuctorial(n: Int): Int{
        var result = 1
        for (i in 1..n) {
            result *= i
        }
        return result
    }

    fun calculate(n: Int) {
        if (thread1 != null) {
            thread1?.interrupt()
        }
        running = true
        thread1 = Thread {
            synchronized(list) {
                list.clear()
            }
            fibNumbers = Pair(BigInteger.ONE, BigInteger.ONE)
            for (i in 2 .. fuctorial(n)) {
                if (!running) {
                    return@Thread
                }
                fibNumbers = Pair(fibNumbers!!.second, fibNumbers!!.first + fibNumbers!!.second)
                onUpdate(fibNumbers!!.first, false)
                Log.i("IterNum", "Iteration: ${i}")
                synchronized(list) {
                    list.add(fibNumbers!!.first)
                }
            }
            Log.i("IterNum", "Operation Finished")
            sendNotification("ready")
            onUpdate(fibNumbers!!.second, true)
        }
        thread1?.start()
//        thread1?.isDaemon = true
    }

    fun getState(): BigInteger? {
        return fibNumbers?.second
    }

    fun stopCalculation() {
        if (thread1 == null) {
            Log.wtf("MyService", "empty tread and try to stop")
            return
        }
//        thread1?.interrupt()
        running = false
        Log.wtf("thread1", thread1?.isAlive.toString())
//        thread1?.stop()

    }

    fun isFinished(): Boolean {
        return thread1?.isAlive?.not() ?: false
    }

    fun calcExists(): Boolean {
        return thread1 != null
    }


    private fun sendNotification(msg: String): Boolean {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "my_channel_01" // Уникальный идентификатор канала
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений (Android 8.0+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "chanel"
            }
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("Уведомление")
            .setContentText(msg)
            .setAutoCancel(true) // Уведомление исчезает после нажатия
            .setContentIntent(pendingIntent) // Назначаем открытие Activity по нажатию


        val notificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),100);
            return false
        }
        notificationManagerCompat.notify(123, notificationBuilder.build())
        return true


    }
}