package vn.edu.fpt.account.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.request.profile.ChangeAvatarRequest;
import vn.edu.fpt.account.dto.request.profile.CreateProfileRequest;
import vn.edu.fpt.account.dto.request.profile.UpdateProfileRequest;
import vn.edu.fpt.account.dto.response.profile.GetCVOfAccountResponse;
import vn.edu.fpt.account.dto.response.profile.GetProfileDetailResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/11/2022 - 10:06
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/profiles")
public interface ProfileController {

    @PostMapping(value = "/profile")
    ResponseEntity<GeneralResponse<Object>> createProfile(@RequestBody CreateProfileRequest request);

    @PutMapping("/{profile-id}")
    ResponseEntity<GeneralResponse<Object>> updateProfiles(@PathVariable("profile-id") String parameter, @RequestBody UpdateProfileRequest request);

    @GetMapping("/{profile-id}")
    ResponseEntity<GeneralResponse<GetProfileDetailResponse>> getProfileDetails(@PathVariable(name = "profile-id") String profileId);

    @GetMapping("/{account-id}/cv")
    ResponseEntity<GeneralResponse<PageableResponse<GetCVOfAccountResponse>>> getCVOfAccount(@PathVariable(name = "account-id") String accountId);

    @PostMapping(value = "/{profile-id}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<GeneralResponse<Object>> changeAvatar(@PathVariable(name = "profile-id") String profileId, @ModelAttribute ChangeAvatarRequest request);

}
