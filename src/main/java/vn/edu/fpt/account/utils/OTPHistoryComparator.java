package vn.edu.fpt.account.utils;

import vn.edu.fpt.account.entity.OTPHistory;

import java.util.Comparator;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/12/2022 - 16:50
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public class OTPHistoryComparator implements Comparator<OTPHistory> {
    @Override
    public int compare(OTPHistory o1, OTPHistory o2) {
        if(o1.getExpiredTime().isAfter(o2.getExpiredTime())){
            return 1;
        }else{
            return 0;
        }
    }
}
