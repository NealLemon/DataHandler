package DataHandlerTool.exception;

/**
 * 自定义文件异常
 */
public class DataHandlerException extends Exception {
    public DataHandlerException() {
        super();
    }

    public DataHandlerException(String message) {
        super(message);
    }

    public DataHandlerException(Throwable cause) {
        super(cause);
    }
}
