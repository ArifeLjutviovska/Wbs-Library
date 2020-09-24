package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.DemoApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class SearchController {

    @GetMapping("/search")
    public String search(String searchParametar){
        return DemoApplication.search(searchParametar);
    }
}
