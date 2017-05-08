package tomek.cukrzyca.Notifications;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;


public class AddNewNotificationIntent extends TimePickerDialog {


    private int hour;
    private int min;
    private boolean[] days = {false, false, false, false, false, false, false};

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

    AddNewNotificationIntent(final Context context,
                             int hour, int min, final ViewPager viewPager) {
        super(context, null, 0, 0, true);

        this.hour = hour;
        this.min = min;

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
        editText.setId(1999);
        editText.setLayoutParams(editTextParams);
        TextViewCompat.setTextAppearance(editText, FontCache.TEXT_APPEARANCE);
        editText.setTypeface(FontCache.get(context));
        editText.setHint("Nazwa");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);


        RelativeLayout.LayoutParams spinnerLayoutParams = new RelativeLayout.LayoutParams(
                300,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        spinnerLayoutParams.addRule(RelativeLayout.BELOW, 1999);
        spinnerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        spinnerLayoutParams.topMargin = 30;

        final Spinner spinner = new Spinner(context);
        spinner.setLayoutParams(spinnerLayoutParams);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.notificationTypes)) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(FontCache.get(context));
                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);

                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTypeface(FontCache.get(context));
                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);

                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tableLayout.addView(tableRow1);
        tableLayout.addView(tableRow2);
        tableLayout.setStretchAllColumns(true);
        relativeLayout.addView(view);
        relativeLayout.addView(tableLayout);
        relativeLayout.addView(editText);
        relativeLayout.addView(spinner);
        setView(relativeLayout);

        setButton(BUTTON_POSITIVE, "Dodaj", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String time = getHour() + ":" + getMin();
                String name = editText.getText().toString();
                int type = spinner.getSelectedItemPosition();
                StringBuilder sQuery = new StringBuilder();

                sQuery.append("INSERT INTO ").append(Database.NOTIFICATIONS_TABLE)
                        .append("(time, name, type, active, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES('")
                        .append(time).append("', '").append(name).append("', ").append(type).append(", ").append(0).append(", ");


                for(i = 0; i < 7; i++) {
                    sQuery.append(getDays()[i] ? 1 : 0).append(", ");
                }

                sQuery.delete(sQuery.length() - 2, sQuery.length() - 1);
                sQuery.append(");");

                MainActivity.db.exec(sQuery.toString());

                viewPager.getAdapter().notifyDataSetChanged();

                NotificationService.refreshTables();

                MyToast.show(getContext(), "Dodano powiadomienie");
            }
        });

        setButton(BUTTON_NEGATIVE, "Anuluj", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


    }

}
