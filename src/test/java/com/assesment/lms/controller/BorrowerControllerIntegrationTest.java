package com.assesment.lms.controller;

import com.assesment.lms.dto.BookDTO;
import com.assesment.lms.dto.BorrowerDTO;
import com.assesment.lms.service.BorrowerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static com.assesment.lms.constants.Constants.API_V1_LMS_BASE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BorrowerController.class)
@AutoConfigureMockMvc
class BorrowerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BorrowerService borrowerService;

    private BorrowerDTO testBorrowerDTO;

    @BeforeEach
    void setUp() {
        // Sample BorrowerDTO for testing
        testBorrowerDTO = new BorrowerDTO(1L, "janedoe@example.com", "Jane Doe");
    }

    @Test
    void testRegisterBorrower() throws Exception {
        Mockito.when(borrowerService.registerBorrower(Mockito.any(BorrowerDTO.class))).thenReturn(testBorrowerDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V1_LMS_BASE_PATH+ "/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorrowerDTO))).andReturn();

        BorrowerDTO read= objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BorrowerDTO.class);

        assertEquals(testBorrowerDTO.id(), read.id());
        assertEquals(testBorrowerDTO.name(), read.name());
        assertEquals(testBorrowerDTO.email(), read.email());
    }

    @Test
    void testBorrowBook() throws Exception {
        Mockito.doNothing().when(borrowerService).borrowBook(Mockito.anyLong(), Mockito.anyLong());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V1_LMS_BASE_PATH+ "/borrowers/1/borrow/1")).andReturn();

        assertEquals("Book borrowed successfully", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testReturnBook() throws Exception {
        Mockito.doNothing().when(borrowerService).returnBook(Mockito.anyLong(), Mockito.anyLong());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V1_LMS_BASE_PATH+ "/borrowers/1/return/1")).andReturn();

        assertEquals("Book returned successfully", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGetAllBorrowers() throws Exception {
        List<BorrowerDTO> borrowerDTOList = Collections.singletonList(testBorrowerDTO);
        Mockito.when(borrowerService.fetchAllBorrowers()).thenReturn(borrowerDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(API_V1_LMS_BASE_PATH+ "/borrowers")).andReturn();

        List<BorrowerDTO> reads= objectMapper.readValue(mvcResult.getResponse().getContentAsString(),  new TypeReference<>() {
        });

        assertEquals(testBorrowerDTO.id(), reads.get(0).id());
        assertEquals(testBorrowerDTO.name(), reads.get(0).name());
        assertEquals(testBorrowerDTO.email(), reads.get(0).email());
    }
}
