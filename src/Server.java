import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private ServerSocket serverSocket;
    private Socket socket ;





    public static void main(String[] args) throws IOException {


           ServerSocket serverSocket = new ServerSocket(2020);
            Server main = new Server(serverSocket);
            main.startServer();

    }

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void  startServer(){
        try {
            System.out.println("Waiting Users To Connect..... PORT:"+serverSocket.getLocalPort());
            while(!serverSocket.isClosed()){

                /* Accept method returns socket At this point the server object is waiting for sockets without moving to next lines*/
                socket = serverSocket.accept();
                System.out.println("User Connected!");
                ClientHandler ch = new ClientHandler(socket);
                /*Creating Thread and passing in it client handler object*/
                Thread thread = new Thread(ch);
                /*Running The threads run method!*/
                thread.start();
            }

        } catch (IOException e) {
            closeSockets();
        }
    }


    public void closeSockets(){
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
