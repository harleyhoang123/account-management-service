package vn.edu.fpt.account.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateProfileEvent implements Serializable {
    private static final long serialVersionUID = 416998387119684433L;
    private String accountId;
}
