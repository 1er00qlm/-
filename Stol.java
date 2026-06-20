import java.util.ArrayList;

public class Stol {
    private int nomer;
    private int maxMest;
    private ArrayList sidyashie;
    private boolean obedinen;

    public Stol(int nomer, int maxMest) {
        this.nomer = nomer;
        this.maxMest = maxMest;
        this.sidyashie = new ArrayList();
        this.obedinen = false;
    }

    public int getNomer() { return nomer; }
    public int maxMest() { return maxMest; }
    public int svobodnyeMesta() { return maxMest - sidyashie.size(); }
    public boolean svobodnye() { return sidyashie.isEmpty(); }
    public ArrayList getSidyashie() { return new ArrayList(sidyashie); }

    public void zanyatMesto(Chelovek ch) {
        if (ch != null) sidyashie.add(ch);
        else sidyashie.add(null);
    }

    public void osvoboditMesto(Chelovek ch) {
        sidyashie.remove(ch);
    }
}