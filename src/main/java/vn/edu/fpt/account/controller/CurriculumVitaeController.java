package vn.edu.fpt.account.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.request.cv.CreateCVRequest;
import vn.edu.fpt.account.dto.request.cv.UpdateCVRequest;
import vn.edu.fpt.account.dto.response.cv.CreateCVResponse;
import vn.edu.fpt.account.dto.response.cv.GetCVDetailResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/11/2022 - 10:37
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/cv")
public interface CurriculumVitaeController {

    @PostMapping(value = "/{profile-id}")
    ResponseEntity<GeneralResponse<CreateCVResponse>> createCV(@PathVariable(name = "profile-id") String profileId, @RequestBody CreateCVRequest request);

    @PutMapping(value = "/{cv-id}")
    ResponseEntity<GeneralResponse<Object>> updateCV(@PathVariable("cv-id") String cvId, @RequestBody UpdateCVRequest request);
    @GetMapping("/{cv-id}")
    ResponseEntity<GeneralResponse<GetCVDetailResponse>> getCVDetail(@PathVariable(name = "cv-id") String cvId);

    @GetMapping("/{cv-id}/download")
    void downloadCV(@PathVariable(name = "cv-id") String cvId, HttpServletResponse response);

    @DeleteMapping("/{profile-id}/{cv-id}")
    ResponseEntity<GeneralResponse<Object>> deleteCV(@PathVariable(name = "profile-id") String profileId, @PathVariable(name = "cv-id") String cvId);


}
