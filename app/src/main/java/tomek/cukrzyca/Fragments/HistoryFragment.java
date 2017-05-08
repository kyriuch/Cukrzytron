package tomek.cukrzyca.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.Fragments.Calculator.CalculatorFragment;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

public class HistoryFragment extends Fragment {

    private int itemSelected = 0;
    private String lastValidateDate;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<HistoryElement> list;
    private HistoryAdapter historyAdapter;
    public static int id = -1;

    private String[] clauses = {"glycemia",
            "insulin", "carbohydrates", "energyValue",
            "weight", "pressure1, pressure2, pressure3",
            "activity, activityTime", "hba1c", "bmi"};

    private String[] simpleTitles = {"Glikemia: ",
            "Insulina: ", "Węglowodany: ", "Kcal: ", "Waga: ",
            "Ciśnienie i puls: ", "Aktywność fizyczna: ", "HbA1c: ", "BMI: "};

    private String[] registrationTitles = {"Rodzaj pomiaru: ", "Glikemia: ", "Insulina: ",
            "Typ insuliny: ", "Węglowodany: ", "Kcal: ", "Waga: ", "Ciśnienie i puls: ", "Aktywność: ",
            "Czas aktywnosci: ", "HbA1c: ", "Notatka: ", "BMI: "};

    private String[] measureType = {"Na czczo", "Przed posiłkiem", "Po posiłku", "Przed śniadaniem",
            "Po śniadaniu", "Przed obiadem", "Po obiedzie", "Przed kolacją", "Po kolacji",
            "Przed snem", "W nocy", "Przed wysiłkiem", "Po wysiłku", "Złe samopoczucie",
            "Inny"};

    private String[] calcTitles = {"Glikemia: ", "Węglowodany: ", "Podana dawka: "};

    private String[] insulinType = {"Baza", "Bolus"};

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

    public HistoryFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        MainActivity.currentFragment = "historyFragment";

        super.onResume();
    }

    private void createListView(ListView listView, String sDate) {
        list = new ArrayList<>();

        //------ DATA ---------//

        Date date = null;
        SimpleDateFormat dateFormatOld = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            date = dateFormatOld.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormatNew = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        String fDate = dateFormatNew.format(date);

        //------ KONIEC DATY ------//


        //------ WLASNE WPISY -----//

        String sQuery;


        if (itemSelected > 0) {
            sQuery = "SELECT _id, time" + ", " + clauses[itemSelected - 1] + " FROM " +
                    Database.REGISTRATION_TABLE + " WHERE date = '" + fDate + "';";
        } else {
            sQuery = "SELECT * FROM " + Database.REGISTRATION_TABLE + " WHERE date = '" + fDate
                    + "';";
        }

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (itemSelected == 0) {
                    String[] scores = new String[15];


                    for (int i = 3; i < 18; i++) {
                        scores[i - 3] = cursor.getString(i);
                    }

                    String hint = "";

                    for (int i = 0; i < 7; i++) {
                        if (scores[i] == null) {
                            hint += registrationTitles[i] + "brak" + "\n";

                            continue;
                        }

                        if (i == 0) {
                            hint += registrationTitles[i] +
                                    measureType[Integer.parseInt(scores[i])] + "\n";

                            continue;
                        } else if (i == 3) {
                            if(Integer.parseInt(scores[i]) != -1) {
                                hint += registrationTitles[i] +
                                        insulinType[Integer.parseInt(scores[i])] + "\n";
                            } else {
                                hint += registrationTitles[i] + "brak\n";
                            }

                            continue;
                        }

                        hint += registrationTitles[i] + scores[i] + "\n";
                    }

                    if (scores[7] != null && scores[8] != null & scores[9] != null) {
                        hint += registrationTitles[7] + scores[7] + "/" + scores[8] + "/" + scores[9]
                                + "\n";
                    } else {
                        hint += registrationTitles[7] + "brak" + "\n";
                    }


                    for (int i = 10; i < 15; i++) {
                        if (i == 14) {
                            if (scores[14] == null) {
                                hint += registrationTitles[i - 2] + "brak";

                                continue;
                            }

                            hint += registrationTitles[i - 2] + scores[i];
                            continue;
                        }

                        if (scores[i] == null) {
                            hint += registrationTitles[i - 2] + "brak" + "\n";

                            continue;
                        }

                        hint += registrationTitles[i - 2] + scores[i] + "\n";

                    }


                    list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_REGISTRATION,
                            cursor.getString(2), " Własny wpis", hint));
                } else {
                    if (itemSelected < 6 || itemSelected == 9) {
                        if (cursor.getString(2) != null) {
                            list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_REGISTRATION,
                                    cursor.getString(1), " Własny wpis", simpleTitles[itemSelected - 1] + cursor.getString(2)));
                        }
                    } else if (itemSelected == 6) {
                        if (cursor.getString(2) != null) {
                            list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_REGISTRATION,
                                    cursor.getString(1), " Własny wpis", simpleTitles[itemSelected - 1] +
                                    cursor.getString(2) + "/" + cursor.getString(3) + "/" + cursor.getString(4)));
                        }
                    } else if (itemSelected == 7) {
                        if (cursor.getString(2) != null) {
                            list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_REGISTRATION,
                                    cursor.getString(1), " Własny wpis", simpleTitles[itemSelected - 1] +
                                    cursor.getString(3) + " " + cursor.getString(2)));
                        }
                    } else if (itemSelected == 8) {
                        if (cursor.getString(2) != null) {
                            list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_REGISTRATION,
                                    cursor.getString(1), " Własny wpis", simpleTitles[itemSelected - 1] + cursor.getString(2) + "%"));
                        }
                    }
                }
            } while (cursor.moveToNext());
        }

        //------ KONIEC WLASNYCH WPISOW -----//

        //------ DATA ---------//

        dateFormatNew = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        fDate = dateFormatNew.format(date);

        //------ KONIEC DATY ------//

        //------ PEN -----//

        if (itemSelected == 2) {
            sQuery = "SELECT _id, time, amount FROM " + Database.POP_ENTRIES_TABLE +
                    " WHERE date = '" + fDate + "';";
        } else if (itemSelected == 0) {
            sQuery = "SELECT * FROM " + Database.POP_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";
        }

        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (itemSelected == 0) {
                    String[] scores = new String[2];

                    scores[0] = cursor.getString(3);
                    scores[1] = cursor.getString(4);

                    String hint = "";

                    if (scores[0] != null) {
                        hint += "Insulina: " + scores[0] + "\n";
                    } else {
                        hint += "Insulina: brak" + "\n";
                    }

                    if (scores[1] != null) {
                        hint += "Typ insuliny: " + insulinType[Integer.parseInt(scores[1])];
                    } else {
                        hint += "Typ insuliny: brak";
                    }

                    list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_PEN,
                            cursor.getString(2), " Wpis z pena", hint));
                } else if (itemSelected == 2) {
                    if (cursor.getString(2) != null) {
                        list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_PEN,
                                cursor.getString(1), " Wpis z pena", simpleTitles[itemSelected - 1] +
                                cursor.getString(2)));
                    }
                }
            } while (cursor.moveToNext());
        }

        //------ KONIEC PENA -----//


        //------ KALKULATOR ------//


        if (itemSelected == 2) {
            sQuery = "SELECT _id, time, dose FROM " + Database.CALC_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";
        } else if (itemSelected == 1 || itemSelected == 3) {
            sQuery = "SELECT _id, time, " + clauses[itemSelected - 1] + " FROM " + Database.CALC_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";
        } else if (itemSelected == 0) {
            sQuery = "SELECT * FROM " + Database.CALC_ENTRIES_TABLE + " WHERE date = '" + fDate + "';";
        }


        cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (itemSelected == 0) {
                    String[] scores = new String[3];

                    for (int i = 0; i < 3; i++) {
                        scores[i] = cursor.getString(i + 4);
                    }

                    String hint = "";

                    for (int i = 0; i < 2; i++) {
                        if (scores[i] == null) {
                            hint += calcTitles[i] + "brak" + "\n";
                        } else {
                            hint += calcTitles[i] + scores[i] + "\n";
                        }
                    }

                    if (scores[2] == null) {
                        hint += calcTitles[2] + "brak" + "\n";
                    } else {
                        hint += calcTitles[2] + scores[2] + "\n";
                    }

                    list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_CALC,
                            cursor.getString(2), " Wpis z kalkulatora", hint));
                } else if (itemSelected < 4) {
                    if (cursor.getString(2) != null) {
                        list.add(new HistoryElement(cursor.getInt(0), HistoryElement.TYPE_CALC,
                                cursor.getString(1), " Wpis z kalkulatora", simpleTitles[itemSelected - 1] +
                                cursor.getString(2)));
                    }
                }
            } while (cursor.moveToNext());
        }

        //------ KONIEC KALKULATORA -----//


        if(list.size() > 0) {
            Collections.sort(list, new Comparator<HistoryElement>() {
                @Override
                public int compare(HistoryElement historyElement, HistoryElement t1) {
                    int minsOne = 0, minsTwo = 0;

                    String[] first = historyElement.getTime().split(":");
                    String[] second = t1.getTime().split(":");

                    minsOne += Integer.parseInt(first[0]) * 60;
                    minsOne += Integer.parseInt(first[1]);

                    minsTwo += Integer.parseInt(second[0]) * 60;
                    minsTwo += Integer.parseInt(second[1]);

                    if(minsOne > minsTwo) {
                        return 1;
                    } else if(minsOne == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        historyAdapter = new HistoryAdapter(getContext(), R.layout.history_element, list);

        listView.setAdapter(historyAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemLongClickListener(onItemLongClickListener);
        listView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        getActivity().setTitle("Historia");

        final AppCompatSpinner spinner = (AppCompatSpinner) view.findViewById(R.id.spinner5);
        final ListView listView = (ListView) view.findViewById(R.id.listViewHistory);
        final EditText eDate = (EditText) view.findViewById(R.id.editText25);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();

        lastValidateDate = dateFormat.format(date);
        eDate.setText(lastValidateDate);

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
                        lastValidateDate = date;
                        createListView(listView, lastValidateDate);
                    }
                }
            }
        });

        onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Czy chcesz usunąć wpis?")
                        .setCancelable(false)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HistoryElement historyElement = list.get(i);

                                String sQuery = "DELETE FROM ";

                                if (historyElement.getType() == HistoryElement.TYPE_REGISTRATION) {
                                    sQuery += Database.REGISTRATION_TABLE;
                                } else if (historyElement.getType() == HistoryElement.TYPE_CALC) {
                                    sQuery += Database.CALC_ENTRIES_TABLE;
                                } else {
                                    sQuery += Database.POP_ENTRIES_TABLE;
                                }

                                sQuery += " WHERE _id = " + historyElement.getId() + ";";

                                MainActivity.db.exec(sQuery);
                                list.remove(i);
                                historyAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        };

        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HistoryElement historyElement = list.get(i);

                Fragment fragment = null;

                if (historyElement.getType() == HistoryElement.TYPE_REGISTRATION) {
                    fragment = new AddNewRegistrationFragment();
                } else if (historyElement.getType() == HistoryElement.TYPE_CALC) {
                    fragment = new CalculatorFragment();
                }

                if (fragment != null) {
                    id = historyElement.getId();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                    fragmentTransaction.commit();
                }
            }
        };


        createListView(listView, eDate.getText().toString());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.filtrTypes)) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);


                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));
                ((TextView) v).setTextSize(22);

                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(getContext()));
                ((TextView) v).setTextSize(22);

                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = i;
                createListView(listView, lastValidateDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }
}
