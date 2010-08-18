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

import java.util.ArrayList;
import java.util.List;

import twitter4j.internal.http.HttpParameter;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class StatusUpdate implements java.io.Serializable {

    private String status;
    private long inReplyToStatusId = -1l;
    private GeoLocation location = null;
    private String placeId = null;
    private boolean displayCoordinates = true;
    private Annotations annotations = null;
    private static final long serialVersionUID = -3595502688477609916L;

    public StatusUpdate(String status){
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
        
    public Annotations getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotations annotations) {
		this.annotations = annotations;
	}
	
	public StatusUpdate annotations(Annotations annotations) {
		setAnnotations(annotations);
        return this;
	}

	public void addAnnotation(Annotation annotation) {
		if (null == annotations) {
			this.annotations = new Annotations();
		}
		this.annotations.addAnnotation(annotation);
	}
	
	public StatusUpdate annotation(Annotation annotation) {
		addAnnotation(annotation);
        return this;
	}
	
	/*package*/ HttpParameter[] asHttpParameterArray(){
        ArrayList<HttpParameter> params = new ArrayList<HttpParameter>();
        appendParameter("status", status, params);
        if(-1 != inReplyToStatusId){
            appendParameter("in_reply_to_status_id", inReplyToStatusId, params);
        }
        if(null != location){
            appendParameter("lat", location.getLatitude(), params);
            appendParameter("long", location.getLongitude(), params);

        }
        appendParameter("place_id", placeId, params);
        if (!displayCoordinates) {
            appendParameter("display_coordinates", "false", params);
        }
        if ((null != annotations) && (!annotations.isEmpty())) {
        	appendParameter("annotations", annotations.asParameterValue(), params);
        }
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (null != value) {
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
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;
        if (placeId != null ? !placeId.equals(that.placeId) : that.placeId != null)
            return false;
        if (annotations != null ? !annotations.equals(that.annotations) : that.annotations != null)
            return false;
        if (!status.equals(that.status)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (int) (inReplyToStatusId ^ (inReplyToStatusId >>> 32));
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (placeId != null ? placeId.hashCode() : 0);
        result = 31 * result + (displayCoordinates ? 1 : 0);
        result = 31 * result + (annotations != null ? annotations.hashCode() : 0);
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
                ", annotations=" + annotations +
                '}';
    }
}
