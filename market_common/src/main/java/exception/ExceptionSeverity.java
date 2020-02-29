package exception;

public interface ExceptionSeverity {
    /** 轻微 */
    int MINOR = 1;
    /** 一般 */
    int NORMAL = 2;
    /** 重要 */
    int MAJOR = 3;
    /** 严重 */
    int CRITICAL = 4;
}
