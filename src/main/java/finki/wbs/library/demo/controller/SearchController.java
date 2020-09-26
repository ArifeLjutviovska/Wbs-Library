package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.service.AuthorService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class SearchController {




    @GetMapping("/searchByName")
    public List<Book> searchByName(String searchParametar){

        return DemoApplication.searchByBookName(searchParametar);
    }
    @GetMapping("/searchByAuthorName")
    public List<Book> searchByAuthorName(String authorName){

        return DemoApplication.searchByAuthorName(authorName);
    }
    @GetMapping("/searchByContributorName")
    public List<Book> searchByContributorName(String conName){

        return DemoApplication.searchByContributorName(conName);
    }

   @GetMapping("/OrderByLatestBooks")
    public List<Book> orderByLatest(){
       return DemoApplication.orderByLatest();
   }
    @GetMapping("/OrderByOldestBooks")
    public List<Book> orderByOldest(){
        return DemoApplication.orderByOldest();
    }
    @GetMapping("/searchBookByYear")
    public List<Book> searchByYear(String year) {

        return DemoApplication.searchByYear(year);
    }




}
