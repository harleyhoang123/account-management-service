package vn.edu.fpt.account.dto.request.account;

import lombok.*;
import vn.edu.fpt.account.dto.common.PageableRequest;
import vn.edu.fpt.account.utils.RequestDataUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 23/12/2022 - 21:10
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Builder
public class GetAccountNotInLabRequest extends PageableRequest {

    private static final long serialVersionUID = -6437585542255438403L;
    private String username;
    private List<String> accountIds;

    public String getUsername() {
        return RequestDataUtils.convertSearchableData(username);
    }

    public List<String> getAccountIds() {
        return accountIds;
    }
}
