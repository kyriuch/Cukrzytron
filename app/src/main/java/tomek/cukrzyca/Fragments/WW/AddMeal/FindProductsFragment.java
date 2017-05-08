package tomek.cukrzyca.Fragments.WW.AddMeal;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;
import tomek.cukrzyca.Fragments.WW.Product;
import tomek.cukrzyca.R;

public class FindProductsFragment extends Fragment {


    public FindProductsFragment() {
    }

    public void onResume() {
        super.onResume();

        MainActivity.currentFragment = "findProductsFragment";
    }

    private List<Product> products;

    private void addNewRow(LinearLayout root, final Product product) {
        final LinearLayout lRow = new LinearLayout(getContext());
        lRow.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        lRow.setLayoutParams(layoutParams);
        lRow.setGravity(Gravity.CENTER);
        lRow.setBackgroundResource(R.drawable.border);

        final TextView tName = (TextView) getActivity().getLayoutInflater().inflate(R.layout.tvtemplate, null);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 20, 0, 15);

        tName.setText(product.getName());
        tName.setLayoutParams(textParams);
        tName.setTextSize(22.0f);

        lRow.addView(tName);
        root.addView(lRow);

        lRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AddMealFragment.ids.contains(product.getId())) {
                    AddMealFragment.products.add(product);
                    AddMealFragment.ids.add(product.getId());
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_add_product_to_meal, container, false);
        LinearLayout verticalLayout = (LinearLayout) ll.findViewById(R.id.linearLayout21);

        String sQuery = "SELECT _id, name FROM " + Database.PRODUCTS_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        products = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                products.add(new Product(id, name, 0, 0, false));
            } while(cursor.moveToNext());
        }

        for(Product p:products) {
            addNewRow(verticalLayout, p);
        }

        return ll;
    }

}
