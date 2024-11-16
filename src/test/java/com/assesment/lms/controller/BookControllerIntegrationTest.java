package com.assesment.lms.controller;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static com.assesment.lms.constants.Constants.API_V1_LMS_BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO(1L, "978-0134670958", "Effective Java", "Joshua Bloch", null);
    }

    @Test
    void testRegisterBook() throws Exception {
        Mockito.when(bookService.registerBook(Mockito.any(BookDTO.class))).thenReturn(testBookDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V1_LMS_BASE_PATH+ "/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDTO))).andReturn();

        BookDTO read= objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDTO.class);

        assertEquals(testBookDTO.id(), read.id());
        assertEquals(testBookDTO.author(), read.author());
        assertEquals(testBookDTO.isbn(), read.isbn());
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<BookDTO> bookDTOList = Collections.singletonList(testBookDTO);
        Mockito.when(bookService.fetchAllBooks()).thenReturn(bookDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(API_V1_LMS_BASE_PATH+ "/books")).andReturn();

        List<BookDTO> read= objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(testBookDTO.id(), read.get(0).id());
        assertEquals(testBookDTO.author(),  read.get(0).author());
        assertEquals(testBookDTO.isbn(),  read.get(0).isbn());
        assertEquals(testBookDTO.title(),  read.get(0).title());
    }
}
