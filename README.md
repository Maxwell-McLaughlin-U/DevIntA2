# CSCI 2020U Assignment 2

Group Members:

-Maxwell McLaughlin (10075133)

-Soumia Umaputhiran (100744669)

Project Information
To create a file sharing JavaFx application where clients can connect to a server to upload and download files from a central server. The central server is multithreaded and a socket is open until an event has been handled. The instructions of this assignment reads "The file sharing clients will connect to a central server, which will respond to a single client command, and then disconnect. Each time the client needs to issue another command, it will reconnect before sending the command. The server will respond to the following commands: DIR, UPLOAD, DOWNLOAD.

DIR: Will list the contents of the shared folder, on the server’s machine. Then the server will disconnect immediately after sending the list of files to the client.
UPLOAD: With a filename passed through the command line, the server will connect the text from this file, and save it as a new file with the same filename. So copy the contents of the files in the local client server to the shared server. The server will disconnect immediately after copying character by character and writing the characters of the file.
DOWNLOAD: Take the selected file to transfer from the remote server’s shared folder to the local client’s shared folder. This transfer will simply be a copy of every character in the file across the network.

The client side will have a simple user interface that displays the list of filenames on a split pane. The right showing the list of filenames in the shared server and on the left showing a list of filenames in the local client's shared folder.

Image of Application


<img width="425" alt="CSCI2020U Assignment2 Image" src="https://user-images.githubusercontent.com/60481370/113497615-c9d32380-94d3-11eb-8935-76374f3c0edc.png">


Improvements

We added an image to the JavaFx GUI. We also made the first cell of the List View a different colour to outline as a title. 

How to Run: Step-by-Step

1. Click on the green button that says "Code" which is located on the top right corner of the screen.
2. Copy the HTTPS url.
3. Choose a location you want to store your project.
4. Instiate git repository by typing _git init_
5. Then on your terminal type _git remote add origin "url"_
6. Then type _git fetch origin main_
7. This should clone the project onto your local repository
8. Open IntelliJ or any other Java supported IDE. 

Instructions for Intelli J:
- Go to File/Open
- Choose the Assignment2 folder that you cloned onto your computer.
- Go to Server.java located in src/csci2040u.assignment2 and right click on the Java file and press _Run 'Server.main()'_
- Go to ClientServer.java which is located in src/csci2040u.assignment2 and right click on the Java file and press _Run 'Server.main()'_

Other resources

[1] https://www.rgagnon.com/javadetails/java-0542.html - Used to figure out how to read characters of a file through the network and write out to a file.

