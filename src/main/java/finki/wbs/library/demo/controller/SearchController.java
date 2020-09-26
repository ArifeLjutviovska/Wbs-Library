package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Book;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class SearchController {

    @GetMapping("/search")
    public List<Book> search(String searchParametar){
        return DemoApplication.search(searchParametar);
    }
}
