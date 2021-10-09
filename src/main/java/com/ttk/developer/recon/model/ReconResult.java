package com.ttk.developer.recon.model;

import java.util.List;

public class ReconResult {

    int totalRecords;
    int matchedRecords;

    /**
     * {to add two count to get unmatched count}
     */
    int unmatchedRecords;

    int notFoundInAnotherFileCount;
    int foundButNotMatchInAnotherFileCount;

    List<String> keyListNotFoundInFileTwo;
    List<String> keyListFoundInFileTwoButNotMatch;

    List<CompareResult> compareResultList;


    ////


    public int getTotalRecords() {
        return totalRecords;
    }

    public int getMatchedRecords() {
        return matchedRecords;
    }

    public int getUnmatchedRecords() {
        return unmatchedRecords;
    }

    public int getNotFoundInAnotherFileCount() {
        return notFoundInAnotherFileCount;
    }

    public int getFoundButNotMatchInAnotherFileCount() {
        return foundButNotMatchInAnotherFileCount;
    }

    public List<String> getKeyListNotFoundInFileTwo() {
        return keyListNotFoundInFileTwo;
    }

    public List<String> getKeyListFoundInFileTwoButNotMatch() {
        return keyListFoundInFileTwoButNotMatch;
    }

    public List<CompareResult> getCompareResultList() {
        return compareResultList;
    }

    ///////////


    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setMatchedRecords(int matchedRecords) {
        this.matchedRecords = matchedRecords;
    }

    public void setUnmatchedRecords(int unmatchedRecords) {
        this.unmatchedRecords = unmatchedRecords;
    }

    public void setNotFoundInAnotherFileCount(int notFoundInAnotherFileCount) {
        this.notFoundInAnotherFileCount = notFoundInAnotherFileCount;
    }

    public void setFoundButNotMatchInAnotherFileCount(int foundButNotMatchInAnotherFileCount) {
        this.foundButNotMatchInAnotherFileCount = foundButNotMatchInAnotherFileCount;
    }

    public void setKeyListNotFoundInFileTwo(List<String> keyListNotFoundInFileTwo) {
        this.keyListNotFoundInFileTwo = keyListNotFoundInFileTwo;
    }

    public void setKeyListFoundInFileTwoButNotMatch(List<String> keyListFoundInFileTwoButNotMatch) {
        this.keyListFoundInFileTwoButNotMatch = keyListFoundInFileTwoButNotMatch;
    }

    public void setCompareResultList(List<CompareResult> compareResultList) {
        this.compareResultList = compareResultList;
    }
}
