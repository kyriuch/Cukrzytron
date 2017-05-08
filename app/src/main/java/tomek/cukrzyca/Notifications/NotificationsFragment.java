package tomek.cukrzyca.Notifications;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Calendar;

import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;


public class NotificationsFragment extends Fragment {

    private PagerAdapter mPagerAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        MainActivity.currentFragment = "notifsFragment";
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifs, container, false);

        getActivity().setTitle("Przypomnienia");

        this.mPagerAdapter = new NotifsPagerAdapter(getChildFragmentManager());
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.vPager);
        viewPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pagerSlidingTabStrip);
        tabs.setIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        tabs.setTypeface(FontCache.get(getContext()), 1);
        tabs.setTextColor(Color.BLACK);
        tabs.setViewPager(viewPager);

        Button bAddNew = (Button) view.findViewById(R.id.button2);
        bAddNew.setTypeface(FontCache.get(getContext()));

        bAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                final AddNewNotificationIntent newNotificationIntent = new AddNewNotificationIntent(
                        getContext(), cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE), viewPager);
                newNotificationIntent.show();
            }
        });

        return view;
    }
}
