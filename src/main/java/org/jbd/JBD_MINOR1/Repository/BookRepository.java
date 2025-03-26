package org.jbd.JBD_MINOR1.Repository;

import org.jbd.JBD_MINOR1.Model.Book;
import org.jbd.JBD_MINOR1.Model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    //findBy"should be exactly same by the name we want to search,ie, in book class "title"
    List<Book> findByTitle(String title);

    List<Book> findByTitleContaining(String title);

    List<Book> findByBookType(BookType bookType);

    List<Book> findByBookNo(String bookNo);

}
