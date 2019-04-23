package kr.hs.dgsw.web01blog.Service;


import kr.hs.dgsw.web01blog.Domain.User;

public interface UserService {
    User chkLogin(String id, String pw);
    int addUser(User user);
    User modifyUser(User user);
    boolean removeUser(String id);
}
