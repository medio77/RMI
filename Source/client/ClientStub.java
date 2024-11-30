// package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientStub implements interfaces.Stock {
    ConcurrentHashMap<String,String> services = new ConcurrentHashMap<>();

    @Override
    public int sum(int a, int b, int c) {
        String Error_Message = "Error While Computing";
        File myObj = new File("directoryClient.txt");
        Scanner myReader;
        String Binder_IP = null;
        int Binder_PORT = 0;
        try {
            myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                Binder_IP = myReader.nextLine();
                Binder_PORT = Integer.valueOf(myReader.nextLine());
            }
        }
        catch (FileNotFoundException ex) {}

        String interface_name = "Stock";
        JSONObject res = null;
        String addr;
        int trycnt=0;
        while(true){
            if(trycnt==2){
                trycnt=0;
                try {
                    throw new RemoteException(Error_Message);
                } catch (RemoteException ex) {
                    System.err.println(Error_Message);
                    return -1;
                }
            }
            trycnt+=1;
            try {
                if(services.containsKey(interface_name)){
                    addr = services.get(interface_name);
                }
                else {
                    addr = lookup(Binder_IP, Binder_PORT,"Stock");
                    if(addr.equals("Service Not Available!")){
                        throw new RemoteException(Error_Message);
                    }
                }
                if(addr.equals("-1")){
                    throw new RemoteException(Error_Message);
                }
                else{

                    int cnt = 0;
                    int flag=0;
                    Socket s;
                    DataInputStream dis = null;
                    DataOutputStream dos = null;
                    services.put(interface_name, addr.split(":")[0]);
                    while(cnt != 2){
                        try{

                            s = new Socket(addr.split(":")[0],Integer.valueOf(addr.split(":")[1]));
                            dis = new DataInputStream(s.getInputStream());
                            dos = new DataOutputStream(s.getOutputStream());
                            flag=1;
                            break;
                        }
                        catch(Exception ex){
                            cnt+=1;
                        }
                    }
                    if(flag==1){
                        flag=0;

                        JSONObject json = new JSONObject();
                        JSONArray array = new JSONArray();
                        JSONObject item = new JSONObject();
                        json.put("versionID", VersionUID);
                        json.put("Name", new Object(){}.getClass().getEnclosingMethod().getName());
//                        json.put("Args Number",new Object(){}.getClass().getEnclosingMethod().getParameters().length);
                        json.put("Byte Order", "Little Endian");
                        json.put("Message Type", "Call");
                        item.put("Arg0",a);
                        item.put("Arg1",b);
                        item.put("Arg2",c);
                        array.put(item);

                        json.put("Args", array);

                        dos.writeUTF(json.toString());
                        String reply = dis.readUTF();
                        if (reply == null){
                            throw new RemoteException(Error_Message);
                        }
                        else{
                            res = new JSONObject(reply);
                            if (res.getString("Status").equals("Successful") && res.getString("Message Type").equals("Reply"))
                                return (int) res.get("Result");
                            else
                                throw new RemoteException(Error_Message);
                        }
                    }
                    else{
                        services.remove(interface_name);
                        continue;
                    }
                }
            }
            catch (JSONException | IOException | RemoteException ex) {
                System.err.println(Error_Message);
                return -1;
            }
        }
    }

    public String lookup(String IP, int PORT, String Service) {
        try{
            Socket s = new Socket(IP, PORT);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            JSONObject json = new JSONObject();
            JSONObject item = new JSONObject();

            json.put("Message Type", "Look up");
            json.put("Service", Service);
            dos.writeUTF(json.toString());
            String address = dis.readUTF();
            JSONObject re = new JSONObject(address);
            s.close();
            return re.getString("Result");
        }
        catch(IOException | JSONException ex){
                return "-1";
        }
    }

    @Override
    public int getPrice(String symbol) {
        String Error_Message = "Error While Computing";

        File myObj = new File("directoryClient.txt");
        Scanner myReader;
        String Binder_IP = null;
        int Binder_PORT = 0;
        try {
            myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                Binder_IP = myReader.nextLine();
                Binder_PORT = Integer.valueOf(myReader.nextLine());
            }
        }
        catch (FileNotFoundException ex) {}

        String interface_name = "Stock";
        JSONObject res = null;
        String addr;

        int trycnt = 0;
        while(true){
            if(trycnt==2){
                trycnt=0;
                try {
                    throw new RemoteException(Error_Message);
                } catch (RemoteException ex) {
                    System.err.println(Error_Message);
                    return -1;
                }
            }
            trycnt+=1;

            try {
                if(services.containsKey(interface_name)){
                    addr = services.get(interface_name);
                }
                else {
                    addr = lookup(Binder_IP, Binder_PORT,"Stock");
                    if(addr.equals("Service Not Available!")){
                        throw new RemoteException(Error_Message);
                    }
                }
                if(addr.equals("-1")){
                    throw new RemoteException(Error_Message);
                }
                else{

                    int cnt = 0;
                    int flag=0;
                    Socket s;
                    DataInputStream dis = null;
                    DataOutputStream dos = null;
                    services.put(interface_name, addr.split(":")[0]);
                    while(cnt != 2){
                        try{

                            s = new Socket(addr.split(":")[0], Integer.valueOf(addr.split(":")[1]));
                            dis = new DataInputStream(s.getInputStream());
                            dos = new DataOutputStream(s.getOutputStream());
                            flag=1;
                            break;
                        }
                        catch(Exception ex){
                            cnt+=1;
                        }
                    }
                    if(flag==1){
                        flag=0;

                        JSONObject json = new JSONObject();
                        JSONArray array = new JSONArray();
                        JSONObject item = new JSONObject();
                        json.put("versionID", VersionUID);
                        json.put("Name", new Object(){}.getClass().getEnclosingMethod().getName());
                        json.put("Args Number",new Object(){}.getClass().getEnclosingMethod().getParameters().length);
                        json.put("Byte Order", "Little Endian");
                        json.put("Message Type", "Call");
                        item.put("Arg0",symbol);
                        array.put(item);

                        json.put("Args", array);

                        dos.writeUTF(json.toString());
                        String reply = dis.readUTF();
                        if (reply == null){
                            throw new RemoteException(Error_Message);
                        }
                        else{
                            res = new JSONObject(reply);
                            if (res.getString("Status").equals("Successful") && res.getString("Message Type").equals("Reply"))
                                return (int) res.get("Result");
                            else
                                throw new RemoteException(Error_Message);
                        }
                    }
                    else{
                        services.remove(interface_name);
                        continue;
                    }
                }
            } catch (JSONException | IOException | RemoteException ex) {
                System.err.println(Error_Message);
                return -1;
            }
        }
    }

    @Override
    public List<Boolean> CheckStatus(List VMs) {
        String Error_Message = "Error While Computing";

        File myObj = new File("directoryClient.txt");
        Scanner myReader;
        String Binder_IP = null;
        int Binder_PORT = 0;
        try {
            myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                Binder_IP = myReader.nextLine();
                Binder_PORT = Integer.valueOf(myReader.nextLine());
            }
        }
        catch (FileNotFoundException ex) {}

        List<Boolean> result = new ArrayList<>();
        String interface_name = "Stock";
        JSONObject res = null;
        String addr;

        int trycnt=0;
        while(true){
            if(trycnt==2){
                trycnt=0;
                try {
                    throw new RemoteException(Error_Message);
                } catch (RemoteException ex) {}
            }
            trycnt+=1;
            try {
                if(services.containsKey(interface_name)){
                    addr = services.get(interface_name);
                }
                else {
                    addr = lookup(Binder_IP, Binder_PORT,"Stock");
                    if(addr.equals("Service Not Available!")){
                        throw new RemoteException(Error_Message);
                    }
                }
                if(addr.equals("-1")){
                    throw new RemoteException(Error_Message);
                }
                else{

                    int cnt = 0;
                    int flag=0;
                    Socket s;
                    DataInputStream dis = null;
                    DataOutputStream dos = null;
                    services.put(interface_name, addr.split(":")[0]);
                    while(cnt != 2){
                        try{

                            s = new Socket(addr.split(":")[0],Integer.valueOf(addr.split(":")[1]));
                            dis = new DataInputStream(s.getInputStream());
                            dos = new DataOutputStream(s.getOutputStream());
                            flag=1;
                            break;
                        }
                        catch(Exception ex){
                            cnt+=1;
                        }
                    }
                    if(flag==1){
                        flag=0;

                        JSONObject json = new JSONObject();
                        JSONArray array = new JSONArray();
                        JSONObject item = new JSONObject();
                        json.put("versionID", VersionUID);
                        json.put("Name", new Object(){}.getClass().getEnclosingMethod().getName());
                        json.put("Args Number",new Object(){}.getClass().getEnclosingMethod().getParameters().length);
                        json.put("Byte Order", "Little Endian");
                        json.put("Message Type", "Call");
                        item.put("Arg0",VMs);
                        array.put(item);

                        json.put("Args", array);

                        dos.writeUTF(json.toString());
                        String reply = dis.readUTF();
                        if (reply == null){
                            throw new RemoteException(Error_Message);
                        }
                        else{
                            res = new JSONObject(reply);
                            if (res.getString("Status").equals("Successful") && res.getString("Message Type").equals("Reply"))
                                for (int i=0;i<res.getJSONArray("Result").length();i++){

                                    result.add(res.getJSONArray("Result").getBoolean(i));
                                }
                        }
                    }
                    else{
                        services.remove(interface_name);
                        continue;
                    }
                }
            } catch (JSONException | IOException |RemoteException ex) {
                System.err.println(Error_Message);
                return result;
            }
            break;
        }

        return result;
    }

    @Override
    public List<List<String>> getOrder(String symbol, int count) {
        String Error_Message = "Error While Computing";

        File myObj = new File("directoryClient.txt");
        Scanner myReader;
        String Binder_IP = null;
        int Binder_PORT = 0;
        try {
            myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                Binder_IP = myReader.nextLine();
                Binder_PORT = Integer.valueOf(myReader.nextLine());
            }
        }
        catch (FileNotFoundException ex) {}

        List<List<String>> result = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        String interface_name = "Stock";
        JSONObject res = null;
        String addr;

        int trycnt=0;
        while(true){
            if(trycnt==2){
                trycnt=0;
                try {
                    throw new RemoteException(Error_Message);
                } catch (RemoteException ex) {}
            }
            trycnt+=1;
            try {
                if(services.containsKey(interface_name)){
                    addr = services.get(interface_name);
                }
                else {
                    addr = lookup(Binder_IP, Binder_PORT,"Stock");
                    if(addr.equals("Service Not Available!")){
                        throw new RemoteException(Error_Message);
                    }
                }
                if(addr.equals("-1")){
                    throw new RemoteException(Error_Message);
                }
                else{

                    int cnt = 0;
                    int flag=0;
                    Socket s;
                    DataInputStream dis = null;
                    DataOutputStream dos = null;
                    services.put(interface_name, addr.split(":")[0]);
                    while(cnt != 2){
                        try{

                            s = new Socket(addr.split(":")[0],Integer.valueOf(addr.split(":")[1]));
                            dis = new DataInputStream(s.getInputStream());
                            dos = new DataOutputStream(s.getOutputStream());
                            flag=1;
                            break;
                        }
                        catch(Exception ex){
                            cnt+=1;
                        }
                    }
                    if(flag==1){
                        flag=0;


                        JSONObject json = new JSONObject();
                        JSONArray array = new JSONArray();
                        JSONObject item = new JSONObject();
                        json.put("versionID", VersionUID);
                        json.put("Name", new Object(){}.getClass().getEnclosingMethod().getName());
//                        json.put("Args Number",new Object(){}.getClass().getEnclosingMethod().getParameters().length);
                        json.put("Byte Order", "Little Endian");
                        json.put("Message Type", "Call");
                        item.put("Arg0",symbol);
                        item.put("Arg1",count);
                        array.put(item);

                        json.put("Args", array);

                        dos.writeUTF(json.toString());
                        String reply = dis.readUTF();
                        if (reply == null){
                            throw new RemoteException(Error_Message);
                        }
                        else{
                            res = new JSONObject(reply);
                            if (res.getString("Status").equals("Successful") && res.getString("Message Type").equals("Reply"))
                                for (int i=0;i<res.getJSONArray("Result").length();i++){
                                     for(int j=0;j< res.getJSONArray("Result").getJSONArray(i).length();j++){
                                         tmp.add((String) res.getJSONArray("Result").getJSONArray(i).get(j));
                                     }
                                     result.add(new ArrayList<String>(tmp));
                                     tmp.clear();

                                }
                        }
                    }
                    else{
                        services.remove(interface_name);
                        continue;
                    }
                }
            } catch (JSONException | IOException | RemoteException ex) {
                System.err.println(Error_Message);
                return result;
            }
            break;
        }
        return result;
    }
}
