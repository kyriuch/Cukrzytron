package tomek.cukrzyca.Fragments;


public class HistoryElement {
    private int id;
    private int type;
    private String title;
    private String time;
    private String hint = null;

    public static final int TYPE_REGISTRATION = 0;
    public static final int TYPE_CALC = 1;
    public static final int TYPE_PEN = 2;

    public HistoryElement(int id, int type, String time, String title, String hint) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.title = title;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public String getTime() { return time; }
}
