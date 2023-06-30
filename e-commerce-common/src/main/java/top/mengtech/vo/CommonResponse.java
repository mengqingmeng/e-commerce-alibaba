package top.mengtech.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用相应
 * {
 *     "code": 0,
 *     "message": "",
 *     "data": {}
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;

    /**
     * 相应数据
     */
    private T data;

    public CommonResponse(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
