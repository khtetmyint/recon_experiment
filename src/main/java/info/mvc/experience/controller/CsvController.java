package info.mvc.experience.controller;

import info.mvc.experience.utility.CsvParserSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping("/api/csv")
public class CsvController {

    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }

    @GetMapping("/")
    public String hello(Model model) {

        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);

        logger.debug("Hello from Logback {}", data);

        model.addAttribute("num", data);

        return "index"; // index.html
    }

    @PostMapping("/upload-csv-file")
    public String uploadCsv(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        logger.info("Upload Csv method: File1 = {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            CsvParserSimple obj = new CsvParserSimple();
            List<String[]> result = obj.readAsMultipartFile(file,1);

            int listIndex = 0;
            for (String[] arrays : result) {
                System.out.println("\nString[" + listIndex++ + "] : " + Arrays.toString(arrays));

                int index = 0;
                for (String array : arrays) {
                    System.out.println(index++ + " : " + array);
                }

            }


            model.addAttribute("message", "CSV file1 uploaded");
            model.addAttribute("status", true);
        }



        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
