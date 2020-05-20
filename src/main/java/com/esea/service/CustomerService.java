package com.esea.service;

import com.esea.model.CustomerInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CustomerService {
    List<CustomerInfo> customerInfoData = new ArrayList<>();

    CustomerService() {
        // customerInfoData.add(new CustomerInfo("elif", "deniz"));
    }

    public CustomerInfo findCustomerInfo(String id) {
        List<CustomerInfo> pList = customerInfoData.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        if (pList != null && !pList.isEmpty()) {
            return pList.get(0);
        }
        return null;
    }
}
