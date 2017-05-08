package tomek.cukrzyca.Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import java.util.ArrayList;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.Notifications.NotificationService;
import tomek.cukrzyca.R;


class UpdateRecommendationIntent extends TimePickerDialog {


    private int hour;
    private int min;

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

    UpdateRecommendationIntent(final Context context,
                               final ArrayList<Recommendation> recommendationArrayList,
                               final int position,
                               final RecommendationAdapter recommendationAdapter) {
        super(context, null, 0, 0, true);

        String[] times = recommendationArrayList.get(position).getTime().split(":");

        this.min = Integer.parseInt(times[1]);
        this.hour = Integer.parseInt(times[0]);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams mainLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        relativeLayout.setLayoutParams(mainLayoutParams);

        RelativeLayout.LayoutParams pickerParams = new RelativeLayout.LayoutParams(
                1000,
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

        view.setCurrentHour(this.hour);
        view.setCurrentMinute(this.min);

        RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        editTextParams.addRule(RelativeLayout.BELOW, 1997);
        editTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        editTextParams.leftMargin = 40;
        editTextParams.rightMargin = 40;

        final EditText eName = new EditText(context);
        eName.setId(1999);
        eName.setLayoutParams(editTextParams);
        TextViewCompat.setTextAppearance(eName, FontCache.TEXT_APPEARANCE);
        eName.setTypeface(FontCache.get(context));
        eName.setHint("Nazwa posiłku");
        eName.setInputType(InputType.TYPE_CLASS_TEXT);

        RelativeLayout.LayoutParams editTextParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        editTextParams2.addRule(RelativeLayout.BELOW, 1999);
        editTextParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        editTextParams2.bottomMargin = 30;
        editTextParams2.leftMargin = 40;
        editTextParams2.rightMargin = 40;

        final EditText eCarbohydrates = new EditText(context);
        eCarbohydrates.setLayoutParams(editTextParams2);
        TextViewCompat.setTextAppearance(eCarbohydrates, FontCache.TEXT_APPEARANCE);
        eCarbohydrates.setTypeface(FontCache.get(context));
        eCarbohydrates.setHint("kcal");
        eCarbohydrates.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        eName.setText(recommendationArrayList.get(position).getName());
        eCarbohydrates.setText(String.valueOf(recommendationArrayList.get(position).getCarbohydrates()));

        relativeLayout.addView(view);
        relativeLayout.addView(eName);
        relativeLayout.addView(eCarbohydrates);
        setView(relativeLayout);

        setButton(BUTTON_POSITIVE, "Aktualizuj", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String time = getHour() + ":" + getMin();
                String name = eName.getText().toString();
                String carbohydrates = eCarbohydrates.getText().toString();


                String sQuery = "UPDATE " + Database.RECOMMENDATIONS_TABLE +
                        " SET title = '" + name + "', time = '" + time + "', carbohydrates = " +
                        carbohydrates + " WHERE _id = " + recommendationArrayList.get(position)
                        .getId() + ";";

                MainActivity.db.exec(sQuery);

                recommendationArrayList.get(position).setCarbohydrates(Float.parseFloat(carbohydrates));
                recommendationArrayList.get(position).setName(name);
                recommendationArrayList.get(position).setTime(time);

                recommendationAdapter.notifyDataSetChanged();

                NotificationService.refreshTables();

                MyToast.show(getContext(), "Zaktualizowano zalecenie");
            }
        });

        setButton(BUTTON_NEGATIVE, "Usuń", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sQuery = "DELETE FROM " + Database.RECOMMENDATIONS_TABLE + " WHERE _id = " +
                        recommendationArrayList.get(position).getId();

                recommendationArrayList.remove(position);
                recommendationAdapter.notifyDataSetChanged();

                MainActivity.db.exec(sQuery);

                NotificationService.refreshTables();

                MyToast.show(getContext(), "Usunięto zalecenie");
            }
        });


    }

}
