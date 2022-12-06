package vn.edu.fpt.account.dto.request.cv;

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
 * @created : 01/12/2022 - 17:48
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateCVRequest implements Serializable {

    private static final long serialVersionUID = 2487988312120991051L;
    private String cvName;
    private String description;
    private CreateFileRequest cv;
}
