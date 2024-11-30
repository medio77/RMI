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