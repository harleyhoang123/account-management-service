package vn.edu.fpt.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.dto.request.cv.CreateCVRequest;
import vn.edu.fpt.account.dto.request.cv.UpdateCVRequest;
import vn.edu.fpt.account.dto.response.cv.CreateCVResponse;
import vn.edu.fpt.account.dto.response.cv.GetCVDetailResponse;
import vn.edu.fpt.account.entity.CurriculumVitae;
import vn.edu.fpt.account.entity.Profile;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.factory.ResponseFactory;
import vn.edu.fpt.account.repository.CVRepository;
import vn.edu.fpt.account.repository.ProfileRepository;
import vn.edu.fpt.account.service.CVService;
import vn.edu.fpt.account.service.S3BucketStorageService;
import vn.edu.fpt.account.service.UserInfoService;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 18:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class CVServiceImpl implements CVService {

    private final ProfileRepository profileRepository;
    private final S3BucketStorageService s3BucketStorageService;
    private final CVRepository cvRepository;
    private final UserInfoService userInfoService;

    @Override
    public CreateCVResponse createCV(String profileId, CreateCVRequest request) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID not exist"));

        s3BucketStorageService.uploadFile(request.getCv());
        CurriculumVitae cv = CurriculumVitae.builder()
                .cvName(request.getCvName())
                .description(request.getDescription())
                .build();
        return null;
    }

    @Override
    public GetCVDetailResponse getCVDetailById(String cvId) {
        return null;
    }

    @Override
    public void updateCVById(String cvId, UpdateCVRequest request) {

    }

    @Override
    public void downloadCV(String cvId, HttpServletResponse response) {

    }

    @Override
    public void deleteCVById(String cvId) {

    }
}
