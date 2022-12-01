package vn.edu.fpt.account.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.dto.cache.UserInfo;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.service.UserInfoService;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 07:55
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public UserInfo getUserInfo(String accountId) {
        try {
            String userInfoStr = redisTemplate.opsForValue().get(String.format("userinfo:%s", accountId));
            return objectMapper.readValue(userInfoStr, UserInfo.class);
        }catch (Exception ex){
            log.error("Can't get userinfo in redis: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public void addAvatarToUserInfo(String accountId, String avatarURL) {
        try {
            String userInfoStr = redisTemplate.opsForValue().get(String.format("userinfo:%s", accountId));
            UserInfo userInfo = objectMapper.readValue(userInfoStr, UserInfo.class);
            userInfo.setAvatar(avatarURL);
            pushUserInfoToRedis(accountId, userInfo);
        }catch (Exception ex){
            throw new BusinessException("Can't avatar to userinfo: "+ ex.getMessage());
        }
    }

    private void pushUserInfoToRedis(String accountId, UserInfo userInfo){
        try {
            redisTemplate.opsForValue().set(String.format("userinfo:%s", accountId),objectMapper.writeValueAsString(userInfo));
        }catch (Exception ex){
            throw new BusinessException("Can't push userinfo to redis: "+ ex.getMessage());
        }
    }
}
