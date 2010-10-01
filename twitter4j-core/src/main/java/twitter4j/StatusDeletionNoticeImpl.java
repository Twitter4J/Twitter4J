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
package twitter4j;

import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

/**
 * StatusDeletionNotice implementation. This class is NOT intended to be extended but left non-final for the ease of mock testing.
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.2
 */
class StatusDeletionNoticeImpl implements StatusDeletionNotice, java.io.Serializable {

    private long statusId;
    private int userId;
    private static final long serialVersionUID = 1723338404242596062L;

    /*package*/ StatusDeletionNoticeImpl(JSONObject json) throws JSONException {
        JSONObject deletionNotice = json.getJSONObject("delete");
        JSONObject status = deletionNotice.has("status")
            ? deletionNotice.getJSONObject("status")
            : deletionNotice.getJSONObject("direct_message");
        this.statusId = ParseUtil.getLong("id", status);
        this.userId = ParseUtil.getInt("user_id", status);
    }

    public long getStatusId() {
        return statusId;
    }

    public int getUserId() {
        return userId;
    }

    public int compareTo(StatusDeletionNotice that) {
        long delta = this.statusId - that.getStatusId();
        if (delta < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else if (delta > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusDeletionNotice)) return false;

        StatusDeletionNotice that = (StatusDeletionNotice) o;

        if (statusId != that.getStatusId()) return false;
        if (userId != that.getUserId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (statusId ^ (statusId >>> 32));
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "StatusDeletionNoticeImpl{" +
                "statusId=" + statusId +
                ", userId=" + userId +
                '}';
    }
}

