import java.util.ArrayList;

public class Statistika {
    public static void pokazat() {
        System.out.println("\n СТАТИСТИКА ");
        System.out.println("Время симуляции: " + Stolovaya.getTekusheeVremya());
        System.out.println("Обслужено посетителей: " + Stolovaya.getObsluzheno());
        System.out.println("Ушли из-за нехватки денег: " + Stolovaya.getUshliBezDeneg());
        System.out.println("Ушли из-за отсутствия блюд: " + Stolovaya.getUshliBezBlyud());
        System.out.println("Ушли из-за нехватки расходников: " + Stolovaya.getUshliBezMaterialov());
        System.out.println("Очередь к раздаче: " + Stolovaya.getDlinaOcherediRazdacha());
        System.out.println("Очередь к кассе: " + Stolovaya.getDlinaOcherediKassa());
        System.out.println("Очередь грязной посуды: " + Stolovaya.getDlinaOcherediOkno());
        ArrayList stoly = Stolovaya.getStoly();
        int zanyato = 0;
        for (int i = 0; i < stoly.size(); i++) {
            Stol s = (Stol) stoly.get(i);
            if (!s.svobodnye()) zanyato++;
        }
        System.out.println("Занято столов: " + zanyato + " из " + Stolovaya.STOLOV);
    }
}