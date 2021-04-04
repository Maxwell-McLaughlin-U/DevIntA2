package csci2040u.assignment2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    //Variable to store the socket created
    private final Socket socket;

    /**
     * A constructor for the ClientServer class to instantiate the
     * private variable socket.
     * @param socket An instance of the Socket class
     */
    public ClientServer(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) {
        BufferedReader in;
        PrintWriter out;

        String hostname = args[1];
        String uri = args[2];
        int port = 8080;

        if (args.length > 3) {
            port = Integer.parseInt(args[3]);
        }

        try{
            Socket socket = new Socket(hostname, port);
            //Creating an object of the ClientConnectionHandler class
            ClientConnectionHandler client = new ClientConnectionHandler(socket);

            //Reading in the contents from socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Writing out the contents from socket
            out = new PrintWriter(socket.getOutputStream());

            // send the request
            System.out.println("Request:");
            System.out.print(args[0]+ " " + uri + " HTTP/1.1\r\n");
            System.out.print("Host: " + hostname + "\r\n\r\n");
            out.print(args[0] + " " + uri + " HTTP/1.1\r\n");
            out.print("Host: " + hostname + "\r\n");

            //Handling the directory command line argument
            if(args[0].equals("DIR")){
                System.out.println("Files in folder are...");
                System.out.println(client.dir());
            }

            //Handling the upload command line argument
            //Variable to store that state of wheather an argument for upload has been passed to command line
            boolean upl = false;
            if(args[0].equals("UPLOAD")){
                System.out.println("Uploading " + uri + "...");
                File upFile = new File("src\\csci2040u\\assignment2\\LocalFolder\\" + uri);
                Scanner fileReader = new Scanner(upFile);
                while (fileReader.hasNextLine()){
                    out.print(fileReader.nextLine() + "\n");
                    out.flush();
                }
                //Displaying messages of status of upload
                System.out.println("Uploaded "+uri+ " to client server.");
                System.out.println("Socket disconnected");
                fileReader.close();
                out.flush();
                socket.close();
                upl = true;
            }
            //Handling the download command line argument
            if(args[0].equals("DOWNLOAD") && !upl) {
                // read and print the response
                System.out.println("New Thread Created");
                System.out.println("Downloading "+ uri+"...");
                String line;

                out = new PrintWriter(socket.getOutputStream());

                while ((line = in.readLine()) != null) {

                    if (line.equals("file contents:")) {
                        FileWriter downWriter = new FileWriter("src\\csci2040u\\assignment2\\LocalFolder\\" + uri);
                        while ((line = in.readLine()) != null) {
                            downWriter.write(line + "\n");
                        }
                        downWriter.close();
                        break;
                    }
                    System.out.println(line);

                }
                //Displaying messages of status of download
                System.out.println("Download complete.");
                System.out.println("Downloaded "+uri+ " to local server.");
                System.out.println("Socket disconnected");
                // close everything
                in.close();
                out.close();
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
