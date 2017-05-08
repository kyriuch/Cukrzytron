package tomek.cukrzyca.Fragments.WW.AddProduct;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.Fragments.WW.Product;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class AddProductFragment extends Fragment {

    public static Product product = new Product(-1, null, 0, 0, false);

    private EditText name;
    private EditText wwValue;

    public AddProductFragment() {
    }


    @Override
    public void onResume() {
        super.onResume();

        if(product.getId() != -1) {
            name.setText(product.getName());
            wwValue.setText(String.valueOf(product.getPointer()));

            name.setFocusable(false);
            wwValue.setFocusable(false);
        }

        MainActivity.currentFragment = "addProductFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_add_product, container, false);

        Button save = (Button) ll.findViewById(R.id.button13);
        Button cancel = (Button) ll.findViewById(R.id.button14);
        name = (EditText) ll.findViewById(R.id.editText5);
        final EditText carbohydrates = (EditText) ll.findViewById(R.id.editText6);
        final EditText energyValue = (EditText) ll.findViewById(R.id.editText7);
        wwValue = (EditText) ll.findViewById(R.id.editText9);

        wwValue.setFocusable(false);

        carbohydrates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sValue = editable.toString();

                if(sValue.isEmpty()) {
                    wwValue.setText("");
                } else {
                    Float fValue = Float.parseFloat(sValue) / 10;

                    DecimalFormat decimalFormat = new DecimalFormat();
                    wwValue.setText(decimalFormat.format(fValue).replace(",", ""));
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = s.length() - 1; i > 0; i--) {
                    if (s.charAt(i) == '\n') {
                        s.delete(i, i + 1);
                        return;
                    }
                }
            }
        });

        save.setTypeface(FontCache.get(getContext()));
        cancel.setTypeface(FontCache.get(getContext()));
        name.setTypeface(FontCache.get(getContext()));
        carbohydrates.setTypeface(FontCache.get(getContext()));
        energyValue.setTypeface(FontCache.get(getContext()));
        wwValue.setTypeface(FontCache.get(getContext()));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sName = name.getText().toString();
                String sCarbohydrates = carbohydrates.getText().toString();
                String sEnergyValue = energyValue.getText().toString();
                String sWW = wwValue.getText().toString();

                if (sName.isEmpty() || sName.equals("") || sName.trim().length() == 0 ||
                        sCarbohydrates.isEmpty() || sCarbohydrates.equals("") || sCarbohydrates.length() == 0 ||
                        sEnergyValue.isEmpty() || sEnergyValue.equals("") || sEnergyValue.length() == 0 ||
                        sWW.isEmpty() || sWW.equals("") || sWW.length() == 0 ||
                        MainActivity.db.exists(Database.PRODUCTS_TABLE, "name", new String[]{sName}) ||
                        (MainActivity.db.exists(Database.WW_TABLE, "name", new String[]{sName}))) {
                    MyToast.show(getContext(), "Nie można dodać produktu");

                    return;
                }

                float iCarbohydrates = Float.parseFloat(carbohydrates.getText().toString());
                int iEnergyValue = (int) Float.parseFloat(energyValue.getText().toString());
                float fWW = Float.parseFloat(wwValue.getText().toString());

                if (iCarbohydrates < 1 || iCarbohydrates > 100000 || fWW == 0 || fWW > 100000) {
                    MyToast.show(getContext(), "Popraw pole: węglowodany");
                    return;
                }

                String sQuery = "INSERT INTO " + Database.PRODUCTS_TABLE + "(name, carbohydrates, energy_value) VALUES('" +
                        sName + "', " + iCarbohydrates + ", " + iEnergyValue + ");";

                MainActivity.db.exec(sQuery);


                sQuery = "INSERT INTO " + Database.WW_TABLE + "(name, pointer) VALUES('" +
                        sName + "', " + fWW + ");";
                MainActivity.db.exec(sQuery);

                MyToast.show(getContext(), "Dodano nowy produkt");

                getActivity().onBackPressed();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return ll;
    }

}
