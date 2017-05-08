package tomek.cukrzyca.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;


public class StatsFragment extends Fragment {

    private NiceTextView avgGlycemia;
    private NiceTextView measuresCount;
    private NiceTextView above;
    private NiceTextView below;
    private NiceTextView insulin;
    private NiceTextView baza;
    private NiceTextView bolus;
    private NiceTextView carbohydrates;
    private NiceTextView energyValue;
    private NiceTextView avgPressure;
    private NiceTextView avgWeight;
    private NiceTextView activity;
    private NiceTextView hba1c;

    public StatsFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        MainActivity.currentFragment = "statsFragment";

        super.onResume();
    }

    private boolean validateDate(String value) {
        if (value.equals("") || value.isEmpty()) {
            return false;
        }

        Date date = null;
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // birth date format
        Date old = new Date();
        try {
            date = sdf.parse(value);
            old = sdf.parse("31/12/1899");

            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return false;
        } else if ((date.before(today) || date.equals(today)) && date.after(old)) {
            return true;
        }

        return false;
    }

    public void setValues(String sDate) {
        int[] iGlycemia = new int[2];
        int iGlycemiaAbove = 0;
        int iGlycemiaBelow = 0;
        int iInsulin = 0;
        int[] iInsulinType = new int[2];
        float fCarbohydrates = 0;
        int iEnergyValue = 0;
        int[][] iAvgPressure = new int[3][2];
        int[] iAvgWeight = new int[2];
        int mins = 0;
        int[] iHbA1c = new int[2];

        Date date = null;
        SimpleDateFormat dateFormatOld = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            date = dateFormatOld.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormatNew = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        String fDate = dateFormatNew.format(date);

        String sQuery = "SELECT * FROM " + Database.REGISTRATION_TABLE + " WHERE date = '"
                + fDate + "';";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                iGlycemia[1]++;

                sQuery = "SELECT pointer FROM " + Database.CALC_GLYCEMIA_TABLE + " WHERE '" +
                        cursor.getString(2) + "' BETWEEN time_start AND time_stop;";

                Cursor cursor2 = MainActivity.db.query(sQuery);

                if (cursor2 != null && cursor2.moveToFirst()) {
                    if (cursor2.getString(0) != null) {
                        int lowRange = (int) (cursor2.getInt(0) - cursor2.getInt(0) * 0.05);
                        int highRange = (int) (cursor2.getInt(0) + cursor2.getInt(0) * 0.05);


                        if (cursor.getString(4) != null) {
                            iGlycemia[0] += cursor.getInt(4);

                            if (cursor.getInt(4) > highRange) {
                                iGlycemiaAbove++;
                            } else if (cursor.getInt(4) < lowRange) {
                                iGlycemiaBelow++;
                            }
                        }
                    }
                } else {
                    if (cursor.getString(4) != null) {
                        iGlycemia[0] += cursor.getInt(4);
                    }
                }

                iInsulin += cursor.getInt(5);
                iInsulinType[cursor.getInt(6)] += cursor.getInt(5);

                if (cursor.getString(7) != null) {
                    fCarbohydrates += cursor.getFloat(7);
                }

                if (cursor.getString(8) != null) {
                    iEnergyValue += cursor.getInt(8);
                }

                if (cursor.getString(10) != null) {
                    iAvgPressure[0][0] += cursor.getInt(10);
                    iAvgPressure[0][1]++;
                }

                if (cursor.getString(11) != null) {
                    iAvgPressure[1][0] += cursor.getInt(11);
                    iAvgPressure[1][1]++;
                }

                if (cursor.getString(12) != null) {
                    iAvgPressure[2][0] += cursor.getInt(12);
                    iAvgPressure[2][1]++;
                }

                if (cursor.getString(9) != null) {
                    iAvgWeight[0] += cursor.getInt(9);
                    iAvgWeight[1]++;
                }

                if (cursor.getString(14) != null) {
                    String[] sTime = cursor.getString(14).split(":");

                    mins += 60 * Integer.parseInt(sTime[0]);
                    mins += Integer.parseInt(sTime[1]);
                }

                if (cursor.getString(15) != null) {
                    iHbA1c[0] += cursor.getInt(15);
                    iHbA1c[1]++;
                }
            } while (cursor.moveToNext());
        }

        dateFormatNew = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        fDate = dateFormatNew.format(date);

        sQuery = "SELECT * FROM " + Database.CALC_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                iGlycemia[1]++;

                sQuery = "SELECT pointer FROM " + Database.CALC_GLYCEMIA_TABLE + " WHERE '" +
                        cursor.getString(2) + "' BETWEEN time_start AND time_stop;";

                Cursor cursor2 = MainActivity.db.query(sQuery);

                if (cursor2 != null && cursor2.moveToFirst()) {
                    if (cursor2.getString(0) != null) {
                        int lowRange = (int) (cursor2.getInt(0) - cursor2.getInt(0) * 0.05);
                        int highRange = (int) (cursor2.getInt(0) + cursor2.getInt(0) * 0.05);


                        if (cursor.getString(4) != null) {
                            iGlycemia[0] += cursor.getInt(4);

                            if (cursor.getInt(4) > highRange) {
                                iGlycemiaAbove++;
                            } else if (cursor.getInt(4) < lowRange) {
                                iGlycemiaBelow++;
                            }
                        }
                    }
                }  else {
                    if (cursor.getString(4) != null) {
                        iGlycemia[0] += cursor.getInt(4);
                    }
                }

                if (cursor.getString(5) != null) {
                    fCarbohydrates += cursor.getFloat(5);
                }

                iInsulin += cursor.getInt(6);
            } while (cursor.moveToNext());
        }

        sQuery = "SELECT * FROM " + Database.POP_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                iGlycemia[1]++;
                iInsulin += cursor.getInt(3);
                iInsulinType[cursor.getInt(4)] += cursor.getInt(3);
            } while (cursor.moveToNext());
        }

        if (iGlycemia[1] > 0) {
            avgGlycemia.setText(String.valueOf(iGlycemia[0] / iGlycemia[1]) + " mg/dl");
        } else {
            avgGlycemia.setText("brak");
        }

        if (iGlycemia[1] > 0) {
            measuresCount.setText(String.valueOf(iGlycemia[1]));
        } else {
            measuresCount.setText("brak");
        }

        if(iGlycemiaAbove > 0) {
            above.setText(iGlycemiaAbove / iGlycemia[1] * 100 + "%");
        } else {
            above.setText("brak");
        }

        System.out.println(iGlycemiaBelow + " " + iGlycemia[1]);

        if(iGlycemiaBelow > 0) {
            below.setText(iGlycemiaBelow / iGlycemia[1] * 100 + "%");
        } else {
            below.setText("brak");
        }

        if (iInsulin > 0) {
            insulin.setText(String.valueOf(iInsulin) + " J");
        } else {
            insulin.setText("brak");
        }

        if (iInsulinType[0] > 0) {
            baza.setText(String.valueOf(iInsulinType[0]));
        } else {
            baza.setText("brak");
        }

        if (iInsulinType[1] > 0) {
            bolus.setText(String.valueOf(iInsulinType[1]));
        } else {
            bolus.setText("brak");
        }


        if (fCarbohydrates > 0) {
            carbohydrates.setText(String.valueOf(fCarbohydrates).replace(",", ".") + " g");
        } else {
            carbohydrates.setText("brak");
        }

        if (iEnergyValue > 0) {
            energyValue.setText(String.valueOf(iEnergyValue) + " kcal");
        } else {
            energyValue.setText("brak");
        }

        if (iAvgPressure[0][1] > 0 && iAvgPressure[1][1] > 0 && iAvgPressure[2][1] > 0) {
            avgPressure.setText(String.valueOf(iAvgPressure[0][0] / iAvgPressure[0][1] + "/" +
                    iAvgPressure[1][0] / iAvgPressure[1][1] + "/" +
                    iAvgPressure[2][0] / iAvgPressure[2][1]));
        } else {
            avgPressure.setText("brak");
        }

        if (iAvgWeight[1] > 0) {
            avgWeight.setText(String.valueOf(iAvgWeight[0] / iAvgWeight[1]) + " kg");
        } else {
            avgWeight.setText("brak");
        }

        if (mins > 0) {
            String sTime = "";

            if (mins >= 60) {
                int hour = (mins / 60);

                if (hour < 10) {
                    sTime += "0" + String.valueOf(hour) + ":";
                } else {
                    sTime += String.valueOf(hour) + ":";
                }

                mins -= 60 * hour;

                if (mins < 10) {
                    sTime += "0" + mins;
                } else {
                    sTime += mins;
                }
            } else {
                sTime += "00:";

                if (mins < 10) {
                    sTime += "0" + mins;
                } else {
                    sTime += mins;
                }
            }

            activity.setText(sTime);
        } else {
            activity.setText("brak");
        }

        if (iHbA1c[1] > 0) {
            hba1c.setText(String.valueOf(iHbA1c[0] / iHbA1c[1]) + "%");
        } else {
            hba1c.setText("brak");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        avgGlycemia = (NiceTextView) view.findViewById(R.id.niceTextView50);
        measuresCount = (NiceTextView) view.findViewById(R.id.niceTextView47);
        above = (NiceTextView) view.findViewById(R.id.niceTextView48);
        below = (NiceTextView) view.findViewById(R.id.niceTextView40);
        insulin = (NiceTextView) view.findViewById(R.id.niceTextView49);
        baza = (NiceTextView) view.findViewById(R.id.niceTextView51);
        bolus = (NiceTextView) view.findViewById(R.id.niceTextView52);
        carbohydrates = (NiceTextView) view.findViewById(R.id.niceTextView53);
        energyValue = (NiceTextView) view.findViewById(R.id.niceTextView54);
        avgPressure = (NiceTextView) view.findViewById(R.id.niceTextView55);
        avgWeight = (NiceTextView) view.findViewById(R.id.niceTextView56);
        activity = (NiceTextView) view.findViewById(R.id.niceTextView57);
        hba1c = (NiceTextView) view.findViewById(R.id.niceTextView58);


        getActivity().setTitle("Statystyki");

        final EditText eDate = (EditText) view.findViewById(R.id.editText27);
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        eDate.setText(dateFormat.format(cal.getTime()));

        setValues(eDate.getText().toString());

        eDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String date = editable.toString();
                if (date.length() == 10) {
                    if (validateDate(date)) {
                        setValues(date);
                    }
                }
            }
        });

        return view;
    }

}
