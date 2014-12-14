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
            System.out.println("[1] 3 Agentes Mix");
            System.out.println("[2] 5 Agentes Mix");
            System.out.println("[3] 3 Agentes Random");
            System.out.println("[4] 3 Agentes Spender");
            System.out.println("[5] 3 Agentes Saver");
            System.out.println("[6] 5 Agentes Random");
            System.out.println("[7] 5 Agentes Spender");
            System.out.println("[8] 5 Agentes Saver");
            System.out.println("[9] 4 Agentes Mix");
            System.out.println("[10] Test GUI");
            
            answer = sc.nextInt();
            
            if(answer >= 0 && answer <= 10)
                break;
            else
                System.err.println("Opção Inválida");
        }
        
        if(answer == 0){
            Console c = new Console();
            c.gameCycle();
        }else if(answer > 0 && answer < 10){
            Agents a = new Agents(answer);
        }else{
            GuiUtils gUtils = new GuiUtils();
            gUtils.initVars();
            GUI.start(null);
        }              
    }
}
