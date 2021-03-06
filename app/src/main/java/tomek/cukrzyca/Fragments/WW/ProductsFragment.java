package tomek.cukrzyca.Fragments.WW;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    public static int productId;
    private String searchString = "";
    private boolean getOnlyFav = false;

    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    private void createListView(ListView listView) {
        final ArrayList<Product> products = new ArrayList<>();

        String sQuery = "SELECT _id, name, fav FROM " + Database.PRODUCTS_TABLE;

        if(searchString.trim().length() > 0 || getOnlyFav) {
            sQuery += " WHERE ";

            if(searchString.trim().length() > 0) {
                sQuery += "name LIKE '%" + searchString + "%'";

                if(getOnlyFav) {
                    sQuery += " AND fav = 1";
                }
            }
            else if(getOnlyFav) {
                sQuery += "fav = 1";
            }
        }

        sQuery += ";";

        Cursor cursorProducts = MainActivity.db.query(sQuery);

        if(cursorProducts != null && cursorProducts.moveToFirst()) {
            do {
                int id = cursorProducts.getInt(0);
                String name = cursorProducts.getString(1);
                boolean fav = cursorProducts.getInt(2) == 1;
                products.add(new Product(id, name, 0, 0, fav));
            } while(cursorProducts.moveToNext());
        }

        final ProductAdapter productAdapter = new ProductAdapter(getContext(), R.layout.ww_simple_fragment, products);

        listView.setAdapter(productAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productId = products.get(i).getId();

                ProductInfoFragment fragment = new ProductInfoFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.productsListView);
        EditText eSearch = (EditText) view.findViewById(R.id.editText18);
        SwitchCompat mySwitch = (SwitchCompat) view.findViewById(R.id.switch2);

        eSearch.setTypeface(FontCache.get(getContext()));

        createListView(listView);

        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchString = editable.toString();
                createListView(listView);
            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getOnlyFav = b;
                createListView(listView);
            }
        });

        return view;
    }

}
