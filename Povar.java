class Povar extends Rabotnik {
    Povar() {
        super("повар");
    }

    void rabotat() {
        if (Stolovaya.getTekusheeVremya() % 5 == 0) {
            // пополнение блюд
            java.util.ArrayList blyuda = Stolovaya.getBlyuda();
            for (int i = 0; i < blyuda.size(); i++) {
                Blyudo b = (Blyudo) blyuda.get(i);
                int dob = 1 + Stolovaya.sluch.nextInt(3);
                for (int j = 0; j < dob; j++) b.uvelichit();
            }
            // пополнение расходных материалов
            Stolovaya.addMaterials(2, 3, 3, 5);
            obsluzheno++;
        }
    }
}