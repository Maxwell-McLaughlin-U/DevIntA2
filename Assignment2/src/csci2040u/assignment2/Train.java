package csci2040u.assignment2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.lang.String;

/**
 * An abstract class that is used in the TestFile class
 * to train the spam detector to determine if a file is spam
 */
public abstract class Train{
    /**
     * A static function that returns a map of words and number of files that contain that word
     * @param fileList is a list of files from a path
     * @return a hash map of words and its frequency in that directory.
     * @throws IOException
     */
    private static Map<String,Double> getFreq(List<File> fileList) throws IOException{
        String strRegex = "^[a-zA-Z]+$";    //A regex to see if a word is valid

        //A hash map to store the words and the num of files that contain that word  in the directory.
        Map<String, Double> trainFrequency = new HashMap<String, Double>();

        /**
         * Going through the files in the directory and reading them word by word to
         * store the words and number of files the word appears in, in the directory.
         */
        for(int i = 0; i<fileList.size();i++){
            File currentFile = fileList.get(i);                //Stores a file from the directory
            Scanner lineReader = new Scanner(currentFile);     //A scanner to store the line it's reading from the file

            //A map that counts the num of words in a specific file and is used to add to the trainFreqency map
            Map<String, Boolean> currentMap = new HashMap<String, Boolean>();

            /**
             * Read through the file line by line and split the lines into words.
             * It is then going to store the words into the currentMap if the word is not already stored.
             */
            while(lineReader.hasNextLine()){
                String strLine = lineReader.nextLine();        //Store the line that is being read in the file
                String[] strWords = strLine.split(" ");  //Stores the array of words in a line of a file

                /**
                 * For loop goes through the words in the array strWords
                 * and adds it to the map if the word is not already there and
                 * if the word is a valid word (so matches the regex).
                 */
                for(int j = 0;j < strWords.length;j++){
                    //Converts the words to lower case so same word isn't counted as two separate words
                    strWords[j] = strWords[j].toLowerCase();
                    if ((strWords[j].length() > 1) && (strWords[j].matches(strRegex))){
                        currentMap.putIfAbsent(strWords[j], true);
                    }
                }
            }
            /**
             * For loop to go through the current map and adds and increments the
             * frequency of that word in the trainFrequency map
             */
            for(Map.Entry<String,Boolean> entry : currentMap.entrySet()){
                if(trainFrequency.containsKey(entry.getKey())){
                    trainFrequency.put(entry.getKey(),trainFrequency.get(entry.getKey()) + 1);
                }else{
                    trainFrequency.put(entry.getKey(),1.0);
                }
            }

            lineReader.close(); //Closes the scanner

        }

        //A double variable to store the number of files in the directory
        double numOfFiles = fileList.size();

        /**
         * For loop to convert the number of files that contain a word to the
         * probability that a word appears in the file.
         */
        for(Map.Entry<String,Double> entry : trainFrequency.entrySet()){
            trainFrequency.put(entry.getKey(), entry.getValue() / numOfFiles);
        }

        return(trainFrequency);  //Returning the map of probabilities of a word in a file
    }

    /**
     * A static function to return a map of probabilities that a file is spam given
     * that the word is spam.
     * @param strPath is a string that holds the path to the directory that the files are being read (the path to train directory)
     * @return a map of probabilities that a file is spam given that the word is spam.
     * @throws IOException
     */
    public static Map<String,Double> getSpamProb(String strPath) throws IOException{
        //A hash map to store a word and the probability that the file is spam given that the stored word is spam
        Map<String,Double> retMap = new HashMap<String,Double>();

        /**
         * Creates a list of files by reading the files in a certain path
         * spamFileList being a path to the data/train/spam
         * hamFileList being a path to the data/train/ham
         * hamFileList2 being a path to the data/train/ham2
         */
        List<File> spamFileList = Files.walk(Paths.get(strPath + "/spam")).filter(Files::isRegularFile)
                .map(Path::toFile).collect(Collectors.toList());

        List<File> hamFileList = Files.walk(Paths.get(strPath + "/ham")).filter(Files::isRegularFile)
                .map(Path::toFile).collect(Collectors.toList());

        List<File> hamFileList2 = Files.walk(Paths.get(strPath + "/ham2")).filter(Files::isRegularFile)
                .map(Path::toFile).collect(Collectors.toList());

        //Adding the list of files in hamFileList2 to hamFileList
        hamFileList.addAll(hamFileList2);

        /**
         * Declaring a map to hold the probabilities of a word from the getFreq function in two maps,
         * one for the files in the spam directory and the other for the files in the ham directory
         */
        Map<String,Double> spamMap = getFreq(spamFileList);
        Map<String,Double> hamMap = getFreq(hamFileList);

        //A map to store the words so no keys (no words) are unaccounted for
        Map<String,Boolean> legendKeyMap = new HashMap<String,Boolean>();

        /**
         * For loop to go through the spamMap and adding all the keys from the
         * spamMap to the legendKeyMap.
         */
        for(Map.Entry<String,Double> entry: spamMap.entrySet()){
            legendKeyMap.put(entry.getKey(), true);
        }
        /**
         * For loop to go through the hamMap and adding all the keys from the
         * hamMap to the legendKeyMap.
         */
        for(Map.Entry<String,Double> entry: hamMap.entrySet()){
            legendKeyMap.put(entry.getKey(), true);
        }

        /**
         * For loop to go through the legendKeyMap and
         * calculate the probability that a file is spam
         */
        for(Map.Entry<String,Boolean> entry: legendKeyMap.entrySet()){
            double probSpam = 0.0;  //Double variable to store the probability if a word is spam
            double probHam = 0.0;   //Double variable to store the probability if a word is ham

            //If the word is in the spamMap then get the probability
            if(spamMap.containsKey(entry.getKey())){
                probSpam = spamMap.get(entry.getKey()) ;
            }
            //If the word is in the hamMap then get the probability
            if(hamMap.containsKey(entry.getKey())){
                probHam = hamMap.get(entry.getKey());
            }
            /**
             * Add the word and calculate the probability that the file is spam given that the word
             * contained in the map is a spam
             */
            retMap.put(entry.getKey(),(probSpam)/(probSpam + probHam));
        }
        return retMap; //Returning the map of probabilities that a file is spam given that the word is spam.
    }
}