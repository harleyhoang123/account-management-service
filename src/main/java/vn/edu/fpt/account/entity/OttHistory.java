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

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 25/12/2022 - 01:02
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "ott_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OttHistory implements Serializable {

    private static final long serialVersionUID = -3649247870616732763L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String ottId;
    @Field(name = "ott")
    private String ott;
}
