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

public final class QuickReply implements java.io.Serializable{
    private static final long serialVersionUID = 2928983476392757806L;
    private final String label;
    private final String description;
    private final String metadata;

    /**
     * @param label The text label displayed on the button face. Label text is returned as the user’s message response. String, max length of 36 characters including spaces. Values with URLs are not allowed and will return an error.
     * @param description Optional description text displayed under label text. All options must have this property defined if property is present in any option. Text is auto-wrapped and will display on a max of two lines and supports n for controling line breaks. Description text is not include in the user’s message response. String, max length of 72 characters including spaces.
     * @param metadata Metadata that will be sent back in the webhook request. String, max length of 1,000 characters including spaces.
     */
    public QuickReply(String label, String description, String metadata) {
        this.label = label;
        this.description = description;
        this.metadata = metadata;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuickReply that = (QuickReply) o;

        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return metadata != null ? metadata.equals(that.metadata) : that.metadata == null;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuickReply{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
