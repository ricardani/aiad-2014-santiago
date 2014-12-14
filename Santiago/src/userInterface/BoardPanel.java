package userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import logic.Player;
import logic.Tile;
import static utils.GuiUtils.GAME_PLAYERS;
import static utils.GuiUtils.ROUND_TILES;
import static utils.GuiUtils.getColorForTile;
import static utils.GuiUtils.getImgForChannel;
import static utils.GuiUtils.getImgForTile;

public class BoardPanel extends JPanel {

    public BoardPanel(String clicked) {
        super();
        this.setBackground(Color.GRAY);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //TILES
        int posX = 100, posY = 100;
        Graphics2D g2 = (Graphics2D) g;
        for(int y = 0; y < 6; y++){
            for(int x = 0; x < 8; x++){
                g.drawImage(getImgForTile(y,x), posX, posY, this);
                g2.setColor(getColorForTile(y,x));
                g2.setStroke(new BasicStroke(3));
                g2.draw(new Rectangle2D.Double(posX, posY, 70, 65));
                posX += 75;
                if ( (x % 2) == 1)
                    posX += 45;
            }
            posX = 100;
            posY += 70;
            
            if ( (y % 2) == 1)
                posY += 45;
        }
        
        //WATER
        posX = 100;
        posY = 65;
        
        for(int i = 0; i < 16; i++){
            if ( (i % 4) == 0 && i != 0){
                posX = 100;
                posY += 185;
            }
            g.drawImage(getImgForChannel(i), posX, posY, this);
            posX += 195;
        }
        
        posX = 65;
        posY = 100;
        
        for(int i = 16; i < 31; i++){
            g.drawImage(getImgForChannel(i), posX, posY, this);
            posY += 185;
            
            if ( (i % 3) == 0 && i != 16){
                posX += 195;
                posY = 100;
            }
        }
        
        //Rounf Tiles
        posX = 100;
        posY = 680;
        
        for (Tile t:  ROUND_TILES) {
            g.drawImage(getImgForTile(t), posX, posY, this);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Rectangle2D.Double(posX, posY, 70, 65));
            posX += 90;
        }
        
        //ALL PLAYER INFO
        posX = 900;
        posY = 100;
        char[] text;
        Font newFont = new Font("Impact", Font.PLAIN, 25);
        g.setFont(newFont);
        String info;
        
        for (Player p:  GAME_PLAYERS) {
            info = "Jogador: " + p.getName() + " Escudos: " + p.getEscudos();
            g.setColor(Color.BLACK);
            text = info.toCharArray();
            g.drawChars(text, 0, text.length, posX, posY);
            posY += 100;
        }
        
    }
}
