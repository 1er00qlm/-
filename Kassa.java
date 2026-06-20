import java.util.ArrayList;

public class Kassa {
    private ArrayList ochered;

    public Kassa() {
        this.ochered = new ArrayList();
    }

    public void voytiVOchered(Chelovek ch) {
        ochered.add(ch);
    }

    public void obsluzhit() {
        if (!ochered.isEmpty()) {
            Chelovek ch = (Chelovek) ochered.remove(0);
            ch.oplatit();
        }
    }

    public int dlinaOcheredi() { return ochered.size(); }
    public ArrayList getOchered() { return new ArrayList(ochered); }
}