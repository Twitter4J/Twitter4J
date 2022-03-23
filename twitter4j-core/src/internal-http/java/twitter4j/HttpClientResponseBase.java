package twitter4j;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Map;

public abstract class HttpClientResponseBase extends HttpClientBase implements HttpResponseCode{

    private final Logger logger = Logger.getLogger(getClass());

    public HttpClientResponseBase(HttpClientConfiguration conf) {
        super(conf);
    }

    public void logResponseHeaders(Map<String, List<String>> responseHeaders) {
        for (String key : responseHeaders.keySet()) {
            List<String> values = responseHeaders.get(key);
            for (String value : values) {
                if (key != null) {
                    logger.debug(key + ": " + value);
                } else {
                    logger.debug(value);
                }
            }
        }
    }

    public boolean isValidaResponseCode(int responseCode, int retriedCount, HttpResponse res) throws TwitterException {
        if (responseCode < OK || (responseCode != FOUND && MULTIPLE_CHOICES <= responseCode)) {
            if (responseCode == ENHANCE_YOUR_CLAIM ||
                    responseCode == BAD_REQUEST ||
                    responseCode < INTERNAL_SERVER_ERROR ||
                    retriedCount == CONF.getHttpRetryCount()) {

                throw new TwitterException(res.asString(), res);
            }

            return true;
        } else {
            return false;
        }
    }

    public void setProxyAuthentication() {
        if (CONF.getHttpProxyUser() != null && !CONF.getHttpProxyUser().equals("")) {
            if (logger.isDebugEnabled()) {
                logger.debug("Proxy AuthUser: " + CONF.getHttpProxyUser());
                logger.debug("Proxy AuthPassword: " + CONF.getHttpProxyPassword().replaceAll(".", "*"));
            }

            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication
                getPasswordAuthentication() {
                    if (getRequestorType().equals(RequestorType.PROXY)) {
                        return new PasswordAuthentication(CONF.getHttpProxyUser(),
                                CONF.getHttpProxyPassword().toCharArray());
                    } else {
                        return null;
                    }
                }
            });
        }
    }
}
