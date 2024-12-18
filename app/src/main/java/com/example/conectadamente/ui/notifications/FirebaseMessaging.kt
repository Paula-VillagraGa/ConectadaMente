package com.example.conectadamente.ui.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.conectadamente.MainActivity
import com.example.conectadamente.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token actualizado: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Mensaje recibido: ${remoteMessage.data}")

        // Extraer datos de la notificación
        val title = remoteMessage.notification?.title ?: "Notificación"
        val message = remoteMessage.notification?.body ?: "Tienes un nuevo mensaje"

        // Mostrar notificación
        sendNotification(title, message)
    }

    @SuppressLint("MissingPermission", "ObsoleteSdkInt")
    private fun sendNotification(title: String, message: String) {
        val channelId = "DEFAULT_CHANNEL"

        // Crear canal de notificaciones (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones generales",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones generales"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        // Crear intent para abrir la app al tocar la notificación
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Crear la notificación
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_email) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Intent al tocar la notificación
            .setAutoCancel(true) // Cerrar la notificación al tocarla
            .build()

// Mostrar la notificación
        NotificationManagerCompat.from(this)
            .notify(System.currentTimeMillis().toInt(), notification)
    }
}