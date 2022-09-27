package twitter4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class HttpClient implements Serializable {
    private final static ConcurrentHashMap<HttpClientConfiguration, HttpClient> confClientMap = new ConcurrentHashMap<>();

    static HttpClient getInstance(HttpClientConfiguration conf) {
        return confClientMap.computeIfAbsent(conf, e -> new HttpClientImpl(conf));
    }

    private static final Logger logger = Logger.getLogger();
    private static final long serialVersionUID = -8016974810651763053L;
    protected final HttpClientConfiguration CONF;

    private final Map<String, String> requestHeaders;

    HttpClient(HttpClientConfiguration conf) {
        this.CONF = conf;
        requestHeaders = new HashMap<>();
        requestHeaders.put("X-Twitter-Client-Version", Version.getVersion());
        requestHeaders.put("X-Twitter-Client-URL", "http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        requestHeaders.put("X-Twitter-Client", "Twitter4J");
        requestHeaders.put("User-Agent", "twitter4j http://twitter4j.org/ /" + Version.getVersion());
        if (conf.isGZIPEnabled()) {
            requestHeaders.put("Accept-Encoding", "gzip");
        }
    }

    protected boolean isProxyConfigured() {
        return CONF.getHttpProxyHost() != null && !CONF.getHttpProxyHost().equals("");
    }

    void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    void addDefaultRequestHeader(String name, String value) {
        requestHeaders.put(name, value);
    }

    final HttpResponse request(HttpRequest req) throws TwitterException {
        return handleRequest(req);
    }

    final HttpResponse request(HttpRequest req, HttpResponseListener listener) throws TwitterException {
        try {
            HttpResponse res = handleRequest(req);
            if (listener != null) {
                listener.httpResponseReceived(new HttpResponseEvent(req, res, null));
            }
            return res;
        } catch (TwitterException te) {
            if (listener != null) {
                listener.httpResponseReceived(new HttpResponseEvent(req, null, te));
            }
            throw te;
        }
    }

    abstract HttpResponse handleRequest(HttpRequest req) throws TwitterException;

    HttpResponse get(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, authorization, this.requestHeaders), listener);
    }

    HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, this.requestHeaders));
    }

    HttpResponse post(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, authorization, this.requestHeaders), listener);
    }

    HttpResponse post(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, null, null, this.requestHeaders));
    }

    HttpResponse delete(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, authorization, this.requestHeaders), listener);
    }

    HttpResponse delete(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, null, null, this.requestHeaders));
    }

    HttpResponse head(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, null, null, this.requestHeaders));
    }

    HttpResponse put(String url, HttpParameter[] parameters
            , Authorization authorization, HttpResponseListener listener) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, authorization, this.requestHeaders), listener);
    }

    HttpResponse put(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, null, null, this.requestHeaders));
    }
}