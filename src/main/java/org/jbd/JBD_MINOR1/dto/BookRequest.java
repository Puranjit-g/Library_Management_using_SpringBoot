package org.jbd.JBD_MINOR1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.jbd.JBD_MINOR1.Model.Author;
import org.jbd.JBD_MINOR1.Model.Book;
import org.jbd.JBD_MINOR1.Model.BookType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookRequest {

    // request from user

    @NotBlank(message = "Book title should not be blank")
    private String booktitle;

    @NotBlank(message = "Book no should not be blank")
    private String bookNo;

    @NotBlank(message = "Author name should not be blank")
    private String authorName;

    @NotBlank(message = "Author email should not be blank")
    private String authorEmail;

    @NotNull(message = "Book type should not be blank")
    private BookType type;

    @Positive(message = "Security amount should be positive")
    private int securityAmount;

    public Author toAuthor() {
        return Author.
                builder().
                email(this.authorEmail).
                name(this.authorName).
                build();
    }

    public Book toBook() {
        return Book.builder().
                bookNo(this.bookNo).
                title(this.booktitle).
                securityAmount(this.securityAmount).
                bookType(this.type).
                build();
    }
}
