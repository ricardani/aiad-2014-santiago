package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import logic.Board;
import logic.Channel;
import logic.Player;
import logic.Tile;
import java.awt.Color;
import static utils.Statics.*;

public class GuiUtils {
    public static BufferedImage
            IMG_GROUND, IMG_DESERT,
            IMG_BANANA_1WORKER, IMG_BANANA_2WORKERS,
            IMG_CHILI_1WORKER, IMG_CHILI_2WORKERS,
            IMG_POTATO_1WORKER, IMG_POTATO_2WORKERS,
            IMG_BEAN_1WORKER, IMG_BEAN_2WORKERS,
            IMG_SUGARCANE_1WORKER, IMG_SUGARCANE_2WORKERS,
            IMG_NO_WATER_H, IMG_NO_WATER_V,
            IMG_WATER_H, IMG_WATER_V;
    
    public static Board GAME_BOARD;
    public static Vector<Player> GAME_PLAYERS = new Vector<>();
    public static Vector<Color> GAME_ORDER = new Vector<>();
    
    public static void GuiUtils(){}

    public void initVars(){
        try {
            IMG_GROUND = ImageIO.read(this.getClass().getResource("../img/Ground.png"));
            IMG_DESERT = ImageIO.read(this.getClass().getResource("../img/Desert.png"));
            
            IMG_BANANA_1WORKER = ImageIO.read(this.getClass().getResource("../img/Banana_1Worker.png"));
            IMG_BANANA_2WORKERS = ImageIO.read(this.getClass().getResource("../img/Banana_2Workers.png"));
            
            IMG_CHILI_1WORKER = ImageIO.read(this.getClass().getResource("../img/Chili_1Worker.png"));
            IMG_CHILI_2WORKERS = ImageIO.read(this.getClass().getResource("../img/Chili_2Workers.png"));
            
            IMG_POTATO_1WORKER = ImageIO.read(this.getClass().getResource("../img/Potato_1Worker.png"));
            IMG_POTATO_2WORKERS = ImageIO.read(this.getClass().getResource("../img/Potato_2Workers.png"));
            
            IMG_BEAN_1WORKER = ImageIO.read(this.getClass().getResource("../img/Bean_1Worker.png"));
            IMG_BEAN_2WORKERS = ImageIO.read(this.getClass().getResource("../img/Bean_2Workers.png"));
            
            IMG_SUGARCANE_1WORKER = ImageIO.read(this.getClass().getResource("../img/SugarCane_1Worker.png"));
            IMG_SUGARCANE_2WORKERS = ImageIO.read(this.getClass().getResource("../img/SugarCane_2Workers.png"));
            
            IMG_NO_WATER_H = ImageIO.read(this.getClass().getResource("../img/ChannelWithoutWater H.png"));
            IMG_NO_WATER_V = ImageIO.read(this.getClass().getResource("../img/ChannelWithoutWater V.png"));
            
            IMG_WATER_H = ImageIO.read(this.getClass().getResource("../img/ChannelWithWater H.png"));
            IMG_WATER_V = ImageIO.read(this.getClass().getResource("../img/ChannelWithWater V.png"));
        } catch (IOException ex) {
            Logger.getLogger(GuiUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GAME_BOARD = new Board();
    }
    
    public static BufferedImage getImgForTile(int y, int x){
        Tile t = GAME_BOARD.getTiles()[y][x];
        
        switch(t.getType()){
            case TYPE_BANANA:
                if(t.getWorkers() == 1)
                    return IMG_BANANA_1WORKER;
                else
                    return IMG_BANANA_2WORKERS;
            case TYPE_CHILLI:
                if(t.getWorkers() == 1)
                    return IMG_CHILI_1WORKER;
                else
                    return IMG_CHILI_2WORKERS;
            case TYPE_BEAN:
                if(t.getWorkers() == 1)
                    return IMG_BEAN_1WORKER;
                else
                    return IMG_BEAN_2WORKERS;
            case TYPE_POTATO:
                if(t.getWorkers() == 1)
                    return IMG_POTATO_1WORKER;
                else
                    return IMG_POTATO_2WORKERS;
            case TYPE_SUGARCANE:
                if(t.getWorkers() == 1)
                    return IMG_SUGARCANE_1WORKER;
                else
                    return IMG_SUGARCANE_2WORKERS;
            case TYPE_DESERT:
                return IMG_DESERT;
            default:
                return IMG_GROUND;
                
        }
        
    }
    
    public static Color getColorForTile(int y, int x){
        Tile t = GAME_BOARD.getTiles()[y][x];
        
        if(t.getColor() != null)
            return t.getColor();
        return Color.BLACK;
    }
    
    public static BufferedImage getImgForChannel(int n){
        Channel c = GAME_BOARD.getChannels()[n];
        
        if(n > 15){
            if(c.hasWater())
                return IMG_WATER_V;
            else
                return IMG_NO_WATER_V;
        }
        else{
            if(c.hasWater())
                return IMG_WATER_H;
            else
                return IMG_NO_WATER_H;
        }
        
        
    }
    
}
