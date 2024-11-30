package directoryservice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class BindingService {
    private ServerSocket servskt;
    public static ConcurrentHashMap<String, String> services = new ConcurrentHashMap<>();
    private String IP;
    private int PORT;
    
    public void start(String IP, int PORT) throws JSONException, IOException {
        servskt = new ServerSocket();
        servskt.bind(new InetSocketAddress(IP, PORT));
                
        JSONObject item;

        while (true) {
                Socket socket = servskt.accept();
                
                Thread t = new Handler(socket);
                t.start();
        }
    }
   

    public static void main(String[] args) throws IOException, JSONException {
        System.out.println("SET BIND AGENT IP AND PORT");
        Scanner sc = new Scanner(System.in);
        BindingService agent = new BindingService();
        
        System.out.print("IP = ");
        String IP = sc.next();
        System.out.print("PORT = ");
        int PORT = sc.nextInt();
        System.out.println("Listening...");
        agent.start(IP, PORT);
    }
}
