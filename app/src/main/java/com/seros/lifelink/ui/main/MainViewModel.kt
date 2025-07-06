package com.seros.lifelink.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.seros.lifelink.R
import com.seros.lifelink.domain.FamilyMember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel : ViewModel() {
    private val _members = MutableStateFlow<List<FamilyMember>>(emptyList())
    val members: StateFlow<List<FamilyMember>> get() = _members

    init {
        _members.value = listOf(
            FamilyMember(
                id = 1,
                name = "Дочь",
                location = LatLng(55.6700, 37.5500),
                currentPlace = "Школа",
                iconRes = R.drawable.ic_marker_daughter
            ),
            FamilyMember(
                id = 2,
                name = "Папа",
                location = LatLng(55.6750, 37.5450),
                currentPlace = "Офис",
                iconRes = R.drawable.ic_marker_father
            ),
            FamilyMember(
                id = 3,
                name = "Мама",
                location = LatLng(55.6800, 37.5400),
                currentPlace = "Дом",
                iconRes = R.drawable.ic_marker_mother
            )
        )
    }


    fun sendNotification(context: Context) {
        val channelId = "family_channel"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Family Events",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Действия в семье")
            .setContentText("Дочь вышла из школы")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }


}