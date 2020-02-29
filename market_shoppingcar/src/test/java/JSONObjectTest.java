import com.alibaba.fastjson.JSONObject;
import com.kermi.common.pojo.User;
import org.junit.Test;
import util.SessionAttributes;

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

    @Test
    public void test3() {
        String json = "{\n" +
                "    \"o\": {\n" +
                "        \"email\": \"2775420550@qq.com\",\n" +
                "        \"id\": 1233578431258693632,\n" +
                "        \"name\": \"kermi\",\n" +
                "        \"pwd\": \"\"\n" +
                "    },\n" +
                "    \"sessionId\": \"da83a56d-11c6-438b-b353-2d2f94036b8e\"\n" +
                "}";
        SessionAttributes attributes = JSONObject.parseObject(json, SessionAttributes.class);
        String json2 = String.valueOf(attributes.getO());
        User u = JSONObject.parseObject(json2, User.class);
        System.out.println(u);
    }
}
