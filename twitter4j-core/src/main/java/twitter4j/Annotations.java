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
import java.util.Collections;
import java.util.List;

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;


/**
 * A data class representing the Annotations of a Status or a Tweet
 *
 * @author Roy Reshef - royreshef at gmail.com
 * @since Twitter4J 2.1.4
 * @see <a href="http://dev.twitter.com/pages/annotations_overview">Annotations Overview | dev.twitter.com</a>
 */
public class Annotations implements java.io.Serializable {
	private static final long serialVersionUID = 7928827620306593741L;
	public static final int lengthLimit = 512;
	private List<Annotation> annotations = null;

    /**
     * Construct empty Annotations instance
     */
    public Annotations() {
    	setAnnotations(null);
    }
    
    /**
     * Construct Annotations instance from an exisiting List of Annotation instances
     * @param annotations - the List of Annotation instances
     */
    public Annotations(List<Annotation> annotations) {
    	setAnnotations(annotations);
    }
    
    /**
     * Construct Annotations instance from a JSON Array, returned from Twitter API
     * Package visibility only!
     * @param jsonArray - the JSON Array
     */
    Annotations(JSONArray jsonArray) {
    	setAnnotations(null);
    	try {
	        for (int i = 0; i < jsonArray.length(); i++) {
				addAnnotation(new Annotation(jsonArray.getJSONObject(i)));
	        }
		} catch (JSONException jsone) {
			// malformed JSONObject - just empty the list of Annotations
			annotations.clear();
		}
    }
    
    /**
     * @return the List of Annotation instances
     */
    public List<Annotation> getAnnotations() {
    	return annotations;
    }

    /**
     * Sets the List of Annotation instances
     * Ensures the class's property is not null
     * @param annotations - the List of Annotation instances
     */
    public void setAnnotations(List<Annotation> annotations) {
		this.annotations = 
			(annotations == null) ? new ArrayList<Annotation>() : annotations;
	}

	/**
	 * Adds a single Annotation to the List of Annotation instances
	 * @param annotation - the Annotation to add
	 */
	public void addAnnotation(Annotation annotation) {
		this.annotations.add(annotation);
	}

    /**
	 * Adds a single Annotation to the List of Annotation instances
	 * @param annotation - the Annotation to add
     * @return this (for coding convenience)
     */
    public Annotations annotation(Annotation annotation) {
    	addAnnotation(annotation);
    	return this;
    }
    
	/**
	 * @return true if the List of Annotation instances is empty, false otherwise
	 */
	public boolean isEmpty() {
		return annotations.isEmpty();
	}

    /**
	 * @return the number of Annotation instances in the List
     */
    public Integer size() {
    	return annotations.size();
    }
    
	/**
	 * @param annotations - the instance to test
	 * @return true if the JSON String representation of the instance exceeds the limit imposed by Twitter, false otherwise
	 */
	public static boolean isExceedingLengthLimit(Annotations annotations) {
		return (annotations.asParameterValue().length() > lengthLimit);
	}
	
	/**
	 * @return true if the JSON String representation of this exceeds the limit imposed by Twitter, false otherwise
	 * @see isExceedingLengthLimit(Annotations annotations)
	 */
	public boolean isExceedingLengthLimit() {
		return isExceedingLengthLimit(this);
	}
	
	/**
	 * Package visibility only!
	 * @return the JSON String representation of this
	 */
	String asParameterValue() {
    	JSONArray jsonArray = new JSONArray();
    	for (Annotation annotation : annotations) {
    		jsonArray.put(annotation.asJSONObject());
    	}
    	return jsonArray.toString();    	
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        // shouldn't use the List directly as the Annotations are equal
        // regardless of the order of the Annotation instances
        return ((obj instanceof Annotations) && 
        	((Annotations) obj).getSortedAnnotations().equals(this.getSortedAnnotations()));
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	return getSortedAnnotations().hashCode();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Annotations{");
        for (int i = 0; i < size() ; i++) {
        	if (i > 0) {
        		sb.append(", ");
        	}
        	sb.append(annotations.get(i).toString());
        }
        sb.append('}');
        
        return sb.toString();
    }
   
    /**
     * @return a sorted copy of the List of Annotation instances
     */
    private List<Annotation> getSortedAnnotations() {
		// create a new list - we do not want to change the order in the original one
		List<Annotation> sortedAnnotations = new ArrayList<Annotation>(size());
		sortedAnnotations.addAll(this.annotations);
		Collections.sort(sortedAnnotations);
		return sortedAnnotations;    	
    }

}
