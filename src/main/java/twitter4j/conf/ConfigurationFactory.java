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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class ConfigurationFactory {
    private static final Configuration ROOT_CONFIGURATION;
    public static final String DEFAULT_CONFIGURATION_IMPL = "twitter4j.conf.PropertyConfiguration";
    public static final String CONFIGURATION_IMPL = "twitter4j.configuration.impl";
    static {
        String CONFIG_IMPL = System.getProperty(CONFIGURATION_IMPL, DEFAULT_CONFIGURATION_IMPL);
        try {
            Class configImplClass = Class.forName(CONFIG_IMPL);
            ROOT_CONFIGURATION = (Configuration) configImplClass.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new ExceptionInInitializerError(cnfe);
        } catch (InstantiationException ie) {
            throw new ExceptionInInitializerError(ie);
        } catch (IllegalAccessException iae) {
            throw new ExceptionInInitializerError(iae);
        }
    }


    public static Configuration getInstance() {
        return ROOT_CONFIGURATION;
    }
}
