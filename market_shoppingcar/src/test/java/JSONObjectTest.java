import com.alibaba.fastjson.JSONObject;
import com.kermi.common.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JSONObjectTest {
    @Test
    public void test1(){
        List<Integer> intlist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            intlist.add(i);
        }
        String json = JSONObject.toJSONString(intlist);
        System.out.println(JSONObject.parseObject(json, List.class));
    }

    @Test
    public void test2() {
        List<User> users = new ArrayList<>();
        users.add(new User((long) 1, "test1", "test1", "123"));
        users.add(new User((long) 1, "test2", "test2", "123"));
        String json = JSONObject.toJSONString(users);
        System.out.println(JSONObject.parseObject(json, List.class));
        List<User> usersout = JSONObject.parseArray(json, User.class);
        System.out.println(usersout);
    }
}
