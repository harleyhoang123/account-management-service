package vn.edu.fpt.account.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.account.entity.common.Auditor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:39
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "curriculum_vitae")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class CurriculumVitae extends Auditor {

    private static final long serialVersionUID = 3270598026059811449L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String cvId;
    @Field(name = "cv_name")
    private String cvName;
    @Field(name = "cv_key")
    private String cvKey;
}
