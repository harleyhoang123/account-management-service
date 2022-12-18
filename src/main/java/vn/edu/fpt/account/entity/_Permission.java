package vn.edu.fpt.account.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.account.entity.common.Auditor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class _Permission extends Auditor implements Serializable {

    private static final long serialVersionUID = -234481688324162754L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String permissionId;
    @Field(name = "permission_name")
    @Indexed(unique = true)
    private String permissionName;
    @Field(name = "is_enable")
    @Builder.Default
    private Boolean isEnable = true;
    @Field(name = "description")
    private String description;

}
