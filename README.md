# Financial Transactions Reconciliation
## Introduction
This is web application which performs transactions reconciliation between two sets of data in **csv** file format.

I have developed this application using Spring Boot MVC framework and Thymeleaf template engine. Additionally, it has been hosted at Heroku platform for demonstration purpose.


##  Architecture
I have based reconciliation logic on excel `vlookup` function primarily to find matching and missing records between two set of data. I assumed that there is only one `primary key ID` of the transaction to lookup and compare with second file. (Tested excel workbook is also attached in the project directory)

- Load both files entirely and convert as list of CsvRecord model.
- Convert lists into maps with grouping by `primary key ID`as key and associated records list as value. (i.e. as in bucket with group by each `key`, searching record is more efficient in map structure)
-- Thereby handling those cases where duplicates/ more than one records with same `primary key ID` exist in file.
- for each `key` in first map, search in second map.
    - if found, further compare if every `column values` of record one and two are exact matched or not
    - then records with different `column value` are put in list of unmatched result (with reasons)
    - if `key` not found in second map, associated record is added in the list of unmatched result (with indicative reason)




### Features
- Main Key ID of the record can be configurable via `application.properties` to primarily match against compared file.
- Two ways  (vice versa) comparing between file 1 and 2. Not only compared file 1 (main file) against file 2. The other way around is also compared.
- Set file size limitation when uploading.
- Only CSV format allowed.


## Instructions

### How to build 
Requires Maven (version 3 and above) and JDK 11 to be built.
```sh
mvn clean install -Dmaven.test.skip=true
```
### How to run
After the project is successfully build, run the following:
> by maven

```sh
mvn spring-boot:run
```
> or using java -jar
```sh
java -jar .\target\recon_experiment-0.0.1-SNAPSHOT.jar
```
In local environment, the application can be accessed via [http://localhost:8080](http://localhost:8080)

### Usage
- Via Browser, go to [http://localhost:8080](http://localhost:8080) . 
- Upon home page loaded, select CSV file 1 and 2 and press `compare` button to process and show reconciliation summary in two tables respectively for file 1 and 2. 
- For unmatched records, click `Unmatched Report` button to display two detailed tables (i.e. For file 1 and 2 respectively)
- In Unmatched Report, there are additional information such as unmatched reasons, remarks and row number information of the unmatched transactions displayed as well.

-------------


## Further Consideration

- Layout and UI components are really basic only. Could be improved more to enhance usage experience.
- Could have option to detect duplicate records within same file and indicate in report.
- More robust exception handling, testing covering edge cases and multiple reconciliation logic (other than excel `vlookup` logic) are required to further improve application.


