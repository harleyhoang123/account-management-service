package vn.edu.fpt.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/12/2022 - 15:20
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "otp_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OTPHistory implements Serializable {

    private static final long serialVersionUID = -4681164902806421976L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String otpId;
    @Field(name = "account_id")
    private String accountId;
    @Field(name = "code")
    private String code;
    @Field(name = "expired_time")
    private LocalDateTime expiredTime;
    @Field(name = "data")
    private String data;
    @Field(name = "enable")
    private Boolean enable;
}
