package entity;

/**
 * @Author : Kermi
 * @Date : 2020/2/12 13:12
 * @Version : 1.0
 * 状态码实体类
 */

public class StatusCode {

    public static final int OK = 20000; //成功
    public static final int ERROR = 20001;  //失败
    public static final int LOGINGERROR = 20002;    //用户名或密码错误
    public static final int ACCESSERROR = 20003;    //权限错误
    public static final int REMOTEERROR = 20004;    //远程调用错误
    public static final int REPERROR = 20005;       //重复操作
    public static final int PARAMSLOST = 20006;     //参数缺失
    public static final int USERNAMEOREMAILNOTEXIST = 40000;    //用户名或者邮箱不存在
    public static final int UNKNOWSERVICEERROR = 50000; //服务器未知错误
}
