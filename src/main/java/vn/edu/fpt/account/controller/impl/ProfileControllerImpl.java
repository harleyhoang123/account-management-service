package vn.edu.fpt.account.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.controller.ProfileController;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.dto.request.profile.ChangeAvatarRequest;
import vn.edu.fpt.account.dto.request.profile.CreateProfileRequest;
import vn.edu.fpt.account.dto.request.profile.UpdateProfileRequest;
import vn.edu.fpt.account.dto.response.profile.GetCVOfAccountResponse;
import vn.edu.fpt.account.dto.response.profile.GetProfileDetailResponse;
import vn.edu.fpt.account.factory.ResponseFactory;
import vn.edu.fpt.account.service.ProfileService;

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
public class ProfileControllerImpl implements ProfileController {

    private final ResponseFactory responseFactory;
    private final ProfileService profileService;

    @Override
    public ResponseEntity<GeneralResponse<Object>> createProfile(CreateProfileEvent event) {
        profileService.createProfile(event);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updateProfiles(String profileId, UpdateProfileRequest request) {
        profileService.updateProfile(profileId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<GetProfileDetailResponse>> getProfileDetails(String profileId) {
        return responseFactory.response(profileService.getProfileDetails(profileId));
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetCVOfAccountResponse>>> getCVOfAccount(String accountId) {
        return responseFactory.response(profileService.getCVOfAccount(accountId));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> changeAvatar(String profileId, ChangeAvatarRequest request) {
        profileService.changeAvatar(profileId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
