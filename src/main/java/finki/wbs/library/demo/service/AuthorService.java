package finki.wbs.library.demo.service;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Author;
import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.model.exceptions.InvalidAuthorNameException;
import finki.wbs.library.demo.model.exceptions.InvalidBookNameException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final List<Author> authors= DemoApplication.authors;
    private final List<Book> books= DemoApplication.books;

    public List<Author> getAuthors(){

        return this.authors;
    }
    public Author getOneAuthor(String name){
        return this.authors.stream().filter(a->a.getName().equals(name)).findFirst().orElseThrow(InvalidAuthorNameException::new);
    }

}
