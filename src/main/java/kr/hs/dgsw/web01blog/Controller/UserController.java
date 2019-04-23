package kr.hs.dgsw.web01blog.Controller;

import kr.hs.dgsw.web01blog.Domain.User;
import kr.hs.dgsw.web01blog.Protocol.ResponseFormat;
import kr.hs.dgsw.web01blog.Protocol.ResponseType;
import kr.hs.dgsw.web01blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseFormat list(){
        return new ResponseFormat(ResponseType.POST_ADD, "Hello");
    }

    @GetMapping("/login/{id}/{pw}")
    public ResponseFormat login(@PathVariable String id, @PathVariable String pw){
        User u = this.userService.chkLogin(id, pw);
        if(u != null){
            return new ResponseFormat(ResponseType.POST_GET, u);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

    @PostMapping("/regis")
    public ResponseFormat addUser(@RequestBody User user){
        int data = this.userService.addUser(user);
        if(data == 1){
            return new ResponseFormat(ResponseType.FAIL, null);
        }
        else if(data == 2){
            return new ResponseFormat(ResponseType.FAIL, null);
        }
        else{
            return new ResponseFormat(ResponseType.USER_ADD, data);
        }
    }

    @PutMapping("/modify")
    public ResponseFormat modify(@RequestBody User user){
        User u = this.userService.modifyUser(user);
        if(u != null){
            return new ResponseFormat(ResponseType.USER_UPDATE, u);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseFormat removeUser(@PathVariable String id){
        boolean data = this.userService.removeUser(id);
        if(data){
            return new ResponseFormat(ResponseType.USER_DELETE, data);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

}

