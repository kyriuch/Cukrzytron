package tomek.cukrzyca.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class PopFragment extends Fragment {


    public PopFragment() {
    }

    public void onResume() {
        super.onResume();
        MainActivity.currentFragment = "popFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pop, container, false);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupPop);

        getActivity().setTitle("Dane z pena");

        final TextView tAmount = (TextView) view.findViewById(R.id.textView83);
        tAmount.setText(String.valueOf(MainFragment.input));

        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl-PL"));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", new Locale("pl-PL"));

        final TextView tDate = (TextView) view.findViewById(R.id.textView70);
        final TextView tHour = (TextView) view.findViewById(R.id.textView81);
        tDate.setText(dateFormat.format(cal.getTime()));
        tHour.setText(hourFormat.format(cal.getTime()));

        Button bBack = (Button) view.findViewById(R.id.button3);
        Button bSave = (Button) view.findViewById(R.id.button4);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));

                if(type == -1) {
                    MyToast.show(getContext(), "Wybierz rodzaj dawki");

                    return;
                }

                String sQuery = "INSERT INTO " + Database.POP_ENTRIES_TABLE + "(date, time, type, " +
                        "amount) VALUES('" + tDate.getText().toString().replace('-', ':') + "', '" +
                        tHour.getText().toString() + "', " + type + ", " + Float.parseFloat(tAmount.getText().toString()) +
                        ");";

                MainActivity.db.exec(sQuery);

                MyToast.show(getContext(), "Pomyślnie zapisano dawkę");
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
