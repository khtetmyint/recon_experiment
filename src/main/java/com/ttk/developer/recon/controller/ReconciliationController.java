package com.ttk.developer.recon.controller;

import com.ttk.developer.recon.model.CompareResult;
import com.ttk.developer.recon.model.ReconViewResult;
import com.ttk.developer.recon.model.User;
import com.ttk.developer.recon.service.CsvTransactionReconService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Controller to handle CSV files upload, Transactions Reconciliation and Display Result operations.
 */
@Controller
@RequestMapping("/")
public class ReconciliationController {

    private static final Logger logger = LoggerFactory.getLogger(ReconciliationController.class);


    private final CsvTransactionReconService csvTransactionReconService;

    /**
     * Autowiring by Constructor
     * @param csvTransactionReconService
     */
    public ReconciliationController(CsvTransactionReconService csvTransactionReconService) {
        this.csvTransactionReconService = csvTransactionReconService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model) {



        return "index"; // index.html
    }

    //////

    /////

    @PostMapping("/")
    public String processReconcile(@RequestParam(value = "file1") MultipartFile file1,
                                   @RequestParam(value = "file2") MultipartFile file2,
                                   Model model)  {
        logger.info("Received Csv File1 ={} to reconcile with another Csv File2 ={}",
                file1.getOriginalFilename(),
                file2.getOriginalFilename());
        ReconViewResult reconViewResult = csvTransactionReconService.processAndReconcileFiles(file1, file2);

        logger.info("View Result={}", reconViewResult.toString());
        model.addAttribute("file_one_name", file1.getOriginalFilename());
        model.addAttribute("file_two_name", file2.getOriginalFilename());
        model.addAttribute("reconViewResult", reconViewResult);
        model.addAttribute("computeStatus", reconViewResult.isComputeStatus());
            logger.info("1 unmatch={}, 2 unmatch={}",
        reconViewResult.getReconResultFileOne().getUnmatchedRecords(),
        reconViewResult.getReconResultFileTwo().getUnmatchedRecords());
//        for (CompareResult compareResult : reconViewResult.getReconResultFileOne().getCompareResultList()) {
//            logger.info("In Loop ResultFileOne:{},{},{},{},{},{}",
//            compareResult.getMainRecord().getRowNumber(),
//            compareResult.getRowNumberInFileOne(),
//            compareResult.getCompareKey(),
//            compareResult.getReason(),
//            compareResult.getMainRecord().getTransaction().get("TransactionDate"),
//            compareResult.getMainRecord().getTransaction().get("TransactionAmount"));
//        }
//
//        for (CompareResult compareResult : reconViewResult.getReconResultFileTwo().getCompareResultList()) {
//            logger.info("In Loop ResultFileTwo:{},{},{},{},{},{}",
//                    compareResult.getMainRecord().getRowNumber(),
//                    compareResult.getRowNumberInFileOne(),
//                    compareResult.getCompareKey(),
//                    compareResult.getReason(),
//                    compareResult.getMainRecord().getTransaction().get("TransactionDate"),
//                    compareResult.getMainRecord().getTransaction().get("TransactionAmount"));
//        }
//        if (file.isEmpty()) {
//            model.addAttribute("message", "Please select a CSV file to upload.");
//            model.addAttribute("status", false);
//        } else {
//
//
//            model.addAttribute("message", "CSV file1 uploaded");
//            model.addAttribute("status", true);
//        }



//        return "redirect:/";


        return "result";

    }

//    @GetMapping("/result")
//    public String result(){
//        return "result";
//    }
}
