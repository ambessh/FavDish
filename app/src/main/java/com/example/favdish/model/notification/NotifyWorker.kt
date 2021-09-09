package com.example.favdish.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.favdish.R
import com.example.favdish.utils.Constants
import com.example.favdish.view.activities.MainActivity

class NotifyWorker(context: Context, params:WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        Log.i("Logging","Something to the console")
        sendNotification()
        return Result.success()

    }

   private fun sendNotification(){
     val notification_id=0
       val intent= Intent(applicationContext,MainActivity::class.java)
       intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
       intent.putExtra(Constants.Notification_id,notification_id)

       val notificationManager=applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

       val notificationTitle=applicationContext.getString(R.string.notification_title)
       val notificationSub=applicationContext.getString(R.string.notification_subtitle)

       val notificationImage:Bitmap?=applicationContext.vectorToBitmap(R.drawable.food)

       val bigPicStyle=NotificationCompat.BigPictureStyle().bigLargeIcon(null)
           .bigPicture(notificationImage)

       val pendingIntent=PendingIntent.getActivity(applicationContext,0,intent,0)

       val notification=NotificationCompat.Builder(applicationContext,Constants.Notification_channel)
           .setContentTitle(notificationTitle)
           .setContentText(notificationSub)
           .setSmallIcon(R.drawable.ic_notifications_black_24dp)
           .setLargeIcon(notificationImage)
           .setDefaults(NotificationCompat.DEFAULT_ALL)
           .setContentIntent(pendingIntent)
           .setStyle(bigPicStyle)
           .setAutoCancel(true)

       notification.priority=NotificationCompat.PRIORITY_MAX

       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           notification.setChannelId(Constants.Notification_channel)
           val ringToneManager=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
           val audioAttr=AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
               .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

           var channel=NotificationChannel(Constants.Notification_channel,Constants.Notification_name,NotificationManager.IMPORTANCE_HIGH)
           channel.enableLights(true)
           channel.lightColor=Color.RED
           channel.enableVibration(true)
           channel.vibrationPattern=longArrayOf(100,200,300,400,500,400,300,200)
           channel.setSound(ringToneManager,audioAttr)
           notificationManager.createNotificationChannel(channel)


       }else{

       }

       notificationManager.notify(notification_id,notification.build())






    }

    fun Context.vectorToBitmap(drawableId:Int):Bitmap?{
        val drawable=ContextCompat.getDrawable(this,drawableId) ?:return null
        val bitmap=Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight
        ,Bitmap.Config.ARGB_8888)?:return null
        val canvas=Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}