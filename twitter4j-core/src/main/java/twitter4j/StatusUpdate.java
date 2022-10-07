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

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
@SuppressWarnings("unused")
public final class StatusUpdate implements java.io.Serializable {

    private static final long serialVersionUID = 7422094739799350035L;
    /**
     * String
     */
    private final String status;
    /**
     * inReplyToStatusId
     */
    private long inReplyToStatusId = -1L;
    /**
     * location
     */
    private GeoLocation location = null;
    /**
     * placeId
     */
    private String placeId = null;
    /**
     * displayCoordinates
     */
    private boolean displayCoordinates = true;
    /**
     * possiblySensitive
     */
    private boolean possiblySensitive;
    /**
     * mediaName
     */
    private String mediaName;
    private transient InputStream mediaBody;
    /**
     * mediaFile
     */
    private File mediaFile;
    /**
     * mediaIds
     */
    private long[] mediaIds;
    /**
     * autoPopulateReplyMetadata
     */
    private boolean autoPopulateReplyMetadata;
    /**
     * attachmentUrl
     */
    private String attachmentUrl = null;

    /**
     * @param status tweet text
     */
    public StatusUpdate(String status) {
        this.status = status;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return in reply to status id
     */
    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }


    /**
     * @param inReplyToStatusId in reply to status id
     * @return this instance
     */
    public StatusUpdate inReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
        return this;
    }

    /**
     * @return location
     */
    public GeoLocation getLocation() {
        return location;
    }


    /**
     * @param location location
     * @return this instance
     */
    public StatusUpdate location(GeoLocation location) {
        this.location = location;
        return this;
    }

    /**
     * @return place id
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId place id
     * @return this instance
     */
    public StatusUpdate placeId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    /**
     * @return display coordinates
     */
    public boolean isDisplayCoordinates() {
        return displayCoordinates;
    }

    /**
     * @param displayCoordinates display coordinates
     * @return this instance
     */
    public StatusUpdate displayCoordinates(boolean displayCoordinates) {
        this.displayCoordinates = displayCoordinates;
        return this;
    }

    /**
     * @param file media file
     * @return this instance
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(File file) {
        this.mediaFile = file;
        return this;
    }

    /**
     * @param name name
     * @param body media body as stream
     * @since Twitter4J 2.2.5
     */
    public void setMedia(String name, InputStream body) {
        this.mediaName = name;
        this.mediaBody = body;
    }

    /**
     * @param mediaIds media ids
     * @since Twitter4J 4.0.2
     */
    public void setMediaIds(long... mediaIds) {
        this.mediaIds = mediaIds;
    }

    /**
     * @return attachment url
     * @since Twitter4J 4.0.7
     */
    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    /**
     * @param attachmentUrl attachment url
     * @return status update
     * @since Twitter4J 4.0.7
     */
    public StatusUpdate attachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
        return this;
    }

    /*package*/ boolean isForUpdateWithMedia() {
        return mediaFile != null || mediaName != null;
    }

    /**
     * @param name media name
     * @param body media body
     * @return this instance
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(String name, InputStream body) {
        setMedia(name, body);
        return this;
    }

    /**
     * @param possiblySensitive possibly sensitive
     * @return this instance
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate possiblySensitive(boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
        return this;
    }

    /**
     * @return possibly sensitive
     * @since Twitter4J 2.2.5
     */
    public boolean isPossiblySensitive() {
        return possiblySensitive;
    }

    /**
     * @return autoPopulateReplyMetadata
     * @since Twitter4J 4.0.7
     */
    public boolean isAutoPopulateReplyMetadata() {
        return autoPopulateReplyMetadata;
    }

    /**
     * @param autoPopulateReplyMetadata auto reply meta data
     * @return this instance
     * @since Twitter4J 4.0.7
     */
    public StatusUpdate autoPopulateReplyMetadata(boolean autoPopulateReplyMetadata) {
        this.autoPopulateReplyMetadata = autoPopulateReplyMetadata;
        return this;
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
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
        } else if (mediaIds != null && mediaIds.length >= 1) {
            params.add(new HttpParameter("media_ids", StringUtil.join(mediaIds)));
        }
        if (autoPopulateReplyMetadata) {
            appendParameter("auto_populate_reply_metadata", "true", params);
        }
        appendParameter("attachment_url", attachmentUrl, params);
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

    private void appendParameter(@SuppressWarnings("SameParameterValue") String name, long value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusUpdate that = (StatusUpdate) o;

        if (inReplyToStatusId != that.inReplyToStatusId) return false;
        if (displayCoordinates != that.displayCoordinates) return false;
        if (possiblySensitive != that.possiblySensitive) return false;
        if (autoPopulateReplyMetadata != that.autoPopulateReplyMetadata) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(location, that.location)) return false;
        if (!Objects.equals(placeId, that.placeId)) return false;
        if (!Objects.equals(mediaName, that.mediaName)) return false;
        if (!Objects.equals(mediaBody, that.mediaBody)) return false;
        if (!Objects.equals(mediaFile, that.mediaFile)) return false;
        if (!Arrays.equals(mediaIds, that.mediaIds)) return false;
        return Objects.equals(attachmentUrl, that.attachmentUrl);
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
        result = 31 * result + Arrays.hashCode(mediaIds);
        result = 31 * result + (autoPopulateReplyMetadata ? 1 : 0);
        result = 31 * result + (attachmentUrl != null ? attachmentUrl.hashCode() : 0);
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
                ", mediaIds=" + Arrays.toString(mediaIds) +
                ", autoPopulateReplyMetadata=" + autoPopulateReplyMetadata +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                '}';
    }
}
