package kr.hs.dgsw.web01blog.Controller;


import kr.hs.dgsw.web01blog.Domain.Post;
import kr.hs.dgsw.web01blog.Protocol.PostUsernameProtocol;
import kr.hs.dgsw.web01blog.Protocol.ResponseFormat;
import kr.hs.dgsw.web01blog.Protocol.ResponseType;
import kr.hs.dgsw.web01blog.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/postlist")
    public ResponseFormat list(){
        List<PostUsernameProtocol> data = this.postService.listAllPosts();
        if(data.isEmpty()){
            return new ResponseFormat(ResponseType.FAIL, data);
        }
        else{
            return new ResponseFormat(ResponseType.POST_GET, null);
        }
    }

    @GetMapping("/postfind/{id}")
    public ResponseFormat findComments(@PathVariable Long id) {
        Post p = this.postService.findPost(id);
        if(p != null){
            return new ResponseFormat(ResponseType.POST_GET, p);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

    @PostMapping("/postadd")
    public ResponseFormat addPost(@RequestBody Post post){
        Post p = this.postService.addPost(post);
        if(p != null){
            return new ResponseFormat(ResponseType.POST_ADD, p);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

    @PutMapping("/postmodify")
    public ResponseFormat modify(@RequestBody Post post){
        Post p = this.postService.modifyPost(post);
        if(p != null){
            return new ResponseFormat(ResponseType.POST_UPDATE, p);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

    @DeleteMapping("/postdelete/{id}")
    public ResponseFormat removePost(@PathVariable Long id){
        boolean data = this.postService.deletePost(id);
        if(data){
            return new ResponseFormat(ResponseType.POST_DELETE, data);
        }
        else{
            return new ResponseFormat(ResponseType.FAIL, null);
        }
    }

}
