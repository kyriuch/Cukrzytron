package tomek.cukrzyca.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;

public class UserDataFragment extends Fragment {

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "userDataFragment";
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        Context context = getContext();

        final EditText eFirstName = (EditText) view.findViewById(R.id.editText22);
        final EditText eLastName = (EditText) view.findViewById(R.id.editText23);
        final EditText eBirthDate = (EditText) view.findViewById(R.id.editText20);
        final RadioGroup radioSexGroup = (RadioGroup) view.findViewById(R.id.RD3);
        final RadioButton buttonMale = (RadioButton) radioSexGroup.findViewById(R.id.radioButton5);
        final RadioButton buttonFemale = (RadioButton) radioSexGroup.findViewById(R.id.radioButton6);
        final EditText eHeight = (EditText) view.findViewById(R.id.editText21);

        eFirstName.setTypeface(FontCache.get(context));
        eLastName.setTypeface(FontCache.get(context));
        eBirthDate.setTypeface(FontCache.get(context));
        eHeight.setTypeface(FontCache.get(context));

        final Button bSave = (Button) view.findViewById(R.id.button10);
        bSave.setTypeface(FontCache.get(context));

        String sQuery = "SELECT * FROM " + Database.USER_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = eFirstName.getText().toString();
                String lastName = eLastName.getText().toString();
                String birthDate = eBirthDate.getText().toString();
                RadioButton rButton = (RadioButton) radioSexGroup.findViewById(radioSexGroup.getCheckedRadioButtonId());

                if (rButton == null) {
                    MyToast.show(getContext(), "Uzupełnij błędne/puste pola");

                    return;
                }

                String sex = rButton.getText().toString();
                String height = eHeight.getText().toString();

                if (!firstName.trim().isEmpty() &&
                        !lastName.trim().isEmpty() &&
                        !birthDate.trim().isEmpty() &&
                        !height.trim().isEmpty() &&
                        validateDate(birthDate)) {

                    String sSex;

                    if (sex.equals("M")) {
                        sSex = "0";
                    } else {
                        sSex = "1";
                    }

                    int iHeight = Integer.parseInt(height);
                    if (iHeight < 10 || iHeight > 250) {
                        MyToast.show(getContext(), "Uzupełnij błędne/puste pola");
                        return;
                    }


                    String sQuery = "UPDATE " + Database.USER_TABLE + " SET firstName = '" + firstName + "', lastName = '" + lastName + "', birthDate = '" +
                            birthDate + "', sex = " + sSex + ", height = " + height + ";";

                    MainActivity.db.exec(sQuery);

                    MyToast.show(getContext(), "Zaktualizowano dane");

                    getActivity().onBackPressed();
                } else {
                    MyToast.show(getContext(), "Uzupełnij błędne/puste pola");
                }
            }
        };

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String birthDate = cursor.getString(3);
            int sex = cursor.getInt(4);
            int height = cursor.getInt(5);

            cursor.close();

            eFirstName.setText(firstName);
            eLastName.setText(lastName);
            eBirthDate.setText(birthDate);

            if (sex == 0) {
                buttonMale.setChecked(true);
            } else {
                buttonFemale.setChecked(true);
            }

            eHeight.setText(String.valueOf(height));

            bSave.setOnClickListener(onClickListener);

        } else {
            bSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String firstName = eFirstName.getText().toString();
                    String lastName = eLastName.getText().toString();
                    String birthDate = eBirthDate.getText().toString();
                    RadioButton rButton = (RadioButton) radioSexGroup.findViewById(radioSexGroup.getCheckedRadioButtonId());

                    if (rButton == null) {
                        MyToast.show(getContext(), "Uzupełnij błędne/puste pola");

                        return;
                    }

                    String sex = rButton.getText().toString();
                    String height = eHeight.getText().toString();

                    if (!firstName.trim().isEmpty() &&
                            !lastName.trim().isEmpty() &&
                            !birthDate.trim().isEmpty() &&
                            !height.trim().isEmpty() &&
                            validateDate(birthDate)) {

                        String sSex;

                        if (sex.equals("M")) {
                            sSex = "0";
                        } else {
                            sSex = "1";
                        }

                        int iHeight = Integer.parseInt(height);
                        if (iHeight < 10 || iHeight > 250) {
                            eFirstName.setText("");
                            eLastName.setText("");
                            eBirthDate.setText("");
                            eHeight.setText("");
                            return;
                        }

                        String sQuery = "INSERT INTO " + Database.USER_TABLE + "(firstName, lastName, birthDate, sex, height)" +
                                " VALUES('" + firstName + "', '" + lastName + "', '" + birthDate + "', " + sSex + ", " + height + ");";
                        MainActivity.db.exec(sQuery);

                        MyToast.show(getContext(), "Zapisano dane");

                        getActivity().onBackPressed();

                        bSave.setOnClickListener(onClickListener);
                    } else {
                        MyToast.show(getContext(), "Uzupełnij błędne/puste pola");
                    }
                }
            });
        }

        return view;
    }
}
