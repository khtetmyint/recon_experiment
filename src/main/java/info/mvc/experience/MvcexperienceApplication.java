package info.mvc.experience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MvcexperienceApplication {

    private static final Logger logger = LoggerFactory.getLogger(MvcexperienceApplication.class);

    private static final String COMMA_DELIMITER = ",";
    public static void main(String[] args) {
//        ApplicationContext configurableApplicationContext=
        SpringApplication.run(MvcexperienceApplication.class, args);

//        logger.info("{}, {}",configurableApplicationContext.getId(),
//                configurableApplicationContext.getDisplayName());
//        https://mkyong.com/spring-boot/spring-boot-slf4j-logging-example/

//        https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/

        // uploading and parsing
//        https://attacomsian.com/blog/spring-boot-upload-parse-csv-file



//        String fileName = "csv/monitor.csv";
//        InputStream inputStreamResource = MvcexperienceApplication.class.getClassLoader().
//                getResourceAsStream(fileName);
//
//        if (inputStreamResource == null)
//            throw new IllegalArgumentException("file not found! " + fileName);
//
//            try (InputStreamReader streamReader =
//                         new InputStreamReader(inputStreamResource, StandardCharsets.UTF_8);
//                 BufferedReader reader = new BufferedReader(streamReader)) {
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        try {
//            List<String[]> collect =
//                    Files.lines(Paths.get("c:\\test\\csv\\country.csv"))
//                            .map(line -> line.split(COMMA_DELIMITER))
//                            .collect(Collectors.toList());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        List<List<String>> records = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader("book.csv"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(COMMA_DELIMITER);
//                records.add(Arrays.asList(values));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }


//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//    }
}
