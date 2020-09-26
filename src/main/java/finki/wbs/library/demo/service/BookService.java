package finki.wbs.library.demo.service;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Author;
import finki.wbs.library.demo.model.Book;
import finki.wbs.library.demo.model.Contributor;
import finki.wbs.library.demo.model.exceptions.InvalidBookNameException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookService {

    private final List<Book> books= DemoApplication.books;


    public BookService() {
    }

   public List<Book> getBooks(){
        return this.books;
   }
   public Book getOneBook(String name){
        return this.books.stream().filter(b->b.getName().contains(name)).findFirst().orElseThrow(InvalidBookNameException::new);
   }




}
