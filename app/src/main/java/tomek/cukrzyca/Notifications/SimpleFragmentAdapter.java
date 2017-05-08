package tomek.cukrzyca.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import tomek.cukrzyca.NiceTextView;
import tomek.cukrzyca.R;

public class SimpleFragmentAdapter extends ArrayAdapter<String>{

    public SimpleFragmentAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SimpleFragmentAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(String item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.settings_list_item, null);
        }

        String name = getItem(position);

        if(name != null) {
            NiceTextView tName = (NiceTextView) view.findViewById(R.id.textView15);

            if(tName != null) {
                tName.setText(name);
            }
        }

        setNotifyOnChange(true);

        return view;
    }
}
