package cz.mendelu.pef.pokus1.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.mendelu.pef.pokus1.R

// TODO(5): create channel
fun Context.createNotificationChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableVibration(true)
            enableLights(true)
            lightColor = Color.GREEN
        }

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun Context.showPackageNotification(
    recipientName: String
) {
    val notification = NotificationCompat.Builder(
        this,
        getString(R.string.notification_channel_id_packages)
    )
        .setSmallIcon(R.drawable.notif) // libovolná ikona
        .setContentTitle(recipientName)
        .setContentText("Balíček čeká na vyzvednutí")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(this)
        .notify(recipientName.hashCode(), notification)
}

/*
* @SuppressLint("MissingPermission")
fun Context.showSimpleNotification(
    title: String,
    message: String,
    notificationId: Int = 0
) {
    val notification = NotificationCompat.Builder(
        this,
        getString(R.string.notification_channel_id_packages)
    )
        .setSmallIcon(R.drawable.notif)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(this)
        .notify(notificationId, notification)
}
val context = LocalContext.current

context.showSimpleNotification(
    title = "Test",
    message = "zkouška textu"
)

*
* @Composable
fun TestNotificationButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            context.showSimpleNotification(
                title = "Test",
                message = "zkouška textu"
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Zobrazit notifikaci")
    }
}

POROVNANI BALICEK
* context.showSimpleNotification(
    title = pkg.recipientName.orEmpty(),
    message = "Balíček čeká na vyzvednutí",
    notificationId = pkg.id?.toInt() ?: 0
)
*
*
*
*
*
* */