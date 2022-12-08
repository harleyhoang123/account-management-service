package vn.edu.fpt.account.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.account.dto.common.CreateFileRequest;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 19:14
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangeAvatarRequest implements Serializable {

    private static final long serialVersionUID = 3747410200687205822L;
    private CreateFileRequest avatar;
}
