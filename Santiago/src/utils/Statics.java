package utils;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import static utils.Statics.Colors;

public class Statics {
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    
    public static Map<String, String> Colors = new HashMap<>();
    public static Map<String, String> ColorName = new HashMap<>();
    public static Vector<Color> PlayerColor = new Vector<>();
    
    public static final Random RANDOM_GENERATOR = new Random();
    
    public static void fillStaticVars(){
        Colors.put(Color.BLACK.toString(), ANSI_BLACK);
        Colors.put(Color.RED.toString(), ANSI_RED);
        Colors.put(Color.GREEN.toString(), ANSI_GREEN);
        Colors.put(Color.YELLOW.toString(), ANSI_YELLOW);
        Colors.put(Color.BLUE.toString(), ANSI_BLUE);
        Colors.put(Color.MAGENTA.toString(), ANSI_PURPLE);
        
        ColorName.put(Color.BLACK.toString(), "Black");
        ColorName.put(Color.RED.toString(), "Red");
        ColorName.put(Color.GREEN.toString(), "Green");
        ColorName.put(Color.YELLOW.toString(), "Yellow");
        ColorName.put(Color.BLUE.toString(), "Blue");
        ColorName.put(Color.MAGENTA.toString(), "Purple");
        
        PlayerColor.add(Color.BLUE);
        PlayerColor.add(Color.RED);
        PlayerColor.add(Color.GREEN);
        PlayerColor.add(Color.YELLOW);
        PlayerColor.add(Color.MAGENTA);
    }
    
    public static void sort(Pair<Color, Integer>[] newOrder) {
        boolean swapped = true;
        int j = 0;
        Pair<Color, Integer> tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < newOrder.length - j; i++) {
                if (newOrder[i].getSecond() < newOrder[i + 1].getSecond()) {
                    tmp = newOrder[i];
                    newOrder[i] = newOrder[i + 1];
                    newOrder[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
    }
    
    public static final int MONEY_BONUS = 3;
    
    public static final String TYPE_DESERT = "Desert";
    public static final String TYPE_GROUND = "Ground";
    public static final String TYPE_BANANA = "Banana";
    public static final String TYPE_CHILLI = "Chilli";
    public static final String TYPE_BEAN = "Feijão";
    public static final String TYPE_POTATO = "Potato";
    public static final String TYPE_SUGARCANE = "Sugar";
    
    public static final String VERTICAL = "Vertical";
    public static final String HORIZONTAL = "Horizontal";
    
    
    //Static Vars For Agents
    public static final int TIME_TO_WAIT = 0;
    
    public static final String ENTER_GAME = "Enter Game";
    public static final String EXIT_GAME = "Exit Game";
    
    public static final String COLOR_BLUE = "Color Blue";
    public static final String COLOR_RED = "Color Red";
    public static final String COLOR_GREEN = "Color Green";
    public static final String COLOR_YELLOW = "Color Yellow";
    public static final String COLOR_PURPLE = "Color Purple";
    
    public static final int GAME_START = 0;
    public static final int GAME_TILES_BIDDING = 1;
    public static final int GAME_TILES_PLACEMENT = 2;
    public static final int GAME_WATER_BIDDING = 3;
    public static final int GAME_WATER_PLACEMENT = 4;
    public static final int GAME_END = 5;
    
    public static final int NO_INTERFACE = 0;
    public static final int CONSOLE_INTERFACE = 1;
    public static final int GRAPHIC_INTERFACE = 2;
    
    public static final int LOGIC_RANDOM = 0;
    public static final int LOGIC_SPENDER = 1;
    public static final int LOGIC_SAVER = 2;
    public static final int LOGIC_MIXED = 3;
    
    public static final String BOARD = "Board";
    public static final String TILES = "Tiles";
    public static final String CHANNELS = "Channels";
    public static final String WATER_LICITATIONS = "Water Licitations";
    public static final String PLAYERS = "Players";
    
    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;
    public static final int PLAYER_3 = 2;
    public static final int PLAYER_4 = 3;
    public static final int PLAYER_5 = 4;
    
    public static final String REQUEST_TILE_LICITATION = "Request Tile Licitation";
    public static final String CONFIRM_TILE_LICITAION = "Confirm Tile Licitation";
    public static final String REFUSE_TILE_LICITATION = "Refuse Tile Licitation";
    
    public static final String REQUEST_WATER_LICITATION = "Request Water Licitation";
    public static final String CONFIRM_WATER_LICITAION = "Confirm Water Licitation";
    public static final String REFUSE_WATER_LICITATION = "Refuse Water Licitation";
    
    public static final String REQUEST_TILE_PLACEMENT = "Request Tile Placement";
    public static final String CONFIRM_TILE_PLACEMENT = "Confirm Tile Placement";
    public static final String REFUSE_TILE_PLACEMENT = "Refuse Tile Placement";
    
    public static final String REQUEST_WATER_PLACEMENT = "Request Water Placement";
    public static final String CONFIRM_WATER_PLACEMENT = "Confirm Water Placement";
    public static final String REFUSE_WATER_PLACEMENT = "Refuse Water Placement";
    
    public static final int LICITATION_TILES = 0;
    public static final int LICITATION_WATER = 1;
    
    //Write to File
    public static void writeToFile(String content){
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
            out.println(content);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
