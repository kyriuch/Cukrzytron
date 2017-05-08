package tomek.cukrzyca;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tomek on 11.01.2017.
 */

public class MyToast {

    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
