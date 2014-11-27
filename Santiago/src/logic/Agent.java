package logic;

import java.util.Vector;
import static utils.Statics.*;
import utils.TilePlacement;

public class Agent {

    public static int generateLicitation(Player p, int logic, Vector<Player> vp) {
        int money = 0;

        switch (logic) {
            case LOGIC_RANDOM:
                money = RANDOM_GENERATOR.nextInt(p.getEscudos());
                break;
            case LOGIC_SPENDER:
                money = getSpenderLicitation(p, vp);
                break;
            case LOGIC_SAVER:
                money = getSaverLicitation(p, vp);
                break;
        }

        return money;
    }
    
    public static TilePlacement generatePlantChoice(Player p, int logic, Board b, Vector<Tile> tiles){
        TilePlacement tile = null;

        switch (logic) {
            case LOGIC_RANDOM:                   
                tile = randomTilePlacement(tiles, b);
                break;
            case LOGIC_SPENDER:
                break;
            case LOGIC_SAVER:
                break;
        }

        return tile;
    }

    private static int getSpenderLicitation(Player p, Vector<Player> vp) {
        int maxEscudos = getMaxEscudos(p, vp);

        if (maxEscudos < 0.8 * p.getEscudos()) {
            return maxEscudos + 1;
        }

        if (0.8 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.8 * maxEscudos);
        }

        if (0.6 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.6 * maxEscudos);
        }
        if (0.4 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.4 * maxEscudos);
        }
        if (0.2 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.2 * maxEscudos);
        }
        return 0;
    }

    private static int getSaverLicitation(Player p, Vector<Player> vp) {
        int maxEscudos = getMaxEscudos(p, vp);

        if(maxEscudos < 0.2 * p.getEscudos()){
            return maxEscudos;
        }
        
        return 0;
    }

    private static int getMaxEscudos(Player p, Vector<Player> vp) {
        int maxEscudos = 0;
        for (Player otherP : vp) {
            if (!otherP.getColor().equals(p.getColor())) {
                if (otherP.getEscudos() > maxEscudos) {
                    maxEscudos = otherP.getEscudos();
                }
            }
        }
        return maxEscudos;
    }

    private static TilePlacement randomTilePlacement(Vector<Tile> tiles, Board b) {
        int index = RANDOM_GENERATOR.nextInt(tiles.size());
        
        int x, y;
        do{
            x = RANDOM_GENERATOR.nextInt(8);
            y = RANDOM_GENERATOR.nextInt(6);
        }while(!b.getTiles()[y][x].can_plant());
        
        return new TilePlacement(tiles.get(index), x, y);
    }

}
