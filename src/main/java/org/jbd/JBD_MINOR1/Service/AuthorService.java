package org.jbd.JBD_MINOR1.Service;

import org.jbd.JBD_MINOR1.Model.Author;
import org.jbd.JBD_MINOR1.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorData(String email) {
        return authorRepository.getAuthorByEmail(email);
    }
}
