package logic;

import java.awt.Color;
import java.io.Serializable;
import static utils.Statics.*;

public class Player implements Serializable, Comparable<Player>{
    private final Color color;
    private int escudos;
    private final String Name;
    
    public Player(Color c){
        color = c;
        escudos = 10;
        Name = ColorName.get(c.toString());
    }   
    
    @Override
    public String toString()
    {
        return color.toString();
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return Name;
    }

    public int getEscudos() {
        return escudos;}

    public boolean Pay(int e) {
        if(escudos >= e){
            escudos -= e;
            return true;
        }
        return false;
    }
    
    public void receiveMoney(int e){
        escudos += e;
    }

    @Override
    public int compareTo(Player p) {
        return ((Integer)this.escudos).compareTo(p.getEscudos());
    }
}
