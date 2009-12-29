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
package twitter4j.http;

import twitter4j.Device;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dan Checkoway - dcheckoway at gmail.com
 */
final class RequestMethod implements java.io.Serializable {
    private final String name;
    private static final Map<String, RequestMethod> instances = new HashMap<String, RequestMethod>(5);

    public static final RequestMethod GET = new RequestMethod("GET");
    public static final RequestMethod POST = new RequestMethod("POST");
    public static final RequestMethod DELETE = new RequestMethod("DELETE");
    public static final RequestMethod HEAD = new RequestMethod("HEAD");
    public static final RequestMethod PUT = new RequestMethod("PUT");

    private static final long serialVersionUID = -4399222582680270381L;

    private RequestMethod(String name) {
        this.name = name;
        instances.put(name, this);
    }

    public final String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestMethod)) return false;

        RequestMethod that = (RequestMethod) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "RequestMethod{" +
                "name='" + name + '\'' +
                '}';
    }

    private static RequestMethod getInstance(String name) {
        return instances.get(name);
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(name);
    }
}
