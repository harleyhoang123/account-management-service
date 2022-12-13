package vn.edu.fpt.account.dto.request.cv;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import vn.edu.fpt.account.dto.common.AuditableRequest;
import vn.edu.fpt.account.utils.RequestDataUtils;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@SuperBuilder
public class GetCVOfAccountRequest extends AuditableRequest {

    private static final long serialVersionUID = 8940627563889743527L;
    private String cvId;
    private String cvName;

    public ObjectId getCvId() {
        return RequestDataUtils.convertObjectId(cvId);
    }

    public String getCvName() {
        return RequestDataUtils.convertSearchableData(cvName);
    }
}
