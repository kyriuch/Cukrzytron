package tomek.cukrzyca.Fragments;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import tomek.cukrzyca.Bluetooth.AcceptThread;
import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

public class MainFragment extends Fragment {

    private TextView textView;
    private TextView textView2;
    private TextView insulinType;
    private ScrollView scrollView;
    private ScrollView scrollView2;
    private Context context;
    private AcceptThread thread = null;
    private Handler handler;
    private SwitchCompat sSwitch;
    private BluetoothAdapter mBluetoothAdapter;

    static int input;

    public MainFragment() {

    }

    @Override
    public void onResume()
    {
        float amount = 0;

        String lastTime = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());

        String sDate = format.format(cal.getTime());

        String[][] times = {{null, null, null}, {null, null, null}, {null, null, null}};

        String sQuery = "SELECT _id, time, insulinType FROM " + Database.REGISTRATION_TABLE + " WHERE date = '" +
                sDate + "' AND insulin IS NOT NULL ORDER BY time DESC, _id DESC;";

        Cursor cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            times[0][0] = cursor.getString(0);
            times[0][1] = cursor.getString(1);
            if(cursor.getInt(2) == 0) {
                times[0][2] = "Baza";
            } else if(cursor.getInt(2) == 1){
                times[0][2] = "Bolus";
            } else {
                times[0][2] = "brak";
            }
        }

        String[] lastTimes = {null, null};

        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String sHour = formatHour.format(cal.getTime());

        sQuery = "SELECT time FROM " + Database.REGISTRATION_TABLE + " WHERE insulinType = 1 AND " +
                "date = '" + sDate + "' AND time <= '" + sHour + "' ORDER BY time DESC;";

        cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            lastTimes[0] = cursor.getString(0);
        }

        format = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());

        sDate = format.format(cal.getTime());

        sQuery = "SELECT _id, time, type FROM " + Database.POP_ENTRIES_TABLE + " WHERE date = '" +
                sDate + "' ORDER BY time DESC;";

        cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            times[1][0] = cursor.getString(0);
            times[1][1] = cursor.getString(1);

            if(cursor.getInt(2) == 0) {
                times[1][2] = "Baza";
            } else {
                times[1][2] = "Bolus";
            }
        }

        sQuery = "SELECT time FROM " + Database.POP_ENTRIES_TABLE + " WHERE type = 1 AND date = '" +
                sDate + "' AND time <= '" + sHour +"' ORDER BY time DESC;";

        cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            lastTimes[1] = cursor.getString(0);
        }

        sQuery = "SELECT _id, time FROM " + Database.CALC_ENTRIES_TABLE + " WHERE date = '" +
                sDate + "' ORDER BY time DESC;";

        cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            times[2][0] = cursor.getString(0);
            times[2][1] = cursor.getString(1);
            times[2][2] = "Brak";
        }

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();

        if(times[0][0] != null) {
            String[] parts = times[0][1].split(":");
            cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        }

        if(times[1][0] != null) {
            String[] parts = times[1][1].split(":");
            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        }

        if(times[2][0] != null) {
            String[] parts = times[2][1].split(":");
            cal3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal3.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        }

        if(times[0][0] != null && times[1][0] != null) {
            if(times[2][0] != null) {

                if(cal1.after(cal2) && cal1.after(cal3)) {
                    times[2][0] = null;
                    times[1][0] = null;
                } else if(cal2.after(cal1) && cal2.after(cal3)) {
                    times[2][0] = null;
                    times[0][0] = null;
                } else {
                    times[1][0] = null;
                    times[0][0] = null;
                }

            }

            if(cal1.after(cal2)) {
                times[1][0] = null;
            } else {
                times[0][0] = null;
            }
        } else if(times[0][0] != null && times[2][0] != null) {
            if(cal1.after(cal3)) {
                times[2][0] = null;
            } else {
                times[0][0] = null;
            }
        } else if(times[1][0] != null && times[2][0] != null) {
            if(cal2.after(cal3)) {
                times[2][0] = null;
            } else {
                times[1][0] = null;
            }
        }

        sQuery = null;

        if(times[0][0] != null) {
            sQuery = "SELECT insulin FROM " + Database.REGISTRATION_TABLE + " WHERE _id = " + times[0][0] +
            " AND insulin IS NOT NULL";
            insulinType.setText(times[0][2]);
        } else if(times[1][0] != null) {
            sQuery = "SELECT amount FROM " + Database.POP_ENTRIES_TABLE + " WHERE _id = " + times[1][0];
            insulinType.setText(times[1][2]);
        } else if(times[2][0] != null) {
            sQuery = "SELECT dose FROM " + Database.CALC_ENTRIES_TABLE + " WHERE _id = " + times[2][0];
            insulinType.setText(times[2][2]);
        } else {
            amount = 0;
        }

        if(sQuery != null) {
            cursor = MainActivity.db.query(sQuery);

            if(cursor != null && cursor.moveToFirst()) {
                amount = cursor.getFloat(0);
            }
        }

        if(lastTimes[0] != null && lastTimes[1] != null) {
            String[] parts = lastTimes[0].split(":");

            cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

            parts = lastTimes[1].split(":");

            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

            if(cal1.after(cal2)) {
                lastTimes[1] = null;
            } else {
                lastTimes[0] = null;
            }
        }


        if(lastTimes[0] != null) {
            lastTime = lastTimes[0];
        } else if(lastTimes[1] != null) {
            lastTime = lastTimes[1];
        }

        textView.setText(String.valueOf(amount));

        if(lastTime != null) {
            try {
                Date date1 = formatHour.parse(sHour);
                Date date2 = formatHour.parse(lastTime);

                long difference = date1.getTime() - date2.getTime();

                textView2.setText(String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toHours(difference)
                        , TimeUnit.MILLISECONDS.toMinutes(difference) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Animation mLoadAnimation = AnimationUtils.loadAnimation(context.getApplicationContext(), android.R.anim.fade_in);
        mLoadAnimation.setDuration(3000);

        TransitionDrawable transition = (TransitionDrawable) scrollView.getBackground();
        TransitionDrawable transition2 = (TransitionDrawable) scrollView2.getBackground();

        if(amount < 1) {
            transition.startTransition(0);
        }

        if(lastTime == null) {
            transition2.startTransition(0);
        }

        if(mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                sSwitch.setChecked(false);
            } else {
                sSwitch.setChecked(true);
            }
        }

        MainActivity.currentFragment = "mainFragment";

        FragmentManager fm = getActivity().getSupportFragmentManager();

        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        getActivity().setTitle("Strona główna");

        TextView tDate = (TextView) view.findViewById(R.id.mainDate);
        insulinType = (TextView) view.findViewById(R.id.textView13);


        ///// TEXTVIEWS

        TextView insulinSum = (TextView) view.findViewById(R.id.niceTextView21);
        TextView glycemiaAvg = (TextView) view.findViewById(R.id.niceTextView24);
        TextView glycemiaLast = (TextView) view.findViewById(R.id.textView52);
        TextView glycemiaLastTime = (TextView) view.findViewById(R.id.niceTextView25);

        float fInsulinSum = 0;
        int iGlycemiaSum = 0;
        int iGlycdemiaCounter = 0;

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());

        String sDate = format.format(cal.getTime());

        String sQuery = "SELECT insulin, glycemia, time FROM " + Database.REGISTRATION_TABLE + " WHERE date = '" +
                sDate + "' ORDER BY time DESC;";

        Cursor cursor = MainActivity.db.query(sQuery);

        String[][] times = {{null, null}, {null, null}};

        if(cursor != null && cursor.moveToFirst()) {
            do {
                if(cursor.getString(0) != null) fInsulinSum += cursor.getFloat(0);

                if(cursor.getString(1) != null) {
                    iGlycdemiaCounter++;
                    iGlycemiaSum += cursor.getInt(1);
                }

                if(times[0][0] == null) {
                    times[0][0] = cursor.getString(2);
                    times[0][1] = cursor.getString(1);
                }
            } while(cursor.moveToNext());
        }

        format = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());

        sDate = format.format(cal.getTime());

        sQuery = "SELECT amount FROM " + Database.POP_ENTRIES_TABLE + " WHERE date = '" + sDate + "';";

        cursor = MainActivity.db.query(sQuery);

        if(cursor != null && cursor.moveToFirst()) {
            do {
                if(cursor.getString(0) != null) fInsulinSum += cursor.getFloat(0);
            } while(cursor.moveToNext());
        }

        sQuery = "SELECT dose, glycemia, time FROM " + Database.CALC_ENTRIES_TABLE + " WHERE DATE = '"
                + sDate + "' ORDER BY time DESC;";

        cursor = MainActivity.db.query(sQuery);


        if(cursor != null && cursor.moveToFirst()) {
            do {
                if(cursor.getString(0) != null) fInsulinSum += cursor.getFloat(0);

                if(cursor.getString(1) != null) {
                    iGlycdemiaCounter++;
                    iGlycemiaSum += cursor.getInt(1);
                }

                if(times[1][0] == null) {
                    times[1][0] = cursor.getString(2);
                    times[1][1] = cursor.getString(1);
                }
            } while(cursor.moveToNext());
        }

        if(fInsulinSum > 0) {
            insulinSum.setText(String.valueOf(fInsulinSum));
        }

        if(iGlycdemiaCounter > 0) {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(2);

            float score = ((float) iGlycemiaSum / (float)iGlycdemiaCounter);

            glycemiaAvg.setText(decimalFormat.format(score));
        }

        if(times[0][0] != null && times[1][0] != null) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            String[] parts = times[0][0].split(":");
            cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

            parts = times[1][0].split(":");
            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

            if(cal2.after(cal1)) {
                times[0][0] = null;
            } else {
                times[1][0] = null;
            }
        }

        if(times[0][0] != null) {
            glycemiaLast.setText(times[0][1]);
            glycemiaLastTime.setText(times[0][0]);
        } else if(times[1][0] != null) {
            glycemiaLast.setText(times[1][1]);
            glycemiaLastTime.setText(times[1][0]);
        }

        ////// TEXTVIIEWS





        format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tDate.setText(format.format(cal.getTime()));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        sSwitch = (SwitchCompat) view.findViewById(R.id.Switch);
        sSwitch.setClickable(false);

        context = getActivity();

        scrollView = (ScrollView) view.findViewById(R.id.mainScoll2);
        scrollView2 = (ScrollView) view.findViewById(R.id.mainScoll1);

        textView = (TextView) view.findViewById(R.id.textView12);
        textView2 = (TextView) view.findViewById(R.id.niceTextView19);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what > 0) {
                    input = msg.what;

                    if(MainActivity.paused) {
                        return true;
                    }

                    PopFragment fragmentPop = new PopFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentPop);
                    fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                    fragmentTransaction.commit();

                    return true;
                }

                return false;
            }
        });

        if(mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                sSwitch.setChecked(true);
                thread = new AcceptThread(handler);
                thread.start();
            } else if (thread != null) {
                thread.cancel();
            }

            sSwitch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (sSwitch.isChecked()) {
                        mBluetoothAdapter.disable();
                    } else {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }

                    return false;
                }
            });

            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

                        if (state == BluetoothAdapter.STATE_OFF) {
                            sSwitch.setChecked(false);

                            if (thread != null) {
                                thread.cancel();
                            }
                        } else if (state == BluetoothAdapter.STATE_ON) {
                            sSwitch.setChecked(true);

                            thread = new AcceptThread(handler);
                            thread.start();
                        }
                    }
                }
            };

            getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        } else {
            sSwitch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("To urządzenie nie posiada Bluetooth.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    return false;
                }
            });
        }

        return view;
    }

}
