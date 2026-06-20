import java.util.ArrayList;

public class Razdacha {
    private ArrayList ochered;

    public Razdacha() {
        this.ochered = new ArrayList();
    }

    public boolean voytiVOchered(Chelovek ch) {
        ochered.add(ch);
        return true;
    }

    public Chelovek obsluzhitSled() {
        if (ochered.isEmpty()) return null;
        return (Chelovek) ochered.remove(0);
    }

    public int dlinaOcheredi() { return ochered.size(); }
    public ArrayList getOchered() { return new ArrayList(ochered); }
}
