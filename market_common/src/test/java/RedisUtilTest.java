import org.junit.Test;
import util.RedisUtil;

public class RedisUtilTest {
    @Test
    public void setkey() {
        RedisUtil rs = new RedisUtil();
        rs.set("1","1");
    }
}
