package kr.hs.dgsw.web01blog.Service;

import kr.hs.dgsw.web01blog.Domain.User;
import kr.hs.dgsw.web01blog.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User chkLogin(String id, String pw) {
        Optional<User> data = userRepository.findByUserid(id);
        if(data.isPresent()){
            String datapw = data.get().getPassword();
            if(datapw.equals(pw)){
                return data.get();
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    @Override
    public int addUser(User user) {
        Optional<User> found = this.userRepository.findByUserid(user.getAccount());
        Optional<User> found2 = this.userRepository.findByEmail(user.getEmail());
        User u = null;
        if(found.isPresent()){
            return 1;
        }
        else if(found2.isPresent()){
            return 2;
        }
        else {
            u = new User(user.getAccount(), user.getPassword(), user.getUsername(), user.getEmail(), user.getPhone());
            this.userRepository.save(u);
            return 3;
        }
    }

    @Override
    public User modifyUser(User user) {
        return this.userRepository.findById(user.getId())
                .map(found -> {
                    found.setAccount(Optional.ofNullable(user.getAccount()).orElse(user.getAccount()));
                    found.setUsername(Optional.ofNullable(user.getUsername()).orElse(found.getUsername()));
                    found.setPassword(Optional.ofNullable(user.getPassword()).orElse(found.getPassword()));
                    found.setEmail(Optional.ofNullable(user.getEmail()).orElse(found.getEmail()));
                    return this.userRepository.save(found);
                })
                .orElse(null);

    }

    @Override
    public boolean removeUser(String id) {
        try {
            User u = this.userRepository.findByUserid(id).get();
            this.userRepository.delete(u);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
