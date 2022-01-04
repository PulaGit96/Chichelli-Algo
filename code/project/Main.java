package project;

import java.io.*;
import java.time.*;
import java.util.*;

public class Main {
    public static ArrayList<Integer> hashValues = new ArrayList<>();
    static ArrayList<String> words = new ArrayList<String>(); // ArrayList to store keywords from keywords file

    // HashMaps to chichelli functiuoins
    public static HashMap<String, Integer> wordsValuesMap = new HashMap<>();
    public static HashMap<String, Integer> testValuesMap = new HashMap<>();
    public static HashMap<Character, Integer> letterValuesMap = new HashMap<>();
    public static HashMap<Character, Integer> gValuesMap = new HashMap<>();
    static Stack wordStack = new Stack();

    public static int g = 0; // g value
    public static int max = 0; // maximum value

    public static String textFileName; // name of the keyword file
    public static String keyFileName;// name of the word file

    public static BufferedReader reader;//

    public static void main(String args[]) {

        Scanner user = new Scanner(System.in);
        System.out.println("Enter name of Keyword File: ");
        keyFileName = user.nextLine().trim();

        try {
            reader = new BufferedReader(new FileReader(keyFileName));
            String line = reader.readLine().trim();
            if (line == null) {
                // keyword file is empty
                System.out.println("keyword file is empty");
                // successful termination.
                System.exit(0);
            }
            // adding keywords to the ArrayList
            while (line != null) {
                String[] wordsLine = line.split(" ");
                for (String str : wordsLine) {
                    words.add(str); // consider lowercase to avoid case sensitivity
                    // words.add(str.toLowerCase());
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // keyword file not found
            System.out.println("keyword file not found");
            // successful termination.
            System.exit(0);

        } catch (IOException e) {
            // IO error
            System.out.println("IO Error");
            // successful termination.
            System.exit(0);
        }

        System.out.println("Enter name of text file: ");

        textFileName = user.nextLine().trim();

        // time countdown begins here
        Instant start = Instant.now();

        // Building a minimal perfect hash table
        cichelliHashTable cht = new cichelliHashTable(words);
        cht.CountLetters();
        cht.populateStack(cht.wordsValuesMap);
        cht.cichelli(cht.wordStack);

        ArrayList<String> keyWords = cht.words;
        gValuesMap = cht.getGValues();

        ArrayList<String> scannedText = new ArrayList<String>();
        ArrayList<String> keyWordsCollected = new ArrayList<String>(); // to save keywords
        Integer numberOfLines = 0; // save number of lines read

        BufferedReader reader;

        // reading text file
        try {

            reader = new BufferedReader(new FileReader(textFileName));
            String line = reader.readLine().trim();

            if (line == null) {
                // check if word file is empty
                System.out.println("word file is empty");
                // successful termination.
                System.exit(0);
            }

            while (line != null) {

                String[] words = line.split(" ");
                for (String str : words) {
                    scannedText.add(str); // consider lowercase to avoid case sensitivity
                    //scannedText.add(str.toLowerCase());
                }
                numberOfLines++; // increment number of lines read
                line = reader.readLine();
            }
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("IO Error file reader didnt closed succefully");
            }

        } catch (FileNotFoundException e) {
            // word file not found
            System.out.println("word file not found");
            // successful termination.
            System.exit(0);

        } catch (IOException e) {
            // IO error
            System.out.println("IO Error");
            // successful termination.
            System.exit(0);
        }

        // print results

        System.out.println("     Statistics results:\n ...................................");
        System.out.println("Total lines read: " + numberOfLines);
        System.out.println("Total words read: " + scannedText.size());
        System.out.println("Breakdown by Keyword");
        for (String word : scannedText) {

            // populate the KeyWOrds colected array
            if (!word.isEmpty()) {
                char first = word.charAt(0);
                char last = word.charAt(word.length() - 1);

                // check if both first and last letter have been assigned g-values

                if (cht.gValuesMap.containsKey(first) && cht.gValuesMap.containsKey(last)) {
                    cht.cichelliTable.get(cht.hashIt(word, cht.gValuesMap.get(first), cht.gValuesMap.get(last)));

                    if (word.equals(cht.cichelliTable
                            .get(cht.hashIt(word, cht.gValuesMap.get(first), cht.gValuesMap.get(last))))) {
                        keyWordsCollected.add(word);
                    }
                }
            }
        }
        for (String key : keyWords) {
            // compute number of key words in the file
            System.out.println("      " + key + " : " + Collections.frequency(keyWordsCollected, key));
        }

        System.out.println("Total keywords: " + keyWordsCollected.size());

        // end of execution time
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println("Execution time: " + timeElapsed + " milliseconds");

    }

}
