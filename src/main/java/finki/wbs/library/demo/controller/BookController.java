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
    @GetMapping("/{name}/creators")
    public List<String> getBookCreators(@PathVariable("name") String name){
        return this.bookService.getCreators(name);

    }

    @GetMapping("/{name}/contributors")
    public List<String> getBookContributors(@PathVariable("name") String name){
        return this.bookService.getContributors(name);

    }

    @GetMapping("/{name}/availability")
    public boolean getBookAvailability(@PathVariable("name") String name){
        Book book=this.bookService.getOneBook(name);
        return book.availability();

    }
    @PatchMapping("/{name}/editStock")
    public Book editBookStock(@PathVariable("name") String name,int stock){
        Book book=this.bookService.getOneBook(name);
        book.setStock(stock);
        return book;

    }


}
