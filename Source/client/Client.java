// package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws FileNotFoundException, IOException {

        ClientStub st = new ClientStub();
        Scanner sc = new Scanner(System.in);
        System.out.println("|  MENU SELECTION   |");
        System.out.println("|  1. sum           |");
        System.out.println("|  2. getPrice      |");
        System.out.println("|  3. CheckStatus |");
        System.out.println("|  4. getOrder      |");

        List<String> lst = Arrays.asList("Famli", "Fulad", "Khodro", "Mellat", "Shapna", "Fars", "Barekat", "Shasta", "Zagros", "Damavand");
        System.out.println("Available Symbols: Famli, Fulad, Khodro, Mellat, Shapna, Fars, Barekat, Shasta, Zagros, Damavand");
        while(true){
            try{
                String cmd = sc.next();
                switch (cmd) {
                    case "1":
                        System.out.println("SUM Has 3 Argument");
                        System.out.print("A = ");
                        int a = sc.nextInt();
                        System.out.print("B = ");
                        int b = sc.nextInt();
                        System.out.print("C = ");
                        int c = sc.nextInt();
                        int sum = st.sum(a, b, c);
                        System.out.println("SUM = "+sum);
                        break;
                    case "2":
                        {
                            System.out.println("getPrice Argument Is Symbol Name");
                            System.out.print("Symbol Name = ");
                            String symbol = sc.next();
                            if(lst.contains(symbol)){
                                int price = st.getPrice(symbol);
                                System.out.println("PRICE = "+price);
                            }       break;
                        }
                    case "3":

                        {
                            System.out.println("CheckStatus Arguments Is List Of VMs Name With , Seprated Like: agent-0,firewall-1");
                            System.out.print("VM Names: ");
                            String symbols = sc.next();
                            String[] list = symbols.split(",");
                            List<String> symbols_list = Arrays.asList(list);
                            List<Boolean> result = st.CheckStatus(symbols_list);
                            System.out.println("Status = "+result);
                            break;
                        }
                    case "4":
                        {
                            System.out.println("Available Symbols For This : Famli, Mellat, Shasta, Zagros, Damavand");
                            System.out.println("getOrder Arguments Is Symbol Name And Integer For How Many Order Do You Want Up to 5");
                            System.out.print("Symbol Name: ");
                            String symbol = sc.next();
                            System.out.print("How Many Days: ");
                            int number = sc.nextInt();
                            List<List<String>> result = st.getOrder(symbol, number);
                            System.out.println("Orders = "+result);
                            break;
                        }
                    default:
                        System.out.println("Wrong input!!");
                        break;
                }
            }
            catch(Exception ex){
                System.out.println("Error");
                System.exit(0);
            }
        }
    }
}
