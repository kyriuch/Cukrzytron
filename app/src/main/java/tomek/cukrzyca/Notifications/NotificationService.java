package tomek.cukrzyca.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.Fragments.Recommendation;
import tomek.cukrzyca.R;


public class NotificationService extends Service {

    static ArrayList<tomek.cukrzyca.Notifications.Notification> notificationList = new ArrayList<>();
    static ArrayList<Recommendation> recommendationsList = new ArrayList<>();
    static boolean recommendationsOn = false;
    static SQLiteDatabase db;
    static String[] types = {"Dieta", "Glikemia", "Insulina", "Inne"};


    public static void refreshTables() {
        notificationList.clear();
        recommendationsList.clear();

        String sQuery = "SELECT * FROM " + Database.NOTIFICATIONS_TABLE + ";";

        Cursor cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                tomek.cukrzyca.Notifications.Notification notification = new tomek.cukrzyca.Notifications.Notification(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4) == 1,
                        new boolean[]{
                                cursor.getInt(5) == 1,
                                cursor.getInt(6) == 1,
                                cursor.getInt(7) == 1,
                                cursor.getInt(8) == 1,
                                cursor.getInt(9) == 1,
                                cursor.getInt(10) == 1,
                                cursor.getInt(11) == 1
                        });

                notificationList.add(notification);
            } while (cursor.moveToNext());
        }

        sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_TABLE + ";";

        cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                recommendationsList.add(new Recommendation(cursor.getInt(0),
                        cursor.getString(2), cursor.getString(1), cursor.getFloat(3)));
            } while (cursor.moveToNext());
        }

        sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_SETTINGS + ";";

        cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getInt(0) == 1) {
                recommendationsOn = true;
            } else {
                recommendationsOn = false;
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        db = getApplicationContext().openOrCreateDatabase("database.db", Context.MODE_PRIVATE, null);

        String sQuery = "SELECT * FROM " + Database.NOTIFICATIONS_TABLE + ";";

        Cursor cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                tomek.cukrzyca.Notifications.Notification notification = new tomek.cukrzyca.Notifications.Notification(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4) == 1,
                        new boolean[]{
                                cursor.getInt(5) == 1,
                                cursor.getInt(6) == 1,
                                cursor.getInt(7) == 1,
                                cursor.getInt(8) == 1,
                                cursor.getInt(9) == 1,
                                cursor.getInt(10) == 1,
                                cursor.getInt(11) == 1
                        });

                notificationList.add(notification);
            } while (cursor.moveToNext());
        }

        sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_TABLE + ";";

        cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                recommendationsList.add(new Recommendation(cursor.getInt(0),
                        cursor.getString(2), cursor.getString(1), cursor.getFloat(3)));
            } while (cursor.moveToNext());
        }

        sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_SETTINGS + ";";

        cursor = db.rawQuery(sQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getInt(0) == 1) {
                recommendationsOn = true;
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        final int interval = 1000 * 60;

        final Handler mHandler = new Handler();

        new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, interval);
                Calendar cal = Calendar.getInstance();

                String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(cal.get(Calendar.MINUTE));
                int day = cal.get(Calendar.DAY_OF_WEEK);

                day -= 2;

                if (day < 0) {
                    day = 6;
                }

                for (tomek.cukrzyca.Notifications.Notification n : notificationList) {
                    String time = n.getTime();

                    String arr[] = time.split(":");

                    if (hour.equals(arr[0]) && minute.equals(arr[1]) && n.getDays()[day] && n.isActive()) {
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                        mBuilder.setDefaults(Notification.DEFAULT_ALL);
                        mBuilder.setSmallIcon(R.drawable.ic_stat_maps_local_restaurant);
                        mBuilder.setContentTitle(time + " " + types[n.getType()]);
                        mBuilder.setContentText(n.getName());
                        mBuilder.setWhen(System.currentTimeMillis());
                        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                        mBuilder.setAutoCancel(true);

                        NotificationManager myNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        myNotifyMgr.notify(n.getId(), mBuilder.build());
                    }
                }
                if (recommendationsOn) {
                    for (Recommendation r : recommendationsList) {
                        String time = r.getTime();

                        String arr[] = time.split(":");

                        if (hour.equals(arr[0]) && minute.equals(arr[1])) {
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                            mBuilder.setDefaults(Notification.DEFAULT_ALL);
                            mBuilder.setSmallIcon(R.drawable.ic_stat_maps_local_restaurant);
                            mBuilder.setContentTitle(time + " " + r.getName());
                            mBuilder.setContentText(String.valueOf(r.getCarbohydrates()));
                            mBuilder.setWhen(System.currentTimeMillis());
                            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                            mBuilder.setAutoCancel(true);

                            NotificationManager myNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            myNotifyMgr.notify(r.getId(), mBuilder.build());
                        }
                    }
                }

            }
        }.run();
    }
}
