/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.conf;

/**
 * A configuration with sensible defaults that can be directly manipulated.  This configuration bean
 * is useful for clients that wish to configure twitter4j from command line flags for example.
 *
 * @author John Sirois - john.sirois at gamil.com
 */
public final class ConfigurationBean extends ConfigurationBase {

  public void setDebugEnabled(boolean debugEnabled) {
    setDebug(debugEnabled);
  }

  @Override
  public void setSource(String source) {
    super.setSource(source);
  }

  @Override
  public void setUserAgent(String userAgent) {
    super.setUserAgent(userAgent);
  }

  @Override
  public void setUser(String user) {
    super.setUser(user);
  }

  @Override
  public void setPassword(String password) {
    super.setPassword(password);
  }

  @Override
  public void setHttpProxyHost(String httpProxyHost) {
    super.setHttpProxyHost(httpProxyHost);
  }

  @Override
  public void setHttpProxyUser(String httpProxyUser) {
    super.setHttpProxyUser(httpProxyUser);
  }

  @Override
  public void setHttpProxyPassword(String httpProxyPassword) {
    super.setHttpProxyPassword(httpProxyPassword);
  }

  @Override
  public void setHttpProxyPort(int httpProxyPort) {
    super.setHttpProxyPort(httpProxyPort);
  }

  @Override
  public void setHttpConnectionTimeout(int httpConnectionTimeout) {
    super.setHttpConnectionTimeout(httpConnectionTimeout);
  }

  @Override
  public void setHttpReadTimeout(int httpReadTimeout) {
    super.setHttpReadTimeout(httpReadTimeout);
  }

  @Override
  public void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
    super.setHttpStreamingReadTimeout(httpStreamingReadTimeout);
  }

  @Override
  public void setHttpRetryCount(int httpRetryCount) {
    super.setHttpRetryCount(httpRetryCount);
  }

  @Override
  public void setHttpRetryIntervalSeconds(int httpRetryIntervalSeconds) {
    super.setHttpRetryIntervalSeconds(httpRetryIntervalSeconds);
  }

  @Override
  public void setOAuthConsumerKey(String oAuthConsumerKey) {
    super.setOAuthConsumerKey(oAuthConsumerKey);
  }

  @Override
  public void setOAuthConsumerSecret(String oAuthConsumerSecret) {
    super.setOAuthConsumerSecret(oAuthConsumerSecret);
  }

  @Override
  public void setOAuthAccessToken(String oAuthAccessToken) {
    super.setOAuthAccessToken(oAuthAccessToken);
  }

  @Override
  public void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
    super.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
  }

  @Override
  public void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
    super.setOAuthRequestTokenURL(oAuthRequestTokenURL);
  }

  @Override
  public void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
    super.setOAuthAuthorizationURL(oAuthAuthorizationURL);
  }

  @Override
  public void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
    super.setOAuthAccessTokenURL(oAuthAccessTokenURL);
  }

  @Override
  public void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
    super.setOAuthAuthenticationURL(oAuthAuthenticationURL);
  }

  @Override
  public void setRestBaseURL(String restBaseURL) {
    super.setRestBaseURL(restBaseURL);
  }

  @Override
  public void setSearchBaseURL(String searchBaseURL) {
    super.setSearchBaseURL(searchBaseURL);
  }

  @Override
  public void setStreamBaseURL(String streamBaseURL) {
    super.setStreamBaseURL(streamBaseURL);
  }

  @Override
  public void setAsyncNumThreads(int asyncNumThreads) {
    super.setAsyncNumThreads(asyncNumThreads);
  }

  @Override
  public void setClientVersion(String clientVersion) {
    super.setClientVersion(clientVersion);
  }

  @Override
  public void setClientURL(String clientURL) {
    super.setClientURL(clientURL);
  }
}
