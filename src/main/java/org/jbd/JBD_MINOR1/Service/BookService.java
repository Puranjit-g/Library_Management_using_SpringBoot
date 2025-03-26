package org.jbd.JBD_MINOR1.Service;

import jakarta.validation.Valid;
import org.jbd.JBD_MINOR1.Model.*;
import org.jbd.JBD_MINOR1.Repository.AuthorRepository;
import org.jbd.JBD_MINOR1.Repository.BookRepository;
import org.jbd.JBD_MINOR1.dto.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(@Valid BookRequest bookRequest) {

        //Either the data is already present in db and jst fetching it from database
        Author authorFromDB = authorRepository.getAuthorByEmail(bookRequest.getAuthorEmail());

        if(authorFromDB == null){
            //object of author table
            //save data to author table

            //or saved in table and got the updated data fom db
           authorFromDB = authorRepository.save(bookRequest.toAuthor());
        }

        Book book = bookRequest.toBook();
        book.setAuthor(authorFromDB);
        return bookRepository.save(book);
    }

    public List<Book> filter(BookFilterType filterType, Operator operator, String value) {
        switch (filterType){
            case BOOK_TITLE:
                switch (operator){
                    case EQUALS :
                        return bookRepository.findByTitle(value);
                    case LIKE:
                        return bookRepository.findByTitleContaining(value);
                    default:
                        new ArrayList<>();
                }
            case BOOK_TYPE:
                switch (operator) {
                    case EQUALS:
                        //String "value" is changed to BookType by the below code.
                        return bookRepository.findByBookType(BookType.valueOf(value));
                }
            case BOOK_NO :
                switch (operator) {
                    case EQUALS:
                        return bookRepository.findByBookNo(value);
                }
        }
        return new ArrayList<>();

    }

    public void updateBookData(Book bookFromDb) {
        bookRepository.save(bookFromDb);
    }
}
