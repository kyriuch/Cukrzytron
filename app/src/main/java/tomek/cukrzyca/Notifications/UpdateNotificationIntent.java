package tomek.cukrzyca.Notifications;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;


class UpdateNotificationIntent extends TimePickerDialog {

    private int hour;
    private int min;
    private boolean[] days;

    private int getHour() {
        return hour;
    }

    private void setHour(int hour) {
        this.hour = hour;
    }

    private int getMin() {
        return min;
    }

    private void setMin(int min) {
        this.min = min;
    }

    private boolean[] getDays() {
        return days;
    }

    UpdateNotificationIntent(Context context,
                             int hour, int min, final Notification notification,
                             final NotificationAdapter notificationAdapter) {
        super(context, null, 0, 0, true);

        this.hour = hour;
        this.min = min;
        this.days = notification.getDays();

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams mainLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        relativeLayout.setLayoutParams(mainLayoutParams);

        RelativeLayout.LayoutParams pickerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        pickerParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TimePicker view = (TimePicker) inflater.inflate(R.layout.spinner_time_picker, null);
        view.setCurrentHour(hour);
        view.setCurrentMinute(min);
        view.setLayoutParams(pickerParams);
        view.setIs24HourView(true);
        view.setId(1997);
        view.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                setHour(i);
                setMin(i1);
            }
        });

        RelativeLayout.LayoutParams tableRelativeLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        tableRelativeLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        tableRelativeLayout.addRule(RelativeLayout.BELOW, 1997);
        tableRelativeLayout.bottomMargin = 30;
        tableRelativeLayout.leftMargin = 40;
        tableRelativeLayout.rightMargin = 40;

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setId(1998);
        tableLayout.setLayoutParams(tableRelativeLayout);

        TableRow tableRow1 = new TableRow(context);

        TableRow.LayoutParams dayParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        dayParams.leftMargin = 15;

        for(String day:new String[]{"Pn", "Wt", "Śr", "Czw", "Pt", "Sb", "Ndz"}) {
            NiceTextView textViewDay = new NiceTextView(context);
            textViewDay.setTypeface(true);
            textViewDay.setText(day);
            textViewDay.setLayoutParams(dayParams);
            tableRow1.addView(textViewDay);
        }

        TableRow.LayoutParams checkBoxesParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        TableRow tableRow2 = new TableRow(context);

        for(int i = 0; i < 7; i++) {
            final int pos = i;
            CheckBox checkBox = new CheckBox(context);
            checkBox.setLayoutParams(checkBoxesParams);
            checkBox.setChecked(notification.getDays()[i]);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    days[pos] = b;
                }
            });

            tableRow2.addView(checkBox);
        }

        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                300,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        editTextParams.addRule(RelativeLayout.BELOW, 1998);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        final EditText editText = new EditText(context);
        editText.setLayoutParams(editTextParams);
        TextViewCompat.setTextAppearance(editText, FontCache.TEXT_APPEARANCE);
        editText.setTypeface(FontCache.get(context));
        editText.setText(notification.getName());
        editText.setHint("Nazwa");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        tableLayout.addView(tableRow1);
        tableLayout.addView(tableRow2);
        tableLayout.setStretchAllColumns(true);
        relativeLayout.addView(view);
        relativeLayout.addView(tableLayout);
        relativeLayout.addView(editText);
        setView(relativeLayout);

        setButton(BUTTON_POSITIVE, "Aktualizuj", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String time = getHour() + ":" + getMin();
                String name = editText.getText().toString();
                StringBuilder sQuery = new StringBuilder();
                sQuery.append("UPDATE ").append(Database.NOTIFICATIONS_TABLE)
                        .append(" SET name = '").append(name)
                        .append("', time = '").append(time)
                        .append("', ");

                String days[] = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};

                for(i = 0; i < 7; i++) {
                    sQuery.append(days[i]).append(" = ").append(getDays()[i] ? 1 : 0).append(", ");
                }

                sQuery.delete(sQuery.length() - 2, sQuery.length() - 1);
                sQuery.append(" WHERE _id = ").append(notification.getId()).append(";");

                MainActivity.db.exec(sQuery.toString());
                notification.setTime(time);
                notification.setName(name);
                notification.setDays(getDays());
                notificationAdapter.notifyDataSetChanged();

                NotificationService.refreshTables();

                MyToast.show(getContext(), "Zaktualizowano powiadomienie");

            }
        });

        setButton(BUTTON_NEGATIVE, "Usuń", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sQuery = "DELETE FROM " + Database.NOTIFICATIONS_TABLE + " WHERE _id = " +
                        notification.getId() + ";";

                MainActivity.db.exec(sQuery);

                notificationAdapter.remove(notification);

                NotificationService.refreshTables();

                MyToast.show(getContext(), "Usunięto powiadomienie");
            }
        });


    }


}
