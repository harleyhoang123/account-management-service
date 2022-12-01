package vn.edu.fpt.account.dto.request.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.account.config.datetime.CustomDateDeserializer;
import vn.edu.fpt.account.config.datetime.CustomDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:44
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateProfileRequest implements Serializable {

    private static final long serialVersionUID = -5543084924933646003L;
    private String gender;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String studentId;
    private String studentCode;
    private String major;
    private Integer currentTermNo;
    private String specialized;
}
