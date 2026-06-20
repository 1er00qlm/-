import java.util.ArrayList;

public class Okno {
    private ArrayList podnosy;

    public Okno() {
        this.podnosy = new ArrayList();
    }

    public void dobavitPodnos() {
        podnosy.add(new Object());
    }

    public void ubratPodnos() {
        if (!podnosy.isEmpty()) podnosy.remove(0);
    }

    public int dlinaOcheredi() { return podnosy.size(); }
}