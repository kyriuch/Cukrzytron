package tomek.cukrzyca.Notifications;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class NotifsPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 4;
    private String[] titles = {"Dieta", "Glikemia", "Insulina", "Inne"};

    public NotifsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DietFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return tomek.cukrzyca.Notifications.GlycemiaFragment.newInstance();
            case 2: // Fragment # 1 - This will show SecondFragment
                return InsulinFragment.newInstance();
            case 3:
                return OthersFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
