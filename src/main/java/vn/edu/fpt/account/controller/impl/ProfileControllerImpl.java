package vn.edu.fpt.account.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.controller.ProfileController;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.common.SortableRequest;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.dto.request.cv.GetCVOfAccountRequest;
import vn.edu.fpt.account.dto.request.profile.ChangeAvatarRequest;
import vn.edu.fpt.account.dto.request.profile.CreateProfileRequest;
import vn.edu.fpt.account.dto.request.profile.UpdateProfileRequest;
import vn.edu.fpt.account.dto.response.profile.GetCVOfAccountResponse;
import vn.edu.fpt.account.dto.response.profile.GetProfileDetailResponse;
import vn.edu.fpt.account.factory.ResponseFactory;
import vn.edu.fpt.account.service.ProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<GeneralResponse<PageableResponse<GetCVOfAccountResponse>>> getCVOfAccount(String accountId,
                                                                                                    String cvId,
                                                                                                    String cvName,
                                                                                                    String cvNameSortBy,
                                                                                                    String createdBy,
                                                                                                    String createdDateFrom,
                                                                                                    String createdDateTo,
                                                                                                    String createdDateSortBy,
                                                                                                    String lastModifiedBy,
                                                                                                    String lastModifiedDateFrom,
                                                                                                    String lastModifiedDateTo,
                                                                                                    String lastModifiedDateSortBy,
                                                                                                    Integer page,
                                                                                                    Integer size) {
        List<SortableRequest> sortableRequests = new ArrayList<>();
        if(Objects.nonNull(cvNameSortBy)) {
            sortableRequests.add(new SortableRequest("cv_name", cvNameSortBy));
        }
        if(Objects.nonNull(createdDateSortBy)){
            sortableRequests.add(new SortableRequest("created_date", createdDateSortBy));
        }
        if(Objects.nonNull(lastModifiedDateSortBy)){
            sortableRequests.add(new SortableRequest("last_modified_date", lastModifiedDateSortBy));
        }
        GetCVOfAccountRequest request = GetCVOfAccountRequest.builder()
                .cvId(cvId)
                .cvName(cvName)
                .createdBy(createdBy)
                .createdDateFrom(createdDateFrom)
                .createdDateTo(createdDateTo)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedDateFrom(lastModifiedDateFrom)
                .lastModifiedDateTo(lastModifiedDateTo)
                .page(page)
                .size(size)
                .sortBy(sortableRequests)
                .build();
        return responseFactory.response(profileService.getCVOfAccount(accountId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> changeAvatar(String profileId, ChangeAvatarRequest request) {
        profileService.changeAvatar(profileId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
