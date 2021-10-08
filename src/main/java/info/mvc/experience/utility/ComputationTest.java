package info.mvc.experience.utility;

import info.mvc.experience.model.CsvRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ComputationTest {
    private static final Logger logger = LoggerFactory.getLogger(ComputationTest.class);

    static final String LOAD_CSV_FILENAME_ONE = "file_one.csv";
    static final String LOAD_CSV_FILENAME_TWO = "file_two.csv";

    static final String COMPARE_KEY = "TransactionID";

    public static void main(String[] args) throws Exception {
        //TEST getResourceAsStream
//        String fileName = "csv/monitor.csv";
//        InputStream inputStreamResource = ComputationTest.class.getClassLoader().
//                getResourceAsStream(fileName);
//
//        if (inputStreamResource == null)
//            throw new IllegalArgumentException("file not found! " + fileName);
//
//        try (InputStreamReader streamReader =
//                     new InputStreamReader(inputStreamResource, StandardCharsets.UTF_8);
//             BufferedReader reader = new BufferedReader(streamReader)) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                logger.info(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        final Map<String, List<CsvRecord>> fileOneRecordMap = getIntoRecordMap(LOAD_CSV_FILENAME_ONE);
        final Map<String, List<CsvRecord>> fileTwoRecordMap = getIntoRecordMap(LOAD_CSV_FILENAME_TWO);
        System.out.println("==================================Print Out Record Map=============");
        int matchCountAgainstFileTwo =0;
        int unmatchCountAgainstFileTwo =0;
        List<String> unmatchKeyListAgainstFileTwo = new ArrayList<>();

        fileOneRecordMap.forEach((key,listOfCsvRecord)->{
            logger.info("{}={}, CsvRecord Per Key={}",COMPARE_KEY, key, listOfCsvRecord.size());

        });
        System.out.println("==================================Print Out Record Map=============");
        fileTwoRecordMap.forEach((key,listOfCsvRecord)->{
            logger.info("{}={}, CsvRecord Per Key={}",COMPARE_KEY, key, listOfCsvRecord.size());
        });
        System.out.println("===================================================================");

        for (String key : fileOneRecordMap.keySet()) {
            boolean found = fileTwoRecordMap.containsKey(key)?true:false;
            if(fileTwoRecordMap.containsKey(key)){
                matchCountAgainstFileTwo++;
            } else{
                unmatchKeyListAgainstFileTwo.add(key);
                unmatchCountAgainstFileTwo++;
            }
        }
        logger.info("\nmatchCountAgainstFileTwo={} \nunmatchCountAgainstFileTwo={}",
                matchCountAgainstFileTwo, unmatchCountAgainstFileTwo);
        logger.info("\nunmatchKeyListAgainstFileTwo={}",unmatchKeyListAgainstFileTwo.toString());
        //fileOneRecordMap.entrySet();


    }

    static private Map<String, List<CsvRecord>> getIntoRecordMap(String localFilePath) throws Exception {

        //Testing with Local file
        System.out.println("Receiving request to Load and Convert Filename="+localFilePath);
        File file = Paths.get(localFilePath).toFile();
        CsvParserSimple obj = new CsvParserSimple();
        //Read As Local file
        List<String[]> result = obj.readFile(file);

        Map<String, Integer> headerMap = new LinkedHashMap<>();
        List<CsvRecord> csvRecordList = new ArrayList<>();
        int rowIndex = 0;
        for (String[] arrays : result) {

            if (rowIndex == 0) { //READING for header only
                for (int i = 0; i < arrays.length; i++) {
                    headerMap.put(arrays[i], i);
                }
            }

            CsvRecord csvRecord = new CsvRecord(rowIndex, headerMap, Arrays.asList(arrays));
            csvRecordList.add(csvRecord);
//            System.out.println("\nString[" + rowIndex + "] : " + Arrays.toString(arrays));
//
//            int index = 0;
//            for (String array : arrays) {
//                System.out.println(index++ + " : " + array);
//            }
            //INCREMENT to next row record
            rowIndex++;

        }
        System.out.println("===============================");
        System.out.println("Total Row/Records (-Headers)="+(csvRecordList.size()-1));
        System.out.println("===============================");
//        for (CsvRecord csvRecord : csvRecordList) {
//            csvRecord.getDataByHeader();
//            logger.info("Row:{}", csvRecord.getRowNumber());
//            csvRecord.convertToTransaction().getValuesHolder().forEach((k,v)->logger.info("{}={}", k,v));
//            csvRecord.getTransaction().forEach((k,v)->logger.info("{}={}", k,v));
//        }

        Map<String, List<CsvRecord>> csvRecordMap = csvRecordList
                .stream()
                .filter(csvRecord -> csvRecord.getRowNumber() != 0)

                .collect(Collectors.groupingBy((map) ->
                        map.getTransaction().get(COMPARE_KEY), LinkedHashMap::new, Collectors.toList()));

        return csvRecordMap;
    }
}
