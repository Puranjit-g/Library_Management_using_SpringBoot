package org.jbd.JBD_MINOR1.controller;


import org.jbd.JBD_MINOR1.Controller.BookController;
import org.jbd.JBD_MINOR1.Service.BookService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {BookController.class})
public class TestBookController {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testAddBook() throws Exception {
        JSONObject object = new JSONObject();
        object.put("booktitle", "title");
        object.put("bookNo","1");
        object.put("authorName", "author");
        object.put("authorEmail", "email.com");
        object.put("securityAmount", 100);
        object.put("type", "EDUCATIONAL");
        RequestBuilder requestBuilder = post("/book/addBook").
                contentType(MediaType.APPLICATION_JSON).content(String.valueOf(object));
        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
//validations
//call ur method just like it is being called by a dispatcher servlet