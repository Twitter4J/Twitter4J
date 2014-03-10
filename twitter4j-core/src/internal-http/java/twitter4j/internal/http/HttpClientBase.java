package twitter4j.internal.http;

import twitter4j.internal.logging.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class HttpClientBase implements HttpClient, Serializable {
    private static final Logger logger = Logger.getLogger(HttpClientBase.class);
    private static final long serialVersionUID = 6944924907755685265L;
    protected final HttpClientConfiguration CONF;

    public HttpClientBase(HttpClientConfiguration conf) {
        this.CONF = conf;
    }

    @Override
    public void shutdown() {
    }

    protected boolean isProxyConfigured() {
        return CONF.getHttpProxyHost() != null && !CONF.getHttpProxyHost().equals("");
    }

    public void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClientBase)) return false;

        HttpClientBase that = (HttpClientBase) o;

        if (!CONF.equals(that.CONF)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return CONF.hashCode();
    }

    @Override
    public String toString() {
        return "HttpClientBase{" +
                "CONF=" + CONF +
                '}';
    }
}