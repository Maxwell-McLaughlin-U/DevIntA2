package csci2040u.assignment2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private ServerSocket serverSocket = null;
    private int port;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    public void handleRequests() throws IOException{
        System.out.println("Listening to port: "+port);

        // creating a thread to handle each of the clients
        while(true){
            Socket clientSocket = serverSocket.accept();
            ClientConnectionHandler handler = new ClientConnectionHandler(clientSocket);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
        }

    }



    public static void main(String[] args) {
        int port = 8080;
        // port to listen default 8080, or the port from the argument
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            //Instantiating the HttpServer Class
            Server server = new Server(port);
            // handle any requests from the port
            server.handleRequests();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

