package com.ttk.developer.recon.service;

import com.ttk.developer.recon.model.ReconResult;
import com.ttk.developer.recon.model.ReconViewResult;
import org.springframework.web.multipart.MultipartFile;

public interface CsvTransactionReconService {

    ReconViewResult processAndReconcileFiles(MultipartFile file1, MultipartFile file2);
}
