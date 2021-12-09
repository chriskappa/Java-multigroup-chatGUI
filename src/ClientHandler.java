

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class ClientHandler implements Runnable {


    public static ArrayList<ClientHandler> users = new ArrayList<ClientHandler>();
    
    public static ArrayList<ClientHandler> LondonMet = new ArrayList<ClientHandler>();
    public static ArrayList<ClientHandler> PublicGroup = new ArrayList<ClientHandler>();
    public static ArrayList<ClientHandler> News = new ArrayList<ClientHandler>();
    Date time = java.util.Calendar.getInstance().getTime();
    
    /**
     Creating Socket reference we are storing
     clients socket and can use its socket
     input and output stream of bites*/
    private Socket socket;
    private String clientUsername;

    /**
     *  Creating Buffer Reader And Writer References
     *  in order to be able to Send(Write) and Receive(Read) Data
     */
    BufferedReader Reader;
    BufferedWriter Writer;


    /**
     * Creating Client Handler Constructor which accepts the clients Socket
     * Then we assign the socket to the socket reference from above.
     * */
    public ClientHandler(Socket socket){
        try{

            this.socket = socket;
            this.Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.Writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            /** This line of code is blocking line
             * and waits until the users sends
             * the input, at this case the username*/
            this.clientUsername = Reader.readLine();
            users.add(this); //Adding "this" object which is the current Client to the array
            sendMessage("SERVER: Client "+clientUsername+" welcome to the chat!");
            writeToFile("SERVER: Client "+clientUsername+" welcome to the chat!"+time+"\n");

        }
        catch(IOException e){
            closeStreams(socket,Reader,Writer);
        }

    }
    @Override
    public void run(){
        String msgFromClient;
    
        while(socket.isConnected()){
            try{

                msgFromClient = Reader.readLine();
                userCommands(msgFromClient);
                writeToFile("(MESSAGE SENT BY)"+this.clientUsername+":"+msgFromClient+" at "+time+" \n");
                
   
                
               
               

            }
            catch(IOException e){
                closeStreams(socket,Reader,Writer);
                break;
            }
        }
    }
    
    
    /** This Method Is Converting String Array To Single Word **/
    public String convertStringArrayToString(String msg){
        String[] arrayString = msg.split(" ");
        String word="";
       for(int i=2;i<arrayString.length; i++){
           
           word+=arrayString[i]+" ";
        }
        return word;
        
    }
    
     public void writeToFile(String message){
        try {
           FileWriter myWriter = new FileWriter("ChatEventLogger.txt",true);
           myWriter.write(message);
           myWriter.close();
           System.out.println("Successfully wrote to the file.");
         } catch (IOException e) {
           System.out.println("An error occurred.");
           e.printStackTrace();
         }
    }
    
    
     /** This Method Is Responsible To Accept User Commands
      * Available Commands
      * !online ,  !join groupName (Entering Group Chat), !group groupName Message (Sending Message to Group Chat)
      **/
    public void userCommands(String msgFromClient){
          
            String [] toUser = msgFromClient.split(" ");
                if(msgFromClient.contains("!online")) {
                    printOnlineUsers();
                } 
                else if(msgFromClient.contains("!private")){
                    try {
                         privateMessage("PRIVATE MESSAGE BY "+this.clientUsername+":"+convertStringArrayToString(msgFromClient)+" at ("+time+")", toUser[1].toString());
                    } catch (Exception e) {
                        System.out.println("Error User Not send");
                    }
                   
                }
                else if(msgFromClient.contains("!join")){      
                    joinGroup(toUser[1]);
                }
                else if(msgFromClient.contains("!group")){
                    
                   sendGroupMessage(this.clientUsername+":"+convertStringArrayToString(msgFromClient)+" at ("+time+")",toUser[1]);
                }
                 else{
                   sendMessage(clientUsername+" : "+ msgFromClient+"at ("+time+")");

                }
               
    
    }

    
     /** This Function Is Printing All Online Users Of The Chat**/
    private void printOnlineUsers() {
        sendMessage("_______ONLINE USERS_______",true);
        for(ClientHandler user : users){
            try{

                sendMessage(user.clientUsername+" is Online",true);
            }
            catch(Exception e){
                e.printStackTrace();
            }


        }
    }

    
    
     /** JoinGroup Method Is Registering Users To Group Chat 
      Available Group Chats:
      LondonMet,PublicGroup,News (!join LondonMet , !join PublicGroup , !join News)
      **/
    public void joinGroup(String groupName){
        
        try {
            switch(groupName){
                case "LondonMet": {
                    System.out.println("Adding To LondonMet");
                    LondonMet.add(this);
                    break;
                }
                case "PublicGroup": {
                    System.out.println("Adding To PublicGroup");
                    PublicGroup.add(this);
                    break;
                    }
                case "News":{
                    System.out.println("Adding To News");
                    News.add(this);
                    break;
                }
              
            }
               
        } catch (Exception e) {
            System.out.println("Couldnt Add client to Group");
        }
    }
    
     /** closeStreams Method Is Responsible To Close All Open Channels (socket,reader,writer) **/
    public void closeStreams(Socket socket , BufferedReader reader , BufferedWriter writer){
        removeUser();
        try{
            if(socket != null) socket.close();
            if(reader != null ) reader.close();
            if(writer != null ) writer.close();
        }
        catch(IOException e){
            closeStreams(socket,Reader,Writer);

        }

    }

    public void sendMessage(String msg ){

            /**
             * Sending Messages to clients except the Client who sends this message
             * And then we flush the buffer
             * because the buffer sends data only when its full so I do it manually in case its not full
             * After users Message*/
            for(ClientHandler client:users){
                try{
                   
                        client.Writer.write(msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                    

                }catch(IOException e){
                    closeStreams(socket,Reader,Writer);
                }


            }

    }

    
    
     /** isOnline Method Returns If Specific User Is Online! **/
    public boolean isOnline(String user){
    for(ClientHandler client:users){
            try{
                    if(client.clientUsername == user){
                        return true;
                    }



            }catch(Exception e){
                closeStreams(socket,Reader,Writer);
            }


        }
    return false;
    }
    
     /**
         * Sending Messages to clients except the Client who sends this message
         * And then we flush the buffer
         * because the buffer sends data only when its full so we do it manualy in case its not full
         * After users Message*/
    public void sendMessage(String msg,boolean all ){
        for(ClientHandler client:users){
            try{
                    if(client.clientUsername == clientUsername){
                        client.Writer.write(msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                    }



            }catch(IOException e){
                closeStreams(socket,Reader,Writer);
            }


        }

    }
    
    
     /**
         * Sending Messages to clients except the Client who sends this message
         * And then we flush the buffer
         * because the buffer sends data only when its full so we do it manualy in case its not full
         * After users Message*/
    public void privateMessage(String msg,String user ){
        for(ClientHandler client:users){
            try{
                    if(client.clientUsername.equals(user) ){
                        client.Writer.write(msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                    }
                   

                    

            }catch(IOException e){
                closeStreams(socket,Reader,Writer);
            }

                              

        }


    }
    
    
     /** This Method Returns If User Participate In Specific Group Chat**/
    public boolean isPartOfTheGroup(String user,String groupName){
        boolean isRegistered = false;
        switch(groupName){
            case "LondonMet":{
                for(ClientHandler currentUser : LondonMet){
                    try{
                        if(currentUser.clientUsername == this.clientUsername ){
                            isRegistered = true;
                            break;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                 }
                break;
            }
              case "PublicGroup":{
                for(ClientHandler currentUser : PublicGroup){
                    try{
                        if(currentUser.clientUsername == this.clientUsername ){
                            isRegistered = true;
                            break;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
                case "News":{
                for(ClientHandler currentUser : News){
                    try{
                        if(currentUser.clientUsername == this.clientUsername ){
                            isRegistered = true;
                            break;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        return isRegistered;
    }
    
     /**
         * Sending Messages to clients except the Client who sends this message
         * And then we flush the buffer
         * because the buffer sends data only when its full so we do it manualy in case its not full
         * After users Message*/
    public void sendGroupMessage(String msg,String group ){       
        try {
             
           boolean isRegistered = isPartOfTheGroup(this.clientUsername,group);
           if(isRegistered){
               
               switch(group){
                   case "LondonMet":{
                       
                     for(ClientHandler client:LondonMet){
                        try{
                   
                        client.Writer.write("GROUP MESSAGE(LondonMet)"+msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                       

                    }catch(IOException e){
                    closeStreams(socket,Reader,Writer);
                    }


               }
                     break;
                   }
                    case "PublicGroup":{
                       
                     for(ClientHandler client:PublicGroup){
                        try{
                   
                        client.Writer.write("GROUP MESSAGE(PublicGroup)"+msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                       

                    }catch(IOException e){
                    closeStreams(socket,Reader,Writer);
                    }


               }
                     break;
                   }
                     case "News":{
                       
                     for(ClientHandler client:News){
                        try{
                   
                        client.Writer.write("GROUP MESSAGE(News)"+msg);
                        client.Writer.newLine();
                        client.Writer.flush();
                       

                    }catch(IOException e){
                    closeStreams(socket,Reader,Writer);
                    }


               }
                     break;
                   }
               }

               
           }
           else{
               System.out.println("Please Registerd to the group!");
           }
        } catch (Exception e) {
        }

    }



    /**
     * Method in order to remove users from the chat*/
    public void removeUser(){
        users.remove(this);
        sendMessage("SERVER:"+this.clientUsername+" left the chat!");
    }

    /**
     * Method for testing the objects while coding the application*/
    public String toString(){
        return "UserName:"+clientUsername;
    }
}
