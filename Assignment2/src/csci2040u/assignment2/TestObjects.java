package csci2040u.assignment2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TestObjects creates a list of TestFile objects.
 * It uses the probability map from the training section (Train class)
 * to determine the spam probability of a file. Uses a Bayesian spam detector model.
 */
public class TestObjects {
    /**
     * Double variables to store the accuracy and
     * precision of the spam detector.
     */
    public double accuracy = 0.0;
    public double precision = 0.0;

    /**
     * A static function which returns an array list of TestFile objects
     * @param strDirectory the path from which the user accesses the data folder in the Main class
     * @return an array list of TestFile objects
     * @throws IOException
     */
    public ObservableList<TestFile> getSpamData(String strDirectory) throws IOException {

        //Creating an array list to store the TestFile objects
        ObservableList<TestFile> spamResults = FXCollections.observableArrayList();
        /**
         * Creating a map to store the word and probability got from the Train class
         * which is the probability that a file is spam.
         */
        Map<String, Double> trainMap = Train.getSpamProb(strDirectory + "/train");

        //Double variable to store the probability that a file is spam when reading the files in the test directory
        double spamProb = 0.0;

        /**
         * Double variables to use to calculate the accuracy and precision of the
         * spam probability of each file.
         * These variables are used to determine how many are correctly calculated from the Training and
         * how many are incorrectly counted from the Training.
         */
        double numCorrectGuesses = 0.0;    //Stores the number of probabilities correctly calculated
        double numGuesses = 0.0;           //Stores the number of files read
        double numTruePositives = 0.0;     //Stores the number of correct probabilities calculated
        double numFalsePositives = 0.0;    //Stores the number of correct probabilities calculated

        //Storing the files in the ham directory into a list of files.
        List<File> hamTest = Files.walk(Paths.get(strDirectory + "/test/ham")).filter(Files::isRegularFile)
                .map(Path::toFile).collect(Collectors.toList());

        /**
         * For loop to loop over all the files in the ham directory
         * and to calculate and store the file name, directory name (ham) and spam
         * probability of the files in the ham directory. Stores these in the array list spamResults.
         */
        for(int i = 0; i < hamTest.size(); i++) {
            File currentFile = hamTest.get(i);              //Gets the current file that the ham directory is going to read
            Scanner lineReader = new Scanner(currentFile);  //A scanner variable that is used to read line by line of a file
            double nExponent = 0.0;                         //A double variable to store the exponent for the spam probability equation

            /**
             * Reads the file in the ham directory line by line
             * and then reads each word in the line. Uses
             * the probability stored in a Map from the Training class to
             * calculate the probability that a file is spam.
             */
            while (lineReader.hasNext()) {
                String strLine = lineReader.nextLine();        //Stores a line in the file of the ham directory.
                String[] strWords = strLine.split(" ");  //Stores the words in the line of the file.

                /**
                 *For loop to read over the words in a line of a file
                 * and to calculate the spam probability of that file
                 */
                for (int j = 0; j < strWords.length; j++) {
                    strWords[j] = strWords[j].toLowerCase();  //Converting the words in the line to all lower case
                    /**
                     * Checks to see if word read in the file is the probability map from the Train class
                     * and if the probability of the word is below one in the probability map from the Train
                     * class, then the exponent is calculated.
                     */
                    if (trainMap.containsKey(strWords[j]) && trainMap.get(strWords[j]) < 1) {
                        /**
                         * Check to see if the word is in the probability map
                         * and if the probability of the word is 1, change the probability
                         * to 0.9 so, the calculation for the exponent could be done.
                         */
                        if (trainMap.get(strWords[j]) == 1) {
                            trainMap.put((strWords[j]), 0.9);
                        }
                        //Calculating the exponent for the spam probability equation using the probability of the word from the Train class
                        nExponent += (Math.log(1.0 - trainMap.get(strWords[j])) - Math.log(trainMap.get(strWords[j])));
                    }
                }

            }
            lineReader.close(); //Closes the scanner
            //Calculates the probability that a file in the ham directory is spam
            spamProb = 1.0 / (1.0 + Math.pow(Math.E, nExponent));

            /**
             * Incrementing the number of correct guesses and
             * number of guesses if spam probability of the file is below 0.5
             */
            if(spamProb < 0.5){
                numCorrectGuesses++;
                numGuesses ++;
            }
            /**
             * Incrementing the number of probabilities above 0.5 (numFalsePositives)
             * and number of guesses if spam probability of the file is below 0.5
             */
            else{
                numFalsePositives ++;
                numGuesses ++;
            }
            //Adding the file name, actual class (ham) and spam probability of the file to the spamResults list
            spamResults.add(new TestFile(currentFile.getName(), "ham", spamProb));
        }

        //Resetting the spam probability to zero for the probability calculations of the files in the spam directory
        spamProb = 0.0;

        //Storing the files in the ham directory into a list of files.
        List<File> spamTest = Files.walk(Paths.get(strDirectory + "/test/spam")).filter(Files::isRegularFile)
                .map(Path::toFile).collect(Collectors.toList());

        for (int i = 0; i < spamTest.size(); i++) {
            File currentFile = spamTest.get(i);             //Gets the current file that the spam directory is going to read
            Scanner lineReader = new Scanner(currentFile);  //A scanner variable that is used to read line by line of a file
            double nExponent = 0.0;                         //A double variable to store the exponent for the spam probability equation

            /**
             * Reads the file in the spam directory line by line
             * and then reads each word in the line. Uses
             * the probability stored in a Map from the Training class to
             * calculate the probability that a file is spam.
             */
            while (lineReader.hasNext()) {
                String strLine = lineReader.nextLine();          //Stores a line in the file of the spam directory.
                String[] strWords = strLine.split(" ");    //Stores the words in the line of the file.
                /**
                 *For loop to read over the words in a line of a file
                 * and to calculate the spam probability of that file
                 */
                for (int j = 0; j < strWords.length; j++) {
                    strWords[j] = strWords[j].toLowerCase();
                    if (trainMap.containsKey(strWords[j]) && trainMap.get(strWords[j]) < 1) {
                        /**
                         * Check to see if the word is in the probability map
                         * and if the probability of the word is 1, change the probability
                         * to 0.5 Bayesian model predicts 50% of emails are assumed to be spam.
                         */
                        if (trainMap.get(strWords[j]) == 0){
                            trainMap.put((strWords[j]),0.5);
                        }
                        /**
                         * Check to see if the word is in the probability map
                         * and if the probability of the word is 1, change the probability
                         * to 0.5 so, the calculation for the exponent could be done.
                         */
                        if (trainMap.get(strWords[j]) == 1){
                            trainMap.put((strWords[j]),0.9);
                        }
                        nExponent += (Math.log(1.0 - trainMap.get(strWords[j])) - Math.log(trainMap.get(strWords[j])));
                    }
                }

            }
            lineReader.close();
            spamProb = 1.0 / (1.0 + Math.pow(Math.E, nExponent));

            /**
             * Incrementing the number of correct guesses, number of correct spam
             * identification and number of guesses if spam probability of the file is above 0.5
             */
            if(spamProb > 0.5){
                numCorrectGuesses++;
                numGuesses ++;
                numTruePositives ++;
            }
            else{
                numGuesses ++;
            }
            //Adding the file name, actual class (ham) and spam probability of the file to the spamResults list
            spamResults.add(new TestFile(currentFile.getName(), "spam", spamProb));
        }

        /**
         * Calculating the accuracy of the spam detector by
         * dividing number of correct spam identifications by number of files read
         */
        this.accuracy = numCorrectGuesses/numGuesses;
        /**
         * Calculating the precision of the spam detector by
         * dividing number of correct spam identifications by number of incorrect spam identifications
         * plus the number of correct spam identifications
         */
        //Calculating the precision of the spam detector
        this.precision = numTruePositives/(numFalsePositives + numTruePositives);

        return spamResults;  //Returning the spamResults list which stores the TestFile objects
    }
}
