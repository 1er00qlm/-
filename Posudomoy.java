class Posudomoy extends Rabotnik {
    Posudomoy() {
        super("посудомой");
    }

    void rabotat() {
        if (Stolovaya.getTekusheeVremya() % 2 == 0) {
            Stolovaya.getOkno().ubratPodnos();
            obsluzheno++;
        }
    }
}