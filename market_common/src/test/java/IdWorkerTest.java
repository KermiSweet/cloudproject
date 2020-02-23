import org.junit.Test;
import util.IdWorker;

public class IdWorkerTest {
    @Test
    public void getid() {
        IdWorker idWorker = new IdWorker(0, 0);
        System.out.println(String.valueOf(idWorker.nextId()).length());
        for (int i = 0; i < 1000 ; i++) {
            long id = idWorker.nextId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }
}
