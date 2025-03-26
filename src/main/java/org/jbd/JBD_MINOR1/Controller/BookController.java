package org.jbd.JBD_MINOR1.Controller;

import jakarta.validation.Valid;
import org.jbd.JBD_MINOR1.Model.Book;
import org.jbd.JBD_MINOR1.Model.BookFilterType;
import org.jbd.JBD_MINOR1.Model.Operator;
import org.jbd.JBD_MINOR1.Service.BookService;
import org.jbd.JBD_MINOR1.dto.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public Book addBook(@RequestBody @Valid BookRequest bookRequest){
        // 1.validations before business logic
//        if(StringUtil.isNullOrEmpty(bookRequest.getBookNo())){
//            throw new Exception("book no should not be blank");
//        }

        //2. to call the business logic
        Book book = bookService.addBook(bookRequest);

        return book;
    }

    @GetMapping("/filter")
    public List<Book> findByTitle(@RequestParam("filterBy") BookFilterType filterType,
                                  @RequestParam("operator")Operator operator,
                                  @RequestParam("value") String value){
        return bookService.filter(filterType, operator, value);
    }

}

// creating a row in book table
// have to provide information about the author who has written this.