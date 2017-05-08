package tomek.cukrzyca.Fragments.WW;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tomek.cukrzyca.Notifications.DietFragment;
import tomek.cukrzyca.Notifications.InsulinFragment;
import tomek.cukrzyca.Notifications.OthersFragment;


public class WwPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    private String[] titles = {"Produkty", "Posiłki"};

    public WwPagerAdapter(FragmentManager fragmentManager) {
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
                return ProductsFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return MealsFragment.newInstance();
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
