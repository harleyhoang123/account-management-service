package vn.edu.fpt.account.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
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
    ResponseEntity<GeneralResponse<Object>> createProfile(@RequestBody CreateProfileEvent event);

    @PutMapping("/{profile-id}")
    ResponseEntity<GeneralResponse<Object>> updateProfiles(@PathVariable("profile-id") String parameter, @RequestBody UpdateProfileRequest request);

    @GetMapping("/{profile-id}")
    ResponseEntity<GeneralResponse<GetProfileDetailResponse>> getProfileDetails(@PathVariable(name = "profile-id") String profileId);

    @GetMapping("/{account-id}/cv")
    ResponseEntity<GeneralResponse<PageableResponse<GetCVOfAccountResponse>>> getCVOfAccount(
            @PathVariable(name = "account-id") String accountId,
            @RequestParam(name = "cv-id", required = false) String cvId,
            @RequestParam(name = "cv-name", required = false) String cvName,
            @RequestParam(name = "cv-name-sort-by", required = false) String cvNameSortBy,
            @RequestParam(name = "created-by", required = false) String createdBy,
            @RequestParam(name = "created-date-from", required = false) String createdDateFrom,
            @RequestParam(name = "created-date-to", required = false) String createdDateTo,
            @RequestParam(name = "created-date-sort-by", required = false) String createdDateSortBy,
            @RequestParam(name = "last-modified-by", required = false) String lastModifiedBy,
            @RequestParam(name = "last-modified-from", required = false) String lastModifiedDateFrom,
            @RequestParam(name = "last-modified-to", required = false) String lastModifiedDateTo,
            @RequestParam(name = "last-modified-date-sort-by", required = false) String lastModifiedDateSortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size);

    @PostMapping(value = "/{profile-id}/avatar")
    ResponseEntity<GeneralResponse<Object>> changeAvatar(@PathVariable(name = "profile-id") String profileId, @RequestBody ChangeAvatarRequest request);

}
