package vn.edu.fpt.account.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.fpt.account.dto.request.account.LoginRequest;
import vn.edu.fpt.account.entity.Account;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 20:20
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface _TokenService {

    Optional<Authentication> getAuthenticationFromToken(String token);

    String generateToken(Account account, UserDetails userDetails);

    LocalDateTime getExpiredTimeFromToken(String token);

    String generateRefreshToken(LoginRequest request);

    LoginRequest getLoginRequestFromToken(String token);

    String generateOTT(String username, String projectId);

    String getOTTClaims(String ott, String key);

}
