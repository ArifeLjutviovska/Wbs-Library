package finki.wbs.library.demo.model;

import java.util.List;

public class Book {

    private String Name;
    private String Title;
    private List<String> creators;
    private List<String> contributors;
    private String isbn;
    private String BNB;
    private String datePublished;
    private int stock;

    public Book() {
    }

    public Book(String name, String title, List<String> creators, List<String> contributors, String isbn, String BNB, String datePublished,int stock) {
        Name = name;
        Title = title;
        this.creators = creators;
        this.contributors = contributors;
        this.isbn = isbn;
        this.BNB = BNB;
        this.datePublished = datePublished;
        this.stock=stock;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<String> getCreators() {
        return creators;
    }

    public void setCreators(List<String> creators) {
        this.creators = creators;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBNB() {
        return BNB;
    }

    public void setBNB(String BNB) {
        this.BNB = BNB;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public boolean availability(){
        return this.stock>0;
    }

    public Book reserveBook(){
        this.stock-=1;
        return this;
    }
}
