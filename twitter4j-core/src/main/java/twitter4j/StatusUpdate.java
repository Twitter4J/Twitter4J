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

package twitter4j;

import twitter4j.internal.http.HttpParameter;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class StatusUpdate implements java.io.Serializable {

    private String status;
    private long inReplyToStatusId = -1L;
    private GeoLocation location = null;
    private String placeId = null;
    private boolean displayCoordinates = true;
    private boolean possiblySensitive;
    private String mediaName;
    private transient InputStream mediaBody;
    private File mediaFile;
    private static final long serialVersionUID = -3595502688477609916L;

    public StatusUpdate(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public StatusUpdate inReplyToStatusId(long inReplyToStatusId) {
        setInReplyToStatusId(inReplyToStatusId);
        return this;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public StatusUpdate location(GeoLocation location) {
        setLocation(location);
        return this;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public StatusUpdate placeId(String placeId) {
        setPlaceId(placeId);
        return this;
    }

    public boolean isDisplayCoordinates() {
        return displayCoordinates;
    }

    public void setDisplayCoordinates(boolean displayCoordinates) {
        this.displayCoordinates = displayCoordinates;
    }

    public StatusUpdate displayCoordinates(boolean displayCoordinates) {
        setDisplayCoordinates(displayCoordinates);
        return this;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public void setMedia(File file) {
        this.mediaFile = file;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(File file) {
        setMedia(file);
        return this;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public void setMedia(String name, InputStream body) {
        this.mediaName = name;
        this.mediaBody = body;
    }

    /*package*/ boolean isWithMedia() {
        return mediaFile != null || mediaName != null;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(String name, InputStream body) {
        setMedia(name, body);
        return this;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public void setPossiblySensitive(boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate possiblySensitive(boolean possiblySensitive) {
        setPossiblySensitive(possiblySensitive);
        return this;
    }

    /**
     * @since Twitter4J 2.2.5
     */
    public boolean isPossiblySensitive() {
        return possiblySensitive;
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();
        appendParameter("status", status, params);
        if (-1 != inReplyToStatusId) {
            appendParameter("in_reply_to_status_id", inReplyToStatusId, params);
        }
        if (location != null) {
            appendParameter("lat", location.getLatitude(), params);
            appendParameter("long", location.getLongitude(), params);

        }
        appendParameter("place_id", placeId, params);
        if (!displayCoordinates) {
            appendParameter("display_coordinates", "false", params);
        }
        if (null != mediaFile) {
            params.add(new HttpParameter("media[]", mediaFile));
            params.add(new HttpParameter("possibly_sensitive", possiblySensitive));
        } else if (mediaName != null && mediaBody != null) {
            params.add(new HttpParameter("media[]", mediaName, mediaBody));
            params.add(new HttpParameter("possibly_sensitive", possiblySensitive));
        }
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusUpdate that = (StatusUpdate) o;

        if (displayCoordinates != that.displayCoordinates) return false;
        if (inReplyToStatusId != that.inReplyToStatusId) return false;
        if (possiblySensitive != that.possiblySensitive) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (mediaBody != null ? !mediaBody.equals(that.mediaBody) : that.mediaBody != null) return false;
        if (mediaFile != null ? !mediaFile.equals(that.mediaFile) : that.mediaFile != null) return false;
        if (mediaName != null ? !mediaName.equals(that.mediaName) : that.mediaName != null) return false;
        if (placeId != null ? !placeId.equals(that.placeId) : that.placeId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (int) (inReplyToStatusId ^ (inReplyToStatusId >>> 32));
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (placeId != null ? placeId.hashCode() : 0);
        result = 31 * result + (displayCoordinates ? 1 : 0);
        result = 31 * result + (possiblySensitive ? 1 : 0);
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        result = 31 * result + (mediaBody != null ? mediaBody.hashCode() : 0);
        result = 31 * result + (mediaFile != null ? mediaFile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatusUpdate{" +
                "status='" + status + '\'' +
                ", inReplyToStatusId=" + inReplyToStatusId +
                ", location=" + location +
                ", placeId='" + placeId + '\'' +
                ", displayCoordinates=" + displayCoordinates +
                ", possiblySensitive=" + possiblySensitive +
                ", mediaName='" + mediaName + '\'' +
                ", mediaBody=" + mediaBody +
                ", mediaFile=" + mediaFile +
                '}';
    }
}
