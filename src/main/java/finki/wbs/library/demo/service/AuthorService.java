package finki.wbs.library.demo.service;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Author;
import finki.wbs.library.demo.model.exceptions.InvalidAuthorNameException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AuthorService {
    private final List<Author> authors= DemoApplication.authors;

    public List<Author> getAuthors(){

        return this.authors;
    }
    public Author getOneAuthor(String name){
        return this.authors.stream().filter(a->a.getName().equals(name)).findFirst().orElseThrow(InvalidAuthorNameException::new);
    }
}
