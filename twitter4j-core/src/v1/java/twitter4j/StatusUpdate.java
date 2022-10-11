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
     * status tweet text
     */
    public final String status;
    /**
     * in reply to status id
     */
    public final long inReplyToStatusId;
    /**
     * location
     */
    public final GeoLocation location;
    /**
     * place id
     */
    public final String placeId;
    /**
     * display coordinates
     */
    public final boolean displayCoordinates;
    /**
     * possibly sensitive
     */
    public final boolean possiblySensitive;
    /**
     * mediaName
     */
    public final String mediaName;
    /**
     * mediaBody
     */
    public final transient InputStream mediaBody;
    /**
     * mediaFile
     */
    public final File mediaFile;
    /**
     * mediaIds
     */
    public final long[] mediaIds;
    /**
     * autoPopulateReplyMetadata
     */
    public final boolean autoPopulateReplyMetadata;
    /**
     * attachment URL
     */
    public final String attachmentUrl;

    /**
     * @param tweetText tweet text
     * @return StatusUpdate
     */
    public static StatusUpdate of(String tweetText) {
        return new StatusUpdate(tweetText, -1L, null, null, false, false, null, null, null, null, false, null);
    }

    private StatusUpdate(String status, long inReplyToStatusId, GeoLocation location, String placeId, boolean displayCoordinates, boolean possiblySensitive, String mediaName, InputStream mediaBody, File mediaFile, long[] mediaIds, boolean autoPopulateReplyMetadata, String attachmentUrl) {
        this.status = status;
        this.inReplyToStatusId = inReplyToStatusId;
        this.location = location;
        this.placeId = placeId;
        this.displayCoordinates = displayCoordinates;
        this.possiblySensitive = possiblySensitive;
        this.mediaName = mediaName;
        this.mediaBody = mediaBody;
        this.mediaFile = mediaFile;
        this.mediaIds = mediaIds;
        this.autoPopulateReplyMetadata = autoPopulateReplyMetadata;
        this.attachmentUrl = attachmentUrl;
    }

    /**
     * @param inReplyToStatusId in reply to status id
     * @return new instance with the specified value
     */
    public StatusUpdate inReplyToStatusId(long inReplyToStatusId) {
        return new StatusUpdate(this.status, inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param latitude latitude
     * @param longitude longitude
     * @return new instance with the specified value
     */
    public StatusUpdate location(double latitude, double longitude) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, GeoLocation.of(latitude,longitude), this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param placeId place id
     * @return new instance with the specified value
     */
    public StatusUpdate placeId(String placeId) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param displayCoordinates display coordinates
     * @return new instance with the specified value
     */
    public StatusUpdate displayCoordinates(boolean displayCoordinates) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param mediaFile media file
     * @return new instance with the specified value
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(File mediaFile) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }


    /**
     * @param mediaIds media ids
     * @return new instance with the specified value
     * @since Twitter4J 4.0.2
     */
    public StatusUpdate mediaIds(long... mediaIds) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param attachmentUrl attachment url
     * @return status update
     * @since Twitter4J 4.0.7
     */
    public StatusUpdate attachmentUrl(String attachmentUrl) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, attachmentUrl);
    }

    /*package*/ boolean isForUpdateWithMedia() {
        return mediaFile != null || mediaName != null;
    }

    /**
     * @param mediaName media name
     * @param mediaBody media body
     * @return new instance with the specified value
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate media(String mediaName, InputStream mediaBody) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, mediaName, mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param possiblySensitive possibly sensitive
     * @return new instance with the specified value
     * @since Twitter4J 2.2.5
     */
    public StatusUpdate possiblySensitive(boolean possiblySensitive) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, this.autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /**
     * @param autoPopulateReplyMetadata auto reply meta data
     * @return new instance with the specified value
     * @since Twitter4J 4.0.7
     */
    public StatusUpdate autoPopulateReplyMetadata(boolean autoPopulateReplyMetadata) {
        return new StatusUpdate(this.status, this.inReplyToStatusId, this.location, this.placeId, this.displayCoordinates, this.possiblySensitive, this.mediaName, this.mediaBody, this.mediaFile, this.mediaIds, autoPopulateReplyMetadata, this.attachmentUrl);
    }

    /*package*/ HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        appendParameter("status", status, params);
        if (-1 != inReplyToStatusId) {
            appendParameter("in_reply_to_status_id", inReplyToStatusId, params);
        }
        if (location != null) {
            appendParameter("lat", location.latitude, params);
            appendParameter("long", location.longitude, params);

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
        return inReplyToStatusId == that.inReplyToStatusId && displayCoordinates == that.displayCoordinates && possiblySensitive == that.possiblySensitive && autoPopulateReplyMetadata == that.autoPopulateReplyMetadata && Objects.equals(status, that.status) && Objects.equals(location, that.location) && Objects.equals(placeId, that.placeId) && Objects.equals(mediaName, that.mediaName) && Objects.equals(mediaBody, that.mediaBody) && Objects.equals(mediaFile, that.mediaFile) && Arrays.equals(mediaIds, that.mediaIds) && Objects.equals(attachmentUrl, that.attachmentUrl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, inReplyToStatusId, location, placeId, displayCoordinates, possiblySensitive, mediaName, mediaBody, mediaFile, autoPopulateReplyMetadata, attachmentUrl);
        result = 31 * result + Arrays.hashCode(mediaIds);
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
