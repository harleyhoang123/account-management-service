package vn.edu.fpt.account.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 02/09/2022 - 22:05
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationUtils {


    public static String addRolePrefix(String role) {
        return "ROLE_" + role;
    }

    public static String addPermissionPrefix(String role, String permission) {
        return role + ":" + permission;
    }

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        if (phoneNumber.matches("^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")) {
            if (phoneNumber.charAt(0) == '0') {
                return phoneNumber.replaceFirst("0", "+84");
            } else {
                return "+84" + phoneNumber;
            }
        } else {
            return null;
        }

    }
}
