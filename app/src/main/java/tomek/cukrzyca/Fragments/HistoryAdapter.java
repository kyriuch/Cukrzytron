package tomek.cukrzyca.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;
import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;

public class HistoryAdapter extends ArrayAdapter<HistoryElement>{

    public HistoryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public HistoryAdapter(Context context, int resource, List<HistoryElement> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(HistoryElement item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.history_element, null);
        }

        HistoryElement element = getItem(position);

        if(element != null && view != null) {
            NiceTextView tTitle = (NiceTextView) view.findViewById(R.id.textView24);
            NiceTextView tHint = (NiceTextView) view.findViewById(R.id.historyHint);
            ImageView iIcon = (ImageView) view.findViewById(R.id.imageView6);


            if(tTitle != null) {
                tTitle.setText(element.getTime() + " " +element.getTitle());
            }

            if(element.getHint() != null) {
                tHint.setText(element.getHint());
            }

            if(element.getType() == HistoryElement.TYPE_REGISTRATION) {
                iIcon.setBackgroundResource(R.drawable.ic_history_plus);
            } else if(element.getType() == HistoryElement.TYPE_CALC) {
                iIcon.setBackgroundResource(R.drawable.ic_history_calc);
            } else {
                iIcon.setBackgroundResource(R.drawable.ic_history_pen);
            }

        }

        setNotifyOnChange(true);

        return view;
    }

    
}
