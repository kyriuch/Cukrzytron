package tomek.cukrzyca.Fragments.WW;


import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;


public class ProductInfoFragment extends Fragment {

    private TransitionDrawable td;

    public ProductInfoFragment() {
    }

    private float ww = 0.0f;

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "productInfoFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_product_info, container, false);

        final TextView tName = (TextView) view.findViewById(R.id.textView14);
        final TextView tCarbohydrates = (TextView) view.findViewById(R.id.textView35);
        final TextView tWW = (TextView) view.findViewById(R.id.textView36);
        final TextView tEnergyValue = (TextView) view.findViewById(R.id.textView37);
        final ImageView iFav = (ImageView) view.findViewById(R.id.imageView3);
        final EditText eGrams = (EditText) view.findViewById(R.id.editText11);

        final TextView newCarbohydrates = (TextView) view.findViewById(R.id.textView40);
        final TextView newWW = (TextView) view.findViewById(R.id.textView41);
        final TextView newEnergyValue = (TextView) view.findViewById(R.id.textView42);

        final Product product = new Product(ProductsFragment.productId, null, 0, 0, false);

        String sQuery = "SELECT name, carbohydrates, energy_value, fav FROM " + Database.PRODUCTS_TABLE + " WHERE _id = ?;";

        Cursor cursor = MainActivity.db.query(sQuery, new String[]{String.valueOf(product.getId())});

        if(cursor != null) {
            cursor.moveToFirst();

            String tmpName = cursor.getString(0);
            float tmpCarbohydrates = cursor.getFloat(1);
            int tmpEnergyValue = cursor.getInt(2);
            boolean tmpFav = cursor.getInt(3) == 1;

            product.setName(tmpName);
            product.setCarbohydrates(tmpCarbohydrates);
            product.setEnergyValue(tmpEnergyValue);
            product.setFav(tmpFav);
        }

        sQuery = "SELECT pointer FROM " + Database.WW_TABLE + " WHERE name = ?";

        cursor = MainActivity.db.query(sQuery, new String[]{product.getName()});

        if(cursor != null) {
            cursor.moveToFirst();
            ww = cursor.getFloat(0);
        }

        tName.setText(product.getName());
        tCarbohydrates.setText(String.valueOf(product.getCarbohydrates()) + " g");
        tWW.setText(String.valueOf(ww) + " ww");
        tEnergyValue.setText(String.valueOf(product.getEnergyValue()));

        final boolean productWasFav = product.isFav();


        td = new TransitionDrawable(new Drawable[] {
                ContextCompat.getDrawable(getContext(), R.drawable.ic_hearth_dark),
                ContextCompat.getDrawable(getContext(), R.drawable.ic_hearth_light)
        });

        iFav.setImageDrawable(td);

        if(productWasFav) {
            td.startTransition(0);
        }

        iFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isFav = product.isFav() ? 0 : 1;

                String sQuery = "UPDATE " + Database.PRODUCTS_TABLE + " SET fav = " + isFav + " WHERE _id = " + product.getId() + ";";
                MainActivity.db.exec(sQuery);

                product.setFav(isFav == 0);

                if(productWasFav) {
                    if(product.isFav()) {
                        td.reverseTransition(200);
                    } else {

                        td.startTransition(200);
                    }
                } else {
                    if(product.isFav()) {
                        td.startTransition(200);
                    } else {
                        td.reverseTransition(200);
                    }
                }
            }
        });

        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        eGrams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sGrams = s.toString();

                if(sGrams.isEmpty() || sGrams.equals("") || sGrams.trim().length() == 0)
                {
                    newCarbohydrates.setText("0 g");
                    newWW.setText("0 ww");
                    newEnergyValue.setText("0");
                } else {
                    float grams = Float.parseFloat(sGrams);

                    if(grams < 1 || grams > 10000) {
                        return;
                    }

                    float multiplier = grams / 100;

                    float fCarbohydrates = product.getCarbohydrates() * multiplier;
                    double fWW = ww * multiplier;
                    int fEnergyValue = (int) ((float) product.getEnergyValue() * multiplier);

                    newCarbohydrates.setText(df.format(fCarbohydrates) + " g");
                    newWW.setText(df.format(fWW) + " ww");
                    newEnergyValue.setText(String.valueOf(fEnergyValue));
                }
            }
        });

        return view;
    }

}
