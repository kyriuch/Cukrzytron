package tomek.cukrzyca.Notifications;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

public class OthersFragment extends Fragment {

    public static OthersFragment newInstance() {
        return new OthersFragment();
    }

    public OthersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createListView(ListView listView, final Context context) {
        final ArrayList<Notification> notificationList = new ArrayList<>();
        String sQuery = "SELECT * FROM " + Database.NOTIFICATIONS_TABLE + " WHERE type = ?;";

        Cursor cursor = MainActivity.db.query(sQuery, new String[]{String.valueOf(3)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(cursor.getInt(0),
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

        if (!notificationList.isEmpty()) {
            final NotificationAdapter notificationAdapter = new NotificationAdapter(context, R.layout.notifaction_list_item, notificationList);
            listView.setAdapter(notificationAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Notification currentNotification = notificationList.get(i);
                    SwitchCompat mySwitch = (SwitchCompat) view.findViewById(R.id.switch1);
                    StringBuilder sQuery = new StringBuilder();
                    sQuery.append("UPDATE ").append(Database.NOTIFICATIONS_TABLE).append(" SET active = ");

                    if(currentNotification.isActive()) {
                        sQuery.append("0 WHERE _id = ").append(currentNotification.getId()).append(";");
                    } else {
                        sQuery.append("1 WHERE _id = ").append(currentNotification.getId()).append(";");
                    }

                    MainActivity.db.exec(sQuery.toString());
                    currentNotification.setActive(!currentNotification.isActive());
                    mySwitch.setChecked(currentNotification.isActive());

                    NotificationService.refreshTables();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Notification currentNotification = notificationList.get(i);

                    String[] splitTime = currentNotification.getTime().split(":");
                    int hour = Integer.parseInt(splitTime[0]);
                    int min = Integer.parseInt(splitTime[1]);

                    final UpdateNotificationIntent updateNotificationIntent = new UpdateNotificationIntent(
                            context, hour, min, currentNotification, notificationAdapter);
                    updateNotificationIntent.show();

                    return true;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, container, false);
        final Context context = getContext();
        final ListView listView = (ListView) view.findViewById(R.id.othersListView);
        createListView(listView, context);

        return view;
    }
}
