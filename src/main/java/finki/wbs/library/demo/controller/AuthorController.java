package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.model.Author;
import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.service.AuthorService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> getAuthors(){
        return this.authorService.getAuthors();
    }

    @GetMapping("/{name}")
    public Author getOneAuthor(@PathVariable("name") String name){
        return this.authorService.getOneAuthor(name);
    }

}
