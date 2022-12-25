package vn.edu.fpt.account.dto.response.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.account.config.datetime.CustomDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 8619678507118001064L;
    private String accountId;
    private String username;
    private String email;
    private String fullName;
    private List<String> role;
    private String avatar;
    private String token;
    private String refreshToken;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private LocalDateTime tokenExpireTime;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private LocalDateTime refreshTokenExpireTime;
    private String documentId;
}
