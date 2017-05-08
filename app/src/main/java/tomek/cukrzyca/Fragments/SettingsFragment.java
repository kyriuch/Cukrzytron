package tomek.cukrzyca.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.Fragments.Calculator.CalcFirstStepFragment;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.Notifications.SimpleFragmentAdapter;
import tomek.cukrzyca.R;

public class SettingsFragment extends Fragment {

    private List<String> settingsList = new ArrayList<>();
    private List<Fragment> fragmentsList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragment = "settingsFragment";
        MainActivity.fromCalc = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getActivity().setTitle("Ustawienia");

        ListView listView = (ListView) view.findViewById(R.id.settingsListView);

        settingsList.clear();
        fragmentsList.clear();

        settingsList.add("Twoje dane");
        settingsList.add("Cele leczenia");
        settingsList.add("Kalkulator dawki");
        settingsList.add("Zalecenia dietetyczne");

        fragmentsList.add(new UserDataFragment());
        fragmentsList.add(new TreatmentDataFragment());
        fragmentsList.add(new CalcFirstStepFragment());
        fragmentsList.add(new RecommendationsFragment());

        SimpleFragmentAdapter simpleFragmentAdapter = new SimpleFragmentAdapter(getContext(), R.layout.settings_list_item, settingsList);
        listView.setAdapter(simpleFragmentAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = fragmentsList.get(i);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Czy zrestartować dane osobiste?")
                            .setCancelable(false)
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String sQuery = "DELETE FROM " + Database.USER_TABLE  + ";";
                                    MainActivity.db.exec(sQuery);
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(i == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Czy zrestartować cele leczenia?")
                            .setCancelable(false)
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String sQuery = "DELETE FROM " + Database.SETTINGS_TREATMENT_TABLE  + ";";
                                    MainActivity.db.exec(sQuery);
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if(i == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Czy zrestartować ustawienia kalkulatora?")
                            .setCancelable(false)
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String sQuery = "DELETE FROM " + Database.CALC_SETTINGS_TABLE  + ";";
                                    MainActivity.db.exec(sQuery);

                                    sQuery = "DELETE FROM " + Database.CALC_GLYCEMIA_TABLE + ";";
                                    MainActivity.db.exec(sQuery);

                                    sQuery = "DELETE FROM " + Database.CALC_INSULIN_RESISTANCE_TABLE  + ";";
                                    MainActivity.db.exec(sQuery);

                                    sQuery = "DELETE FROM " + Database.CALC_INSULIN_WW_TABLE  + ";";
                                    MainActivity.db.exec(sQuery);
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                return true;
            }
        });

        return view;
    }
}
