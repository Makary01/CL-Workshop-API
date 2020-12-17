package pl.coderslab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @RequestMapping("/helloBook")
    public Book helloBook() throws IOException, SQLException {
        MemoryBookService mBS = MemoryBookService.getMemoryBookService();
        List<Book> books = mBS.readAllBooks();
        return books.get(0);
    }


}

