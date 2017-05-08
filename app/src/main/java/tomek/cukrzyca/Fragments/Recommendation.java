package tomek.cukrzyca.Fragments;


public class Recommendation {
    private int id;
    private String name;
    private String time;
    private float carbohydrates;

    public Recommendation(int id, String name, String time, float carbohydrates) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.carbohydrates = carbohydrates;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
