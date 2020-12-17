package pl.coderslab.controller;

import org.springframework.web.bind.annotation.*;
import pl.coderslab.model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private MemoryBookService mBS = MemoryBookService.getMemoryBookService();

    @RequestMapping("/helloBook")
    public Book helloBook() throws IOException, SQLException {

        return mBS.readBookById(2L);
    }

    @GetMapping(value = "")
    public List<Book> books(){
        return mBS.readAllBooks();
    }

    @GetMapping(value = "/{id}")
    public Book book(@PathVariable("id") long id){
        return mBS.readBookById(id);
    }



}

