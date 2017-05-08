package tomek.cukrzyca.Fragments.Calculator;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class CalcFourthStepFragment extends Fragment {

    public CalcFourthStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calc_fourth_step, container, false);

        final Button bSave = (Button) view.findViewById(R.id.button28);
        final Button bBack = (Button) view.findViewById(R.id.button27);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);

        bSave.setTypeface(FontCache.get(getContext()));
        bBack.setTypeface(FontCache.get(getContext()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.accuracies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sQuery = "DELETE FROM " + Database.CALC_SETTINGS_TABLE + ";";

                MainActivity.db.exec(sQuery);

                sQuery = "DELETE FROM " + Database.CALC_GLYCEMIA_TABLE + ";";

                MainActivity.db.exec(sQuery);

                sQuery = "DELETE FROM " + Database.CALC_INSULIN_RESISTANCE_TABLE + ";";

                MainActivity.db.exec(sQuery);

                sQuery = "DELETE FROM " + Database.CALC_INSULIN_WW_TABLE + ";";

                MainActivity.db.exec(sQuery);

                float x = Float.parseFloat(spinner.getSelectedItem().toString());

                sQuery = "INSERT INTO " + Database.CALC_SETTINGS_TABLE + "(accuracy) VALUES(" + x + ");";

                MainActivity.db.exec(sQuery);

                String start, stop;
                int y;

                for(int i = 0; i < CalcFirstStepFragment.start.size(); i++) {
                    start = CalcFirstStepFragment.start.get(i);
                    stop = CalcFirstStepFragment.stop.get(i);
                    y = CalcFirstStepFragment.amount.get(i);

                    sQuery = "INSERT INTO " + Database.CALC_GLYCEMIA_TABLE + "(time_start, time_stop, pointer) VALUES('" +
                            start + "', '" + stop + "', " + y + ");";

                    MainActivity.db.exec(sQuery);
                }

                for(int i = 0; i < CalcSecondStepFragment.start.size(); i++) {
                    start = CalcSecondStepFragment.start.get(i);
                    stop = CalcSecondStepFragment.stop.get(i);
                    y = CalcSecondStepFragment.amount.get(i);

                    sQuery = "INSERT INTO " + Database.CALC_INSULIN_RESISTANCE_TABLE + "(time_start, time_stop, pointer) VALUES('" +
                            start + "', '" + stop + "', " + y + ");";

                    MainActivity.db.exec(sQuery);
                }

                for(int i = 0; i < CalcThirdStepFragment.start.size(); i++) {
                    start = CalcThirdStepFragment.start.get(i);
                    stop = CalcThirdStepFragment.stop.get(i);
                    y = CalcThirdStepFragment.amount.get(i);

                    sQuery = "INSERT INTO " + Database.CALC_INSULIN_WW_TABLE + "(time_start, time_stop, pointer) VALUES('" +
                            start + "', '" + stop + "', " + y + ");";

                    MainActivity.db.exec(sQuery);
                }

                if(MainActivity.fromCalc) {
                    getFragmentManager().popBackStack("calcFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    getFragmentManager().popBackStack("settingsFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                MyToast.show(getContext(), "Pomyślnie zapisano ustawienia");
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        String sQuery = "SELECT accuracy FROM " + Database.CALC_SETTINGS_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            float x = cursor.getFloat(0);
            System.out.println(x);

            if(x == 0.1) {
                spinner.setSelection(0);
            } else if(x == 0.5) {
                spinner.setSelection(1);
            } else if(x == 1.0){
                spinner.setSelection(2);
            }
        }

        return view;
    }

}
