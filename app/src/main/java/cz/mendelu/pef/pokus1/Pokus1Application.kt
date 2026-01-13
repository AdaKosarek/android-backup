package cz.mendelu.pef.pokus1

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.mendelu.pef.pokus1.notification.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Pokus1Application : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel(
            channelId = getString(R.string.notification_channel_id_packages),
            channelName = getString(R.string.notification_channel_name_packages)
        )
    }
}
