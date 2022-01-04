package project;

import java.util.*;

public class cichelliHashTable {

    public ArrayList<Integer> hashValues = new ArrayList<>();
    public static HashMap<String, Integer> wordsValuesMap = new HashMap<>();
    public static HashMap<String, Integer> testValuesMap = new HashMap<>();
    public static HashMap<Character, Integer> letterValuesMap = new HashMap<>();
    public static HashMap<Character, Integer> gValuesMap = new HashMap<>();

    static Stack wordStack = new Stack();
    public static Hashtable<Integer, String> cichelliTable = new Hashtable<Integer, String>();

    static ArrayList<String> words;

    public static int g = 0; // g value
    public static int max = 0; // maximum value

    public cichelliHashTable(ArrayList<String> words) {
        // constructor passing the keyword ArrayList
        this.words = words;
    }

    // calculates the hash value of a word
    public int hashIt(String word, int g_first, int g_last) {
        char first = word.charAt(0);
        char last = word.charAt(word.length() - 1);
        return (word.length() + (g_first) + (g_last)) % words.size();
    }

    // count presence of a letter in the first and last letter placeholder
    // results put in lettervalues hashmap
    // then words are graded using the first and last letter valuefrequencies
    // then result is then stored in wordvaluesmap

    public void CountLetters() {

        for (int i = 0; i < words.size(); i++) {
            char first = words.get(i).charAt(0);
            char last = words.get(i).charAt(words.get(i).length() - 1);

            if (!letterValuesMap.containsKey(first)) {
                letterValuesMap.put(first, 1);
            } else {
                letterValuesMap.put(first, letterValuesMap.get(first) + 1);
            }
            if (!letterValuesMap.containsKey(last)) {
                letterValuesMap.put(last, 1);
            } else {
                letterValuesMap.put(last, letterValuesMap.get(last) + 1);
            }
        }
        // sort list to have highest values first
        letterValuesMap = sortByValues(letterValuesMap);

        // find the first+last word value
        for (int i = 0; i < words.size(); i++) {
            char first = words.get(i).charAt(0);
            char last = words.get(i).charAt(words.get(i).length() - 1);
            int sum = letterValuesMap.get(first) + letterValuesMap.get(last);
            wordsValuesMap.put(words.get(i), sum);
        }
        wordsValuesMap = sortHashMapByValues(wordsValuesMap);
    }

    // sorts the Hashmap by values in descending order
    public static LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {

        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        java.util.Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            java.util.Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    // populate the wordStack starting with the lowest in wordValuesMap
    public Stack populateStack(HashMap wordsValuesMap) {
        for (Object key : wordsValuesMap.keySet()) {
            wordStack.push(key);
        }
        return wordStack;
    }

    public boolean cichelli(Stack wordStack) {
        // take max valueas the number of words divided by 2
        max = (int) ((long) words.size() / 2);

        while (!wordStack.isEmpty()) {
            String word = (String) wordStack.pop();
            char first = word.charAt(0);
            char last = word.charAt(word.length() - 1);
            int wordLength = word.length();
            // check for both first and last letter have g-values
            if (gValuesMap.containsKey(first) && gValuesMap.containsKey(last)) {

                // check hash value for word is valid
                if (!hashValues.contains(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)))) {
                    hashValues.add(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)));
                    if (cichelli(wordStack)) {
                        cichelliTable.put(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)), word);
                        return true;
                    } else {
                        hashValues.remove(hashValues.size() - 1); // remove the last object in the arrray
                    }
                } // push word back on top of wordStack
                wordStack.push(word);
                return false;
            }
            // if letter not assigned g-value and first != last letter
            else if (!gValuesMap.containsKey(first) && !gValuesMap.containsKey(last) && first != last) {
                for (int n = 0; n <= max; n++) {
                    for (int m = 0; n <= max; m++) {
                        int g_first = m;
                        int g_last = n;
                        // add the new g values to the gvaluesMAp
                        gValuesMap.put(first, g_first);
                        gValuesMap.put(last, g_last);
                        // check hash value for word is valid
                        if (!hashValues.contains(hashIt(word, g_first, g_last))) {
                            hashValues.add(hashIt(word, g_first, g_last));
                            if (cichelli(wordStack)) {
                                cichelliTable.put(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)), word);
                                return true;
                            } else {
                                hashValues.remove(hashValues.size() - 1); // remove the last object in the arrray
                            }
                        }
                    }
                }
                // reset g-value for letters as unassigned
                gValuesMap.remove(first);
                gValuesMap.remove(last);
                // push word back on top of wordStack
                wordStack.push(word);
                return false;
            }
            // only one letter assigned g-value or first = last letter
            else {
                char missingChar = '!';
                for (int m = 0; m <= max; m++) {
                    // if first and last letters are same, assign them to m
                    if (first == last) {
                        int g_same_letter = m;
                        missingChar = first;

                    }
                    // if they're two different letters if first letter is not in map assign and
                    // retrieve last from map
                    else if (!gValuesMap.containsKey(first)) {
                        int g_first = m;
                        missingChar = first;
                        int g_last = gValuesMap.get(last);
                        // new g value to the gvaluesMap
                        gValuesMap.put(first, g_first);
                    } else if (!gValuesMap.containsKey(last)) {
                        int g_first = gValuesMap.get(first);
                        int g_last = m;
                        missingChar = last;
                    }
                    // add the new g value to the gvaluesMap or update
                    gValuesMap.put(missingChar, m);
                    // check if hash value for word is valid
                    if (!hashValues.contains(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)))) {
                        hashValues.add(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)));
                        if (cichelli(wordStack)) {
                            cichelliTable.put(hashIt(word, gValuesMap.get(first), gValuesMap.get(last)), word);
                            return true;
                        } else {
                            hashValues.remove(hashValues.size() - 1); // remove the last object in the arrray
                        }
                    }
                }
                // reset g-value for letters as unassigned
                gValuesMap.remove(first);
                gValuesMap.remove(last);
                // push word back on top of wordStac and return false
                wordStack.push(word);
                return false;
            }
        }
        return true; // empty stack
    }

    // function to get g valuesmap to the cichellie table class
    public HashMap<Character, Integer> getGValues() {
        return gValuesMap;
    }

    private static HashMap sortByValues(HashMap map) {
        List<?> list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // copying the sorted list in HashMap inkedHashMap to preserve the insertion
        // order
        HashMap sortedHashMap = new LinkedHashMap();
        for (java.util.Iterator it = list.iterator(); ((java.util.Iterator) it).hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

}