/*
Copyright (c) 2007-2011, Yusuke Yamamoto
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
package twitter4j.media;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.8
 */
public class MediaProvider implements java.io.Serializable {
    private static final long serialVersionUID = -258215809702057490L;

    private static final Map<String, MediaProvider> instances = new HashMap<String, MediaProvider>();

    public static MediaProvider IMG_LY = new MediaProvider("IMG_LY");
    public static MediaProvider PLIXI = new MediaProvider("PLIXI");
    public static MediaProvider TWIPPLE = new MediaProvider("TWIPPLE");
    public static MediaProvider TWITGOO= new MediaProvider("TWITGOO");
    public static MediaProvider TWITPIC = new MediaProvider("TWITPIC");
    public static MediaProvider YFROG = new MediaProvider("YFROG");
    public static MediaProvider MOBYPICTURE = new MediaProvider("MOBYPICTURE");
    public static MediaProvider TWIPL= new MediaProvider("TWIPL");
    public static MediaProvider POSTEROUS = new MediaProvider("POSTEROUS");

    private final String name;

    private MediaProvider() {
        throw new AssertionError();
    }

    MediaProvider(String name) {
        this.name = name;
        instances.put(name, this);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaProvider device = (MediaProvider) o;

        if (!name.equals(device.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    private static MediaProvider getInstance(String name) {
        return instances.get(name);
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(name);
    }

}
