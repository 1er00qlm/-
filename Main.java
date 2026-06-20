import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;

public class Main extends Application {
    private Pane panelStolovoy;
    private TextArea textStatistika;
    private AnimationTimer timer;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Столовая ");

        Button btnStart = new Button("Старт");
        Button btnPause = new Button("Пауза");
        Button btnStop = new Button("Стоп");
        Button btnStat = new Button("Статистика");
        HBox panelKnopok = new HBox(10, btnStart, btnPause, btnStop, btnStat);
        panelKnopok.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");

        panelStolovoy = new Pane();
        panelStolovoy.setPrefSize(800, 600);
        panelStolovoy.setStyle("-fx-border-color: black; -fx-background-color: white;");

        textStatistika = new TextArea();
        textStatistika.setEditable(false);
        textStatistika.setPrefWidth(300);
        textStatistika.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");

        BorderPane root = new BorderPane();
        root.setTop(panelKnopok);
        root.setCenter(panelStolovoy);
        root.setRight(textStatistika);

        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnStart.setOnAction(new StartHandler());
        btnPause.setOnAction(new PauseHandler());
        btnStop.setOnAction(new StopHandler());
        btnStat.setOnAction(new StatHandler());

        timer = new TimerUpdater();
        timer.start();

        primaryStage.setOnCloseRequest(e -> {
            Stolovaya.stopSimulation();
            Platform.exit();
        });

        Thread simThread = new Thread(new SimulationRunner());
        simThread.setDaemon(true);
        simThread.start();
    }

    private void obnovitShemu() {
        panelStolovoy.getChildren().clear();

        int w = (int) panelStolovoy.getWidth();
        int y = 30;

        // Раздача
        Rectangle razdachaRect = new Rectangle(20, y, 180, 80);
        razdachaRect.setFill(Color.LIGHTGREEN);
        razdachaRect.setStroke(Color.BLACK);
        Text razdachaText = new Text(70, y + 20, "РАЗДАЧА");
        Text ocheredRazd = new Text(30, y + 50, "Очередь: " + Stolovaya.getDlinaOcherediRazdacha());
        panelStolovoy.getChildren().addAll(razdachaRect, razdachaText, ocheredRazd);
        ArrayList ocheredRazdSpisok = Stolovaya.getOcheredRazdacha();
        for (int i = 0; i < ocheredRazdSpisok.size() && i < 5; i++) {
            Circle c = new Circle(40 + i * 15, y + 70, 5);
            c.setFill(Color.BLUE);
            panelStolovoy.getChildren().add(c);
        }

        // Касса
        Rectangle kassaRect = new Rectangle(220, y, 180, 80);
        kassaRect.setFill(Color.LIGHTCORAL);
        kassaRect.setStroke(Color.BLACK);
        Text kassaText = new Text(280, y + 20, "КАССА");
        Text ocheredKass = new Text(240, y + 50, "Очередь: " + Stolovaya.getDlinaOcherediKassa());
        panelStolovoy.getChildren().addAll(kassaRect, kassaText, ocheredKass);
        ArrayList ocheredKassSpisok = Stolovaya.getOcheredKassa();
        for (int i = 0; i < ocheredKassSpisok.size() && i < 5; i++) {
            Circle c = new Circle(240 + i * 15, y + 70, 5);
            c.setFill(Color.BLUE);
            panelStolovoy.getChildren().add(c);
        }

        // Окно грязной посуды
        Rectangle oknoRect = new Rectangle(420, y, 180, 80);
        oknoRect.setFill(Color.LIGHTBLUE);
        oknoRect.setStroke(Color.BLACK);
        Text oknoText = new Text(470, y + 20, "ПОСУДА");
        Text ocheredOkn = new Text(440, y + 50, "Очередь: " + Stolovaya.getDlinaOcherediOkno());
        panelStolovoy.getChildren().addAll(oknoRect, oknoText, ocheredOkn);

        // Столы
        ArrayList stoly = Stolovaya.getStoly();
        int stolY = y + 120;
        int startX = 40;
        int stolW = 100;
        int stolH = 80;
        int margin = 20;
        int cols = 4;
        for (int i = 0; i < stoly.size(); i++) {
            Stol s = (Stol) stoly.get(i);
            int col = i % cols;
            int row = i / cols;
            int x = startX + col * (stolW + margin);
            int yStol = stolY + row * (stolH + margin);
            int free = s.svobodnyeMesta();
            int max = s.maxMest();
            Color color;
            if (free == max) {
                color = Color.LIGHTGREEN;
            } else if (free == 0) {
                color = Color.LIGHTCORAL;
            } else {
                color = Color.LIGHTYELLOW;
            }
            Rectangle rect = new Rectangle(x, yStol, stolW, stolH);
            rect.setFill(color);
            rect.setStroke(Color.BLACK);
            Text numText = new Text(x + 20, yStol + 25, "Стол " + s.getNomer());
            Text freeText = new Text(x + 20, yStol + 55, "Своб: " + free + "/" + max);
            panelStolovoy.getChildren().addAll(rect, numText, freeText);
            ArrayList sidyashie = s.getSidyashie();
            for (int j = 0; j < sidyashie.size(); j++) {
                Circle c = new Circle(x + 15 + (j % 2) * 20, yStol + 40 + (j / 2) * 20, 6);
                c.setFill(Color.DARKBLUE);
                panelStolovoy.getChildren().add(c);
            }
        }

        // Блюда
        ArrayList blyuda = Stolovaya.getBlyuda();
        int bx = w - 150;
        Text blyudaTitle = new Text(bx, 30, "БЛЮДА:");
        panelStolovoy.getChildren().add(blyudaTitle);
        for (int i = 0; i < blyuda.size(); i++) {
            Blyudo b = (Blyudo) blyuda.get(i);
            Text bText = new Text(bx, 50 + i * 20, b.imya() + ": " + b.kolichestvo());
            panelStolovoy.getChildren().add(bText);
        }

        // Расходные материалы
        Text rashodText = new Text(bx, 50 + blyuda.size() * 20 + 10, "Подносы: " + Stolovaya.getPodnosy() +
                "\n Вилки: " + Stolovaya.getVilki() + "\n Ложки: " + Stolovaya.getLozhki() + "\n Салфетки: " + Stolovaya.getSalfetki());
        panelStolovoy.getChildren().add(rashodText);
    }

    private void obnovitStatistiku() {
        StringBuilder sb = new StringBuilder();
        sb.append(" СТАТИСТИКА \n");
        sb.append("Время: ").append(Stolovaya.getTekusheeVremya()).append(" мин\n");
        sb.append("Обслужено: ").append(Stolovaya.getObsluzheno()).append("\n");
        sb.append("Ушли без денег: ").append(Stolovaya.getUshliBezDeneg()).append("\n");
        sb.append("Ушли без блюд: ").append(Stolovaya.getUshliBezBlyud()).append("\n");
        sb.append("Ушли из-за нехватки расходников: ").append(Stolovaya.getUshliBezMaterialov()).append("\n");
        sb.append("\n Очереди \n");
        sb.append("Раздача: ").append(Stolovaya.getDlinaOcherediRazdacha()).append("\n");
        sb.append("Касса: ").append(Stolovaya.getDlinaOcherediKassa()).append("\n");
        sb.append("Окно посуды: ").append(Stolovaya.getDlinaOcherediOkno()).append("\n");
        sb.append("\n Столы \n");
        ArrayList stoly = Stolovaya.getStoly();
        for (int i = 0; i < stoly.size(); i++) {
            Stol s = (Stol) stoly.get(i);
            sb.append("Стол ").append(s.getNomer()).append(": свободно ")
                    .append(s.svobodnyeMesta()).append("/").append(s.maxMest()).append("\n");
        }

        Platform.runLater(() -> textStatistika.setText(sb.toString()));
    }

    private void pokazatStatistiku() {
        obnovitStatistiku();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class TimerUpdater extends AnimationTimer {
        public void handle(long now) {
            obnovitShemu();
        }
    }

    class StartHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Stolovaya.setPauza(false);
            if (!Stolovaya.isRunning()) {
                new Thread(new SimulationRunner()).start();
            }
        }
    }

    class PauseHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Stolovaya.setPauza(true);
        }
    }

    class StopHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Stolovaya.stopSimulation();
        }
    }

    class StatHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            pokazatStatistiku();
        }
    }

    class SimulationRunner implements Runnable {
        public void run() {
            Stolovaya.zagruzitConfig("config.txt");
            Stolovaya.init();
            Stolovaya.zapusk();
        }
    }
}