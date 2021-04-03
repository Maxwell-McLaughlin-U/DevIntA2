package csci2040u.assignment2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    public static void main(String[] args) {
        BufferedReader in;
        PrintWriter out;

        if (args.length <= 1) {
            System.out.println("Usage: java HttpClient <GET or UPL> <host> <uri> [<port>=80]");
            System.exit(0);
        }

        String hostname = args[1];
        String uri = args[2];
        int port = 80;
        if (args.length > 3) {
            port = Integer.parseInt(args[3]);
        }

        try{
            Socket socket = new Socket(hostname, port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            // send the request
            System.out.println("Request:");
            System.out.print(args[0]+ " " + uri + " HTTP/1.1\r\n");
            System.out.print("Host: " + hostname + "\r\n\r\n");
            out.print(args[0] + " " + uri + " HTTP/1.1\r\n");
            out.print("Host: " + hostname + "\r\n");

            if(args[0].equals("UPL")){
                //out.print("hey\r\n");
                File upFile = new File("src\\csci2040u\\assignment2\\LocalFolder\\" + uri);
                Scanner fileReader = new Scanner(upFile);
                while (fileReader.hasNextLine()){
                    out.print(fileReader.nextLine() + "\n");
                }
                fileReader.close();
            }

            //out.print("heya\r\n\r\n");


            out.flush();

            // read and print the response
            System.out.println("Response:");
            String line;



            while ((line = in.readLine()) != null) {

                if(line.equals("file contents:")){
                    //System.out.println("debug: wolf");

                    FileWriter downWriter = new FileWriter("src\\csci2040u\\assignment2\\LocalFolder\\" + uri);
                    while ((line = in.readLine()) != null){
                        downWriter.write(line + "\n");
                    }
                    downWriter.close();
                    break;
                }

                System.out.println(line);

            }

            // close everything

            in.close();
            out.close();
            socket.close();

        }catch(IOException e){
            e.printStackTrace();
        }


    }


}
