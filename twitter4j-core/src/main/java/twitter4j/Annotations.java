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

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A data class representing the Annotations of a Status or a Tweet
 *
 * @author Roy Reshef - royreshef at gmail.com
 * @see <a href="http://dev.twitter.com/pages/annotations_overview">Annotations Overview | Twitter Developers</a>
 * @since Twitter4J 2.1.4
 * @deprecated Annotations is not available for now. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/4d5ff2ec4d2ce4a7">Annotations - Twitter Development Talk | Google Groups</a>
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
     *
     * @param annotations - the List of Annotation instances
     */
    public Annotations(List<Annotation> annotations) {
        setAnnotations(annotations);
    }

    /**
     * Construct Annotations instance from a JSON Array, returned from Twitter API
     * Package visibility only!
     *
     * @param jsonArray - the JSON Array
     */
    public Annotations(JSONArray jsonArray) {
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
     *
     * @param annotations - the List of Annotation instances
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations =
                (annotations == null) ? new ArrayList<Annotation>() : annotations;
    }

    /**
     * Adds a single Annotation to the List of Annotation instances
     *
     * @param annotation - the Annotation to add
     */
    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    /**
     * Adds a single Annotation to the List of Annotation instances
     *
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
     */
    public boolean isExceedingLengthLimit() {
        return isExceedingLengthLimit(this);
    }

    /**
     * Package visibility only!
     *
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
        for (int i = 0; i < size(); i++) {
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
