package vn.edu.fpt.account.dto.response.profile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.account.config.datetime.CustomDateDeserializer;
import vn.edu.fpt.account.config.datetime.CustomDateSerializer;
import vn.edu.fpt.account.dto.common.AuditableResponse;

import java.time.LocalDate;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:45
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class GetProfileDetailResponse extends AuditableResponse {

    private static final long serialVersionUID = 5684387979889249940L;
    private String profileId;
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
    private String avatar;
}
