/*
Copyright (c) 2007-2009, Yusuke Yamamoto
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

import java.lang.reflect.InvocationTargetException;

/**
 * Static factory of Configuration. This class wraps ConfigurationFactory implementations.<br>
 * By default, twitter4j.conf.PropertyConfigurationFactory will be used and can be changed with -Dtwitter4j.configurationFactory system property.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ConfigurationContext {
    public static final String DEFAULT_CONFIGURATION_FACTORY = "twitter4j.conf.PropertyConfigurationFactory";
    public static final String CONFIGURATION_IMPL = "twitter4j.configurationFactory";
    private static final ConfigurationFactory factory;

    static {
        String CONFIG_IMPL = System.getProperty(CONFIGURATION_IMPL, DEFAULT_CONFIGURATION_FACTORY);
        try {
            factory = (ConfigurationFactory)Class.forName(CONFIG_IMPL).newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new AssertionError(cnfe);
        } catch (InstantiationException ie) {
            throw new AssertionError(ie);
        } catch (IllegalAccessException iae) {
            throw new AssertionError(iae);
        }
    }


    public static Configuration getInstance() {
        return factory.getInstance();
    }
    public static Configuration getInstance(String configTreePath) {
        return factory.getInstance(configTreePath);
    }
}
