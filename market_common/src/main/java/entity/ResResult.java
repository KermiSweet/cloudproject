package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : Kermi
 * @Date : 2020/2/12 13:05
 * @Version : 1.0
 */
@Data
@NoArgsConstructor
public class ResResult {

    private boolean flag;   //是否成功
    private Integer code;   //返回码
    private String message; //返回信息
    private Object data;    //返回数据

    public ResResult(boolean flag, Integer code, String message, Object data) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResResult(boolean flag, Integer code, String message) {
        super();
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

}
