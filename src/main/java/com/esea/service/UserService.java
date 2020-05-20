package com.esea.service;

import com.esea.model.Token;
import com.esea.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    List<User> userData = new ArrayList<>();

    UserService() {
        userData.add(new User("elif", "deniz"));
    }
    public User findUser(String userName, String password){
        List<User> pList = userData.stream()
                .filter(p -> p.getUserName().equalsIgnoreCase(userName)
                        && p.getPassword().equalsIgnoreCase(password))
                .collect(Collectors.toList());
        if (pList != null && !pList.isEmpty()) {
            return pList.get(0);
        }
        return null;
    }
    public User generateToken(User user){
        Token t=new Token();
        user.setToken(t);
        return user;
    }
    public boolean isValidToken(String token){
        try {
            List<User> pList = userData.stream()
                    .filter(p -> p.getToken().getTokenInfo().equalsIgnoreCase(token)
                    )
                    .collect(Collectors.toList());
            for(User u:pList){
                if(u.getToken().getTokenEndDate().after(new Date())){
                    return true;
                }
            }
        }catch (Exception e){

        }

        return false;

    }

}
