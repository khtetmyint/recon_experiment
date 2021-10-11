package com.ttk.developer.recon.service.impl;

import com.ttk.developer.recon.model.ReconViewResult;
import com.ttk.developer.recon.service.CsvTransactionReconService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CsvTransactionReconServiceImplTest {
    private static final Logger logger = LoggerFactory.getLogger(CsvTransactionReconServiceImplTest.class);
//    @Mock
//    private CsvTransactionReconServiceImpl service;

    MockMultipartFile file1;
    MockMultipartFile file2;


    @BeforeEach
    void setUp()  {


        CsvTransactionReconService service = Mockito.mock(CsvTransactionReconServiceImpl.class);
        logger.info("Setup Testing = {}", service.toString());

        final String LOAD_CSV_FILENAME_ONE = "file_one.csv";
        final String LOAD_CSV_FILENAME_TWO = "file_two.csv";

        file1 = new MockMultipartFile("file1Name", LOAD_CSV_FILENAME_ONE, "text/plain", "Some1".getBytes());
        file2 = new MockMultipartFile("file2Name", LOAD_CSV_FILENAME_TWO, "text/plain", "Two".getBytes());



    }

    @Test
    void test_Mock_ProcessAndReconcileFiles() {
        CsvTransactionReconService service = Mockito.mock(CsvTransactionReconServiceImpl.class);
        //logger.info("Test Setup = {}", service.toString());
        Mockito.when(service.processAndReconcileFiles(file1, file2)).thenReturn(new ReconViewResult());

        ReconViewResult reconViewResult= service.processAndReconcileFiles(file1, file2);
        Assert.isTrue(reconViewResult instanceof ReconViewResult, "Return as expected");

    }


}