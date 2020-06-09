package com.shoes.shoeslaundry.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Message;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.ui.admin.MainAdminActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE_NEWMESSAGE = "NEWSMESSAGE";
    public static final String TYPE_NEWSTATUS = "NEWSTATUS";
    public static final String TYPE_NEWORDER = "NEWORDER";
    private static final String EXTRA_TYPE = "type";

    private final static int ID_NEWMESSAGE = 100;
    private final static int ID_NEWSTATUS = 101;
    private final static int ID_NEWORDER = 102;

    private int idNotifNewMessage;
    private int idNotifNewStatus;
    private int idNotifNewOrder;

    private int newUnreadmessages = 0;
    private int newOrder = 0;

    @Override
    public void onReceive(final Context context, Intent intent) {
        idNotifNewMessage = 1;
        idNotifNewStatus = 2;
        idNotifNewOrder = 3;

        String type = intent.getStringExtra(EXTRA_TYPE);
        if (type.equalsIgnoreCase(TYPE_NEWMESSAGE)) {
            checkNewMessageUser(new MessageCallback() {
                @Override
                public void onSuccess(int newMessage) {
                    if (newMessage != 0){
                        showAlarmNotification(context,"Pesan Baru","Ada "+ newMessage+" pesan baru", idNotifNewMessage);
                    }
                }

                @Override
                public void onError(boolean failure) {

                }
            });
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        Intent intent = new Intent(context.getApplicationContext(), MainAdminActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.wash)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    public void setAlarm(Context context, String type) {
        Log.d("cek", "masuk setrepeating" + type);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_NEWMESSAGE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);
        }
    }


    private void checkNewMessageUser(final MessageCallback messageCallback) {
        Log.d("cek", "masuk checknewusers");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Message").child(auth.getUid());
        sewaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newUnreadmessages = 0;
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Message message = dt.getValue(Message.class);
                    assert message != null;
                    if (!message.isRead()) {
                        newUnreadmessages++;
                    }
                }
                Log.d("cek", "onDataChange: "+newUnreadmessages);
                messageCallback.onSuccess(newUnreadmessages);
                messageCallback.onError(false);
                newUnreadmessages = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void checkNewBencana(final NotifikasiCallback notifikasiCallback) {
//        Log.d("cek", "masuk checknewbencana");
//        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Bencana");
//        sewaRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dt : dataSnapshot.getChildren()) {
//                    Bencana mBencana = dt.getValue(Bencana.class);
//                    if (!mBencana.isStatus()) {
//                        newBencana++;
//                    }
//                }
//                notifikasiCallback.onSuccess(newBencana);
//                notifikasiCallback.onError(false);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
