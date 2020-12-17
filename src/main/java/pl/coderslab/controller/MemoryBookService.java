package pl.coderslab.controller;


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

    private static final String READ_ALL_BOOKS = "SELECT * FROM books ;";
    private static final String READ_BOOK_BY_ID = "SELECT * FROM books WHERE id = ? ;";
    private static final String EDIT_BOOK = "UPDATE books SET";
    private static final String DELETE_BOOK = "DELETE FROM books WHERE id=? ;";


    private MemoryBookService(){

    }

    public static MemoryBookService getMemoryBookService() throws IOException, SQLException {
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


}
