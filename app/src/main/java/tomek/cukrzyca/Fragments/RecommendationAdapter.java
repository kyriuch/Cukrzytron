package tomek.cukrzyca.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.DecimalFormat;
import java.util.List;

import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;

class RecommendationAdapter extends ArrayAdapter<Recommendation>{

    RecommendationAdapter(Context context, int resource, List<Recommendation> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(Recommendation item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.recomendation_list_item, null);
        }

        Recommendation recommendation = getItem(position);

        if(recommendation != null && view != null) {
            NiceTextView tName = (NiceTextView) view.findViewById(R.id.textView58);
            NiceTextView tTime = (NiceTextView) view.findViewById(R.id.textView57);
            NiceTextView tCarbohydrates = (NiceTextView) view.findViewById(R.id.niceTextView20);

            if(tName != null) {
                tName.setText(recommendation.getName());
            }

            if(tTime != null) {
                String spreadTime[] = recommendation.getTime().split(":");
                String hour = spreadTime[0];
                String min = spreadTime[1];

                if(hour.length() == 1) {
                    hour = "0" + hour;
                }

                if(min.length() == 1) {
                    min = "0" + min;
                }

                tTime.setText(hour + ":" + min);
            }

            if(tCarbohydrates != null) {
                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(2);

                tCarbohydrates.setText(format.format(recommendation.getCarbohydrates()));
            }
        }

        setNotifyOnChange(true);

        return view;
    }

    
}
