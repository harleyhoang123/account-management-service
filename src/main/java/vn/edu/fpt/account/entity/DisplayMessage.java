package vn.edu.fpt.account.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:37
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DisplayMessage implements Serializable {

    private static final long serialVersionUID = -2757240779293611765L;
    private String displayMessageId;
    private String code;
    @Builder.Default
    private String language = "en";
    private String message;

}
