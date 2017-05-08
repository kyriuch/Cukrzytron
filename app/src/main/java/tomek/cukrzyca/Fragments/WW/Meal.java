package tomek.cukrzyca.Fragments.WW;


import java.util.ArrayList;

public class Meal {
    private int id;
    private String name;
    private boolean fav;
    public ArrayList<Product> productList;

    public Meal(int id, String name, boolean fav, ArrayList<Product> productList) {
        this.id = id;
        this.name = name;
        this.fav = fav;
        this.productList = productList;
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

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
