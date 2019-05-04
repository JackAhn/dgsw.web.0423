package kr.hs.dgsw.web01blog.Repository;


import kr.hs.dgsw.web01blog.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findTopByUserIdOrderByIdDesc(Long id);
    List<Post> findByUserId(Long id);
}
