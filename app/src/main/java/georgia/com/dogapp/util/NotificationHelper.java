package georgia.com.dogapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import georgia.com.dogapp.R;
import georgia.com.dogapp.view.MainActivity;

public class NotificationHelper {
    private static NotificationHelper instance;
    private Context context;
    private static String CHANNEL_ID="Dogs Channel id";
    private static final int NOTIFICATION_ID=123;

    private NotificationHelper(Context context) {
        this.context = context;

    }

    public static NotificationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationHelper(context);
        }
        return instance;
    }
    private void createNotification(){
        createNotificationChannel();
        Intent intent=new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        Bitmap icon= BitmapFactory.decodeResource(context.getResources(), R.drawable.dogiconnote_one);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder notification=new Notification.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.dogiconnote)
                    .setLargeIcon(icon)
                    .setContentTitle("Dog Retrived")
                    .setContentText("This is the notification  to let you know that the dog  information has been retrived")
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigLargeIcon(icon)
                            .bigLargeIcon(null)
                    )
                    .setActions()




        }

    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String name=CHANNEL_ID;
            String description="Dogs retrived Notification Channel";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager= (NotificationManager) context.getSystemService (Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
