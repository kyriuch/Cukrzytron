package tomek.cukrzyca.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.Notifications.NotificationService;
import tomek.cukrzyca.R;


public class RecommendationsFragment extends Fragment {

    ArrayList<Recommendation> recommendationList;
    RecommendationAdapter recommendationAdapter;

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        MainActivity.currentFragment = "recommendationsFragment";

        super.onResume();
    }

    private void createListView(ListView listView, final Context context) {
        recommendationList = new ArrayList<>();

        String sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Recommendation recommendation = new Recommendation(cursor.getInt(0),
                        cursor.getString(2), cursor.getString(1), cursor.getFloat(3));

                recommendationList.add(recommendation);
            } while (cursor.moveToNext());
        }


        recommendationAdapter = new RecommendationAdapter(context, R.layout.recomendation_list_item, recommendationList);
        listView.setAdapter(recommendationAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final UpdateRecommendationIntent updateRecommendationIntent =
                        new UpdateRecommendationIntent(getContext(),
                                recommendationList,
                                i,
                                recommendationAdapter);

                updateRecommendationIntent.show();

                return true;

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recomendations, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.recList);
        final SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.switch4);
        final Button button = (Button) view.findViewById(R.id.button29);

        createListView(listView, getContext());

        String sQuery = "SELECT * FROM " + Database.RECOMMENDATIONS_SETTINGS + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getInt(0) == 1) {
                switchCompat.setChecked(true);
            }
        } else {
            sQuery = "INSERT INTO " + Database.RECOMMENDATIONS_SETTINGS + " VALUES(0);";

            MainActivity.db.exec(sQuery);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String sQuery = "UPDATE " + Database.RECOMMENDATIONS_SETTINGS + " SET active = ";

                if (b) {
                    sQuery += "1";
                } else {
                    sQuery += "0";
                }

                sQuery += ";";

                MainActivity.db.exec(sQuery);

                NotificationService.refreshTables();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                final AddNewRecommendationIntent newRecommendationIntent =
                        new AddNewRecommendationIntent(getContext(),
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                recommendationList,
                                recommendationAdapter);

                newRecommendationIntent.show();
            }
        });

        return view;
    }

}
