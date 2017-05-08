package tomek.cukrzyca;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Tomek on 22.12.2016.
 */

public class FontCache {

    private static Typeface tf = null;
    public static final int TEXT_APPEARANCE = android.support.v7.appcompat.R.style.TextAppearance_AppCompat;
    public static Typeface get(Context context) {
        if(tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), "Geomanist-Regular.otf");
        }

        return tf;
    }
}