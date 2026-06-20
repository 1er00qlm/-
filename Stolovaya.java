import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Stolovaya {
    // параметры
    public static int STOLOV = 8;
    public static int MESTA_ZA_STOLOM = 4;
    public static int NACHAINOYE_KOLICHESTVO_BLYUD = 5;
    public static int TIPOV_BLYUD = 3;
    public static double VEROYATNOST_PRIHODA = 0.3;
    public static double VEROYATNOST_GRUPPY = 0.4;
    public static double KOEF_TSENY = 1.0;
    public static int DENGI_MIN = 50;
    public static int DENGI_MAX = 200;
    public static int DLYA_SIMULYATSII = 1000;
    public static int PAUZA_MS = 100;
    public static double RAZBROS_PREDPOCHTENIY = 2.0;

    // расходные материалы
    public static int PODNOSY = 20;
    public static int VILKI = 30;
    public static int LOZHKI = 30;
    public static int SALFETKI = 50;

    // приватные поля
    private static ArrayList vseLyudi = new ArrayList();
    private static ArrayList stoly = new ArrayList();
    private static ArrayList blyuda = new ArrayList();
    public static Random sluch = new Random();  // оставляем public для удобства (генератор)
    private static boolean rabotaet = true;
    private static int tekusheeVremya = 0;
    private static int obsluzheno = 0;
    private static int ushliBezDeneg = 0;
    private static int ushliBezBlyud = 0;
    private static int ushliBezMaterialov = 0;
    private static boolean pauza = false;

    public static Razdacha razdacha = new Razdacha();
    public static Kassa kassa = new Kassa();
    public static Okno okno = new Okno();
    public static Povar povar = new Povar();
    public static Posudomoy posudomoy = new Posudomoy();

    private static ArrayList ojidayushie = new ArrayList();

    // Геттеры
    public static int getTekusheeVremya() { return tekusheeVremya; }
    public static int getObsluzheno() { return obsluzheno; }
    public static int getUshliBezDeneg() { return ushliBezDeneg; }
    public static int getUshliBezBlyud() { return ushliBezBlyud; }
    public static int getUshliBezMaterialov() { return ushliBezMaterialov; }
    public static boolean isRunning() { return rabotaet; }
    public static boolean isPauza() { return pauza; }
    public static void setPauza(boolean p) { pauza = p; }
    public static void stopSimulation() { rabotaet = false; }

    // Методы для увеличения счётчиков
    public static void incrementObsluzheno() { obsluzheno++; }
    public static void incrementUshliBezDeneg() { ushliBezDeneg++; }
    public static void incrementUshliBezBlyud() { ushliBezBlyud++; }
    public static void incrementUshliBezMaterialov() { ushliBezMaterialov++; }

    public static synchronized ArrayList getVseLyudi() { return new ArrayList(vseLyudi); }
    public static synchronized ArrayList getStoly() { return new ArrayList(stoly); }
    public static synchronized ArrayList getBlyuda() { return new ArrayList(blyuda); }
    public static synchronized ArrayList getOcheredRazdacha() { return razdacha.getOchered(); }
    public static synchronized ArrayList getOcheredKassa() { return kassa.getOchered(); }
    public static synchronized int getDlinaOcherediRazdacha() { return razdacha.dlinaOcheredi(); }
    public static synchronized int getDlinaOcherediKassa() { return kassa.dlinaOcheredi(); }
    public static synchronized int getDlinaOcherediOkno() { return okno.dlinaOcheredi(); }

    public static Razdacha getRazdacha() { return razdacha; }
    public static Kassa getKassa() { return kassa; }
    public static Okno getOkno() { return okno; }

    public static int getPodnosy() { return PODNOSY; }
    public static int getVilki() { return VILKI; }
    public static int getLozhki() { return LOZHKI; }
    public static int getSalfetki() { return SALFETKI; }

    public static void useMaterialy() {
        PODNOSY--;
        VILKI--;
        LOZHKI--;
        SALFETKI--;
    }

    public static void addMaterials(int pod, int vil, int lozh, int salf) {
        PODNOSY += pod;
        VILKI += vil;
        LOZHKI += lozh;
        SALFETKI += salf;
    }

    public static void zagruzitConfig(String imyaFayla) {
        try {
            File f = new File(imyaFayla);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String stroka = sc.nextLine();
                if (stroka.startsWith("#") || stroka.trim().isEmpty()) continue;
                String[] chasti = stroka.split("=");
                if (chasti.length < 2) continue;
                String klyuch = chasti[0].trim();
                String znachenie = chasti[1].trim();
                if (klyuch.equals("TABLE_COUNT")) STOLOV = Integer.parseInt(znachenie);
                else if (klyuch.equals("TABLE_SEATS")) MESTA_ZA_STOLOM = Integer.parseInt(znachenie);
                else if (klyuch.equals("INITIAL_DISH_COUNT")) NACHAINOYE_KOLICHESTVO_BLYUD = Integer.parseInt(znachenie);
                else if (klyuch.equals("DISH_TYPES")) TIPOV_BLYUD = Integer.parseInt(znachenie);
                else if (klyuch.equals("PERSON_ARRIVAL_PROB")) VEROYATNOST_PRIHODA = Double.parseDouble(znachenie);
                else if (klyuch.equals("GROUP_PROB")) VEROYATNOST_GRUPPY = Double.parseDouble(znachenie);
                else if (klyuch.equals("FOOD_PRICE_FACTOR")) KOEF_TSENY = Double.parseDouble(znachenie);
                else if (klyuch.equals("MONEY_MIN")) DENGI_MIN = Integer.parseInt(znachenie);
                else if (klyuch.equals("MONEY_MAX")) DENGI_MAX = Integer.parseInt(znachenie);
                else if (klyuch.equals("SIMULATION_DURATION")) DLYA_SIMULYATSII = Integer.parseInt(znachenie);
                else if (klyuch.equals("TIME_STEP_MS")) PAUZA_MS = Integer.parseInt(znachenie);
                else if (klyuch.equals("DISH_PREFERENCE_RANGE")) RAZBROS_PREDPOCHTENIY = Double.parseDouble(znachenie);
                else if (klyuch.equals("PODNOSY")) PODNOSY = Integer.parseInt(znachenie);
                else if (klyuch.equals("VILKI")) VILKI = Integer.parseInt(znachenie);
                else if (klyuch.equals("LOZHKI")) LOZHKI = Integer.parseInt(znachenie);
                else if (klyuch.equals("SALFETKI")) SALFETKI = Integer.parseInt(znachenie);
            }
            sc.close();
            System.out.println("Конфигурация загружена из " + imyaFayla);
        } catch (FileNotFoundException e) {
            System.out.println("Файл конфигурации не найден, используются значения по умолчанию.");
        }
    }

    public static void init() {
        stoly.clear();
        for (int i = 0; i < STOLOV; i++) {
            Stol st = new Stol(i + 1, MESTA_ZA_STOLOM);
            stoly.add(st);
        }
        String[] imenaBlyud = {"Суп", "Гарнир", "Котлета", "Салат", "Компот", "Плов", "Омлет"};
        blyuda.clear();
        for (int i = 0; i < TIPOV_BLYUD; i++) {
            double predp = 0.5 + sluch.nextDouble() * RAZBROS_PREDPOCHTENIY;
            double cena = (sluch.nextInt(50) + 30) * KOEF_TSENY;
            Blyudo b = new Blyudo(imenaBlyud[i % imenaBlyud.length] + (i+1), NACHAINOYE_KOLICHESTVO_BLYUD, cena, predp);
            blyuda.add(b);
        }
        vseLyudi.clear();
        ojidayushie.clear();
        tekusheeVremya = 0;
        obsluzheno = 0;
        ushliBezDeneg = 0;
        ushliBezBlyud = 0;
        ushliBezMaterialov = 0;
        rabotaet = true;
        pauza = false;
        System.out.println("Симуляция инициализирована.");
    }

    public static void zapusk() {
        while (rabotaet && tekusheeVremya < DLYA_SIMULYATSII) {
            if (pauza) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                continue;
            }
            generirovatLyudey();
            for (int i = 0; i < ojidayushie.size(); i++) {
                Chelovek ch = (Chelovek) ojidayushie.get(i);
                if (ch.popytkaVoyti()) {
                    if (PODNOSY <= 0 || VILKI <= 0 || LOZHKI <= 0 || SALFETKI <= 0) {
                        incrementUshliBezMaterialov();
                        ojidayushie.remove(i);
                        i--;
                        continue;
                    }
                    useMaterialy();
                    vseLyudi.add(ch);
                    ojidayushie.remove(i);
                    i--;
                }
            }
            ArrayList kopiya = new ArrayList(vseLyudi);
            for (int i = 0; i < kopiya.size(); i++) {
                Chelovek ch = (Chelovek) kopiya.get(i);
                ch.obnovit();
            }
            povar.rabotat();
            posudomoy.rabotat();
            kassa.obsluzhit();
            try {
                Thread.sleep(PAUZA_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            tekusheeVremya++;
        }
        rabotaet = false;
        System.out.println("Симуляция завершена.");
    }

    private static void generirovatLyudey() {
        if (sluch.nextDouble() < VEROYATNOST_PRIHODA) {
            boolean gruppa = sluch.nextDouble() < VEROYATNOST_GRUPPY;
            if (gruppa) {
                int razmer = 2 + sluch.nextInt(3);
                ArrayList gruppaSpisok = new ArrayList();
                for (int i = 0; i < razmer; i++) {
                    int dengi = DENGI_MIN + sluch.nextInt(DENGI_MAX - DENGI_MIN + 1);
                    Chelovek ch = new Chelovek(dengi, true);
                    gruppaSpisok.add(ch);
                }
                for (int i = 0; i < gruppaSpisok.size(); i++) {
                    Chelovek ch = (Chelovek) gruppaSpisok.get(i);
                    ch.ustanovitGruppu(gruppaSpisok);
                    ojidayushie.add(ch);
                }
            } else {
                int dengi = DENGI_MIN + sluch.nextInt(DENGI_MAX - DENGI_MIN + 1);
                Chelovek ch = new Chelovek(dengi, false);
                ojidayushie.add(ch);
            }
        }
    }

    public static void chelovekUxodit(Chelovek ch) {
        vseLyudi.remove(ch);
        Stol st = ch.poluchitStol();
        if (st != null) st.osvoboditMesto(ch);
        okno.dobavitPodnos();
    }
}