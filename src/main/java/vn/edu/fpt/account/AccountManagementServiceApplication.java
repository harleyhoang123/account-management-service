package vn.edu.fpt.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import vn.edu.fpt.account.service.AccountService;
import vn.edu.fpt.account.service.PermissionService;
import vn.edu.fpt.account.service.RoleService;

@SpringBootApplication
public class AccountManagementServiceApplication {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    public static void main(String[] args) {
        SpringApplication.run(AccountManagementServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDefaultData(){
        permissionService.init();
        roleService.init();
        accountService.init();
    }
}
