package tomek.cukrzyca.Fragments.WW;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;

import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.Fragments.WW.AddMeal.AddMealFragment;
import tomek.cukrzyca.Fragments.WW.AddProduct.AddProductFragment;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

public class WwFragment extends Fragment {

    private WwPagerAdapter mPagerAdapter;

    public WwFragment() {

    }

    @Override
    public void onResume() {
        MainActivity.currentFragment = "wwFragment";
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ww, container, false);

        getActivity().setTitle("ww");

        this.mPagerAdapter = new WwPagerAdapter(getChildFragmentManager());
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.vPagerWw);
        viewPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pagerSlidingTabStrip);
        tabs.setIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        tabs.setTypeface(FontCache.get(getContext()), 1);
        tabs.setTextColor(Color.BLACK);
        tabs.setShouldExpand(true);
        tabs.setViewPager(viewPager);

        Button bNewProduct = (Button) view.findViewById(R.id.button11);
        Button bNewMeal = (Button) view.findViewById(R.id.button12);

        bNewProduct.setTypeface(FontCache.get(getContext()));
        bNewMeal.setTypeface(FontCache.get(getContext()));

        bNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductFragment fragment = new AddProductFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();
            }
        });

        bNewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMealFragment fragment = new AddMealFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();

                AddMealFragment.products.clear();
                AddMealFragment.ids.clear();
            }
        });

        return view;
    }
}
