package kr.hs.dgsw.web01blog.Domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storedPath;

    private Long postId;

    public Attachment(){

    }

    public Attachment(String storedPath, Long postId){
        this.storedPath = storedPath;
        this.postId = postId;
    }
}
