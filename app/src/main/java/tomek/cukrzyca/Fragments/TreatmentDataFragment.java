package tomek.cukrzyca.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class TreatmentDataFragment extends Fragment {


    public TreatmentDataFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "treatmentDataFragment";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_data, container, false);

        final Spinner spinnerBaza = (Spinner) view.findViewById(R.id.spinner3);
        final Spinner spinnerBolus = (Spinner) view.findViewById(R.id.spinner4);

        final Button saveButton = (Button) view.findViewById(R.id.button5);
        saveButton.setTypeface(FontCache.get(getContext()));

        final EditText type = (EditText) view.findViewById(R.id.editText35);
        final EditText insulinDose = (EditText) view.findViewById(R.id.editText36);
        final EditText pressureFrom1 = (EditText) view.findViewById(R.id.editText42);
        final EditText pressureFrom2 = (EditText) view.findViewById(R.id.editText2);
        final EditText pressureFrom3 = (EditText) view.findViewById(R.id.editText10);
        final EditText pressureTo1 = (EditText) view.findViewById(R.id.editText39);
        final EditText pressureTo2 = (EditText) view.findViewById(R.id.editText40);
        final EditText pressureTo3 = (EditText) view.findViewById(R.id.editText41);
        final EditText weightFrom = (EditText) view.findViewById(R.id.editText37);
        final EditText weightTo = (EditText) view.findViewById(R.id.editText38);
        final EditText hba1cFrom = (EditText) view.findViewById(R.id.editText);
        final EditText hba1cTo = (EditText) view.findViewById(R.id.editText24);

        insulinDose.setNextFocusDownId(R.id.editText42);
        pressureFrom1.setNextFocusDownId(R.id.editText2);
        pressureFrom2.setNextFocusDownId(R.id.editText10);
        pressureFrom3.setNextFocusDownId(R.id.editText39);
        pressureTo1.setNextFocusDownId(R.id.editText40);
        pressureTo2.setNextFocusDownId(R.id.editText41);
        pressureTo3.setNextFocusDownId(R.id.editText37);
        weightFrom.setNextFocusDownId(R.id.editText38);
        weightTo.setNextFocusDownId(R.id.editText);
        hba1cFrom.setNextFocusDownId(R.id.editText24);

        ArrayAdapter<CharSequence> adapterBaza = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bazaTypes)) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);


                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));

                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));

                return v;
            }
        };

        adapterBaza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBaza.setAdapter(adapterBaza);

        ArrayAdapter<CharSequence> adapterBolus = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bolusType)) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);


                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));

                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));

                return v;
            }
        };

        adapterBolus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBolus.setAdapter(adapterBolus);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sType, sInsulinDose, sPressureFrom1, sPressureFrom2,
                        sPressureFrom3, sPressureTo1, sPressureTo2, sPressureTo3, sWeightFrom,
                        sWeightTo, sHba1cFrom, sHba1cTo;

                sType = type.getText().toString().trim();
                sInsulinDose = insulinDose.getText().toString().trim();
                int iBazaType = spinnerBaza.getSelectedItemPosition();
                int iBolusType = spinnerBolus.getSelectedItemPosition();
                sPressureFrom1 = pressureFrom1.getText().toString().trim();
                sPressureFrom2 = pressureFrom2.getText().toString().trim();
                sPressureFrom3 = pressureFrom3.getText().toString().trim();
                sPressureTo1 = pressureTo1.getText().toString().trim();
                sPressureTo2 = pressureTo2.getText().toString().trim();
                sPressureTo3 = pressureTo3.getText().toString().trim();
                sWeightFrom = weightFrom.getText().toString().trim();
                sWeightTo = weightTo.getText().toString().trim();
                sHba1cFrom = hba1cFrom.getText().toString().trim();
                sHba1cTo = hba1cTo.getText().toString().trim();

                if(sType.isEmpty() || sInsulinDose.isEmpty() || sPressureFrom1.isEmpty() ||
                        sPressureFrom2.isEmpty() || sPressureFrom3.isEmpty() ||
                        sPressureTo1.isEmpty() || sPressureTo2.isEmpty() ||
                        sPressureTo3.isEmpty() || sWeightFrom.isEmpty() ||
                        sWeightTo.isEmpty() || sHba1cFrom.isEmpty() ||
                        sHba1cTo.isEmpty()) {
                    MyToast.show(getContext(), "Uzupełnij błędne/puste pola");

                    return;
                }

                String sQuery = "UPDATE " + Database.SETTINGS_TREATMENT_TABLE + " SET type = '" +
                        sType + "', bazaType = " + iBazaType + ", bolusType = " +
                        iBolusType + ", insulinDose = " + Float.parseFloat(sInsulinDose) + ", pressureFrom1 = " +
                        sPressureFrom1 + ", pressureFrom2 = " + sPressureFrom2 + ", pressureFrom3 = " +
                        sPressureFrom3 + ", pressureTo1 = " + sPressureTo1 + ", PressureTo2 = " +
                        sPressureTo2 + ", pressureTo3 = " + sPressureTo3 + ", weightFrom = " +
                        Float.parseFloat(sWeightFrom) + ", weightTo = " + Float.parseFloat(sWeightTo) + ", hba1cFrom = " + Float.parseFloat(sHba1cFrom) +
                        ", hba1cTo = " + Float.parseFloat(sHba1cTo) + ";";

                MainActivity.db.exec(sQuery);
                MyToast.show(getContext(), "Zaktualizowano dane");
            }
        };

        String sQuery = "SELECT * FROM " + Database.SETTINGS_TREATMENT_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            type.setText(cursor.getString(1));
            spinnerBaza.setSelection(cursor.getInt(2));
            spinnerBolus.setSelection(cursor.getInt(3));
            insulinDose.setText(String.valueOf(cursor.getFloat(4)));
            pressureFrom1.setText(String.valueOf(cursor.getInt(5)));
            pressureFrom2.setText(String.valueOf(cursor.getInt(6)));
            pressureFrom3.setText(String.valueOf(cursor.getInt(7)));
            pressureTo1.setText(String.valueOf(cursor.getInt(8)));
            pressureTo2.setText(String.valueOf(cursor.getInt(9)));
            pressureTo3.setText(String.valueOf(cursor.getInt(10)));
            weightFrom.setText(String.valueOf(cursor.getFloat(11)));
            weightTo.setText(String.valueOf(cursor.getFloat(12)));
            hba1cFrom.setText(String.valueOf(cursor.getFloat(13)));
            hba1cTo.setText(String.valueOf(cursor.getFloat(14)));

            cursor.close();

            saveButton.setOnClickListener(onClickListener);
        } else {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sType, sInsulinDose, sPressureFrom1, sPressureFrom2,
                            sPressureFrom3, sPressureTo1, sPressureTo2, sPressureTo3, sWeightFrom,
                            sWeightTo, sHba1cFrom, sHba1cTo;

                    sType = type.getText().toString().trim();
                    sInsulinDose = insulinDose.getText().toString().trim();
                    int iBazaType = spinnerBaza.getSelectedItemPosition();
                    int iBolusType = spinnerBolus.getSelectedItemPosition();
                    sPressureFrom1 = pressureFrom1.getText().toString().trim();
                    sPressureFrom2 = pressureFrom2.getText().toString().trim();
                    sPressureFrom3 = pressureFrom3.getText().toString().trim();
                    sPressureTo1 = pressureTo1.getText().toString().trim();
                    sPressureTo2 = pressureTo2.getText().toString().trim();
                    sPressureTo3 = pressureTo3.getText().toString().trim();
                    sWeightFrom = weightFrom.getText().toString().trim();
                    sWeightTo = weightTo.getText().toString().trim();
                    sHba1cFrom = hba1cFrom.getText().toString().trim();
                    sHba1cTo = hba1cTo.getText().toString().trim();

                    if(sType.isEmpty() || sInsulinDose.isEmpty() || sPressureFrom1.isEmpty() ||
                            sPressureFrom2.isEmpty() || sPressureFrom3.isEmpty() ||
                            sPressureTo1.isEmpty() || sPressureTo2.isEmpty() ||
                            sPressureTo3.isEmpty() || sWeightFrom.isEmpty() ||
                            sWeightTo.isEmpty() || sHba1cFrom.isEmpty() ||
                            sHba1cTo.isEmpty()) {
                        MyToast.show(getContext(), "Uzupełnij błędne/puste pola");

                        return;
                    }

                    String sQuery = "INSERT INTO " + Database.SETTINGS_TREATMENT_TABLE + "(type, " +
                            "bazaType, bolusType, insulinDose, pressureFrom1, pressureFrom2, " +
                            "pressureFrom3, pressureTo1, pressureTo2, pressureTo3, weightFrom, " +
                            "weightTo, hba1cFrom, hba1cTo) VALUES('" + sType + "', " + iBazaType +
                            ", " + iBolusType + ", " + Float.parseFloat(sInsulinDose) + ", " + sPressureFrom1 + ", " +
                            sPressureFrom2 + ", " + sPressureFrom3 + ", " + sPressureTo1 + ", " +
                            sPressureTo2 + ", " + sPressureTo3 + ", " + Float.parseFloat(sWeightFrom) + ", " +
                            Float.parseFloat(sWeightTo) + ", " + Float.parseFloat(sHba1cFrom) + ", " + Float.parseFloat(sHba1cTo) + ");";


                    MainActivity.db.exec(sQuery);
                    MyToast.show(getContext(), "Zaktualizowano dane");

                    getActivity().onBackPressed();
                }
            });
        }

        return view;
    }

}
