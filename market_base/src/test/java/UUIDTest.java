import org.junit.Test;

import java.util.UUID;

public class UUIDTest {
    @Test
    public void UUID() {
        System.out.println(UUID.randomUUID().toString());
        System.out.println(UUID.randomUUID().toString().length());
    }
}
