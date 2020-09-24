package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.service.BookService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public List<Book>  getBooks(){
        return this.bookService.getBooks();
    }

    @GetMapping("/{name}")
    public Book getOneBook(@PathVariable("name") String name){
        return this.bookService.getOneBook(name);

    }


}
