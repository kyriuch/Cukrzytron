package tomek.cukrzyca.Fragments.WW;

import android.database.Cursor;

import tomek.cukrzyca.Database;
import tomek.cukrzyca.MainActivity;

public class Product {
    private int id;
    private String name;
    private float carbohydrates;
    private int energyValue;
    private boolean fav;
    private float pointer;

    public Product(int id, String name, float carbohydrates, int energyValue, boolean fav) {
        this.id = id;
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.energyValue = energyValue;
        this.fav = fav;

        String sQuery = "SELECT pointer FROM " + Database.WW_TABLE + " WHERE name = ?;";

        if(name != null) {
            Cursor cursor = MainActivity.db.query(sQuery, new String[]{name});

            if (cursor != null && cursor.moveToFirst()) {
                pointer = cursor.getFloat(0);
            } else {
                pointer = 0.0f;
            }
        } else {
            pointer = 0.0f;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(int energyValue) {
        this.energyValue = energyValue;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public float getPointer() {
        return pointer;
    }

    public void setPointer(float pointer) {
        this.pointer = pointer;
    }
}
