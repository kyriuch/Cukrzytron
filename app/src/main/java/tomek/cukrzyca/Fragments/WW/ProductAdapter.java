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

public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(Product item) {
        super.add(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ww_simple_fragment, null);
        }

        Product product = getItem(position);

        if(product != null && view != null) {
            NiceTextView tName = (NiceTextView) view.findViewById(R.id.textView74);
            ImageView iFav = (ImageView) view.findViewById(R.id.imageView5);

            if(tName != null) {
                tName.setText(product.getName());
            }

            if(iFav != null) {
                if(product.isFav()) {
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
