package com.ttk.developer.recon.controller;

import com.ttk.developer.recon.model.ReconViewResult;
import com.ttk.developer.recon.service.CsvTransactionReconService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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




        return "result";

    }

    /**
     * Instead of using Global Exception Handler with @ControllerAdvice
     * I have opt to use ResponseStatusException and @ExceptionHandler way because only have single controller to handle
     * Error View is not pretty and plain basic to handle/ show error
     * @param request
     * @param ex
     * @return
     */

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
