package com.ttk.developer.recon.controller;

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

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Name_"+i);
            user.setEmail("Email_"+i+"@example.com");
            user.setCountryCode("Country_"+i);
            users.add(user);
        }

        model.addAttribute("users", users);


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
//        if (file.isEmpty()) {
//            model.addAttribute("message", "Please select a CSV file to upload.");
//            model.addAttribute("status", false);
//        } else {
//            CsvParserSimple obj = new CsvParserSimple();
//            List<String[]> result = obj.readAsMultipartFile(file,1);
//
//            int listIndex = 0;
//            for (String[] arrays : result) {
//                System.out.println("\nString[" + listIndex++ + "] : " + Arrays.toString(arrays));
//
//                int index = 0;
//                for (String array : arrays) {
//                    System.out.println(index++ + " : " + array);
//                }
//
//            }
//
//
//            model.addAttribute("message", "CSV file1 uploaded");
//            model.addAttribute("status", true);
//        }



//        return "redirect:/";
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Name_"+i);
            user.setEmail("Email_"+i+"@example.com");
            user.setCountryCode("Country_"+i);
            users.add(user);
        }

        model.addAttribute("users", users);

        return "result";

    }

//    @GetMapping("/result")
//    public String result(){
//        return "result";
//    }
}
