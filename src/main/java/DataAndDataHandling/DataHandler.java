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

    public ArrayList<String> createList() {
        return list;
    }

    private void resetBR() throws FileNotFoundException {
        bufferReader = new BufferedReader(new FileReader(file));
    }

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
                String[] possibilities = nextLine.split(": ")[1].split(" ");
                for (String s : possibilities) {
                    if (!data.get(pkg).getDependencies().contains(s)) {
                        if (data.containsKey(s)) {

                            data.get(s).addSupport(pkg);
                            data.get(pkg).addDependency(s);

                        } else {

                           // data.get(pkg).addDependency(s);

                        }
                    }
                }
            }
        }

        bufferReader.close();

    }

}
