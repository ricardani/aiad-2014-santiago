package userInterface;

import java.awt.Color;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import logic.Game;
import logic.Player;
import logic.Tile;
import utils.Pair;
import static utils.Statics.sort;
import static utils.ConsolePrints.*;

public class Console {

    private final Scanner sc = new Scanner(System.in);
    private final Game g;

    public Console() {
        g = new Game();
    }

    private void bidsTiles() {

        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Integer> licitations = new Vector<>();

        int lastIndex = order.size(), firstIndex = 0;
        Pair<Color, Integer>[] newOrder = new Pair[lastIndex];
        Player p;

        for (int i = 0; i < lastIndex; ++i) {
            newOrder[i] = new Pair();
        }

        for (int i = 0; i < order.size(); ++i) {
            p = players.get(order.get(i).toString());
            int escudos;
            while (true) {
                System.out.println("Jogador " + p.getName() + ". Qual é a sua oferta? (Esudos atuais: " + p.getEscudos() + ")");
                escudos = sc.nextInt();

                if (p.Pay(escudos)) {
                    break;
                }

                System.err.println("Valor Inválido");
            }
            if (escudos == 0) {
                newOrder[--lastIndex].setFirst(p.getColor());
                newOrder[lastIndex].setSecond(0);
            } else {
                newOrder[firstIndex].setFirst(p.getColor());
                newOrder[firstIndex++].setSecond(escudos);
            }
        }

        sort(newOrder);

        for (int i = 0; i < order.size(); ++i) {
            order.set(i, newOrder[i].getFirst());
            licitations.add(newOrder[i].getSecond());
        }

        g.setOrder(order);
        g.setPlayers(players);
        g.setLicitationForRound(licitations);

        System.out.println();

    }

    private void bidsWaterChannels() {

        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Pair<Integer, Integer>> licitations = new Vector<>();
        Vector<Integer> waterPossibelPaths = g.getWaterPossiblePaths();

        Player p;
        int escudos = 0, channel;

        for (int i = 0; i < order.size() - 1; ++i) {
            p = players.get(order.get(i).toString());

            while (true) {
                System.out.println("Jogador " + p.getName());
                System.out.println("Escolha o canal [0-" + (waterPossibelPaths.size() - 1) + "]");
                channel = sc.nextInt();

                if (channel >= 0 && channel < waterPossibelPaths.size()) {
                    while (true) {
                        System.out.println("Qual é a sua oferta? (Esudos atuais: " + p.getEscudos() + ")");
                        escudos = sc.nextInt();
                        if (escudos >= 0 && escudos <= p.getEscudos()) {
                            break;
                        } else {
                            System.err.println("Valor Inválido");
                        }
                    }

                    licitations.add(new Pair(channel, escudos));
                    break;

                } else {
                    System.err.println("Valor Inválido");
                }

            }
        }

        g.setWaterLicitationForRound(licitations);
        System.out.println();

    }

    private void showLicitations() {
        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Integer> licitations = g.getLicitationForRound();

        Player p;

        System.out.println("Licitações");

        for (int i = 0; i < order.size(); ++i) {
            p = players.get(order.get(i).toString());
            System.out.println("Joagdor " + p.getName() + "\tLicitação: " + licitations.get(i));
        }
        System.out.println();
    }

    private void showWaterLicitations() {
        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Pair<Integer, Integer>> licitations = g.getWaterLicitationForRound();

        Player p;
        printWaterPossibelPaths(g.getWaterPossiblePaths());

        System.out.println("Licitações");

        for (int i = 0; i < order.size() - 1; ++i) {
            p = players.get(order.get(i).toString());
            System.out.println("Joagdor " + p.getName() + "\tLicitação: " + licitations.get(i));
        }

        System.out.println();

    }

    private void chooseAndPlaceTiles() {
        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Tile> tiles = g.getTilesForRound();
        Player p;

        int tileNumber, x, y;

        for (int i = 0; i < order.size(); ++i) {
            tiles = plantTiles(players, order, i, tiles);
        }

        plantTiles(players, order, 0, tiles);

        System.out.println();
    }

    private void chooseWaterChannel() {
        Vector<Color> order = g.getOrder();
        Map<String, Player> players = g.getPlayers();
        Vector<Pair<Integer, Integer>> licitations = g.getWaterLicitationForRound();
        Vector<Integer> waterPossibelPaths = g.getWaterPossiblePaths();

        Player p;

        int channel, money;

        System.out.println("Canais de Água");

        for (int i = 0; i < waterPossibelPaths.size(); i++) {
            money = 0;
            System.out.print("[" + i + "] -> Canal de Agua numero: " + waterPossibelPaths.get(i));
            for (Pair<Integer, Integer> licitation : licitations) {
                if (licitation.getFirst() == i) {
                    money += licitation.getSecond();
                }
            }
            System.out.println(" Recebe: " + money + " escudos.");
        }

        System.out.println();

        while (true) {
            System.out.println("Escolha o canal. [0-" + (waterPossibelPaths.size() - 1) + "]");
            channel = sc.nextInt();

            if (channel >= 0 && channel < waterPossibelPaths.size()) {

                g.placeWaterChannel(channel);
                break;
            } else {
                System.err.println("Resposta Invalida!");
            }

        }

        System.out.println();

    }

    private Vector<Tile> plantTiles(Map<String, Player> players, Vector<Color> order, int i, Vector<Tile> tiles) {
        Player p;
        int tileNumber;
        int x;
        int y;
        p = players.get(order.get(i).toString());
        System.out.println();
        printBoard(g.getBoard());
        System.out.println();
        printTilesForRound(g.getTilesForRound());
        System.out.println();
        while (true) {
            System.out.println("Jogador " + p.getName() + "\tEscolha uma plantação." + " [0-" + (tiles.size() - 1) + "]");
            tileNumber = sc.nextInt();

            if (tileNumber >= 0 && tileNumber < tiles.size()) {
                while (true) {
                    System.out.println("Escolha coordenada X: [0-7]");
                    x = sc.nextInt();
                    System.out.println("Escolha coordenada Y: [0-5]");
                    y = sc.nextInt();
                    if (x >= 0 && x <= 7 && y >= 0 && y <= 5) {
                        break;
                    } else {
                        System.out.println("Coordenadas Inválidas!");
                    }
                }

                if (g.plant(tiles.get(tileNumber), order.get(i), x, y)) {
                    System.out.println("A plantação foi um sucesso!");
                    tiles.remove(tileNumber);
                    break;
                } else {
                    System.out.println("Impossivel plantar!");
                }

            } else {
                System.out.println("Plantação inixistente!");
            }
        }

        System.out.println();

        return tiles;
    }

    

    //Funções para o ciclo de jogo
    

    

    private void newRound() {
        g.newTilesForRound();
        printTilesForRound(g.getTilesForRound());

    }

    private void init() {
        int numPlayers;

        do {
            System.out.println("Quantos jogadores? (3 a 5)");
            numPlayers = sc.nextInt();
        } while (!g.setNumberOfPlayers(numPlayers));

    }

    private void plantations() {
        bidsTiles();
        showLicitations();
        chooseAndPlaceTiles();
    }

    private void water() {
        printWaterPossibelPaths(g.getWaterPossiblePaths());
        bidsWaterChannels();
        showWaterLicitations();
        chooseWaterChannel();
    }

    private void verifications() {
        g.checkTilesIrrigation();
    }

    private boolean endGame() {
        return g.finish();
    }

    private void finalResults() {
        g.endGame();
    }

    public void gameCycle() {

        init();

        while (!endGame()) {
            printBoard(g.getBoard());
            printPlayerInfo(g.getOrder(), g.getPlayers());

            newRound();

            plantations();

            printBoard(g.getBoard());
            printPlayerInfo(g.getOrder(), g.getPlayers());
            water();

            verifications();
        }
        finalResults();
        printBoard(g.getBoard());
        printPlayerInfo(g.getOrder(), g.getPlayers());

    }
}
