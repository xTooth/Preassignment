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

    private HashMap<String, Data> data;
    private ArrayList<String> packages;
    private DataHandler handler;

    public void startup() throws FileNotFoundException, IOException {
        this.handler = new DataHandler();
        this.packages = handler.createList();
        this.data = handler.createDataMap();
    }

    @GetMapping("/")
    public String index(Model model) throws IOException {
        if (this.data == null) {
            startup();
        }
        model.addAttribute("data", packages);
        return "index";
    }

    @GetMapping("/package/{data}")
    public String pkg(Model model, @PathVariable String data) throws IOException {
        if (this.data == null) {
            startup();
        }
        model.addAttribute("data",this.data.get(data));
        model.addAttribute("allpgk",this.data);
        return "pkg";
    }

}
