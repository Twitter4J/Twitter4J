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
package twitter4j;

import twitter4j.http.Response;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A data class representing Basic list information element
 * @author Dan Checkoway - dcheckoway at gmail.com
 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-id">REST API Documentation - Basic list information element</a>
 */
public class List extends TwitterResponseImpl implements java.io.Serializable {

    private int id;
    private String name;
    private String fullName;
    private String slug;
    private String description;
    private int subscriberCount;
    private int memberCount;
    private String uri;
    private String mode;
    private User user;
    private static final long serialVersionUID = -6345893237975349030L;


    /*package*/List(Response res) throws TwitterException {
        super(res);
        init(res.asJSONObject());
    }

    /*package*/List(Response res, JSONObject json) throws TwitterException {
        super(res);
        init(json);
    }

    /*package*/List(JSONObject json) throws TwitterException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            id = json.getInt("id");
            name = json.getString("name");
            fullName = json.getString("full_name");
            slug = json.getString("slug");
            description = json.getString("description");
            subscriberCount = json.getInt("subscriber_count");
            memberCount = json.getInt("member_count");
            uri = json.getString("uri");
            mode = json.getString("mode");
            if (!json.isNull("user")) {
                user = new User(json.getJSONObject("user"));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    /**
     * Returns the id of the list
     *
     * @return the id of the list
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the list
     *
     * @return the name of the list
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the full name of the list
     *
     * @return the full name of the list
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the slug of the list
     *
     * @return the slug of the list
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Returns the description of the list
     *
     * @return the description of the list
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the subscriber count of the list
     *
     * @return the subscriber count of the list
     */
    public int getSubscriberCount() {
        return subscriberCount;
    }

    /**
     * Returns the member count of the list
     *
     * @return the member count of the list
     */
    public int getMemberCount() {
        return memberCount;
    }

    /**
     * Returns the uri of the list
     *
     * @return the uri of the list
     */
    public URI getURI() {
        try {
            return new URI(uri);
        } catch (URISyntaxException ex) {
            return null;
        }
    }
        
    /**
     * Returns the mode of the list
     *
     * @return the mode of the list
     */
    public String getMode() {
        return mode;
    }
        
    /**
     * Returns the user of the list
     *
     * @return the user of the list
     */
    public User getUser() {
        return user;
    }
    /*package*/ static PagableResponseList<List> createListList(Response res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<List> users =
                    new PagableResponseList<List>(size, json, res);
            for (int i = 0; i < size; i++) {
                users.add(new List(res, list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof List && ((List) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", subscriberCount=" + subscriberCount +
                ", memberCount=" + memberCount +
                ", uri='" + uri + '\'' +
                ", mode='" + mode + '\'' +
                ", user=" + user +
                '}';
    }
}
