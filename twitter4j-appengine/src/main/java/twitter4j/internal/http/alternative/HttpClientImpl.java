/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package twitter4j.internal.http.alternative;

import com.google.appengine.api.urlfetch.FetchOptions.Builder;
import com.google.appengine.api.urlfetch.*;
import twitter4j.TwitterException;
import twitter4j.internal.http.*;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static twitter4j.internal.http.RequestMethod.POST;

/**
 * @author Takao Nakaguchi - takao.nakaguchi at gmail.com
 * @since Twitter4J 2.2.4
 */
public class HttpClientImpl extends HttpClientBase {
    private static Logger logger = Logger.getLogger(HttpClientImpl.class);
    private static final long serialVersionUID = -6969046478967208236L;

    public HttpClientImpl(HttpClientConfiguration conf) {
        super(conf);
    }

    @Override
    public HttpResponse request(HttpRequest req) throws TwitterException {
        HTTPRequest request;
        try {
            request = new HTTPRequest(new URL(req.getURL())
                    , HTTPMethod.valueOf(req.getMethod().name())
                    , Builder.disallowTruncate().setDeadline(CONF.getHttpReadTimeout() / 1000D)
            );
        } catch (MalformedURLException e) {
            throw new TwitterException(e);
        }

        int responseCode = -1;
        ByteArrayOutputStream os;
        try {
            setHeaders(req, request);
            if (req.getMethod() == POST) {
                if (HttpParameter.containsFile(req.getParameters())) {
                    String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
                    request.setHeader(new HTTPHeader("Content-Type", "multipart/form-data; boundary=" + boundary));
                    boundary = "--" + boundary;
                    os = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(os);
                    for (HttpParameter param : req.getParameters()) {
                        if (param.isFile()) {
                            write(out, boundary + "\r\n");
                            write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.getFile().getName() + "\"\r\n");
                            write(out, "Content-Type: " + param.getContentType() + "\r\n\r\n");
                            BufferedInputStream in = new BufferedInputStream(
                                    param.hasFileBody() ? param.getFileBody() : new FileInputStream(param.getFile())
                            );
                            int buff = 0;
                            while ((buff = in.read()) != -1) {
                                out.write(buff);
                            }
                            write(out, "\r\n");
                            in.close();
                        } else {
                            write(out, boundary + "\r\n");
                            write(out, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
                            write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                            logger.debug(param.getValue());
                            out.write(param.getValue().getBytes("UTF-8"));
                            write(out, "\r\n");
                        }
                    }
                    write(out, boundary + "--\r\n");
                    write(out, "\r\n");
                } else {
                    request.setHeader(new HTTPHeader(
                            "Content-Type",
                            "application/x-www-form-urlencoded"
                    ));
                    String postParam = HttpParameter.encodeParameters(req.getParameters());
                    logger.debug("Post Params: ", postParam);
                    byte[] bytes = postParam.getBytes("UTF-8");
                    request.setHeader(new HTTPHeader("Content-Length",
                            Integer.toString(bytes.length)));
                    os = new ByteArrayOutputStream();
                    os.write(bytes);
                }
                request.setPayload(os.toByteArray());
            }
            URLFetchService service = URLFetchServiceFactory.getURLFetchService();
            return new AppEngineHttpResponseImpl(service.fetchAsync(request));
        } catch (IOException ioe) {
            // connection timeout or read timeout
            throw new TwitterException(ioe.getMessage(), ioe, responseCode);
        }
    }

    private void setHeaders(HttpRequest req, HTTPRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(req.getMethod().name() + " ", req.getURL());
        }

        String authorizationHeader;
        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization: ", z_T4JInternalStringUtil.maskString(authorizationHeader));
            }
            request.setHeader(new HTTPHeader("Authorization", authorizationHeader));
        }
        if (null != req.getRequestHeaders()) {
            for (String key : req.getRequestHeaders().keySet()) {
                request.addHeader(new HTTPHeader(key, req.getRequestHeaders().get(key)));
                logger.debug(key + ": " + req.getRequestHeaders().get(key));
            }
        }
    }

}
