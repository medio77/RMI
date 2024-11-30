/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NEMAT
 */
public class Event extends Thread{

    @Override
    public void run() {
//            System.out.println("|  MENU SELECTION  |");
//            System.out.println("|  1. Change Port  |");
//            System.out.println("|  2. Inactivate   |");
            
            Scanner sc = new Scanner(System.in);
            
            Server srv = new Server();
            String input = sc.next();
            if(input.equals("1")){
                try {
                    
                    Server.getSocket().close();
                    Thread env = new Event();
                    System.out.print("NEW PORT:");
                    int NEW_PORT = sc.nextInt();
                    Thread.sleep(250000);
                    if(srv.ChangePort(Server.getIP(),Server.getPORT(),NEW_PORT)){
                        System.out.println("PORT Change Successful");
                        Server.setPORT(NEW_PORT); 
                    }
                    else{
                        System.out.println("PORT Not Changed!");
                    }
                    
                    
                    env.start();
                    srv.start();
                }
                catch (IOException | InterruptedException ex) {
                    System.out.println("Error!!!");
                    System.exit(0);
                }
            }
            else if(input.equals("2")){
                try {
                    Server.getSocket().close();
                    Thread env = new Event();
                    System.out.println("Enter active When You Want To Active Agin!");
                    Thread.sleep(250000);
                    srv.Inactivate();
                    input = sc.next();
                    if(input.equals("active")){
                            env.start();
                            srv.start();
                        }
                    else{
                        System.out.println("Wrong Input!!!");
                        System.exit(0);
                    }
                        
                } 
                catch (IOException ex) {} catch (InterruptedException ex) {
                    System.out.println("Error!!!");
                    System.exit(0);
                }
            }
            else{
                System.out.println("Wrong Input!!!");
                System.exit(0);
                }            
    }
}
