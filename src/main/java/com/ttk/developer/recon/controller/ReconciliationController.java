package com.ttk.developer.recon.controller;

import com.ttk.developer.recon.utility.CsvParserSimple;
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

import java.util.Arrays;
import java.util.List;

/**
 * Main Controller to handle CSV files upload, Transactions Reconciliation and Display Result operations.
 */
@Controller
@RequestMapping("/")
public class ReconciliationController {

    private static final Logger logger = LoggerFactory.getLogger(ReconciliationController.class);

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index() {

        return "index"; // index.html
    }

    @PostMapping("/")
    public String processReconcile(@RequestParam(value = "file1") MultipartFile file1,
                                   @RequestParam(value = "file2") MultipartFile file2,
                                   Model model) throws Exception {
        logger.info("Upload Csv method: File1 = {}", file1.getOriginalFilename());
        logger.info("Upload Csv method: File2 = {}", file2.getOriginalFilename());

        model.addAttribute("file_one_name", file1.getOriginalFilename());
        model.addAttribute("file_two_name", file2.getOriginalFilename());

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
        return "result";

    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
