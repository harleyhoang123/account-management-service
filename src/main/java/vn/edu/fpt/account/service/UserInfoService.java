package vn.edu.fpt.account.service;

import vn.edu.fpt.account.dto.cache.UserInfo;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 07:54
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface UserInfoService {

    UserInfo getUserInfo(String accountId);

    void addAvatarToUserInfo(String accountId, String avatarURL);

}
