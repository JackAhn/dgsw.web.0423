package kr.hs.dgsw.web01blog.Protocol;

import lombok.Data;

@Data
public class ResponseFormat {
    private int code;
    private String desc;
    private Object data;

    public ResponseFormat(ResponseType type, Object data, Object option){
        this.code = type.code();;
        this.desc = option instanceof Long || option instanceof String ? String.format(type.desc(), option) : type.desc();
        this.data = data;
    }

    public ResponseFormat(ResponseType type, Object data){
        this(type, data, null);
    }
}
