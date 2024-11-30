package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONObject;
import java.net.Socket;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServerStub extends Thread{
    private final DataInputStream dis; 
    private final DataOutputStream dos; 
    private final Socket s;
    
    public ServerStub(Socket s) throws IOException 
    {  
        this.s = s;
        this.dis = new DataInputStream(this.s.getInputStream());
        this.dos = new DataOutputStream(this.s.getOutputStream());
    }
    
    @Override
    public void run()  {
        
        
        JSONObject result = new JSONObject();
        JSONObject item;
        JSONObject Args;
        String invoke_method;
        
        while(true){
            try{
                String request =  dis.readUTF();
                item = new JSONObject(request);
                
                result.put("Message Type", "Reply");
                result.put("Byte Order", "Little Endian");
                
                if(item.getLong("versionID")!=(interfaces.Stock.VersionUID)){
                    result.put("Status", "UnSuccessful");
                    result.put("Result", "Class NotFound");
                }
                else{
                    
                    String serverClassName = "server.Server";
                    Class<?> serverClass = Class.forName(serverClassName);
                    Object server = serverClass.newInstance();
                    Method method = null;
                    Args = item.getJSONArray("Args").getJSONObject(0);

                    invoke_method = item.getString("Name");
                    if (invoke_method.equals("sum")){
                       method = server.getClass().getMethod(invoke_method, int.class,  int.class, int.class);
                        int answer =  (int) method.invoke(server, Args.get("Arg0"),Args.get("Arg1"),Args.get("Arg2"));
                        result.put("Result", answer);
                    }
                    
                    else if(invoke_method.equals("getPrice")){
                        method = server.getClass().getMethod(invoke_method, String.class);
                        int answer =  (int) method.invoke(server, Args.get("Arg0"));
                        result.put("Result", answer);
                    }
                    
                    else if (invoke_method.equals("getOrder")){
                        method = server.getClass().getMethod(invoke_method, String.class,int.class);
                        List<List<String>> answer =   (List<List<String>>) method.invoke(server, Args.get("Arg0"), Args.get("Arg1"));
                        result.put("Result", answer);
                    }
                    
                    else if(invoke_method.equals("CheckOpenness")){
                        method = server.getClass().getMethod(invoke_method, List.class);
                        List<String> ss = new ArrayList<>();
                        for (int i=0;i<Args.getJSONArray("Arg0").length();i++){
                            ss.add(Args.getJSONArray("Arg0").getString(i));
                        }
                        List<String[]> answer =   (List<String[]>) method.invoke(server, ss);
                        
                        result.put("Result", answer);                        
                    }

                    result.put("Status", "Successful");                     
                }
                dos.writeUTF(result.toString());
            }
            catch(Exception ex){
                try{
                    dos.close();
                    dis.close();
                    s.close();
                    break;
                }
                catch(Exception ex1){
                break;}
            }
        }
    }
}
