/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAndDataHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Toothy
 */
public class DataHandler {

    private final File file;
    BufferedReader bufferReader;
    private ArrayList<String> list;
    private HashMap<String, Data> data;

    public DataHandler() throws FileNotFoundException {

        // change this dir to your actual status.real for a real time readout. 
        file = new File("src/main/java/projekti/data/status.real");
        list = new ArrayList<>();
        data = new HashMap<>();
        resetBR();
    }
    
    // creates a HashMap of String, data pairs and an ArrayList for alphabetical order. 
    public HashMap<String, Data> createDataMap() throws IOException {
        String nextLine;
        String pkg = "";
        String desc = "-";

        while ((nextLine = bufferReader.readLine()) != null) {

            String[] row = nextLine.split(" ");
            if (row[0].equals("Package:")) {

                // in case there was no description create node with empty desc.
                if (!pkg.equals("")) {
                    data.put(pkg, new Data(pkg, "-"));
                }
                pkg = row[1];
                list.add(row[1]);
            }

            //description found.
            if (row[0].equals("Description:")) {
                desc = nextLine.split(":")[1];

                // copy all of description
                while ((nextLine = bufferReader.readLine()) != null) {

                    if (nextLine.length() == 0) {
                        break;
                    }
                    row = nextLine.split(" ");
                    if (row[0].contains(":")) {
                        break;
                    } else {
                        desc += "\n" + nextLine;
                    }
                }

                // both found, add them to the HashMap.
                data.put(pkg, new Data(pkg, desc));
                pkg = "";
                desc = "-";
            }
        }
        bufferReader.close();
        Collections.sort(list);
        findDependencies();
        return data;

    }

    // "creates" the list, functions as a getter in reality
    public ArrayList<String> createList() {
        return list;
    }

    //resets Buffer reader
    private void resetBR() throws FileNotFoundException {
        bufferReader = new BufferedReader(new FileReader(file));
    }
    
    // adds all dependencies to given package in the hashMap.
    private void findDependencies() throws FileNotFoundException, IOException {
        
        resetBR();
        
        String nextLine;
        String pkg = "";
        
        while ((nextLine = bufferReader.readLine()) != null) {
            String[] row = nextLine.split(" ");
            if (row[0].equals("Package:")) {
                pkg = row[1];
            }
            //dependecies found.
            if (row[0].equals("Depends:")) {
                
                //Split of "Depends: from actual dependencies, and split separate dependencies to own lines
                String[] possibilities = nextLine.split(": ")[1].split(",");
                
                // an int to make sure we dont read empty characters as a line can begin with a " ".
                int wordRead = 0;
                
                String[] words;
                for (int i = 0; i < possibilities.length; i++) {
                    if (i > 0) {
                        wordRead = 1;
                    }
                    if (possibilities[i].contains(" | ")) {
                        words = possibilities[i].split("\\|");
                        addPotentialDependencies(words, wordRead, pkg);
                    } else {
                        words = possibilities[i].split(" ");
                        addForcedDependencies(words, wordRead, pkg);
                    }
                }
            }
        }
        bufferReader.close();
    }
    
    // helpermethod for findDependencies. adds all "forced" dependencies.
    private void addForcedDependencies(String[] words, int wordRead, String pkg) {
        
        String word = words[wordRead].trim();
        
        if (!data.get(pkg).getDependencies().contains(word)) {
            if (data.containsKey(word)) {

                data.get(word).addSupport(pkg);
                data.get(pkg).addDependency(word);

            } else {
                
                // adds optional dependency that doesnt exist among the files packages.
                data.get(pkg).addDependency(word);
            }
        }
    }
    // helpermethod for findDependencies. adds all optional dependencies. ( dependencies separated by "|"
    private void addPotentialDependencies(String[] words, int wordRead, String pkg) {
        for (String s : words) {
            
            //gets rid of version numbers in case they exist
            String word = s.trim().split(" ")[0];
            
            if (!data.get(pkg).getDependencies().contains(word)) {
                if (data.containsKey(word)) {

                    data.get(word).addSupport(pkg);
                    data.get(pkg).addDependency(word);

                } else {
                    data.get(pkg).addDependency(word);
                }
            }
        }
    }

}
