package com.ttk.developer.recon.controller;

import com.ttk.developer.recon.model.ReconViewResult;
import com.ttk.developer.recon.service.CsvTransactionReconService;
import com.ttk.developer.recon.utility.CsvParserSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        try {
            ReconViewResult reconViewResult = csvTransactionReconService.processAndReconcileFiles(file1, file2);

            logger.info("View Result={}", reconViewResult.toString());
            model.addAttribute("file_one_name", file1.getOriginalFilename());
            model.addAttribute("file_two_name", file2.getOriginalFilename());
            model.addAttribute("reconViewResult", reconViewResult);
            model.addAttribute("computeStatus", reconViewResult.isComputeStatus());
        } catch (IllegalArgumentException e){
            logger.warn("Exception Encountered while processing.", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e);
        }

//            logger.info("1 unmatch={}, 2 unmatch={}",
//        reconViewResult.getReconResultFileOne().getUnmatchedRecords(),
//        reconViewResult.getReconResultFileTwo().getUnmatchedRecords());
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
    @Value("#{${compare.headersMap}}")
    Map<String, String> headersMap;

    @PostMapping(value = "/header_only", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> headerOnlyTest(@RequestParam(value = "file") MultipartFile file){

        CsvParserSimple csvParserSimple = new CsvParserSimple();
        List<String> firstRow = new ArrayList<>();
        boolean isContainAllHeaders = false;
        try {
            List<String[]> headersRow = csvParserSimple.readHeadersOnlyMultipartFile(file);
            headersMap.forEach((k,v)->logger.info("Properties Header Key={}", k));
            for (int i = 0; i < headersRow.size(); i++) {
                if(i>0) break;
                String[] headers = headersRow.get(i);
                logger.info("Headers: {}",Arrays.asList(headers));
                firstRow = Arrays.asList(headers);
            }

            int headerMatchCount=0;
            for (String header : firstRow) {
                if(!headersMap.containsKey(header)){
                    isContainAllHeaders = false;
                    break;
                }
                for (String propertiesHead : headersMap.keySet()) {
                    if(propertiesHead.equalsIgnoreCase(header)) headerMatchCount++;
                }
            }
            logger.info("headerMatchCount={}, isContainAllHeaders={}", headerMatchCount, isContainAllHeaders);

            return new ResponseEntity<>( firstRow, HttpStatus.OK );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    boolean isContainsAllHeader(String header){
        if(!headersMap.containsKey(header)){
            return false;
        }
        return true;
    }

    @ExceptionHandler(value = {ResponseStatusException.class, IllegalArgumentException.class,
            MultipartException.class})
    public ModelAndView handleResponseStatusException(HttpServletRequest request, Throwable ex){
        logger.error("Requested URL="+request.getRequestURL());
        logger.error("Exception Raised="+ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception_trace_list", Arrays.asList(ex.getStackTrace()));
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("status", false);
        modelAndView.addObject("message", ex.getLocalizedMessage());


        modelAndView.setViewName("error");
        return modelAndView;
    }
}
