package kr.hs.dgsw.web01blog.Service;


import kr.hs.dgsw.web01blog.Domain.Post;
import kr.hs.dgsw.web01blog.Protocol.PostUsernameProtocol;

import java.util.List;

public interface PostService {
    List<PostUsernameProtocol> listAllPosts();
    int countPost(Long userId);
    Post addPost(Post post);
    Post findPost(Long id);
    Post modifyPost(Post post);
    boolean deletePost(Long postid);
}
