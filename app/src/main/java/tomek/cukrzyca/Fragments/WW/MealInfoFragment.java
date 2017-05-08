package tomek.cukrzyca.Fragments.WW;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.R;

public class MealInfoFragment extends Fragment {

    private SparseIntArray grams = new SparseIntArray();
    private ArrayList<Integer> currentGrams = new ArrayList<>();
    private int keys = 0;
    private Meal meal;
    private int gramsTotal;
    private float carbohydratesTotal;
    private int energyValueTotal;

    private TextView tGrams ;
    private TextView tCarbohydrates;
    private TextView tWW;
    private TextView tEnergyValue;

    private TransitionDrawable td;

    public MealInfoFragment() {
    }

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "mealInfoFragment";
    }

    private void addNewRow(LinearLayout root, Product product) {
        LinearLayout lRow = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lp.setMargins(0, 0, 0, 20);
        lRow.setLayoutParams(lp);
        lRow.setOrientation(LinearLayout.HORIZONTAL);

        final TextView tTmpName = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tTmpCarbohydrates = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tTmpEnergyValue = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tTmpAmount = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final TextView tTmpWw = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);

        String sQuery = "SELECT amount FROM " + Database.PRODUCTS_IN_MEALS_TABLE + " WHERE meal_id = ? AND product_id = ?;";

        Cursor cursor = MainActivity.db.query(sQuery, new String[]{String.valueOf(MealsFragment.mealId), String.valueOf(product.getId())});

        int curr = 0;

        if(cursor != null && cursor.moveToFirst()) {
            curr = cursor.getInt(0);
            tTmpAmount.setText(String.valueOf(cursor.getInt(0)) + "g\t\t");
        }

        tTmpName.setText(product.getName() + "\t\t");
        tTmpCarbohydrates.setText(String.valueOf(product.getCarbohydrates()) + "g\t\t");
        tTmpWw.setText(String.valueOf(product.getPointer() / ((float)100 / (float)curr)) + "ww\t\t");
        tTmpEnergyValue.setText(String.valueOf(product.getEnergyValue()) + "kcal");

        lRow.addView(tTmpName);
        lRow.addView(tTmpAmount);
        lRow.addView(tTmpCarbohydrates);
        lRow.addView(tTmpWw);
        lRow.addView(tTmpEnergyValue);

        keys++;
        final int tmp = keys - 1;

        lRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());



                alert.setTitle("Podaj wartość w g");
                final EditText input = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setEms(3);
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!input.getText().toString().isEmpty()) {
                            int gram = Integer.parseInt(input.getText().toString());
                            grams.put(tmp, gram);

                            Product currentProduct = meal.productList.get(tmp);

                            float multiplier = (float)gram / (float)currentGrams.get(tmp);

                            tTmpCarbohydrates.setText(String.valueOf(currentProduct.getCarbohydrates() * multiplier) + "g\t\t");
                            tTmpWw.setText(String.valueOf(currentProduct.getCarbohydrates() * multiplier / 10.0) + "ww\t\t");
                            tTmpEnergyValue.setText(String.valueOf(currentProduct.getEnergyValue() * multiplier) + "kcal");
                            tTmpAmount.setText(String.valueOf(gram) + "g\t\t");


                            gramsTotal = 0;
                            carbohydratesTotal = 0;
                            energyValueTotal = 0;

                            for(int j = 0; j < meal.productList.size(); j++) {
                                gram = grams.get(j);
                                currentProduct = meal.productList.get(j);

                                multiplier = (float)gram / (float)currentGrams.get(j);

                                gramsTotal += gram;
                                carbohydratesTotal += currentProduct.getCarbohydrates() * multiplier;
                                energyValueTotal += (int)(currentProduct.getEnergyValue() * multiplier);
                            }

                            DecimalFormat decimalFormat = new DecimalFormat();
                            decimalFormat.setMaximumFractionDigits(2);

                            tGrams.setText(String.valueOf(gramsTotal) + "g");
                            tCarbohydrates.setText(decimalFormat.format(carbohydratesTotal) + " g węglowodanów");
                            tWW.setText(decimalFormat.format(carbohydratesTotal / 10.0) + " ww");
                            tEnergyValue.setText(String.valueOf(energyValueTotal) + " kcal");
                        }
                    }
                });

                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alert.show();
            }
        });

        root.addView(lRow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_show_meal_info, container, false);
        final LinearLayout verticalLayout = (LinearLayout) ll.findViewById(R.id.linearLayout17);

        tGrams = (TextView) ll.findViewById(R.id.textView46);
        tCarbohydrates = (TextView) ll.findViewById(R.id.textView47);
        tWW = (TextView) ll.findViewById(R.id.textView48);
        tEnergyValue = (TextView) ll.findViewById(R.id.textView49);
        final TextView tName = (TextView) ll.findViewById(R.id.textView43);
        final ImageView iFav = (ImageView) ll.findViewById(R.id.imageView4);
        meal = new Meal(MealsFragment.mealId, null, false, new ArrayList<Product>());

        String sQuery = "SELECT name, fav FROM " + Database.MEALS_TABLE + " WHERE _id = ?;";

        Cursor cursor = MainActivity.db.query(sQuery, new String[]{String.valueOf(meal.getId())});

        if(cursor != null) {
            cursor.moveToFirst();

            String name = cursor.getString(0);
            boolean fav = cursor.getInt(1) == 1;

            meal.setName(name);
            meal.setFav(fav);
        }

        sQuery = "SELECT p._id, p.name, p.carbohydrates, p.energy_value, p.fav, pm.amount FROM " + Database.PRODUCTS_TABLE + " p JOIN " + Database.PRODUCTS_IN_MEALS_TABLE +
                " pm ON pm.meal_id = ? WHERE pm.product_id = p._id;";

        cursor = MainActivity.db.query(sQuery, new String[]{String.valueOf(meal.getId())});

        gramsTotal = 0;
        carbohydratesTotal = 0;
        float wwTotal = 0;
        energyValueTotal = 0;
        int keys = 0;

        if(cursor != null) {
            cursor.moveToFirst();

            int id;
            String name;
            float carbohydrates;
            int energy_value;
            boolean fav;

            do {
                id = cursor.getInt(0);
                name = cursor.getString(1);
                carbohydrates = cursor.getFloat(2) / ((float)100 / cursor.getInt(5));
                energy_value = (int)(cursor.getInt(3) / ((float)100 / (float)cursor.getInt(5)));
                fav = cursor.getInt(4) == 1;

                Product thisProduct = new Product(id, name, carbohydrates, energy_value, fav);

                meal.productList.add(thisProduct);

                gramsTotal += cursor.getInt(5);
                carbohydratesTotal += thisProduct.getCarbohydrates();
                wwTotal += (thisProduct.getPointer() / ((float)100 / (float)cursor.getInt(5)));
                energyValueTotal += thisProduct.getEnergyValue();

                currentGrams.add(cursor.getInt(5));
                grams.append(keys++, cursor.getInt(5));
            } while(cursor.moveToNext());
        }


        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);

        tGrams.setText(String.valueOf(gramsTotal) + "g");
        tCarbohydrates.setText(decimalFormat.format(carbohydratesTotal) + " g węglowodanów");
        tWW.setText(decimalFormat.format(wwTotal) + " ww");
        tEnergyValue.setText(String.valueOf(energyValueTotal) + " kcal");



        tName.setText(meal.getName());

        final boolean mealWasFav = meal.isFav();


        td = new TransitionDrawable(new Drawable[] {
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_hearth_dark),
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_hearth_light)
        });

        iFav.setImageDrawable(td);

        if(mealWasFav) {
            td.startTransition(0);
        }

        iFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isFav = meal.isFav() ? 0 : 1;

                String sQuery = "UPDATE " + Database.MEALS_TABLE + " SET fav = " + isFav + " WHERE _id = " + meal.getId() + ";";
                MainActivity.db.exec(sQuery);

                meal.setFav(isFav == 0);

                if(mealWasFav) {
                    if(meal.isFav()) {
                        td.reverseTransition(200);
                    } else {

                        td.startTransition(200);
                    }
                } else {
                    if(meal.isFav()) {
                        td.startTransition(200);
                    } else {
                        td.reverseTransition(200);
                    }
                }
            }
        });

        for(Product p:meal.productList) {
            addNewRow(verticalLayout, p);
        }

        return ll;
    }

}
