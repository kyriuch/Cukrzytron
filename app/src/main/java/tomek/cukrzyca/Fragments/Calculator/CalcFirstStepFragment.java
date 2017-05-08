package tomek.cukrzyca.Fragments.Calculator;


import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;

public class CalcFirstStepFragment extends Fragment {

    ArrayList<TextView> aStart = new ArrayList<>();
    ArrayList<TextView> aStop = new ArrayList<>();
    ArrayList<EditText> aAmount = new ArrayList<>();

    static ArrayList<String> start = new ArrayList<>();
    static ArrayList<String> stop = new ArrayList<>();
    static ArrayList<Integer> amount = new ArrayList<>();

    private LinearLayout verticalLayout;

    public CalcFirstStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        start.clear();
        stop.clear();
        amount.clear();
        aStart.clear();
        aStop.clear();
        aAmount.clear();

        String sQuery = "SELECT * FROM " + Database.CALC_GLYCEMIA_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                addNewRow(verticalLayout, cursor.getString(0), cursor.getString(1), cursor.getInt(2));

            } while (cursor.moveToNext());
        }

        MainActivity.currentFragment = "calcFirstStepFragment";
    }

    private void addNewRow(LinearLayout root) {
        RelativeLayout lRow = new RelativeLayout(getContext());
        lRow.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout lLeft = new LinearLayout(getContext());
        lLeft.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        lLeft.setOrientation(LinearLayout.HORIZONTAL);

        final TextView tStart = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tBetween = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tStop = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        EditText eAmount = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL);


        tStart.setText("00:00");
        tBetween.setText(" - ");
        tStop.setText("23:59");
        eAmount.setLayoutParams(lp);

        lLeft.addView(tStart);
        lLeft.addView(tBetween);
        lLeft.addView(tStop);
        lLeft.setLayoutParams(lp2);
        lRow.addView(lLeft);
        lRow.addView(eAmount);

        aStart.add(tStart);
        aStop.add(tStop);
        aAmount.add(eAmount);

        root.addView(lRow);

        final TimePickerDialog.OnTimeSetListener timeStart = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (i < 10) {
                    if (i1 < 10) {
                        tStart.setText(new StringBuilder().append("0").append(i).append(":0").append(i1));
                    } else {
                        tStart.setText(new StringBuilder().append("0").append(i).append(":").append(i1));
                    }
                } else if (i1 < 10) {
                    tStart.setText(new StringBuilder().append(i).append(":0").append(i1));
                } else {
                    tStart.setText(new StringBuilder().append(i).append(":").append(i1));
                }
            }
        };

        tStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timeStart, 0, 0, true).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener timeStop = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (i < 10) {
                    if (i1 < 10) {
                        tStop.setText(new StringBuilder().append("0").append(i).append(":0").append(i1));
                    } else {
                        tStop.setText(new StringBuilder().append("0").append(i).append(":").append(i1));
                    }
                } else if (i1 < 10) {
                    tStop.setText(new StringBuilder().append(i).append(":0").append(i1));
                } else {
                    tStop.setText(new StringBuilder().append(i).append(":").append(i1));
                }
            }
        };

        tStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timeStop, 23, 59, true).show();
            }
        });
    }

    private void addNewRow(LinearLayout root, String sTimeStart, String sTimeStop, int pointer) {
        RelativeLayout lRow = new RelativeLayout(getContext());
        lRow.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout lLeft = new LinearLayout(getContext());
        lLeft.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        lLeft.setOrientation(LinearLayout.HORIZONTAL);

        final TextView tStart = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tBetween = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tStop = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        EditText eAmount = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_VERTICAL);

        tStart.setText(sTimeStart);
        tBetween.setText(" - ");
        tStop.setText(sTimeStop);
        eAmount.setLayoutParams(lp);
        eAmount.setText(String.valueOf(pointer));
        eAmount.setLayoutParams(lp);

        lLeft.addView(tStart);
        lLeft.addView(tBetween);
        lLeft.addView(tStop);
        lLeft.setLayoutParams(lp2);
        lRow.addView(lLeft);
        lRow.addView(eAmount);

        aStart.add(tStart);
        aStop.add(tStop);
        aAmount.add(eAmount);

        root.addView(lRow);

        final TimePickerDialog.OnTimeSetListener timeStart = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (i < 10) {
                    if (i1 < 10) {
                        tStart.setText(new StringBuilder().append("0").append(i).append(":0").append(i1));
                    } else {
                        tStart.setText(new StringBuilder().append("0").append(i).append(":").append(i1));
                    }
                } else if (i1 < 10) {
                    tStart.setText(new StringBuilder().append(i).append(":0").append(i1));
                } else {
                    tStart.setText(new StringBuilder().append(i).append(":").append(i1));
                }
            }
        };

        tStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timeStart, 0, 0, true).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener timeStop = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if (i < 10) {
                    if (i1 < 10) {
                        tStop.setText(new StringBuilder().append("0").append(i).append(":0").append(i1));
                    } else {
                        tStop.setText(new StringBuilder().append("0").append(i).append(":").append(i1));
                    }
                } else if (i1 < 10) {
                    tStop.setText(new StringBuilder().append(i).append(":0").append(i1));
                } else {
                    tStop.setText(new StringBuilder().append(i).append(":").append(i1));
                }
            }
        };

        tStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timeStop, 23, 59, true).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calc_first_step, container, false);

        verticalLayout = (LinearLayout) view.findViewById(R.id.linearLayout14);

        Button bPlus = (Button) view.findViewById(R.id.button16);
        Button bBack = (Button) view.findViewById(R.id.button8);
        Button bNext = (Button) view.findViewById(R.id.button17);
        bPlus.setTypeface(FontCache.get(getContext()));
        bBack.setTypeface(FontCache.get(getContext()));
        bNext.setTypeface(FontCache.get(getContext()));


        bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRow(verticalLayout);
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aAmount.isEmpty()) {
                    MyToast.show(getContext(), "Dodaj choć jeden rekord");

                    return;
                }

                for (EditText e : aAmount) {
                    if (e.getText().toString().trim().isEmpty()) {
                        MyToast.show(getContext(), "Uzupełnij puste wartości");

                        return;
                    }

                    amount.add(Integer.parseInt(e.getText().toString()));
                }

                for (TextView t : aStart) {
                    start.add(t.getText().toString());
                }

                for (TextView t : aStop) {
                    stop.add(t.getText().toString());
                }

                CalcSecondStepFragment fragment = new CalcSecondStepFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
