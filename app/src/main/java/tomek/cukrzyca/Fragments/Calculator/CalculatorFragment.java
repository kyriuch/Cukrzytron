package tomek.cukrzyca.Fragments.Calculator;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.Fragments.HistoryFragment;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class CalculatorFragment extends Fragment {

    private float accuracy;
    private int glycemiaPointer, insulinResistancePointer, insulinWwPointer;

    private EditText eSuggestedDose;
    private EditText eTakenDose;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "calcFragment";
    }

    private double countDose(String sActiveInsulin, String sGlycemia, String sCarbohydrates) {
        int activeInsulin = 0, glycemia = 0;
        float carbohydrates = 0;
        double score, doseG = 0, doseW = 0;

        if (!sActiveInsulin.trim().isEmpty()) {
            activeInsulin = Integer.parseInt(sActiveInsulin);
        }

        if (!sGlycemia.trim().isEmpty()) {
            glycemia = Integer.parseInt(sGlycemia);
        }

        if (!sCarbohydrates.trim().isEmpty()) {
            carbohydrates = Float.parseFloat(sCarbohydrates);
        }

        if (glycemia > 0 && insulinResistancePointer > 0) {
            doseG = (double) ((glycemia - glycemiaPointer) / insulinResistancePointer);
        }

        if (carbohydrates > 0 && insulinWwPointer > 0) {
            doseW = (double) (carbohydrates / insulinWwPointer);
        }

        if (doseG > 0 & doseW > 0) {
            score = doseG + doseW - activeInsulin;
        } else if (doseG > 0) {
            score = doseG;
        } else {
            score = doseW;
        }

        DecimalFormat df = new DecimalFormat();

        if (accuracy == 0.1f) {
            df.setMaximumFractionDigits(1);
            score = Double.parseDouble(df.format(score).replace(",", ".").replaceAll("\\s", ""));
        } else if (accuracy == 0.5f) {
            score = Math.round(score * 2) / 2.0;
        } else {
            df.setMaximumFractionDigits(0);
            score = Double.parseDouble(df.format(score).replace(",", ".").replaceAll("\\s", ""));
        }

        return score;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_calc, container, false);

        getActivity().setTitle("Kalkulator dawki");

        String sQuery = "SELECT * FROM " + Database.CALC_SETTINGS_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor == null || !cursor.moveToFirst()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Nie skonfigurowano kalkulatora.")
                    .setCancelable(false)
                    .setPositiveButton("Skonfiguruj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.fromCalc = true;
                            CalcFirstStepFragment fragment = new CalcFirstStepFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                            fragmentTransaction.commit();
                        }
                    })
                    .setNegativeButton("Wróć", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().onBackPressed();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        final TextView tDate = (TextView) view.findViewById(R.id.textView60);
        final TextView tHour = (TextView) view.findViewById(R.id.textView61);
        final Button bBack = (Button) view.findViewById(R.id.button6);
        final Button bSave = (Button) view.findViewById(R.id.button7);
        final EditText eActiveInsulin = (EditText) view.findViewById(R.id.editText13);
        final EditText eGlycemia = (EditText) view.findViewById(R.id.editText14);
        final EditText eCarbohydrates = (EditText) view.findViewById(R.id.editText15);
        eSuggestedDose = (EditText) view.findViewById(R.id.editText16);
        eTakenDose = (EditText) view.findViewById(R.id.editText17);

        Context context = getContext();

        tDate.setTypeface(FontCache.get(context));
        tHour.setTypeface(FontCache.get(context));
        bBack.setTypeface(FontCache.get(context));
        bSave.setTypeface(FontCache.get(context));
        eActiveInsulin.setTypeface(FontCache.get(context));
        eGlycemia.setTypeface(FontCache.get(context));
        eCarbohydrates.setTypeface(FontCache.get(context));
        eSuggestedDose.setTypeface(FontCache.get(context));
        eTakenDose.setTypeface(FontCache.get(context));


        eSuggestedDose.setFocusable(false);

        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("polish"));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", new Locale("polish"));
        tDate.setText(dateFormat.format(cal.getTime()));
        tHour.setText(hourFormat.format(cal.getTime()));


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                tDate.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year));
            }
        };

        tDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                tHour.setText(new StringBuilder().append(i).append(":").append(i1));

                String sQuery = "SELECT accuracy FROM " + Database.CALC_SETTINGS_TABLE + ";";

                Cursor cursor = MainActivity.db.query(sQuery);

                if (cursor != null && cursor.moveToFirst()) {
                    String x = String.valueOf(cursor.getFloat(0));

                    Log.d("Accuracy", x);
                }

                sQuery = "SELECT pointer FROM " + Database.CALC_GLYCEMIA_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

                cursor = MainActivity.db.query(sQuery);

                if (cursor != null && cursor.moveToFirst()) {
                    glycemiaPointer = cursor.getInt(0);
                }

                sQuery = "SELECT pointer FROM " + Database.CALC_INSULIN_RESISTANCE_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

                cursor = MainActivity.db.query(sQuery);

                if (cursor != null && cursor.moveToFirst()) {
                    insulinResistancePointer = cursor.getInt(0);
                }

                sQuery = "SELECT pointer FROM " + Database.CALC_INSULIN_WW_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

                cursor = MainActivity.db.query(sQuery);

                if (cursor != null && cursor.moveToFirst()) {
                    insulinWwPointer = cursor.getInt(0);
                }
            }
        };

        tHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        if (HistoryFragment.id == -1) {
            bSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sActiveInsulin = eActiveInsulin.getText().toString().trim();
                    String sGlycemia = eGlycemia.getText().toString().trim();
                    String sCarbohydrates = eCarbohydrates.getText().toString().trim();
                    String sDose = eTakenDose.getText().toString().trim();

                    if (sDose.isEmpty()) {
                        MyToast.show(getContext(), "Uzupełnij dawkę");

                        return;
                    }

                    int iActiveInsulin;
                    int iGlycemia;
                    float fCarbohydrates;

                    if (!sActiveInsulin.isEmpty()) {
                        iActiveInsulin = Integer.parseInt(sActiveInsulin);

                        if (iActiveInsulin < 1) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    if (!sGlycemia.isEmpty()) {
                        iGlycemia = Integer.parseInt(sGlycemia);

                        if (iGlycemia < 1) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    if (!sCarbohydrates.isEmpty()) {
                        fCarbohydrates = Float.parseFloat(sCarbohydrates);

                        if (fCarbohydrates <= 0.0) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    float fDose = Float.parseFloat(sDose);

                    if (fDose <= 0.0) {
                        MyToast.show(getContext(), "Podaj wartości większe od 0");

                        return;
                    }

                    String sQuery = "INSERT INTO " + Database.CALC_ENTRIES_TABLE + "(date, time, " +
                            "activeInsulin, glycemia, carbohydrates, dose) VALUES('" +
                            tDate.getText().toString().replace('-', ':') + "', '" + tHour.getText().toString() +
                            "', ";

                    if (!sActiveInsulin.isEmpty()) {
                        sQuery += sActiveInsulin + ", ";
                    } else {
                        sQuery += "NULL, ";
                    }

                    if (!sGlycemia.isEmpty()) {
                        sQuery += sGlycemia + ", ";
                    } else {
                        sQuery += "NULL, ";
                    }

                    if (!sCarbohydrates.isEmpty()) {
                        sQuery += sCarbohydrates + ", ";
                    } else {
                        sQuery += "NULL, ";
                    }

                    sQuery += fDose + ");";

                    MainActivity.db.exec(sQuery);


                    MyToast.show(getContext(), "Pomyślnie zapisano dawkę");
                    getActivity().onBackPressed();
                }
            });
        } else {
            bSave.setText("Aktualizuj");

            sQuery = "SELECT * FROM " + Database.CALC_ENTRIES_TABLE + " WHERE _id = " +
                    HistoryFragment.id + ";";

            cursor = MainActivity.db.query(sQuery);

            if (cursor != null && cursor.moveToFirst()) {
                tDate.setText(cursor.getString(1).replace(":", "-"));
                tHour.setText(cursor.getString(2));
                eGlycemia.setText(cursor.getString(4));
                eCarbohydrates.setText(cursor.getString(5));
                eTakenDose.setText(cursor.getString(6));
            }

            bSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sActiveInsulin = eActiveInsulin.getText().toString().trim();
                    String sGlycemia = eGlycemia.getText().toString().trim();
                    String sCarbohydrates = eCarbohydrates.getText().toString().trim();
                    String sDose = eTakenDose.getText().toString().trim();

                    if (sDose.isEmpty()) {
                        MyToast.show(getContext(), "Uzupełnij dawkę");

                        return;
                    }

                    int iActiveInsulin;
                    int iGlycemia;
                    float fCarbohydrates;

                    if (!sActiveInsulin.isEmpty()) {
                        iActiveInsulin = Integer.parseInt(sActiveInsulin);

                        if (iActiveInsulin < 1) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    if (!sGlycemia.isEmpty()) {
                        iGlycemia = Integer.parseInt(sGlycemia);

                        if (iGlycemia < 1) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    if (!sCarbohydrates.isEmpty()) {
                        fCarbohydrates = Float.parseFloat(sCarbohydrates);

                        if (fCarbohydrates <= 0.0) {
                            MyToast.show(getContext(), "Podaj wartości większe od 0");

                            return;
                        }
                    }

                    float fDose = Float.parseFloat(sDose);

                    if (fDose <= 0.0) {
                        MyToast.show(getContext(), "Podaj wartości większe od 0");

                        return;
                    }

                    String sQuery = "UPDATE " + Database.CALC_ENTRIES_TABLE + " SET date = '" +
                            tDate.getText().toString().replace('-', ':') + "', time = '" +
                            tHour.getText().toString() + "', activeInsulin = ";

                    if (!sActiveInsulin.isEmpty()) {
                        sQuery += sActiveInsulin + ", glycemia = ";
                    } else {
                        sQuery += "NULL, glycemia = ";
                    }

                    if (!sGlycemia.isEmpty()) {
                        sQuery += sGlycemia + ", carbohydrates = ";
                    } else {
                        sQuery += "NULL, carbohydrates = ";
                    }

                    if (!sCarbohydrates.isEmpty()) {
                        sQuery += sCarbohydrates + ", dose = ";
                    } else {
                        sQuery += "NULL, dose = ";
                    }

                    sQuery += fDose + " WHERE _id = " + HistoryFragment.id;

                    MainActivity.db.exec(sQuery);

                    MyToast.show(getContext(), "Pomyślnie zaktualizowano");
                    getFragmentManager().popBackStack();
                }
            });
        }

        sQuery = "SELECT accuracy FROM " + Database.CALC_SETTINGS_TABLE + ";";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            accuracy = cursor.getFloat(0);
        }

        sQuery = "SELECT pointer FROM " + Database.CALC_GLYCEMIA_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            glycemiaPointer = cursor.getInt(0);
        }

        sQuery = "SELECT pointer FROM " + Database.CALC_INSULIN_RESISTANCE_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            insulinResistancePointer = cursor.getInt(0);
        }

        sQuery = "SELECT pointer FROM " + Database.CALC_INSULIN_WW_TABLE + " WHERE '" + tHour.getText().toString() + "' BETWEEN time_start AND time_stop;";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            insulinWwPointer = cursor.getInt(0);
        }

        eGlycemia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sActiveInsulin = eActiveInsulin.getText().toString();
                String sGlycemia = eGlycemia.getText().toString();
                String sCarbohydrates = eCarbohydrates.getText().toString();

                double score = countDose(sActiveInsulin, sGlycemia, sCarbohydrates);

                int finalScore = (int) score;

                if (finalScore < 0) {
                    finalScore = 0;
                }

                eSuggestedDose.setText(String.valueOf(finalScore));
                eTakenDose.setText(String.valueOf(finalScore));
            }
        });

        String sActiveInsulin = eActiveInsulin.getText().toString();
        String sGlycemia = eGlycemia.getText().toString();
        String sCarbohydrates = eCarbohydrates.getText().toString();

        double score = countDose(sActiveInsulin, sGlycemia, sCarbohydrates);

        int finalScore = (int) score;

        if (finalScore < 0) {
            finalScore = 0;
        }

        eSuggestedDose.setText(String.valueOf(finalScore));
        eTakenDose.setText(String.valueOf(finalScore));

        eCarbohydrates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sActiveInsulin = eActiveInsulin.getText().toString();
                String sGlycemia = eGlycemia.getText().toString();
                String sCarbohydrates = eCarbohydrates.getText().toString();

                double score = countDose(sActiveInsulin, sGlycemia, sCarbohydrates);

                eSuggestedDose.setText(String.valueOf(score));
                eTakenDose.setText(String.valueOf(score));
            }
        });

        return view;
    }

}
