package santiago;

import java.util.Scanner;
import userInterface.Console;
import userInterface.Agents;
import userInterface.GUI;
import utils.GuiUtils;

public class Santiago {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int answer;
        
        while (true) {
            System.out.println("Opções de jogo : ");
            System.out.println("[0] Consola");
            System.out.println("[1] 3 Agentes");
            System.out.println("[2] 5 Agentes");
            System.out.println("[3] Test GUI");
            
            answer = sc.nextInt();
            
            if(answer >= 0 && answer <= 3)
                break;
            else
                System.err.println("Opção Inválida");
        }
        
        switch(answer){
            case 0:
                Console c = new Console();
                c.gameCycle();
                break;
            case 1:
                Agents a1 = new Agents(1);
                break;
            case 2:
                Agents a2 = new Agents(2);
                break;
            case 3:
                GuiUtils gUtils = new GuiUtils();
                gUtils.initVars();
                GUI.start(null);
                break;
        }
        
        
        
    }
    
}
