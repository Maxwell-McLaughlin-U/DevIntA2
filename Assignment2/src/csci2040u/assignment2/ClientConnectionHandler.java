package csci2040u.assignment2;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Date;

public class ClientConnectionHandler implements Runnable{
    //A Socket variable to store the socket created by the ClientServer
    private Socket socket = null;
    //A BufferedReader variable to store the input of the file being read
    private BufferedReader requestInput = null;
    //A DataOutputStream variable to store the output created by the file being read
    private DataOutputStream responseOutput = null;

    /**
     * A constructor for the ClientConnectionHandler class
     * to instantiate the private variable socket.
     * @param socket An instance of the Socket class
     * @throws IOException
     */
    public ClientConnectionHandler(Socket socket) throws IOException{
        this.socket = socket;
        requestInput = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        responseOutput = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * A void function that is from the Runnable interface that
     * the ClientConnectionHandler is implementing.
     */
    public void run(){
        try {
            String[] args = requestInput.readLine().split(" ");
            //String[] strHost = requestInput.readLine().split(" ");
            System.out.println(args[0]);
            //System.out.println(strHost[1]);


            if(args[0].equals("DOWNLOAD")){
                sendFile(args[1]);
            }else if(args[0].equals("UPLOAD")){
                upload(args[1]);
                requestInput.close();
                socket.close();
            }
            else if(args[0].equals("DIR")){
                requestInput.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                requestInput.close();
                responseOutput.close();
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    public String dir () throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        responseOutput.writeUTF("DIR");
        String fileList = input.readUTF();
        return fileList;
    }

    /**
     * Takes the file name of a file passed to command line
     * and copies each character in the file to a new file in the shared server with
     * the same filename. Disconnects the socket once done.
     * @param fileName A string of the name of the file being uploaded from shared server to remote server
     * @throws IOException
     */
    public void upload(String fileName) throws IOException {
        System.out.println("Uploading" + fileName + "...");
        //Creating a file in the shared server with the same file name
        File file = new File("src\\csci2040u\\assignment2\\ServerFolder\\" + fileName);
        FileWriter upWriter = new FileWriter(file);
        //A string to store the contents in each line of the file being read
        String line;
        //While loop to parse over file and write to a new file locating to the shared server
        while ((line = requestInput.readLine()) != null){
            upWriter.write(line + "\n");
            //Closing the FileWriter
            upWriter.flush();
        }
        //Closing socket and any other readers and writers
        upWriter.close();
        requestInput.close();
        responseOutput.close();
        socket.close();
    }

    /**
     * A void function that reads character by character of a file in
     * the remote shared server's folder and writes the read contents to the
     * local client’s shared folder.
     * @param target The name of the file that is being passed to be downloaded
     * @throws IOException
     */
    public void sendFile(String target) throws IOException {
        File file = new File("src\\csci2040u\\assignment2\\ServerFolder\\" + target);
        System.out.println(file.exists());

        //Storing the type of file that is being read.
        String contentType = getContentType(file.getName());
        //An array of bytes of length with num of bytes in the file being read to store the bytes of the file.
        byte[] content = new byte[(int)file.length()];
        FileInputStream fileIn = new FileInputStream(file);
        fileIn.read(content);
        //Closing the file parser.
        fileIn.close();
        sendGetResponse("HTTP/1.1 200 Ok\r\n", contentType, content);

    }

    /**
     * A function that checks to see what the file name ends
     * with to determine what type of file it is.
     * @param filename A string that stores the name of the file being passed
     * @return A string of the type of file being passed to this function.
     */
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

    /**
     * A function that writes out to a file and disconnects the socket once completed.
     * @param responseCode  A string that stores the response of the action
     * @param contentType   A string that stores the type of file that is being handled
     * @param content       A byte array that stores the bytes of the file being handled
     * @throws IOException
     */
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
