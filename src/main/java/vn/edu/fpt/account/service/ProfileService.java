package vn.edu.fpt.account.service;

import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.dto.request.cv.GetCVOfAccountRequest;
import vn.edu.fpt.account.dto.request.profile.ChangeAvatarRequest;
import vn.edu.fpt.account.dto.request.profile.CreateProfileRequest;
import vn.edu.fpt.account.dto.request.profile.UpdateProfileRequest;
import vn.edu.fpt.account.dto.response.profile.GetCVOfAccountResponse;
import vn.edu.fpt.account.dto.response.profile.GetProfileDetailResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 18:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface ProfileService {

    void createProfile(CreateProfileEvent event);

    void updateProfile(String profileId, UpdateProfileRequest request);

    GetProfileDetailResponse getProfileDetails(String profileId);

    PageableResponse<GetCVOfAccountResponse> getCVOfAccount(String accountId, GetCVOfAccountRequest request);

    void changeAvatar(String profileId, ChangeAvatarRequest request);
}
