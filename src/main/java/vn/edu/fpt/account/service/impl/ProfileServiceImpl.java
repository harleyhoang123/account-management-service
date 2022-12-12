package vn.edu.fpt.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.dto.common.CreateFileRequest;
import vn.edu.fpt.account.dto.cache.UserInfo;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.common.UserInfoResponse;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.dto.request.cv.GetCVOfAccountRequest;
import vn.edu.fpt.account.dto.request.profile.ChangeAvatarRequest;
import vn.edu.fpt.account.dto.request.profile.CreateProfileRequest;
import vn.edu.fpt.account.dto.request.profile.UpdateProfileRequest;
import vn.edu.fpt.account.dto.response.profile.GetCVOfAccountResponse;
import vn.edu.fpt.account.dto.response.profile.GetProfileDetailResponse;
import vn.edu.fpt.account.entity.CurriculumVitae;
import vn.edu.fpt.account.entity.Profile;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.repository.BaseMongoRepository;
import vn.edu.fpt.account.repository.ProfileRepository;
import vn.edu.fpt.account.service.ProfileService;
import vn.edu.fpt.account.service.S3BucketStorageService;
import vn.edu.fpt.account.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 18:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserInfoService userInfoService;
    private final S3BucketStorageService s3BucketStorageService;
    private final MongoTemplate mongoTemplate;
    @Value("${application.account.cloudfront}")
    private String accountCloudFront;

    @Override
    public void createProfile(CreateProfileEvent event) {
        if (!ObjectId.isValid(event.getAccountId())) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID invalid");
        }
        if (profileRepository.findById(event.getAccountId()).isPresent()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile already exist");
        }

        Profile profile = Profile.builder()
                .profileId(event.getAccountId())
                .build();
        try {
            profileRepository.save(profile);
            log.info("Create profile info success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save profile to database: " + ex.getMessage());
        }
    }

    @Override
    public void updateProfile(String profileId, UpdateProfileRequest request) {
        if (!ObjectId.isValid(profileId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID invalid");
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID not exist"));
        if (Objects.nonNull(request.getGender())) {
            profile.setGender(request.getGender());
        }
        if (Objects.nonNull(request.getAddress())) {
            profile.setAddress(request.getAddress());
        }
        if (Objects.nonNull(request.getMajor())) {
            profile.setMajor(request.getMajor());
        }
        if (Objects.nonNull(request.getDateOfBirth())) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (Objects.nonNull(request.getPhoneNumber())) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }
        if (Objects.nonNull(request.getCurrentTermNo())) {
            profile.setCurrentTermNo(request.getCurrentTermNo());
        }
        if (Objects.nonNull(request.getStudentCode())) {
            profile.setStudentCode(request.getStudentCode());
        }
        if (Objects.nonNull(request.getStudentId())) {
            profile.setStudentId(request.getStudentId());
        }
        if (Objects.nonNull(request.getSpecialized())) {
            profile.setSpecialized(request.getSpecialized());
        }

        if (Objects.nonNull(request.getAward())) {
            profile.setSpecialized(request.getAward());
        }
        if (Objects.nonNull(request.getInterest())) {
            profile.setInterest(request.getInterest());
        }
        if (Objects.nonNull(request.getDescription())) {
            profile.setDescription(request.getDescription());
        }
        try {
            profileRepository.save(profile);
            log.info("Update profile success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update profile in database: " + ex.getMessage());
        }
    }

    @Override
    public GetProfileDetailResponse getProfileDetails(String profileId) {
        if (!ObjectId.isValid(profileId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID invalid");
        }
        UserInfo userInfo = userInfoService.getUserInfo(profileId);
        if(Objects.isNull(userInfo)){
            throw new BusinessException("User info not contain in Redis");
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID not exist"));
        return GetProfileDetailResponse.builder()
                .profileId(profile.getProfileId())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .major(profile.getMajor())
                .username(userInfo.getUsername())
                .email(userInfo.getEmail())
                .roles(userInfo.getRoles())
                .avatar(userInfo.getAvatar())
                .fullName(userInfo.getFullName())
                .dateOfBirth(profile.getDateOfBirth())
                .phoneNumber(profile.getPhoneNumber())
                .currentTermNo(profile.getCurrentTermNo())
                .studentCode(profile.getStudentCode())
                .studentId(profile.getStudentId())
                .specialized(profile.getSpecialized())
                .avatar(profile.getAvatar())
                .createdBy(UserInfoResponse.builder()
                        .accountId(profile.getCreatedBy())
                        .userInfo(userInfoService.getUserInfo(profile.getCreatedBy()))
                        .build())
                .createdDate(profile.getCreatedDate())
                .lastModifiedBy(UserInfoResponse.builder()
                        .accountId(profile.getLastModifiedBy())
                        .userInfo(userInfoService.getUserInfo(profile.getLastModifiedBy()))
                        .build())
                .lastModifiedDate(profile.getLastModifiedDate())
                .build();
    }

    @Override
    public PageableResponse<GetCVOfAccountResponse> getCVOfAccount(String accountId, GetCVOfAccountRequest request) {
        Query query = new Query();
        if (Objects.nonNull(request.getCvId())) {
            query.addCriteria(Criteria.where("_id").is(request.getCvId()));
        }
        if (Objects.nonNull(request.getCvId())) {
            query.addCriteria(Criteria.where("cv_name").is(request.getCvName()));
        }
        if (!ObjectId.isValid(accountId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID not exist");
        }

        Profile profile = profileRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID not exist"));

        List<CurriculumVitae> listCV = profile.getCv();
        List<ObjectId> listCvId = listCV.stream().map(CurriculumVitae::getCvId).map(ObjectId::new).collect(Collectors.toList());
        query.addCriteria(Criteria.where("_id").in(listCvId));

        BaseMongoRepository.addCriteriaWithAuditable(query, request);
        Long totalElements = mongoTemplate.count(query, CurriculumVitae.class);
        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);
        List<CurriculumVitae> cvs = mongoTemplate.find(query, CurriculumVitae.class);

        List<GetCVOfAccountResponse> getCVOfAccountResponses = cvs.stream().map(this::convertCVToGetCVOfAccountResponse).collect(Collectors.toList());
        return new PageableResponse<>(request, totalElements, getCVOfAccountResponses);
    }

    @Override
    public void changeAvatar(String profileId, ChangeAvatarRequest request) {
        if (!ObjectId.isValid(profileId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID invalid");
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Profile ID not exist"));

        if (request.getAvatar() == null) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Avatar invalid");
        }
        CreateFileRequest createFileRequest = request.getAvatar();
        String fileKey = UUID.randomUUID().toString();
        s3BucketStorageService.uploadFile(createFileRequest, fileKey);
        profile.setAvatar(accountCloudFront+fileKey);
        log.info("Change avatar success");

        try {
            profile = profileRepository.save(profile);
        } catch (Exception ex) {
            throw new BusinessException("Can't update avatar in profile");
        }
        userInfoService.addAvatarToUserInfo(profileId, profile.getAvatar());
    }


    private GetCVOfAccountResponse convertCVToGetCVOfAccountResponse(CurriculumVitae curriculumVitae) {
        return GetCVOfAccountResponse.builder()
                .cvId(curriculumVitae.getCvId())
                .cvName(curriculumVitae.getCvName())
                .build();
    }
}
