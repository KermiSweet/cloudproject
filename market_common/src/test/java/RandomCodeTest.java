import org.junit.Test;
import util.VerifyCodeUtil;

public class RandomCodeTest {
    @Test
    public void random(){
        System.out.println(VerifyCodeUtil.generateVerifyCode(6));
    }
}
