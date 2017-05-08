package tomek.cukrzyca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Database {
    public static final String USER_TABLE = "user";
    public static final String WW_TABLE = "ww";
    public static final String PREDEFINED_WW_TABLE = "predefinedWw";
    public static final String PRODUCTS_TABLE = "products";
    public static final String MEALS_TABLE = "meals";
    public static final String PRODUCTS_IN_MEALS_TABLE = "productsInMeals";
    public static final String CALC_SETTINGS_TABLE = "calcSettings";
    public static final String CALC_GLYCEMIA_TABLE = "calcGlycemia";
    public static final String CALC_INSULIN_RESISTANCE_TABLE = "calcInsulinResistance";
    public static final String CALC_INSULIN_WW_TABLE = "calcInsulinWw";
    public static final String NOTIFICATIONS_TABLE = "notifications";
    public static final String SETTINGS_TREATMENT_TABLE = "settingsTreatment";
    public static final String REGISTRATION_TABLE = "registrations";
    public static final String POP_ENTRIES_TABLE = "popEntries";
    public static final String CALC_ENTRIES_TABLE = "calcEntries";
    public static final String RECOMMENDATIONS_TABLE = "recommendations";
    public static final String RECOMMENDATIONS_SETTINGS = "recSettings";

    private SQLiteDatabase db;

    Database(Context context) {
        db = context.openOrCreateDatabase("database.db", Context.MODE_PRIVATE, null);

        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS user " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, firstName VARCHAR(128) NOT NULL, lastName VARCHAR(128) NOT NULL, birthDate DATE NOT NULL, sex INTEGER NOT NULL, height INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_USER);

        String CREATE_TABLE_WW = "CREATE TABLE IF NOT EXISTS ww " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(128) NOT NULL, pointer FLOAT NOT NULL);";
        db.execSQL(CREATE_TABLE_WW);

        String CREATE_TABLE__PREDEFINED_WW = "CREATE TABLE IF NOT EXISTS predefinedWw " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(128) NOT NULL, pointer FLOAT NOT NULL);";
        db.execSQL(CREATE_TABLE__PREDEFINED_WW);

        String CREATE_TABLE_PRODUCTS = "CREATE TABLE IF NOT EXISTS products " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(128), carbohydrates FLOAT NOT NULL, energy_value INTEGER NOT NULL, fav INTEGER DEFAULT 0);";
        db.execSQL(CREATE_TABLE_PRODUCTS);

        String CREATE_TABLE_MEALS = "CREATE TABLE IF NOT EXISTS meals " +
                "(_id INTEGER NOT NULL, name VARCHAR(128), fav INTEGER DEFAULT 0, PRIMARY KEY(_id));";
        db.execSQL(CREATE_TABLE_MEALS);

        String CREATE_TABLE_PRODUCTS_IN_MEALS = "CREATE TABLE IF NOT EXISTS productsInMeals " +
                "(meal_id INTEGER NOT NULL, product_id INTEGER NOT NULL, amount INTEGER NOT NULL, FOREIGN KEY(meal_id) REFERENCES meals(_id), FOREIGN KEY(product_id) REFERENCES products(_id));";
        db.execSQL(CREATE_TABLE_PRODUCTS_IN_MEALS);

        String CREATE_TABLE_RECOMMENDATIONS_SETTINGS = "CREATE TABLE IF NOT EXISTS recSettings " +
                "(active INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_RECOMMENDATIONS_SETTINGS);

        String CREATE_TABLE_CALC_SETTINGS = "CREATE TABLE IF NOT EXISTS calcSettings " +
                "(accuracy FLOAT NOT NULL);";
        db.execSQL(CREATE_TABLE_CALC_SETTINGS);

        String CREATE_TABLE_CALC_GLYCEMIA = "CREATE TABLE IF NOT EXISTS calcGlycemia " +
                "(time_start TIME NOT NULL, time_stop TIME NOT NULL, pointer INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_CALC_GLYCEMIA);

        String CREATE_TABLE_CALC_INSULIN_RESISTANCE = "CREATE TABLE IF NOT EXISTS calcInsulinResistance " +
                "(time_start TIME NOT NULL, time_stop TIME NOT NULL, pointer INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_CALC_INSULIN_RESISTANCE);

        String CREATE_TABLE_CALC_INSULIN_WW = "CREATE TABLE IF NOT EXISTS calcInsulinWw " +
                "(time_start TIME NOT NULL, time_stop TIME NOT NULL, pointer INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_CALC_INSULIN_WW);

        String CREATE_TABLE_SETTINGS_TREATMENT = "CREATE TABLE IF NOT EXISTS settingsTreatment " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR(64) NOT NULL, bazaType INTEGER NOT NULL, bolusType INTEGER NOT NULL, insulinDose FLOAT NOT NULL, pressureFrom1 INTEGER NOT NULL, " +
                "pressureFrom2 INTEGER NOT NULL, pressureFrom3 INTEGER NOT NULL, pressureTo1 INTEGER NOT NULL, pressureTo2 INTEGER NOT NULL, pressureTo3 INTEGER NOT NULL, weightFrom FLOAT NOT NULL," +
                " weightTo FLOAT NOT NULL, hba1cFrom FLOAT NOT NULL, hba1cTo FLOAT NOT NULL);";
        db.execSQL(CREATE_TABLE_SETTINGS_TREATMENT);

        String CREATE_TABLE_REGISTRATIONS = "CREATE TABLE IF NOT EXISTS registrations " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, time TIME NOT NULL, " +
                "type INTEGER NOT NULL, glycemia INTEGER, insulin FLOAT, " +
                "insulinType INTEGER, carbohydrates FLOAT, energyValue INTEGER, weight FLOAT, " +
                "pressure1 INTEGER, pressure2 INTEGER, pressure3 INTEGER, " +
                "activity VARCHAR(64), activityTime, hba1c FLOAT, note VARCHAR(256), bmi FLOAT);";
        db.execSQL(CREATE_TABLE_REGISTRATIONS);

        String CREATE_TABLE_POP_ENTRIES = "CREATE TABLE IF NOT EXISTS popEntries " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, time TIME NOT NULL, amount INTEGER" +
                " NOT NULL, type INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_POP_ENTRIES);

        String CREATE_TABLE_CALC_ENTIRES = "CREATE TABLE IF NOT EXISTS calcEntries " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, time TIME NOT NULL, activeInsulin " +
                "FLOAT, glycemia INTEGER, carbohydrates FLOAT, " +
                "dose INTEGER);";
        db.execSQL(CREATE_TABLE_CALC_ENTIRES);

        StringBuilder CREATE_TABLE_NOTIFICATIONS = new StringBuilder();

        CREATE_TABLE_NOTIFICATIONS.append("CREATE TABLE IF NOT EXISTS notifications ")
                .append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME NOT NULL, name TEXT NOT NULL, type INTEGER NOT NULL, active INTEGER NOT NULL, ");

        for(String day:new String[]{"monday, tuesday, wednesday, thursday, friday, saturday, sunday"}) {

            CREATE_TABLE_NOTIFICATIONS.append(day).append(" INTEGER NOT NULL, ");
            CREATE_TABLE_NOTIFICATIONS.delete(CREATE_TABLE_NOTIFICATIONS.length() - 2, CREATE_TABLE_NOTIFICATIONS.length() - 1);
        }

        CREATE_TABLE_NOTIFICATIONS.append(");");
        db.execSQL(CREATE_TABLE_NOTIFICATIONS.toString());

        String CREATE_TABLE_RECOMMENDATIONS = "CREATE TABLE IF NOT EXISTS recommendations " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME NOT NULL, title TEXT NOT NULL, " +
                "carbohydrates FLOAT NOT NULL);";

        db.execSQL(CREATE_TABLE_RECOMMENDATIONS);
    }

    public Cursor query(String query, String[] args) {
        return db.rawQuery(query, args);
    }

    public Cursor query(String query) { return db.rawQuery(query, null);}

    public void exec(String query) { db.execSQL(query);}


    void dropTable(String tableName) {
        db.execSQL("DROP TABLE " + tableName);
    }

    public boolean exists(String tableName, String columnName, String[] arg) {
        String sQuery = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?;";

        Cursor cursor = db.rawQuery(sQuery, arg);
        boolean output = (cursor != null && cursor.moveToFirst());
        if(output) {
            cursor.close();
        }

        return output;
    }
}
