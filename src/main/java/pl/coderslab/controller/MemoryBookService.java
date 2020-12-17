package pl.coderslab.controller;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import pl.coderslab.model.Book;
import pl.coderslab.utils.DbUtil;
import pl.coderslab.utils.LogUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MemoryBookService {

    private static MemoryBookService instance;
    private static Logger logger;
    private static Connection connection;

    private static final String CREATE_BOOK = "INSERT INTO books(isbn,title,author,publisher,type) VALUES (?,?,?,?,?);";
    private static final String READ_ALL_BOOKS = "SELECT * FROM books ;";
    private static final String READ_BOOK_BY_ID = "SELECT * FROM books WHERE id = ? ;";
    private static final String UPDATE_BOOK = "UPDATE books SET isbn = ?, title = ?, author = ?, publisher = ?, type = ? WHERE id = ?";
    private static final String DELETE_BOOK = "DELETE FROM books WHERE id = ? ;";


    private MemoryBookService(){
    }

    public static MemoryBookService getMemoryBookService(){
        try {
            if (instance == null) {
                synchronized (MemoryBookService.class) {
                    if (instance == null) {
                        instance = new MemoryBookService();
                        connection = DbUtil.getConnection();
                        logger = LogUtil.getLogger();
                        logger.info("Created instance of MemoryBookService");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            logger.severe("IO Exception while creating instance of MemoryBookService");
        }catch (SQLException ex){
            ex.printStackTrace();
            logger.severe("SQL exception while creating instance of MemoryBookService");
        }
        return instance;
    }

    public List<Book> readAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            PreparedStatement prepStm = connection.prepareStatement(READ_ALL_BOOKS);
            ResultSet resultSet = prepStm.executeQuery();
            while (resultSet.next()){
                Book bookToAdd = new Book(
                (long) resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5),
                resultSet.getString(6)
                );
                books.add(bookToAdd);
            }
            logger.info("Successfully read all books");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("SQL exception in readAllBooks() method");
        }
        return books;
    }

    public Book readBookById(Long id){
        Book book = null;
        try {
            PreparedStatement prepStm = connection.prepareStatement(READ_BOOK_BY_ID);
            prepStm.setLong(1,id);
            ResultSet resultSet = prepStm.executeQuery();
            if (resultSet.next()){
                book = new Book(
                        (long) resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );
                logger.info("Successfully read book by id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("SQL exception in readBookById(Integer id) method");
        }
        return book;
    }

    public Book updateBook(Book book){
        try {
            PreparedStatement prepStm = connection.prepareStatement(UPDATE_BOOK);
            setPrepStmParams(prepStm,book);
            prepStm.setLong(6,book.getId());

            switch (prepStm.executeUpdate()){
                case 1:
                    logger.info("Successfully updated");
                    return book;
                case 0:
                    logger.info("Book not updated");
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("SQL exception in updateBook(Book book) method");
        }
        return null;
    }

    public Book deleteBook(Long id){
        Book book = readBookById(id);
        try {
            PreparedStatement prepStm = connection.prepareStatement(DELETE_BOOK);
            prepStm.setLong(1,id);
            switch (prepStm.executeUpdate()){
                case 1:
                    logger.info("Successfully deleted book");
                    return book;
                case 0:
                    logger.info("Book not deleted");
                    return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("SQL exception in deleteBook(Long id) method");
        }
        return null;
    }

    public Book createBook(Book book){
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement prepStm = connection.prepareStatement
                     (CREATE_BOOK, PreparedStatement.RETURN_GENERATED_KEYS);){
            setPrepStmParams(prepStm,book);
            try {
                prepStm.executeUpdate();
            } catch (MySQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
            }
            try (ResultSet generatedKeys = prepStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    book.setId(Long.parseLong(generatedKeys.getInt(1) + ""));
                    logger.info("Successfully inserted book");
                    return book;
                } else {
                    logger.severe("Generated key was not found");
                    throw new RuntimeException("Generated key was not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("SQL exception in createBook(Book book) method");
        }
        return null;
    }

    private void setPrepStmParams(PreparedStatement prepStm, Book book) throws SQLException {
        prepStm.setString(1, book.getIsbn());
        prepStm.setString(2, book.getTitle());
        prepStm.setString(3, book.getAuthor());
        prepStm.setString(4, book.getPublisher());
        prepStm.setString(5, book.getType());
    }
}
