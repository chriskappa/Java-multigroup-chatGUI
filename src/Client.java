import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    private BufferedReader Reader;
    private BufferedWriter Writer;
    private String userName;
    private Socket socket;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.println("Enter Your User Name");
            String userName = scanner.nextLine();
            Socket socket = new Socket("localhost",2020);
            Client main = new Client(socket,userName);
            main.listenForMessages();
            main.sendMessage();

        }
        catch(IOException e){
            e.toString();
        }

    }

    public Client(Socket socket ,String userName){
        try{
            this.socket = socket;
            this.userName = userName;
            this.Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.Writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
       catch(IOException e){
            e.toString();
       }

    }

    /**
     * This method is implementing runnable and creating new thread for each client in order to listen for messages*/
    public void listenForMessages(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChat;
                while(socket.isConnected()){
                    try{
                        msgFromChat = Reader.readLine();
                        System.out.println(msgFromChat);
                    }catch(IOException e){
                        e.toString();
                    }

                }
            }
        }).start();
    }

    public void sendMessage(){

        Scanner scanner = new Scanner(System.in);
        try{
            /**
             * Sending The UserName first Since the ClientHandler Class is awaiting before it continues*/
            Writer.write(userName);
            Writer.newLine();
            Writer.flush();

            while(socket.isConnected()){

                try{
                    System.out.println("Enter Your Message...");
                    String messageToSend = scanner.nextLine();
                    Writer.write(userName+":"+messageToSend);
                    Writer.newLine();
                    Writer.flush();
                }catch(IOException e){
                    e.toString();
                }
            }
        }catch(Exception e){
            e.toString();
        }
    }


}
