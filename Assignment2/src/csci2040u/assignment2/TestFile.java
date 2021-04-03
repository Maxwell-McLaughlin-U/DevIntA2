package csci2040u.assignment2;

import java.text.DecimalFormat;

/**
 * A public class that instantiates the variables used in the main to create the table columns
 */
public class TestFile {
    private String filename;            //A string to store the name of a file
    private double spamProbability;     //A double to store the spam probability of a file
    private String actualClass;         //A string to store what folder it is in, in the data/test directory(spam or ham)

    /**
     * A constructor for the TestFile class to instantiate the private variables
     * @param filename          A string to store the name of a file
     * @param actualClass       A string to store what folder it is in, in the data/test directory(spam or ham)
     * @param spamProbability   A double to store the spam probability of a file
     */
    public TestFile(String filename, String actualClass, double spamProbability) {
        this.filename = filename;
        this.actualClass = actualClass;
        this.spamProbability = spamProbability;
    }
    //Get methods for private variables fileName, spamProbability and actualClass
    public String getFilename(){ return this.filename; }
    public double getSpamProbability(){ return this.spamProbability; }
    public String getActualClass(){return this.actualClass;}
    //get method to format the decimal value of the spam probability
    public String getSpamProbRounded(){
        DecimalFormat df = new DecimalFormat("0.00000000");
        return df.format(this.spamProbability);
    }
    //Set methods for the private variables fileName, spamProbability and actualClass
    public void setFilename(String value) {this.filename = value;}
    public void setSpamProbability(double val) {this.spamProbability = val;}
    public void setActualClass(String value) {this.actualClass = value;}
}
