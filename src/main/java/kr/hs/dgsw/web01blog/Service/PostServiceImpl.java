package kr.hs.dgsw.web01blog.Service;


import kr.hs.dgsw.web01blog.Domain.Post;
import kr.hs.dgsw.web01blog.Domain.User;
import kr.hs.dgsw.web01blog.Protocol.PostUsernameProtocol;
import kr.hs.dgsw.web01blog.Repository.PostRepository;
import kr.hs.dgsw.web01blog.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostUsernameProtocol> listAllPosts() {
        List<Post> postList = this.postRepository.findAll();
        List<PostUsernameProtocol> cupList = new ArrayList<>();
        postList.forEach(post -> {
            Optional<User> found = this.userRepository.findById(post.getUserId());
            String username = (found.isPresent()) ? found.get().getUsername() : null;
            cupList.add(new PostUsernameProtocol(post, username));
        });
        return cupList;
    }

    @Override
    public Post addPost(Post post) {
        Optional<User> user = this.userRepository.findById(post.getUserId());
        if(user.isPresent()){
            //사진 추가 시 Attachment List로 추가시켜야 하는 코드 추가 필요
            this.postRepository.save(post);
            return new PostUsernameProtocol(post, user.get().getUsername());
        }
        else{
            return null;
        }
    }

    @Override
    public Post findPost(Long id) {
        return this.postRepository.findTopByUserIdOrderByIdDesc(id)
                .orElse(null);
    }

    @Override
    public Post modifyPost(Post post) {
        Post modify = this.postRepository.findById(post.getId())
                .map(found -> {
                    found.setContent(Optional.ofNullable(post.getContent()).orElse(found.getContent()));
                    //Attachment List 수정 코드 필요
                    return this.postRepository.save(found);
                })
                .orElse(null);
        return new PostUsernameProtocol(modify, this.userRepository.findById(post.getUserId()).get().getUsername());
    }

    @Override
    public boolean deletePost(Long postid) {
        try{
            this.postRepository.deleteById(postid);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
