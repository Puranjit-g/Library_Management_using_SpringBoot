package org.jbd.JBD_MINOR1.Repository;

import org.jbd.JBD_MINOR1.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "select * from author where email = :email", nativeQuery = true)
    Author getAuthorByEmail(String email);
}
