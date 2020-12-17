package pl.coderslab.controller;

import org.springframework.web.bind.annotation.*;
import pl.coderslab.model.Book;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    private MemoryBookService mBS = MemoryBookService.getMemoryBookService();


    @GetMapping(value = "")
    public List<Book> readAllBooks() {
        return mBS.readAllBooks();
    }

    @GetMapping(value = "/{id}")
    public Book readBook(@PathVariable("id") long id) {
        return mBS.readBookById(id);
    }

//    @PutMapping("")
//    public Book updateBook(HttpServletRequest request){
//        Book bookToUpdate = getBookFromRequest(request);
//        bookToUpdate.setId(Long.parseLong(request.getParameter("id")));
//        return mBS.updateBook(bookToUpdate);
//    }
//
//    @PostMapping("")
//    public Book createBook(HttpServletRequest request){
//        Book bookToCreate = getBookFromRequest(request);
//        return mBS.createBook(bookToCreate);
//    }
//
//    private Book getBookFromRequest(HttpServletRequest request){
//        Book bookToReturn = new Book();
//        bookToReturn.setIsbn(request.getParameter("isbn"));
//        bookToReturn.setTitle(request.getParameter("title"));
//        bookToReturn.setAuthor(request.getParameter("author"));
//        bookToReturn.setPublisher(request.getParameter("publisher"));
//        bookToReturn.setType(request.getParameter("type"));
//        return  bookToReturn;
//    }

    @PutMapping("")
    public Book updateBook(@RequestBody Book book){
        return mBS.updateBook(book);
    }

    @PostMapping("")
    public Book createBook(@RequestBody Book book){
        return mBS.createBook(book);
    }

    @DeleteMapping("/{id}")
    public Book deleteBook(@PathVariable("id") long id){
        return mBS.deleteBook(id);
    }


}

