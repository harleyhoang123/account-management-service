package vn.edu.fpt.account.dto.response.profile;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:47
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class GetCVOfAccountResponse extends AuditableResponse {

    private static final long serialVersionUID = -5646159877445859842L;
    private String cvId;
    private String cvName;
}
