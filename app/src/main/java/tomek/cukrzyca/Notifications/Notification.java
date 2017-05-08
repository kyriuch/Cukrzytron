package tomek.cukrzyca.Notifications;

public class Notification {
    private int id;
    private String time, name;
    private int type;
    private boolean active;
    private boolean[] days = new boolean[7];

    public static final int TYPE_DIET = 0;
    public static final int TYPE_GLYCEMIA = 1;
    public static final int TYPE_INSULIN = 2;
    public static final int TYPE_OTHERS = 3;

    public Notification() {

    }

    public Notification(int id, String time, String name, int type, boolean active, boolean[] days) {
        this.id = id;
        this.time = time;
        this.name = name;
        this.type = type;
        this.active = active;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }
}
