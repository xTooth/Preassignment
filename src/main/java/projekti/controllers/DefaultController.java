package projekti.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import DataAndDataHandling.Data;
import DataAndDataHandling.DataHandler;

@Controller
public class DefaultController {

    private HashMap<String, Data> packageMap;
    private ArrayList<String> packages;
    private DataHandler handler;

    public void startup() throws FileNotFoundException, IOException {
        this.handler = new DataHandler();
        this.packageMap = handler.createDataMap();
        this.packages = handler.createList();
    }

    @GetMapping("/")
    public String index(Model model) throws IOException {
        if (this.packageMap == null) {
            startup();
        }
        model.addAttribute("data", packages);
        return "index";
    }

    @GetMapping("/package/{data}")
    public String pkg(Model model, @PathVariable String data) throws IOException {
        if (this.packageMap == null) {
            startup();
        }
        ArrayList<String> exists = new ArrayList<>();
        ArrayList<String> doesntExist = new ArrayList<>();
        for (String x : this.packageMap.get(data).getDependencies()) {
            if(this.packageMap.containsKey(x)){
                exists.add(x);       
            }else{
                doesntExist.add(x);
            }
        }
        model.addAttribute("data", this.packageMap.get(data));
        model.addAttribute("exists",exists);
        model.addAttribute("doesntExist",doesntExist);
        return "pkg";
    }

}
