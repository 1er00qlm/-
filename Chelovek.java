import java.util.ArrayList;

public class Chelovek {
    private static int sledId = 1;
    private int id;
    private int dengi;
    private int sostoyanie;
    private ArrayList vybrannyeBlyuda;
    private double obshayaCena;
    private Stol tekushiyStol;
    private ArrayList gruppa;
    private boolean chlenGruppy;

    public Chelovek(int dengi, boolean chlenGruppy) {
        this.id = sledId++;
        this.dengi = dengi;
        this.sostoyanie = 0;
        this.vybrannyeBlyuda = new ArrayList();
        this.obshayaCena = 0;
        this.tekushiyStol = null;
        this.gruppa = null;
        this.chlenGruppy = chlenGruppy;
    }

    public int getId() { return id; }
    public int getDengi() { return dengi; }
    public int getSostoyanie() { return sostoyanie; }
    public Stol poluchitStol() { return tekushiyStol; }
    public void ustanovitGruppu(ArrayList g) { this.gruppa = g; }
    public boolean chlenGruppy() { return chlenGruppy; }

    public void obnovit() {
        if (sostoyanie == 0) {
            if (Stolovaya.getRazdacha().voytiVOchered(this)) {
                sostoyanie = 1;
            }
        } else if (sostoyanie == 1) {
            vibratBlyuda();
            if (vybrannyeBlyuda.size() > 0) {
                sostoyanie = 2;
                Stolovaya.getKassa().voytiVOchered(this);
            } else {
                uytiBezBlyud();
            }
        } else if (sostoyanie == 2) {
        } else if (sostoyanie == 3) {
            if (Stolovaya.getTekusheeVremya() % 5 == 0 && (Stolovaya.getTekusheeVremya() / 5) % 1 == 0) {
                zakonchitEst();
            }
        }
    }

    private void vibratBlyuda() {
        ArrayList blyuda = Stolovaya.getBlyuda();
        for (int i = 0; i < blyuda.size(); i++) {
            Blyudo b = (Blyudo) blyuda.get(i);
            if (b.kolichestvo() <= 0) continue;
            double ver = b.predpochtenie() / (Stolovaya.RAZBROS_PREDPOCHTENIY + 1);
            if (Stolovaya.sluch.nextDouble() < ver) {
                if (dengi >= b.cena() + obshayaCena) {
                    if (b.umenshit()) {
                        vybrannyeBlyuda.add(b);
                        obshayaCena += b.cena();
                    }
                } else {
                    break;
                }
            }
        }
    }

    public void oplatit() {
        if (dengi >= obshayaCena) {
            dengi -= obshayaCena;
            Stolovaya.incrementObsluzheno();
            sostoyanie = 3;
            naytiStol();
        } else {
            for (int i = 0; i < vybrannyeBlyuda.size(); i++) {
                Blyudo b = (Blyudo) vybrannyeBlyuda.get(i);
                b.uvelichit();
            }
            vybrannyeBlyuda.clear();
            Stolovaya.incrementUshliBezDeneg();
            uyti();
        }
    }

    private void naytiStol() {
        ArrayList stoly = Stolovaya.getStoly();
        if (chlenGruppy && gruppa != null) {
            int nuzhno = gruppa.size();
            Stol vybranny = null;
            for (int i = 0; i < stoly.size(); i++) {
                Stol s = (Stol) stoly.get(i);
                if (s.svobodnyeMesta() >= nuzhno) {
                    vybranny = s;
                    break;
                }
            }
            if (vybranny == null) {
                vybranny = popytatSdvinutStoly(nuzhno);
            }
            if (vybranny != null) {
                for (int i = 0; i < gruppa.size(); i++) {
                    Chelovek ch = (Chelovek) gruppa.get(i);
                    ch.tekushiyStol = vybranny;
                    vybranny.zanyatMesto(ch);
                    ch.sostoyanie = 3;
                }
            } else {
                for (int i = 0; i < gruppa.size(); i++) {
                    Chelovek ch = (Chelovek) gruppa.get(i);
                    ch.uyti();
                }
            }
        } else {
            for (int i = 0; i < stoly.size(); i++) {
                Stol s = (Stol) stoly.get(i);
                if (s.svobodnyeMesta() > 0) {
                    this.tekushiyStol = s;
                    s.zanyatMesto(this);
                    sostoyanie = 3;
                    return;
                }
            }
            uyti();
        }
    }

    private Stol popytatSdvinutStoly(int nuzhnoMest) {
        ArrayList stoly = Stolovaya.getStoly();
        for (int i = 0; i < stoly.size() - 1; i++) {
            Stol s1 = (Stol) stoly.get(i);
            Stol s2 = (Stol) stoly.get(i+1);
            if (s1.svobodnyeMesta() + s2.svobodnyeMesta() >= nuzhnoMest && s1.svobodnye() && s2.svobodnye()) {
                s1.zanyatMesto(null);
                s2.zanyatMesto(null);
                return s1;
            }
        }
        return null;
    }

    private void zakonchitEst() {
        Stolovaya.getOkno().dobavitPodnos();
        uyti();
    }

    private void uyti() {
        if (tekushiyStol != null) tekushiyStol.osvoboditMesto(this);
        Stolovaya.chelovekUxodit(this);
        sostoyanie = 4;
    }

    private void uytiBezBlyud() {
        Stolovaya.incrementUshliBezBlyud();
        uyti();
    }

    public boolean popytkaVoyti() {
        return true;
    }
}