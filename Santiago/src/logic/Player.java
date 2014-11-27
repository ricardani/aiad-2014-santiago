/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.awt.Color;
import java.io.Serializable;
import static utils.Statics.*;

/**
 *
 * @author Utilizador
 */
public class Player implements Serializable{
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
}
