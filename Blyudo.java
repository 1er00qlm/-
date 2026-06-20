public class Blyudo {
    private String imya;
    private int kolichestvo;
    private double cena;
    private double predpochtenie;

    public Blyudo(String imya, int kolichestvo, double cena, double predpochtenie) {
        this.imya = imya;
        this.kolichestvo = kolichestvo;
        this.cena = cena;
        this.predpochtenie = predpochtenie;
    }

    public String imya() { return imya; }
    public int kolichestvo() { return kolichestvo; }
    public double cena() { return cena; }
    public double predpochtenie() { return predpochtenie; }

    public boolean umenshit() {
        if (kolichestvo > 0) {
            kolichestvo--;
            return true;
        }
        return false;
    }

    public void uvelichit() {
        kolichestvo++;
    }
}