package org.jbd.JBD_MINOR1.Controller;

import org.jbd.JBD_MINOR1.Model.Author;
import org.jbd.JBD_MINOR1.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/getAuthorData")
    public Author getAuthorData(@RequestParam("authorEmail") String email){
        return authorService.getAuthorData(email);
    }
}

