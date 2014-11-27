package santiago;

import java.util.Scanner;
import userInterface.Console;
import userInterface.Agents;

public class Santiago {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int answer;
        
        while (true) {
            System.out.println("Opções de jogo : ");
            System.out.println("[0] Consola");
            System.out.println("[1] Agentes");
            
            answer = sc.nextInt();
            
            if(answer >= 0 && answer <= 1)
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
                Agents a = new Agents();
                break;
        }
        
        
        
    }
    
}
