package vn.edu.fpt.account.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.controller.CurriculumVitaeController;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.request.cv.CreateCVRequest;
import vn.edu.fpt.account.dto.request.cv.UpdateCVRequest;
import vn.edu.fpt.account.dto.response.cv.CreateCVResponse;
import vn.edu.fpt.account.dto.response.cv.GetCVDetailResponse;
import vn.edu.fpt.account.factory.ResponseFactory;
import vn.edu.fpt.account.service.CVService;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/11/2022 - 10:06
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class CurriculumVitaeControllerImpl implements CurriculumVitaeController {

    private final ResponseFactory responseFactory;
    private final CVService cvService;

    @Override
    public ResponseEntity<GeneralResponse<CreateCVResponse>> createCV(String profileId, CreateCVRequest request) {
        return responseFactory.response(cvService.createCV(profileId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateCV(String cvId, UpdateCVRequest request) {
        cvService.updateCVById(cvId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<GetCVDetailResponse>> getCVDetail(String cvId) {
        return responseFactory.response(cvService.getCVDetailById(cvId));
    }

    @Override
    public void downloadCV(String cvId, HttpServletResponse response) {
        cvService.downloadCV(cvId, response);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteCV(String cvId) {
        cvService.deleteCVById(cvId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
