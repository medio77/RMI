
package directoryservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Handler extends Thread {
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s;
     
    // Constructor 
    public Handler(Socket s) throws IOException 
    { 
        this.s = s; 
        this.dis = new DataInputStream(this.s.getInputStream());
        this.dos = new DataOutputStream(this.s.getOutputStream());
    }
    
    @Override
    public void run(){
               
        JSONObject item;
        String message;
        JSONObject res = new JSONObject();
        String address = null;
        
        while(true){
            try {
                message = dis.readUTF();
                
                item = new JSONObject(message);
                
                switch (item.getString("Message Type")) {
                    
                    case "Look up":
                        for (Object o:BindingService.services.keySet()){
                            if(BindingService.services.get(o).equals(item.get("Service"))){ 
                                address =  String.valueOf(o);
                                break;
                            }
                        }
                                                
                        res.put("Message Type", "Reply");
                        if (address == null) {
                            res.put("Status", "UnSuccessful");
                            res.put("Result", "Service Not Available!");
                        } 
                        else {
                            res.put("Status", "Successful");
                            res.put("Result", address);
                        }
                        dos.writeUTF(res.toString());
                        break;
                        
                    case "Register":
                        
                        JSONArray client_service = item.getJSONArray("services");
                        for (int i = 0; i < client_service.length(); i++) {
                            BindingService.services.put(s.getInetAddress().getHostName() + ":" + String.valueOf(s.getPort()),String.valueOf(client_service.get(i)));
                        }
                        dos.writeUTF("Register Successful");
                        break;
                        
                    case "Change Port":
                        
                        String new_port = item.getString("NEW PORT");
                        String service = item.getString("Service");
                        BindingService.services.remove(s.getInetAddress().getHostName() + ":" + String.valueOf(s.getPort()));
                        BindingService.services.put(s.getInetAddress().getHostName() + ":" +new_port, service);
                        dos.writeUTF("Change Successful");
                        break;
                        
                    case "Inactivate":

                        BindingService.services.remove(s.getInetAddress().getHostName() + ":" + String.valueOf(s.getPort()));
                        dos.writeUTF("Inactive Successful");
                        break;
                }
                dis.close();
                dos.close();
                s.close();
                break;                
            } 
            catch (IOException | JSONException ex) {
                try{
                    dis.close();
                    dos.close();
                    s.close();
                    break;
                }
                catch(IOException er){
                    break;
                }
            }
        }        
    }
}
