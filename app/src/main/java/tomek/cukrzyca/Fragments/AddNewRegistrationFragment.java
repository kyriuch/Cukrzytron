package tomek.cukrzyca.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;


public class AddNewRegistrationFragment extends Fragment {

    private RadioGroup radioGroup;
    private SwitchCompat mSwitch;
    private List<EditText> editTextList = new ArrayList<>();
    private int hour;
    private int min;
    private TextView bmiText;
    private View.OnClickListener onClickListener = null;

    public void onResume() {
        super.onResume();
        MainActivity.currentFragment = "newRegistrationFragment";
    }

    private void addNewRow(ViewGroup parent, Context context, String textViewString, String unitString) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.bottomMargin = 15;

        NiceTextView title = new NiceTextView(context);
        title.setLayoutParams(lp);
        title.setText(textViewString);
        TextViewCompat.setTextAppearance(title, FontCache.TEXT_APPEARANCE);
        title.setTextSize(16);
        title.setTypeface(true);

        EditText editText;

        if(textViewString.equals("Insulina") || textViewString.equals("Waga") ||
                textViewString.equals("HbA1c")) {
            editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate_float, null);
        } else {
            editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        }

        editText.setLayoutParams(lp);
        editText.setTextSize(16);
        editText.setTypeface(FontCache.get(context));
        TextViewCompat.setTextAppearance(editText, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText);

        NiceTextView unit = new NiceTextView(context);
        unit.setLayoutParams(lp);
        unit.setText(unitString);
        TextViewCompat.setTextAppearance(unit, FontCache.TEXT_APPEARANCE);
        unit.setTextSize(16);
        unit.setTypeface(true);

        row.addView(title);
        row.addView(editText);
        row.addView(unit);

        parent.addView(row);

        if (textViewString.equals("Waga")) {
            String sQuery = "SELECT height FROM user";

            final Cursor cursor = MainActivity.db.query(sQuery);

            if (cursor != null && cursor.moveToFirst()) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String str = editable.toString();

                        if (!str.isEmpty()) {
                            int weight = Integer.parseInt(str);
                            int height = cursor.getInt(0);

                            if (weight > 0 && height > 0 && height < 300 && weight < 500) {
                                float BMI = (float) weight / (float) ((height * height) / 10000.0);

                                System.out.println(weight + "\n" + height + "\n" + BMI);

                                DecimalFormat format = new DecimalFormat();
                                format.setMaximumFractionDigits(1);

                                bmiText.setText(format.format(BMI));
                            } else {
                                bmiText.setText("");
                            }
                        } else {
                            bmiText.setText("");
                        }
                    }
                });
            }
        }
    }

    private void addNewRow(ViewGroup parent, Context context, String textViewString) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.bottomMargin = 15;

        NiceTextView title = new NiceTextView(context);
        title.setLayoutParams(lp);
        title.setText(textViewString);
        TextViewCompat.setTextAppearance(title, FontCache.TEXT_APPEARANCE);
        title.setTextSize(16);
        title.setTypeface(true);

        bmiText = new NiceTextView(context);
        bmiText.setLayoutParams(lp);
        TextViewCompat.setTextAppearance(bmiText, FontCache.TEXT_APPEARANCE);
        bmiText.setTextSize(16);
        EditText editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        editText.setLayoutParams(lp);
        editText.setTextSize(16);
        editText.setTypeface(FontCache.get(context));
        TextViewCompat.setTextAppearance(editText, FontCache.TEXT_APPEARANCE);

        row.addView(title);
        row.addView(bmiText);

        parent.addView(row);
    }

    private void addNewRadioRow(ViewGroup parent, Context context) {
        TableRow row = new TableRow(context);

        radioGroup = new RadioGroup(context);

        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 15;

        RadioButton radioButton1 = new RadioButton(context);
        RadioButton radioButton2 = new RadioButton(context);

        radioButton1.setLayoutParams(lp);
        radioButton1.setText("Baza");
        TextViewCompat.setTextAppearance(radioButton1, FontCache.TEXT_APPEARANCE);
        radioButton1.setTextSize(16);
        radioButton1.setTypeface(FontCache.get(context));


        radioButton2.setText("Bolus");
        radioButton2.setLayoutParams(lp);
        TextViewCompat.setTextAppearance(radioButton2, FontCache.TEXT_APPEARANCE);
        radioButton2.setTextSize(16);
        radioButton2.setTypeface(FontCache.get(context));

        radioGroup.addView(radioButton1);
        radioGroup.addView(radioButton2);

        row.addView(radioGroup);

        parent.addView(row);
    }

    private void addNewPressurePulseRow(ViewGroup parent, Context context) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.bottomMargin = 15;

        NiceTextView title = new NiceTextView(context);
        title.setLayoutParams(lp);
        title.setText("Ciśnienie i puls");
        TextViewCompat.setTextAppearance(title, FontCache.TEXT_APPEARANCE);
        title.setTextSize(16);
        title.setTypeface(true);

        EditText editText1 = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        editText1.setLayoutParams(lp);
        editText1.setTextSize(16);
        editText1.setTypeface(FontCache.get(context));
        TextViewCompat.setTextAppearance(editText1, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText1);

        LinearLayout linearLayout = new LinearLayout((context));

        NiceTextView slash1 = new NiceTextView(context);
        slash1.setText("/");
        TextViewCompat.setTextAppearance(slash1, FontCache.TEXT_APPEARANCE);
        slash1.setTextSize(16);
        slash1.setTypeface(true);

        EditText editText2 = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        editText2.setId(1000);
        editText2.setTextSize(16);
        editText2.setTypeface(FontCache.get(context));
        TextViewCompat.setTextAppearance(editText2, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText2);

        NiceTextView slash2 = new NiceTextView(context);
        slash2.setText("/");
        TextViewCompat.setTextAppearance(slash2, FontCache.TEXT_APPEARANCE);
        slash2.setTextSize(16);
        slash2.setTypeface(true);

        EditText editText3 = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        editText3.setId(1001);
        editText3.setTextSize(16);
        editText3.setTypeface(FontCache.get(context));
        TextViewCompat.setTextAppearance(editText3, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText3);

        editText1.setNextFocusDownId(1000);
        editText2.setNextFocusDownId(1001);

        linearLayout.addView(slash1);
        linearLayout.addView(editText2);
        linearLayout.addView(slash2);
        linearLayout.addView(editText3);

        row.addView(title);
        row.addView(editText1);
        row.addView(linearLayout);

        parent.addView(row);
    }

    private void addNewActivityRow(ViewGroup parent, Context context) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.bottomMargin = 15;

        NiceTextView title = new NiceTextView(context);
        title.setLayoutParams(lp);
        title.setText("Aktywność fizyczna");
        TextViewCompat.setTextAppearance(title, FontCache.TEXT_APPEARANCE);
        title.setTextSize(16);
        title.setTypeface(true);

        mSwitch = (SwitchCompat) getActivity().getLayoutInflater().inflate(R.layout.switchcompat, null);
        mSwitch.setLayoutParams(lp);

        row.addView(title);
        row.addView(mSwitch);

        parent.addView(row);
    }

    private void addNewTypeRow(ViewGroup parent, Context context) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 15;
        lp.bottomMargin = 15;

        EditText editText1 = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        editText1.setHint("Rodzaj");
        editText1.setLayoutParams(lp);
        editText1.setTextSize(16);
        editText1.setTypeface(FontCache.get(context));
        editText1.setInputType(InputType.TYPE_CLASS_TEXT);
        editText1.setSingleLine(true);
        TextViewCompat.setTextAppearance(editText1, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText1);

        EditText editText2 = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate_string, null);
        editText2.setHint("Czas");
        editText2.setLayoutParams(lp);
        editText2.setTextSize(16);
        editText2.setTypeface(FontCache.get(context));
        editText2.setSingleLine(true);
        TextViewCompat.setTextAppearance(editText2, FontCache.TEXT_APPEARANCE);
        editTextList.add(editText2);


        row.addView(editText1);
        row.addView(editText2);

        parent.addView(row);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_registration, container, false);

        getActivity().setTitle("Nowy wpis");

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.registerLayout);
        ViewGroup table = (ViewGroup) relativeLayout.findViewById(R.id.registerTable);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker2);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
            }
        });

        final Context context = getContext();

        addNewRow(table, context, "Glikemia", "mg/dl");
        addNewRow(table, context, "Insulina", "J");
        addNewRadioRow(table, context);
        addNewRow(table, context, "Węglowodany", "g");
        addNewRow(table, context, "Ilość kcal", "kcal");
        addNewRow(table, context, "Waga", "kg");
        addNewRow(table, context, "BMI");
        addNewPressurePulseRow(table, context);
        addNewActivityRow(table, context);
        addNewTypeRow(table, context);
        addNewRow(table, context, "HbA1c", "%");

        final EditText note = (EditText) view.findViewById(R.id.editText12);
        note.setTypeface(FontCache.get(context));
        note.setTextSize(16);


        Button bAdd = (Button) view.findViewById(R.id.button9);
        bAdd.setTypeface(FontCache.get(getContext()));

        final Spinner spinner = (Spinner) relativeLayout.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.measurementType)) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);


                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(context));

                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TextViewCompat.setTextAppearance(((TextView) v), FontCache.TEXT_APPEARANCE);
                ((TextView) v).setTypeface(FontCache.get(context));

                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (HistoryFragment.id != -1) {
            System.out.println(HistoryFragment.id);

            String sQuery = "SELECT * FROM " + Database.REGISTRATION_TABLE + " WHERE _id = " +
                    HistoryFragment.id + ";";

            final Cursor cursor = MainActivity.db.query(sQuery);

            if (cursor != null && cursor.moveToFirst()) {
                bAdd.setText("Aktualizuj");

                String[] sDate = cursor.getString(1).split("-");
                String[] sHour = cursor.getString(2).split(":");

                System.out.println(sDate[0] + "/" + sDate[1] + "/" + sDate[2]);
                datePicker.updateDate(Integer.parseInt(sDate[2]),
                        Integer.parseInt(sDate[1]) - 1, Integer.parseInt(sDate[0]));
                timePicker.setCurrentHour(Integer.parseInt(sHour[0]));
                timePicker.setCurrentMinute(Integer.parseInt(sHour[1]));

                if (cursor.getString(3) != null) {
                    spinner.setSelection(cursor.getInt(3));
                }

                if (cursor.getInt(6) == 0) {
                    ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
                } else if(cursor.getInt(6) == 1){
                    ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                }

                editTextList.get(0).setText(cursor.getString(4));
                editTextList.get(1).setText(cursor.getString(5));
                editTextList.get(2).setText(cursor.getString(7));
                editTextList.get(3).setText(cursor.getString(8));
                editTextList.get(4).setText(cursor.getString(9));
                editTextList.get(5).setText(cursor.getString(10));
                editTextList.get(6).setText(cursor.getString(11));
                editTextList.get(7).setText(cursor.getString(12));

                if (cursor.getString(13) != null || cursor.getString(14) != null) {
                    mSwitch.setChecked(true);

                    editTextList.get(8).setText(cursor.getString(13));
                    editTextList.get(9).setText(cursor.getString(14));
                }

                editTextList.get(10).setText(cursor.getString(15));
                note.setText(cursor.getString(16));

                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean canSave = false;

                        String sDate = String.valueOf(datePicker.getDayOfMonth()) +
                                "-" + String.valueOf(datePicker.getMonth() + 1) + "-" +
                                String.valueOf(datePicker.getYear());

                        String sTime = "";

                        if (hour < 10) {
                            sTime += "0";
                        }

                        sTime += String.valueOf(hour) + ":";

                        if (min < 10) {
                            sTime += "0";
                        }

                        sTime += String.valueOf(min);

                        int type = spinner.getSelectedItemPosition();

                        int typeId = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));

                        if(typeId >= 0) {
                            canSave = true;
                        }

                        String sNote = note.getText().toString();

                        if(!sNote.isEmpty()) {
                            canSave = true;
                        }

                        String[] sValues = new String[11];

                        int i = 0;

                        for (EditText e : editTextList) {
                            sValues[i++] = e.getText().toString();
                        }


                        if (mSwitch.isChecked()) {
                            if (!sValues[9].isEmpty()) {
                                String rPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

                                Pattern pattern = Pattern.compile(rPattern);
                                if (!pattern.matcher(sValues[9]).matches()) {
                                    MyToast.show(getContext(), "Błędna godzina");

                                    return;
                                }

                                canSave = true;
                            }
                        }

                        int iValue;

                        if (!sValues[3].isEmpty()) {
                            iValue = Integer.parseInt(sValues[3]);

                            if (iValue <= 0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[5].isEmpty()) {
                            iValue = Integer.parseInt(sValues[5]);

                            if (iValue <= 0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[6].isEmpty()) {
                            iValue = Integer.parseInt(sValues[6]);

                            if (iValue <= 0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[7].isEmpty()) {
                            iValue = Integer.parseInt(sValues[7]);


                            if (iValue <= 0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        float fValue = 0;
                        float fInsulin = 0;
                        float fHbA1c = 0;
                        float fWeight = 0;

                        if (!sValues[2].isEmpty()) {
                            fValue = Float.parseFloat(sValues[2]);

                            if (fValue <= 0.0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[1].isEmpty()) {

                            fInsulin = Float.parseFloat(sValues[1]);

                            if (fInsulin <= 0.0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[10].isEmpty()) {

                            fHbA1c = Float.parseFloat(sValues[10]);

                            if (fHbA1c <= 0.0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if (!sValues[4].isEmpty()) {

                            fWeight = Float.parseFloat(sValues[4]);

                            if (fWeight <= 0.0) {
                                MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                                return;
                            }

                            canSave = true;
                        }

                        if(canSave) {
                            String sQuery = "UPDATE " + Database.REGISTRATION_TABLE + " SET date = '" +
                                    sDate + "', time = '" + sTime + "', type = " + type + ", glycemia = ";

                            if (sValues[0] != null && !sValues[0].isEmpty()) {
                                sQuery += sValues[0] + ", insulin = ";
                            } else {
                                sQuery += "NULL, insulin ";
                            }

                            if (sValues[1] != null && !sValues[1].isEmpty()) {
                                sQuery += fInsulin + ", insulinType = ";
                            } else {
                                sQuery += "NULL, insulinType = ";
                            }

                            sQuery += typeId + ", carbohydrates = ";

                            if (sValues[2] != null && !sValues[2].isEmpty()) {
                                sQuery += fValue + ", energyValue = ";
                            } else {
                                sQuery += "NULL, energyValue = ";
                            }

                            String[] ends = {" weight = ", " pressure1 = ", " pressure2 = ",
                                    " pressure3 = ", ""};

                            for (i = 3; i < 8; i++) {
                                if (sValues[i] != null && !sValues[i].isEmpty()) {
                                    if(i == 4) {
                                        sQuery += fWeight + ", " + ends[i - 3];
                                    } else {
                                        sQuery += sValues[i] + ", " + ends[i - 3];
                                    }
                                } else {
                                    sQuery += "NULL, " + ends[i - 3];
                                }
                            }

                            if (mSwitch.isChecked()) {
                                if (sValues[8] != null && !sValues[8].isEmpty()) {
                                    sQuery += "activity = '" + sValues[8] + "', activityTime = ";
                                } else {
                                    sQuery += "activity = NULL, activityTime = ";
                                }

                                if (sValues[9] != null && !sValues[9].isEmpty()) {
                                    sQuery += "'" + sValues[9] + "', hba1c = ";
                                } else {
                                    sQuery += "NULL, hba1c = ";
                                }
                            } else {
                                sQuery += "activity = NULL, activityTime = NULL, hba1c = ";
                            }

                            if (sValues[10] != null && !sValues[10].isEmpty()) {
                                sQuery += fHbA1c + ", note = ";
                            } else {
                                sQuery += "NULL, note = ";
                            }

                            if (!sNote.isEmpty()) {
                                sQuery += "'" + sNote + "', bmi = ";
                            } else {
                                sQuery += "NULL, bmi = ";
                            }

                            String sBmi = bmiText.getText().toString();
                            if (sBmi.isEmpty()) {
                                sQuery += "NULL WHERE _id = " + HistoryFragment.id + ";";
                            } else {
                                sQuery += sBmi + " WHERE _id = " + HistoryFragment.id + ";";
                            }

                            MainActivity.db.exec(sQuery);

                            MyToast.show(getContext(), "Pomyślnie zaktualizowano wpis");

                            getFragmentManager().popBackStack();
                        } else {
                            MyToast.show(getContext(), "Uzupełnij przynajmniej jedno pole");
                        }
                    }
                };
            }
        }

        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean canSave = false;

                    String sDate = String.valueOf(datePicker.getDayOfMonth()) +
                            "-" + String.valueOf(datePicker.getMonth() + 1) + "-" +
                            String.valueOf(datePicker.getYear());

                    String sTime = "";

                    if (hour < 10) {
                        sTime += "0";
                    }

                    sTime += String.valueOf(hour) + ":";

                    if (min < 10) {
                        sTime += "0";
                    }

                    sTime += String.valueOf(min);

                    int type = spinner.getSelectedItemPosition();

                    int typeId = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));

                    String sNote = note.getText().toString();

                    if(!sNote.isEmpty()) {
                        canSave = true;
                    }

                    String[] sValues = new String[11];

                    int i = 0;

                    if (typeId >= 0) {
                        canSave = true;
                    }

                    for (EditText e : editTextList) {
                        sValues[i++] = e.getText().toString();
                    }

                    if (mSwitch.isChecked()) {
                        if (!sValues[9].isEmpty()) {
                            String rPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

                            Pattern pattern = Pattern.compile(rPattern);
                            if (!pattern.matcher(sValues[9]).matches()) {
                                MyToast.show(getContext(), "Błędna godzina");

                                return;
                            }

                            canSave = true;
                        }
                    }

                    int iValue;

                    if (!sValues[0].isEmpty()) {
                        iValue = Integer.parseInt(sValues[0]);

                        if (iValue <= 0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[3].isEmpty()) {
                        iValue = Integer.parseInt(sValues[3]);

                        if (iValue <= 0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[5].isEmpty()) {
                        iValue = Integer.parseInt(sValues[5]);

                        if (iValue <= 0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[6].isEmpty()) {
                        iValue = Integer.parseInt(sValues[6]);

                        if (iValue <= 0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[7].isEmpty()) {
                        iValue = Integer.parseInt(sValues[7]);


                        if (iValue <= 0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    float fValue = 0;
                    float fInsulin = 0;
                    float fHbA1c = 0;
                    float fWeight = 0;

                    if (!sValues[2].isEmpty()) {
                        fValue = Float.parseFloat(sValues[2]);

                        if (fValue <= 0.0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[1].isEmpty()) {

                        fInsulin = Float.parseFloat(sValues[1]);

                        if (fInsulin <= 0.0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[10].isEmpty()) {

                        fHbA1c = Float.parseFloat(sValues[10]);

                        if (fHbA1c <= 0.0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if (!sValues[4].isEmpty()) {

                        fWeight = Float.parseFloat(sValues[4]);

                        if (fWeight <= 0.0) {
                            MyToast.show(getContext(), "Wpisz liczby większe niż 0");

                            return;
                        }

                        canSave = true;
                    }

                    if(canSave) {
                        String sQuery = "INSERT INTO " + Database.REGISTRATION_TABLE + "(date, time, type, glycemia, " +
                                "insulin, insulinType, carbohydrates, energyValue, weight, pressure1, pressure2, pressure3, " +
                                "activity, activityTime, hba1c, note, bmi) VALUES('" + sDate + "', '" + sTime + "', " +
                                type + ", ";

                        if (sValues[0] != null && !sValues[0].isEmpty()) {
                            sQuery += sValues[0] + ", ";
                        } else {
                            sQuery += "NULL, ";
                        }

                        if (sValues[1] != null && !sValues[1].isEmpty()) {
                            sQuery += fInsulin + ", ";
                        } else {
                            sQuery += "NULL, ";
                        }

                        sQuery += typeId + ", ";

                        if (sValues[2] != null && !sValues[2].isEmpty()) {
                            sQuery += fValue + ", ";
                        } else {
                            sQuery += "NULL, ";
                        }

                        for (i = 3; i < 8; i++) {
                            if (sValues[i] != null && !sValues[i].isEmpty()) {
                                if(i == 4) {
                                    sQuery += fWeight + ", ";
                                } else {
                                    sQuery += sValues[i] + ", ";
                                }
                            } else {
                                sQuery += "NULL, ";
                            }
                        }

                        if (mSwitch.isChecked()) {
                            if (sValues[8] != null && !sValues[8].isEmpty()) {
                                sQuery += "'" + sValues[8] + "', ";
                            } else {
                                sQuery += "NULL, ";
                            }

                            if (sValues[9] != null && !sValues[9].isEmpty()) {
                                sQuery += "'" + sValues[9] + "', ";
                            } else {
                                sQuery += "NULL, ";
                            }
                        } else {
                            sQuery += "NULL, NULL, ";
                        }

                        if (sValues[10] != null && !sValues[10].isEmpty()) {
                            sQuery += fHbA1c + ", ";
                        } else {
                            sQuery += "NULL, ";
                        }

                        if (!sNote.isEmpty()) {
                            sQuery += "'" + sNote + "', ";
                        } else {
                            sQuery += "NULL, ";
                        }

                        String sBmi = bmiText.getText().toString();
                        if (sBmi.isEmpty()) {
                            sQuery += "NULL);";
                        } else {
                            sQuery += sBmi + ");";
                        }

                        MainActivity.db.exec(sQuery);

                        MyToast.show(getContext(), "Pomyślnie dodano nowy wpis");

                        getActivity().onBackPressed();
                    } else {
                        MyToast.show(getContext(), "Uzupełnij przynajmniej jedno pole");
                    }
                }
            };
        }

        bAdd.setOnClickListener(onClickListener);

        return view;
    }

}
