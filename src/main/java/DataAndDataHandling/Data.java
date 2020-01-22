/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAndDataHandling;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Toothy
 */
public class Data {

    private String name;
    private String description;
    private ArrayList<String> dependencies;
    private ArrayList<String> supports;

    public Data(String name, String desc) {
        this.name = name;
        this.description = desc;
        this.dependencies = new ArrayList<>();
        this.supports = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getDependencies() {
        return dependencies;
    }

    public ArrayList<String> getSupports() {
        return supports;
    }

    public void addDependency(String dep) {
        dependencies.add(dep);
        Collections.sort(dependencies);
    }

    public void addSupport(String sup) {
        supports.add(sup);
        Collections.sort(supports);
    }

}
