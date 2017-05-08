package tomek.cukrzyca;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class NiceTextView extends TextView {

    public NiceTextView(Context context) {
        super(context);
        setTypeface(FontCache.get(context));
    }

    public NiceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontCache.get(context));
    }

    public NiceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontCache.get(context));
    }

    public void setTypeface(boolean bool) {
        if(bool) {
            setTypeface(FontCache.get(getContext()));
        }
    }
}
