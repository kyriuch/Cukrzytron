package tomek.cukrzyca.Fragments.WW;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;

public class MealAdapter extends ArrayAdapter<Meal>{

    public MealAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MealAdapter(Context context, int resource, List<Meal> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(Meal item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ww_simple_fragment, null);
        }

        Meal meal = getItem(position);

        if(meal != null && view != null) {
            NiceTextView tName = (NiceTextView) view.findViewById(R.id.textView74);
            ImageView iFav = (ImageView) view.findViewById(R.id.imageView5);

            if(tName != null) {
                tName.setText(meal.getName());
            }

            if(iFav != null) {
                if(meal.isFav()) {
                    iFav.setBackgroundResource(R.drawable.ic_hearth_light);
                } else {
                    iFav.setBackgroundResource(R.drawable.ic_hearth_dark);
                }
            }
        }

        setNotifyOnChange(true);

        return view;
    }

    
}
