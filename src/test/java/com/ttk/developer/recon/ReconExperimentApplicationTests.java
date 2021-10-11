package com.ttk.developer.recon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReconExperimentApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(ReconExperimentApplicationTests.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

//    MockMultipartFile file1;
//    MockMultipartFile file2;

    MockMvc mockMvc;

    @BeforeEach
    void setUp()  {

//        final String LOAD_CSV_FILENAME_ONE = "file_one.csv";
//        final String LOAD_CSV_FILENAME_TWO = "file_two.csv";
//
//        file1 = new MockMultipartFile("file1Name", LOAD_CSV_FILENAME_ONE, "text/plain", "Some1".getBytes());
//        file2 = new MockMultipartFile("file2Name", LOAD_CSV_FILENAME_TWO, "text/plain", "Two".getBytes());


        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

//    @Test
//    void contextLoads() {
//    }

    @Test
    public void whenTestPost_thenVerifyResult()
            throws Exception {

//        final String file1Content = "ProfileName,TransactionDate,TransactionAmount,TransactionNarrative,TransactionDescription,TransactionID,TransactionType,WalletReference\n" +
//                "Card Campaign,2014-01-11 22:27:44,-20000,*MOLEPS ATM25             MOLEPOLOLE    BW,DEDUCT,0584011808649511,1,P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5,\n" +
//                "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
//        final String file2Content = "ProfileName,TransactionDate,TransactionAmount,TransactionNarrative,TransactionDescription,TransactionID,TransactionType,WalletReference\n" +
//                "Card Campaign,2014-01-11 22:27:44,-20000,*MOLEPS ATM25             MOLEPOLOLE    BW,DEDUCT,0584011808649511,1,P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5,\n" +
//                "Card Campaign,2014-01-11 22:39:11,-10000,*MOGODITSHANE2            MOGODITHSANE  BW,DEDUCT,0584011815513406,1,P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5,";
//
//
//
//        MockMultipartFile file1
//                = new MockMultipartFile(
//                "file_one",
//                "file_one.csv",
//                MediaType.TEXT_PLAIN_VALUE,
//                "file_one.csv".getBytes()
//        );
//
//        MockMultipartFile file2
//                = new MockMultipartFile(
//                "file_two",
//                "file_two.csv",
//                MediaType.TEXT_PLAIN_VALUE,
//                "file_two.csv".getBytes()
//        );

        mockMvc.perform(post("/")
//                .file(file1)
//                .file(file2)
                .accept(MediaType.TEXT_HTML))
                //.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
                .andExpect(status().isOk());
//                .andExpect(status().isBadRequest());
    }

}
