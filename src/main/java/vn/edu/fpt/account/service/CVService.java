package vn.edu.fpt.account.service;

import vn.edu.fpt.account.dto.request.cv.CreateCVRequest;
import vn.edu.fpt.account.dto.request.cv.UpdateCVRequest;
import vn.edu.fpt.account.dto.response.cv.CreateCVResponse;
import vn.edu.fpt.account.dto.response.cv.GetCVDetailResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 18:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface CVService {

    CreateCVResponse createCV(CreateCVRequest request);

    GetCVDetailResponse getCVDetailById(String cvId);

    void updateCVById(String cvId, UpdateCVRequest request);

    void downloadCV(String cvId, HttpServletResponse response);

    void deleteCVById(String cvId);
}
