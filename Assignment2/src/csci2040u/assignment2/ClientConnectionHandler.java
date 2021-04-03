package csci2040u.assignment2;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Date;

public class ClientConnectionHandler implements Runnable{

    private Socket socket = null;
    private BufferedReader requestInput = null;
    private DataOutputStream responseOutput = null;

    public ClientConnectionHandler(Socket socket) throws IOException{
        this.socket = socket;
        requestInput = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        responseOutput = new DataOutputStream(socket.getOutputStream());
    }


    public void run(){
        //System.out.println("hello");
        try {
            String[] strRequest = requestInput.readLine().split(" ");
            String[] strHost = requestInput.readLine().split(" ");
            System.out.println(strRequest[1]);
            System.out.println(strHost[1]);


            if(strRequest[0].equals("GET")){
                //System.out.println("debug: bear");

                sendFile(strRequest[1]);
            }else if(strRequest[0].equals("UPL")){
                //System.out.println("debug: dog");
                //System.out.println(requestInput.readLine());
                //System.out.println(requestInput.readLine());
                upload(strRequest[1]);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void upload(String fileName) throws IOException {
        System.out.println("debug: monkey");
        System.out.println(fileName);
        //String line = requestInput.readLine();
        //System.out.println("123" + line + "123");
        FileWriter upWriter = new FileWriter("src\\csci2040u\\assignment2\\ServerFolder\\" + fileName);
        String line;

        while ((line = requestInput.readLine()) != null){
            //System.out.println("debug: rabbit");
            System.out.println(line);

            upWriter.write(line + "\n");


        }


        upWriter.close();
    }

    public void sendFile(String target) throws IOException {
        File file = new File("src\\csci2040u\\assignment2\\ServerFolder\\" + target);
        System.out.println("debug: flamingo");
        System.out.println(file.exists());

        String contentType = getContentType(file.getName());
        byte[] content = new byte[(int)file.length()];
        FileInputStream fileIn = new FileInputStream(file);
        fileIn.read(content);
        fileIn.close();
        sendGetResponse("HTTP/1.1 200 Ok\r\n", contentType, content);

    }

    private String getContentType(String filename) {
        if (filename.endsWith(".html") || filename.endsWith(".htm")) {
            return "text/html";
        } else if (filename.endsWith(".css")) {
            return "text/css";
        } else if (filename.endsWith(".js")) {
            return "text/javascript";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "unknown";
        }
    }

    private void sendGetResponse(String responseCode, String contentType, byte[] content) throws IOException {
        responseOutput.writeBytes(responseCode);

        responseOutput.writeBytes("Content-Type: " + contentType + "\r\n");
        responseOutput.writeBytes("Date: " + (new Date()) + "\r\n");
        responseOutput.writeBytes("Server: Simple-Http-Server v1.0.0\r\n");
        responseOutput.writeBytes("Content-Length: " + content.length + "\r\n");
        responseOutput.writeBytes("Connection: Close\r\n\r\n");
        responseOutput.writeBytes("file contents:\n");
        responseOutput.write(content);

        responseOutput.writeBytes("\r\n\r\n");

        responseOutput.flush();
        responseOutput.close();
        socket.close();
    }

}
