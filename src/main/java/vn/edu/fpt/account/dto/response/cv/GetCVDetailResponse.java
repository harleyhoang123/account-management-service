package vn.edu.fpt.account.dto.response.cv;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

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
public class GetCVDetailResponse implements Serializable {

    private static final long serialVersionUID = 7570066309941982807L;
    private String cvId;
    private String cvName;
    private String description;

}
