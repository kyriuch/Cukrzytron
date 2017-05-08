package tomek.cukrzyca.Notifications;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;

public class NotificationAdapter extends ArrayAdapter<Notification>{

    public NotificationAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NotificationAdapter(Context context, int resource, List<Notification> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(Notification item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.notifaction_list_item, null);
        }

        Notification not = getItem(position);

        if(not != null && view != null) {
            NiceTextView tTime = (NiceTextView) view.findViewById(R.id.textView51);
            NiceTextView tName = (NiceTextView) view.findViewById(R.id.textView55);
            SwitchCompat mySwitch = (SwitchCompat) view.findViewById(R.id.switch1);

            if(tTime != null) {
                String spreadTime[] = not.getTime().split(":");
                String hour = spreadTime[0];
                String min = spreadTime[1];

                if(hour.length() == 1) {
                    hour = "0" + hour;
                }

                if(min.length() == 1) {
                    min = "0" + min;
                }

                tTime.setText(hour + ":" + min);
            }

            if(tName != null) {
                tName.setText(not.getName());
            }

            if(mySwitch!= null) {
                mySwitch.setClickable(false);

                if(not.isActive()) {
                    mySwitch.setChecked(true);
                } else {
                    mySwitch.setChecked(false);
                }
            }
        }

        setNotifyOnChange(true);

        return view;
    }

    
}
