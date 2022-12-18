package vn.edu.fpt.account.service;

import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.request.role.*;
import vn.edu.fpt.account.dto.response.role.CreateRoleResponse;
import vn.edu.fpt.account.dto.response.role.GetRoleResponse;
import vn.edu.fpt.account.entity._Role;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 14:23
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface RoleService {

    void init();

    CreateRoleResponse createRole(CreateRoleRequest request);

    void updateRole(String roleId, UpdateRoleRequest request);

    void deleteRole(String roleId);

    void addPermissionToRole(String roleId, AddPermissionToRoleRequest permissionId);

    PageableResponse<GetRoleResponse> getRoleByCondition(GetRoleRequest request);

    void removePermissionFromRole(String roleId, String permissionId);


    GetRoleResponse convertToRoleResponse(_Role role);
}
