package vn.edu.fpt.account.service;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.account.dto.common.CreateFileRequest;
import vn.edu.fpt.account.entity._File;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/10/2022 - 02:01
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface S3BucketStorageService {

    void uploadFile(CreateFileRequest request, String fileKey);

    void downloadFile(String fileKey, HttpServletResponse response);

    void downloadFile(_File file, HttpServletResponse response);
    File downloadFile(String fileKey);

    String sharingUsingPresignedURL(String fileKey);

    // deleteFile
}
