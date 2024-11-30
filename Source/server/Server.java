package server;

import com.opencsv.CSVReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Server implements interfaces.Stock{

    private static ServerSocket srvskt;
    private static String IP;
    private static int PORT;

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Server.IP = IP;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Server.PORT = PORT;
    }


    public static ServerSocket getSocket() {
        return srvskt;
    }

    public void setSocket(Socket socket) {
        this.srvskt = srvskt;
    }
    private String input = "n";

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public int getPrice(String symbol){
        try {
            CSVReader reader = new CSVReader(new FileReader("PriceFile.csv"));
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if(row[0].equals(symbol))
                    return Integer.valueOf(row[1]);
            }
            return -1;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }


    @Override
    public List<Boolean> CheckStatus(List VMs){

        List<Boolean> myList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader("Status.csv"));
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if(VMs.contains(row[0])){
                    if(row[1].equals("Active")){
                       myList.add(Boolean.TRUE);
                    }
                    else{
                        myList.add(Boolean.FALSE);
                    }
                }
            }
            return myList;
        } catch (IOException ex) {}
        return myList;
    }

    @Override
    public List<List<String>> getOrder(String symbol,int count){

        List<List<String>> listOfList = new ArrayList<>();
        List<String> lst = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader("Order.csv"));
            List<String[]> allData = reader.readAll();
            for (String[] row : allData) {
                if(symbol.contains(row[0]) && listOfList.size() < count){
                    for(int j=0;j<row.length;j++){
                        lst.add(row[j]);
                    }
                    listOfList.add(new ArrayList<String>(lst));
                    lst.clear();
                }
            }
            return listOfList;
        } catch (IOException ex) {}
        return listOfList;
    }

    @Override
    public int sum(int a, int b,int c) {
        return a + b + c;
    }

    public boolean Register(String s_IP,int s_PORT){

        String binder = read_AgentINFO();
        String Binder_IP = binder.split(":")[0];
        int Binder_PORT = Integer.valueOf(binder.split(":")[1]);

        int cnt = 0;
        while(cnt <= 5){
            try{
                Socket s = new Socket(Binder_IP, Binder_PORT, InetAddress.getByName(s_IP), s_PORT);
                Thread.sleep(1000);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                JSONObject json = new JSONObject();
                List<String> serv = new ArrayList<>();

                json.put("Message Type", "Register");
                serv.add("Stock");
                json.put("services", serv);
                dos.writeUTF(json.toString());
                String answer = dis.readUTF();
                s.close();

            return answer.equals("Register Successful");

            }
            catch(IOException | SecurityException | JSONException | InterruptedException ex){
                cnt +=1;
            }
        }
        return false;
    }

    public boolean Inactivate(){
        String binder = read_AgentINFO();
        String Binder_IP = binder.split(":")[0];
        int Binder_PORT = Integer.valueOf(binder.split(":")[1]);

        try{
            Socket s = new Socket(Binder_IP, Binder_PORT, InetAddress.getByName(getIP()), getPORT());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            JSONObject json = new JSONObject();
            List<String> serv = new ArrayList<>();

            json.put("Message Type", "Inactivate");
            dos.writeUTF(json.toString());
            String answer = dis.readUTF();
            s.close();

            return answer.equals("Inactive Successful");

        }
        catch(IOException | SecurityException | JSONException ex){}
    return false;
    }

    public boolean ChangePort(String IP,int PORT,int NEW_PORT){
        String binder = read_AgentINFO();
        String Binder_IP = binder.split(":")[0];
        int Binder_PORT = Integer.valueOf(binder.split(":")[1]);
        int cnt = 0;
        try{

            Socket s = new Socket(Binder_IP, Binder_PORT, InetAddress.getByName(getIP()), getPORT());

            Thread.sleep(1000);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            JSONObject json = new JSONObject();
            List<String> serv = new ArrayList<>();

            json.put("Message Type", "Change Port");
            json.put("NEW PORT", NEW_PORT);
            json.put("Service", "Stock");

            dos.writeUTF(json.toString());
            String answer = dis.readUTF();
            s.close();

        return answer.equals("Change Successful");
        }
        catch(IOException | InterruptedException | JSONException ex){}

    return false;
    }

    public void start() throws IOException{

        try{
            srvskt = new ServerSocket();
            srvskt.bind(new InetSocketAddress(getIP(),getPORT()));
            while(true){

                Socket socket = srvskt.accept();
                Thread st = new ServerStub(socket);
                try {
                    st.start();
                } catch (Exception ex) {}
            }
        }
        catch(Exception ex){}

    }

    public String read_AgentINFO(){
        File myObj = new File("directoryServer.txt");
        Scanner myReader;
        String Binder_IP = null;
        String Binder_PORT = null;
        try {
            myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                Binder_IP = myReader.nextLine();
                Binder_PORT = myReader.nextLine();
            }
        }
        catch (FileNotFoundException ex) {}
        return Binder_IP +":"+ Binder_PORT;
    }

    public static void main(String[] args) throws JSONException, IOException, InterruptedException{
        File myObj = new File("directoryServer.txt");
        Scanner myReader = new Scanner(myObj);
        Scanner sc = new Scanner(System.in);
        String Binder_IP = null;
        int Binder_PORT = 0;

        while (myReader.hasNextLine()) {
            Binder_IP = myReader.nextLine();
            Binder_PORT = Integer.valueOf(myReader.nextLine());
        }

        System.out.println("SET SERVER IP AND PORT");

        System.out.print("IP = ");
        String Server_IP = sc.next();
        setIP(Server_IP);
        System.out.print("PORT = ");
        int Server_PORT = sc.nextInt();
        setPORT(Server_PORT);

        Server serv = new Server();

        System.out.println("Try To Register!");
        boolean register = serv.Register(Server_IP, Server_PORT);
        if (register){
            System.out.println("Register Successful");
            System.out.println("");
            System.out.println("|  MENU SELECTION  |");
            System.out.println("|  1. Change Port  |");
            System.out.println("|  2. Inactivate   |");
            Thread cls = new Event();
            cls.start();
            serv.start();

        }
        else
            System.out.println("Register Unsuccessful Try Again!");
    }
}