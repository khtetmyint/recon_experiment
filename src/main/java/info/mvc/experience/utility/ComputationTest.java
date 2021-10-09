package info.mvc.experience.utility;

import info.mvc.experience.model.CompareResult;
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

        final List<CsvRecord> fileOneCsvRecordList = convertToCsvRecordList(LOAD_CSV_FILENAME_ONE);
        final List<CsvRecord> fileTwoCsvRecordList = convertToCsvRecordList(LOAD_CSV_FILENAME_TWO);

        final Map<String, List<CsvRecord>> fileOneRecordMap = getIntoRecordMap(fileOneCsvRecordList);
        final Map<String, List<CsvRecord>> fileTwoRecordMap = getIntoRecordMap(fileTwoCsvRecordList);
        System.out.println("==================================File One - Checking Record Map=============");
        int fileOneTotalRecordCount = 0;
        int fileTwoTotalRecordCount = 0;

        int matchCountAgainstFileTwo = 0;
        int notFoundCountAgainstFileTwo = 0;
        List<String> notFoundKeyListAgainstFileTwo = new ArrayList<>();

        int foundButNotMatch_Count_AgainstFileTwo = 0;
        List<String> foundButNotMatch_KeyList_AgainstFileTwo = new ArrayList<>();

        fileOneTotalRecordCount = fileOneCsvRecordList.size()-1;

        fileTwoTotalRecordCount = fileTwoCsvRecordList.size()-1;

//        fileOneRecordMap.forEach((key,listOfCsvRecord)->{
//            logger.info("{}={}, CsvRecord Per Key={}",COMPARE_KEY, key, listOfCsvRecord.size());
//
//        });
        for (String key : fileOneRecordMap.keySet()) {

            if(fileTwoRecordMap.containsKey(key)){
                //Found ;;; Record with Same Key Exists in File Two ;;; further Matching
                List<CsvRecord> csvRecordsFileOne = fileOneRecordMap.get(key);
                if(csvRecordsFileOne.size()>1){
                    // handle duplicate / having more than 1 Csv Records per key ID
                    logger.warn("Having more than 1 Csv Records per key ID={}", key);
                    for (CsvRecord csvRecordOne : csvRecordsFileOne) {
                        List<CsvRecord> csvRecordsFileTwo = fileTwoRecordMap.get(key);
                        for (CsvRecord csvRecordTwo : csvRecordsFileTwo) {
                            CompareResult compareResult =  compareTwoCsvRecords(csvRecordOne, csvRecordTwo);

                            logger.info("Having Same Key={}, Result ={}", key, compareResult);
                        }
                        //matchCountAgainstFileTwo++;
                    }


                } else if (csvRecordsFileOne.size()==1){
                    CsvRecord csvRecordOne = csvRecordsFileOne.get(0);
                    List<CsvRecord> csvRecordsFileTwo = fileTwoRecordMap.get(key);

                    List<CompareResult> compareResultList = new ArrayList<>();
                    for (CsvRecord csvRecordTwo : csvRecordsFileTwo) {
                        CompareResult compareResult =  compareTwoCsvRecords(csvRecordOne, csvRecordTwo);
                        compareResultList.add(compareResult);
                        if( !compareResult.isRecordMatch() ) {
                            logger.info("Key={}, Unmatch Result ={}", key, compareResult);
                            foundButNotMatch_Count_AgainstFileTwo++;
                            foundButNotMatch_KeyList_AgainstFileTwo.add(key);
                        } else {
                            matchCountAgainstFileTwo++;
                        }

                    }
                    //logger.info("Key={}, Number of Comparing RecordTwo={}", key, compareResultList.size());




                } else {
                    //do nothing ??
                    logger.warn("DO Nothing");
                }

            } else{
                notFoundKeyListAgainstFileTwo.add(key);
                notFoundCountAgainstFileTwo++;
            }
        }
        System.out.println("==================================File Two - Checking Record Map=============");
//        fileTwoRecordMap.forEach((key,listOfCsvRecord)->{
//            logger.info("{}={}, CsvRecord Per Key={}",COMPARE_KEY, key, listOfCsvRecord.size());
//        });
        System.out.println("===================================================================");


        logger.info("=====File One======\nfileOneTotalRecordCount={} " +
                        "\nmatchCountAgainstFileTwo={} " +
                        "\nnotFoundCountAgainstFileTwo={} " +
                        "\nfoundButNotMatch_Count_AgainstFileTwo={}",
                fileOneTotalRecordCount,
                matchCountAgainstFileTwo,
                notFoundCountAgainstFileTwo,
                foundButNotMatch_Count_AgainstFileTwo);

        logger.info("\nfoundButNotMatch_KeyList_AgainstFileTwo={}", foundButNotMatch_KeyList_AgainstFileTwo.toString());
        logger.info("\nnotFoundKeyListAgainstFileTwo={}",notFoundKeyListAgainstFileTwo.toString());
        //fileOneRecordMap.entrySet();


    }

    static private CompareResult compareTwoCsvRecords(CsvRecord one, CsvRecord two){


        CompareResult result = new CompareResult();

        result.setRecordMatch(true);
        for (String headerKey : one.getTransaction().keySet()) {

            String twoValue = two.getTransaction().get(headerKey);
            String oneValue = one.getTransaction().get(headerKey);
            List<String> headers = new ArrayList<>();
            List<String> recordOneValues = new ArrayList<>();
            List<String> recordTwoValues = new ArrayList<>();

            if(!twoValue.equalsIgnoreCase(oneValue) ){

                result.setRecordMatch(false);

                result.setReason("Record Values Not Matched");

                headers.add(headerKey);
                result.setComparedHeader(headers);

                recordOneValues.add(oneValue);
                result.setValueInFileOne(recordOneValues);

                recordTwoValues.add(twoValue);
                result.setValueInFileTwo(recordTwoValues);


                result.setRowNumberInFileOne(String.valueOf(one.getRowNumber()));
                result.setRowNumberInFileTwo(String.valueOf(two.getRowNumber()));

            }

        }
//        if (resultsMap.containsValue(false)) return resultsMap;
//
//        resultsMap.put("AllMatch", String.valueOf(true));
        return result;
    }

    static private List<CsvRecord> convertToCsvRecordList(String localFilePath) throws Exception {
        //Testing with Local file
        System.out.println("Receiving request to Load and Convert Filename="+localFilePath);
        File file = Paths.get(localFilePath).toFile();
        CsvParserSimple obj = new CsvParserSimple();
        //Read As Local file
        List<String[]> result = obj.readFile(file);

        Map<String, Integer> headerMap = new LinkedHashMap<>();
        List<CsvRecord> csvRecordList = new ArrayList<>();
        int rowIndex = 1;
        for (String[] arrays : result) {

            if (rowIndex == 1) { //READING for header only ;;; HEADER row starts at Row 1
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
        System.out.println("Total Records (-Headers)="+(csvRecordList.size()-1));
        System.out.println("===============================");

        return csvRecordList;
    }

    static private Map<String, List<CsvRecord>> getIntoRecordMap(List<CsvRecord> csvRecordList) throws Exception {


//        for (CsvRecord csvRecord : csvRecordList) {
//            csvRecord.getDataByHeader();
//            logger.info("Row:{}", csvRecord.getRowNumber());
//            csvRecord.convertToTransaction().getValuesHolder().forEach((k,v)->logger.info("{}={}", k,v));
//            csvRecord.getTransaction().forEach((k,v)->logger.info("{}={}", k,v));
//        }

        Map<String, List<CsvRecord>> csvRecordMap = csvRecordList
                .stream()
                .filter(csvRecord -> csvRecord.getRowNumber() != 1)

                .collect(Collectors.groupingBy((map) ->
                        map.getTransaction().get(COMPARE_KEY), LinkedHashMap::new, Collectors.toList()));

        return csvRecordMap;
    }
}
