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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        List<CurriculumVitae> listCv = profile.getCv();
        String cvName = request.getCvName();

        if (listCv != null) {
            if (listCv.stream().anyMatch(m -> m.getCvName().equals(cvName))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Cv name is already exist");
            }
        } else {
            listCv = new ArrayList<>();
        }

        String cvKey = s3BucketStorageService.uploadFile(request.getCv());
        CurriculumVitae cv = CurriculumVitae.builder()
                .cvName(request.getCvName())
                .description(request.getDescription())
                .cvKey(cvKey)
                .build();
        try { 
            cv = cvRepository.save(cv);
            log.info("Create cv success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create cv in database: " + ex.getMessage());
        }
        listCv.add(cv);
        profile.setCv(listCv);
        try {
            profileRepository.save(profile);
            log.info("Add cv in profile to database");
        } catch (Exception ex) {
            throw new BusinessException("Can't add cv in profile to database: " + ex.getMessage());
        }


        return CreateCVResponse.builder()
                .cvId(cv.getCvId())
                .build();
    }

    @Override
    public GetCVDetailResponse getCVDetailById(String cvId) {
        CurriculumVitae cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "CV ID not exist"));
        GetCVDetailResponse getCVDetailResponse = GetCVDetailResponse.builder()
                .cvId(cv.getCvId())
                .cvName(cv.getCvName())
                .description(cv.getDescription())
                .build();
        return getCVDetailResponse;
    }

    @Override
    public void updateCVById(String cvId, UpdateCVRequest request) {
        CurriculumVitae cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "CV ID not exist"));
        if (Objects.nonNull(request.getCvName())) {
            cv.setCvName(request.getCvName());
        }
        if (Objects.nonNull(request.getDescription())) {
            cv.setDescription(request.getDescription());
        }
        try {
            cvRepository.save(cv);
            log.info("Update cv success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update cv: " + ex.getMessage());
        }
    }

    @Override
    public void downloadCV(String cvId, HttpServletResponse response) {
        CurriculumVitae cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "CV ID not exist"));
        s3BucketStorageService.downloadFile(cv.getCvKey(), response);
    }

    @Override
    public void deleteCVById(String cvId) {
        CurriculumVitae cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "CV ID not exist"));
        try {
            cvRepository.deleteById(cvId);
            log.info("Delete cv success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete cv: " + ex.getMessage());
        }
    }
}
