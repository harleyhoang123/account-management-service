package vn.edu.fpt.account.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.account.dto.common.PageableRequest;
import vn.edu.fpt.account.utils.RequestDataUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:32
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@SuperBuilder
public class GetAccountRequest extends PageableRequest {

    private static final long serialVersionUID = 6388530270206097979L;
    private String accountId;
    private String username;
    private String fullName;
    private String roleId;
    private List<String> role;
    private String email;
    private Boolean isEnable;
    private Boolean isCredentialNonExpired;
    private Boolean isNonExpired;
    private Boolean isNonLocked;
    private String createdDateFrom;
    private String createdDateTo;
    private String lastModifiedDateFrom;
    private String lastModifiedDateTo;

    public String getRoleId() {
        return roleId;
    }

    public List<String> getRole() {
        return role;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return RequestDataUtils.convertSearchableData(username);
    }

    public String getFullName() {
        return RequestDataUtils.convertSearchableData(fullName);
    }

    public String getEmail() {
        return RequestDataUtils.convertSearchableData(email);
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public Boolean getCredentialNonExpired() {
        return isCredentialNonExpired;
    }

    public Boolean getNonExpired() {
        return isNonExpired;
    }

    public Boolean getNonLocked() {
        return isNonLocked;
    }

    public LocalDateTime getCreatedDateFrom() {
        return RequestDataUtils.convertDateTimeFrom(createdDateFrom);
    }

    public LocalDateTime getCreatedDateTo() {
        return RequestDataUtils.convertDateTimeTo(createdDateTo);
    }

    public LocalDateTime getLastModifiedDateFrom() {
        return RequestDataUtils.convertDateTimeFrom(lastModifiedDateFrom);
    }

    public LocalDateTime getLastModifiedDateTo() {
        return RequestDataUtils.convertDateTimeTo(lastModifiedDateTo);
    }
}
