

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class ClientHandler implements Runnable {


    public static ArrayList<ClientHandler> users = new ArrayList<ClientHandler>();
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
             * the input at this case the username*/
//            this.clientUsername = "test";
            this.clientUsername = Reader.readLine();
            users.add(this); //Adding "this" object which is the current Client to the array
            sendMessage("SERVER: Client "+clientUsername+" welcome to the chat!");

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
  
                
   
                
               
               

            }
            catch(IOException e){
                closeStreams(socket,Reader,Writer);
                break;
            }
        }
    }
    
    public void userCommands(String msgFromClient){
            Date time = java.util.Calendar.getInstance().getTime();
            String [] toUser = msgFromClient.split(" ");
                if(msgFromClient.contains("!online")) {
                printOnlineUsers();
                    
                
                } 
                else if(msgFromClient.contains("!private")){
                    try {
                         privateMessage(msgFromClient, toUser[1].toString());
                    } catch (Exception e) {
                        System.out.println("Error User Not send");
                    }
                   
                }
                 else{
                   sendMessage(clientUsername+" : "+ msgFromClient+"at ("+time+")");

                }
               
    
    }

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
             * because the buffer sends data only when its full so we do it manualy in case its not full
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
    public void sendMessage(String msg,boolean all ){

        /**
         * Sending Messages to clients except the Client who sends this message
         * And then we flush the buffer
         * because the buffer sends data only when its full so we do it manualy in case its not full
         * After users Message*/
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
    
    public void privateMessage(String msg,String user ){

        /**
         * Sending Messages to clients except the Client who sends this message
         * And then we flush the buffer
         * because the buffer sends data only when its full so we do it manualy in case its not full
         * After users Message*/
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
