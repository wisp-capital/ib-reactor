package reactor.types;

import lombok.Data;

@Data
public class IbLogRecord {

    public enum Severity {
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }

    /**
     * Severity of IB event.
     */
    private final Severity severity;
    private final int id;
    /**
     * IB message code.
     *
     * @see <a href="https://interactivebrokers.github.io/tws-api/message_codes.html">Message Codes</a>
     */
    private final int code;
    private final String message;
}
