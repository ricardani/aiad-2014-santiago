package utils;

import java.awt.Color;
import java.util.Map;
import java.util.Vector;
import logic.Board;
import logic.Channel;
import logic.Player;
import logic.Tile;
import static utils.Statics.Colors;

public class ConsolePrints {
    
    public static void printBoard(Board b) {
        
        Tile[][] tiles = b.getTiles();
        Channel[] channels = b.getChannels();
        int waterAux = 0;
        
        System.out.println();
        
        printHorizontalWaterLine(channels, 0, 4);
        
        for (int y = 0; y < 6; ++y) {
            if (y == 2) {
                printHorizontalWaterLine(channels, 4, 8);
            } else if (y == 4) {
                printHorizontalWaterLine(channels, 8, 12);
            }
            
            if (y == 0 || y == 1) {
                waterAux = 16;
            } else if (y == 2 || y == 3) {
                waterAux = 17;
            } else if (y == 4 || y == 5) {
                waterAux = 18;
            }
            
            printVerticalWater(channels[waterAux]);
            
            for (int x = 0; x < 8; ++x) {
                printTile(tiles[y][x]);
                if (x != 0 && (x % 2) != 0) {
                    waterAux += 3;
                    printVerticalWater(channels[waterAux]);
                }
            }
            System.out.println();
        }
        
        printHorizontalWaterLine(channels, 12, 16);
        
        System.out.println();
        
    }
    
    public static void printTilesForRound(Vector<Tile> tiles) {
        
        System.out.println("Plantações para esta ronda :");
        
        for (int i = 0; i < tiles.size(); i++) {
            System.out.println("[" + i + "] -> " + tiles.get(i).toString());
        }
        System.out.println();
    }
    
    public static void printWaterPossibelPaths(Vector<Integer> waterPossibelPaths) {

        System.out.println();
        
        System.out.println("Canais de Água");

        for (int i = 0; i < waterPossibelPaths.size(); i++) {
            System.out.println("[" + i + "] -> Canal de Agua numero: " + waterPossibelPaths.get(i));
        }

        System.out.println();

    }
    
    public static void printPlayerInfo(Vector<Color> order, Map<String, Player> players) {

        System.out.println();
        
        Player p;

        for (int i = 0; i < order.size(); ++i) {
            p = players.get(order.get(i).toString());

            System.out.println("Jogador " + p.getName() + "\t Escudos: " + p.getEscudos());

        }

        System.out.println();

    }
    
    public static void printPlayerInfo(Vector<Color> order, Vector<Player> players) {

        System.out.println();

        for (int i = 0; i < order.size(); ++i) {
            for(Player p : players){
                if(p.toString().equals(order.get(i).toString())){
                    System.out.println("Jogador " + p.getName() + "\t Escudos: " + p.getEscudos());
                }
            }
        }

        System.out.println();

    }
    
    private static void printHorizontalWaterLine(Channel[] channels, int from, int to) {
        System.out.print("   ");
        for (int i = from; i < to; i++) {
            printHorizontalWater(channels[i]);
            printHorizontalWater(channels[i]);
            System.out.print("   ");
        }
        System.out.println();
    }
    
    private static void printHorizontalWater(Channel c) {
        if (c.hasWater()) {
            System.out.print(Colors.get(Color.BLUE.toString()) + "WWWWWWW");
        } else {
            System.out.print(Colors.get(Color.BLACK.toString()) + "WWWWWWW");
        }
        
    }
    
    private static void printVerticalWater(Channel c) {
        if (c.hasWater()) {
            System.out.print(Colors.get(Color.BLUE.toString()) + " W ");
        } else {
            System.out.print(Colors.get(Color.BLACK.toString()) + " W ");
        }
    }
    
    private static void printTile(Tile t) {
        if (t.getType().equals("Ground") || t.getType().equals("Desert")) {
            System.out.print(Colors.get(Color.BLACK.toString()) + "|  " + t.getType().charAt(0) + "  |");
        } else {
            System.out.print(Colors.get(t.getColor().toString()) + "| " + t.getType().charAt(0) + '/' + t.getWorkers() + " |");
        }
    }
    
}
