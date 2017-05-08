package tomek.cukrzyca.Fragments.WW.AddMeal;

import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.FontCache;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.Fragments.WW.Product;
import tomek.cukrzyca.MyToast;
import tomek.cukrzyca.R;


public class AddMealFragment extends Fragment {

    private ArrayList<Integer> list = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<EditText> listEditTexts = new ArrayList<>();

    public AddMealFragment() {
    }

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "addMealFragment";
    }

    private void addNewRow(LinearLayout root, Product product) {
        LinearLayout lRow = new LinearLayout(getContext());
        lRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        lRow.setOrientation(LinearLayout.HORIZONTAL);

        final TextView tName = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);
        final EditText eAmount = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edtemplate, null);
        final TextView tGrams = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);

        tName.setText(product.getName());
        tName.setTypeface(FontCache.get(getContext()));
        tGrams.setText("g");
        tGrams.setTypeface(FontCache.get(getContext()));
        eAmount.setTypeface(FontCache.get(getContext()));

        lRow.addView(tName);
        lRow.addView(eAmount);
        lRow.addView(tGrams);

        root.addView(lRow);

        listEditTexts.add(eAmount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        final LinearLayout verticalLayout = (LinearLayout) view.findViewById(R.id.linearLayout22);
        final Button addProduct = (Button) view.findViewById(R.id.button18);
        final Button saveMeal = (Button) view.findViewById(R.id.button19);
        final Button cancelAddMeal = (Button) view.findViewById(R.id.button20);
        final EditText mealNameEditText = (EditText) view.findViewById(R.id.editText8);

        addProduct.setTypeface(FontCache.get(getContext()));
        saveMeal.setTypeface(FontCache.get(getContext()));
        cancelAddMeal.setTypeface(FontCache.get(getContext()));
        mealNameEditText.setTypeface(FontCache.get(getContext()));

        listEditTexts.clear();

        for(Product p:products) {
            addNewRow(verticalLayout, p);
        }

        mealNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for(int i = s.length()-1; i > 0; i--){
                    if(s.charAt(i) == '\n'){
                        s.delete(i, i + 1);
                        return;
                    }
                }
            }
        });


        saveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealName = mealNameEditText.getText().toString();

                if(mealName.isEmpty() || mealName.equals("")) {
                    MyToast.show(getContext(), "Uzupełnij puste pola");
                    return;
                }

                if(listEditTexts.isEmpty()) {
                    MyToast.show(getContext(), "Posiłek nie zawiera produktów");
                    return;
                }

                for(EditText e:listEditTexts) {
                    String sAmount = e.getText().toString();

                    if(sAmount.trim().isEmpty()) {
                        list.clear();
                        MyToast.show(getContext(), "Uzupełnij puste pola");
                        return;
                    }

                    list.add(Integer.parseInt(sAmount));
                }

                String sQuery = "SELECT (m1._id+1) FROM " + Database.MEALS_TABLE + " m1 LEFT JOIN " + Database.MEALS_TABLE + " m2 " +
                        "ON m1._id+1 = m2._id WHERE m2._id IS NULL;";

                Cursor cursor = MainActivity.db.query(sQuery);

                int meal_id = 0;

                if(cursor != null && cursor.moveToFirst()) {
                    meal_id = cursor.getInt(0);
                }

                for(int i = 0; i < products.size(); i++)
                {
                    Product currentProduct = products.get(i);
                    int amount = list.get(i);

                    sQuery = "INSERT INTO " + Database.PRODUCTS_IN_MEALS_TABLE + "(meal_id, product_id, amount) VALUES(" +
                            meal_id + ", " + currentProduct.getId() + ", " + amount + ");";

                    MainActivity.db.exec(sQuery);
                }

                sQuery = "INSERT INTO " + Database.MEALS_TABLE + "(_id, name) VALUES(" + meal_id + ", '" + mealName + "');";
                MainActivity.db.exec(sQuery);
                MyToast.show(getContext(), "Dodano nowy posiłek");
                getFragmentManager().popBackStack();
            }
        });

        cancelAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindProductsFragment fragment = new FindProductsFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                fragmentTransaction.commit();
            }
        });

        return view;

    }

}
