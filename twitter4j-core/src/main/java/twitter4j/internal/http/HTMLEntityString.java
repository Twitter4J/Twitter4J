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
package twitter4j.internal.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing the escaping of HTML strings.
 * @author Andy Boothe - andy.boothe at gmail.com
 */
public final class HTMLEntityString {
    /**
     * Converts indexes in an HTML-escaped string from the domain of one
     * {@link twitter4j.internal.http.HTMLEntityString.Escapedness} into another {@code Escapedness}.
     * For example, converts indexes from an HTML-escaped string into indexes
     * into its corresponding HTML-unescaped counterpart. This is important
     * when looking at Twitter Entities.
     */
    public static class IndexMapper {
        private Escapedness fromEscapedness;
        private Escapedness toEscapedness;
        private int[] conversion;

        public IndexMapper(int length, List<Entity> entities, Escapedness fromEscapedness, Escapedness toEscapedness) {
            this.conversion = new int[length + 1];
            this.fromEscapedness = fromEscapedness;
            this.toEscapedness = toEscapedness;

            int ei = 0;
            Entity entity = entities.size() != 0 ? entities.get(ei++) : null;

            int ti = 0;
            for (int fi = 0; fi < length; fi++, ti++) {
                if (entity != null) {
                    if (fi <= fromEscapedness.getEntityStart(entity))
                        this.conversion[fi] = ti;
                    else if (fi > fromEscapedness.getEntityStart(entity) && fi < fromEscapedness.getEntityEnd(entity))
                        this.conversion[fi] = -1;
                    else if (fi == fromEscapedness.getEntityEnd(entity)) {
                        this.conversion[fi] = ti = toEscapedness.getEntityEnd(entity);
                        entity = entities.size() > ei ? entities.get(ei++) : null;
                    } else {
                        // This means we somehow skipped over entity.getEscapedEnd(). Since
                        // we're going one-by-one over the string, this should never happen.
                        throw new RuntimeException("Skipped end of HTML entity. Should never happen. index=" + fi);
                    }
                } else
                    this.conversion[fi] = ti;
            }

            this.conversion[length] = ti;
        }

        public Escapedness getFromEscapedness() {
            return fromEscapedness;
        }

        public Escapedness getToEscapedness() {
            return toEscapedness;
        }

        private int[] getConversion() {
            return conversion;
        }

        public int mapIndex(int fi) {
            if (fi < 0 || fi >= getConversion().length)
                throw new IndexOutOfBoundsException();
            int result = getConversion()[fi];
            if (result < 0)
                throw new IllegalArgumentException("Escaped index is in middle of HTML entity; index=" + fi);
            return result;
        }
    }

    /**
     * Represents a single replacement of an HTML entity into its
     * corresponding character (e.g., &amp;amp; to &amp;) OR a
     * single replacement of a character to its corresponding HTML
     * entity (e.g., &amp; to &amp;amp;).
     */
    public static class Entity {
        private int escapedStart;
        private int escapedEnd;
        private String escapedText;

        private int unescapedStart;
        private int unescapedEnd;
        private String unescapedText;

        public Entity(int escapedStart, int escapedEnd, String escapedText, int unescapedStart, int unescapedEnd, String unescapedText) {
            this.escapedStart = escapedStart;
            this.escapedEnd = escapedEnd;
            this.escapedText = escapedText;
            this.unescapedStart = unescapedStart;
            this.unescapedEnd = unescapedEnd;
            this.unescapedText = unescapedText;
        }

        public int getEscapedStart() {
            return escapedStart;
        }

        public int getEscapedEnd() {
            return escapedEnd;
        }

        public String getEscapedText() {
            return escapedText;
        }

        public int getUnescapedStart() {
            return unescapedStart;
        }

        public int getUnescapedEnd() {
            return unescapedEnd;
        }

        public String getUnescapedText() {
            return unescapedText;
        }
    }

    /**
     * Represents a current state of escapedness, namely
     * HTML-escaped or HTML-unescaped.
     */
    public static enum Escapedness {
        ESCAPED {
            public int getEntityStart(Entity entity) {
                return entity.getEscapedStart();
            }

            public int getEntityEnd(Entity entity) {
                return entity.getEscapedEnd();
            }

            public String getEntityText(Entity entity) {
                return entity.getEscapedText();
            }

            public String getTokenAtPosition(StringBuilder s, int index) {
                String result;

                if (s.charAt(index) == '&') {
                    int semicolon = s.indexOf(";", index);
                    if (semicolon != -1) {
                        String substring = s.subSequence(index, semicolon + 1).toString();
                        if (HTMLEntity.escapeEntityMap.containsKey(substring))
                            result = substring;
                        else
                            result = null;
                    } else
                        result = null;
                } else
                    result = null;

                return result;
            }

            public String escapeToken(Escapedness escapedness, String entity) {
                String result;

                if (escapedness == this)
                    result = entity;
                else if (escapedness == UNESCAPED)
                    result = HTMLEntity.escapeEntityMap.get(entity);
                else
                    throw new IllegalArgumentException("Invalid escapedness: " + escapedness);

                return result;
            }
        },
        UNESCAPED {
            public int getEntityStart(Entity entity) {
                return entity.getUnescapedStart();
            }

            public int getEntityEnd(Entity entity) {
                return entity.getUnescapedEnd();
            }

            public String getEntityText(Entity entity) {
                return entity.getUnescapedText();
            }

            public String getTokenAtPosition(StringBuilder s, int index) {
                String result;

                String ch = Character.toString(s.charAt(index));
                if (HTMLEntity.entityEscapeMap.containsKey(ch))
                    result = ch;
                else
                    result = null;

                return result;
            }

            public String escapeToken(Escapedness escapedness, String entity) {
                String result;

                if (escapedness == this)
                    result = entity;
                else if (escapedness == ESCAPED)
                    result = HTMLEntity.entityEscapeMap.get(entity);
                else
                    throw new IllegalArgumentException("Invalid escapedness: " + escapedness);

                return result;
            }
        };

        public abstract int getEntityStart(Entity entity);

        public abstract int getEntityEnd(Entity entity);

        public abstract String getEntityText(Entity entity);

        /**
         * Determines if there's a convertible token in the given buffer at
         * the given index. In this context, a &quot;token&quot; is simply an
         * HTML entity or a character with a corresponding HTML entity, depending
         * on the value of this {@code Escapedness}.
         *
         * @param s     The buffer to check for a convertible token
         * @param index The index in {@code s} to check for a convertible token
         * @return A convertible token starting at {@code index} if such a token
         *         starts at {@code index}; null otherwise. If this method returns
         *         {@code null}, a subsequent call to {@link twitter4j.internal.http.HTMLEntityString.Escapedness#escapeToken(Escapedness, String)}
         *         <strong>must</strong> return a valid converted token (e.g.,
         *         &amp;amp; to &amp;).
         */
        public abstract String getTokenAtPosition(StringBuilder s, int index);

        /**
         * Returns a converted version of the given token if it exists.
         *
         * @param escapedness The {@code Escapedness} to convert the given token to
         * @param token       The convertible token to convert, if possible
         * @return A valid converted token, or {@code null} if no conversion is possible
         */
        public abstract String escapeToken(Escapedness escapedness, String token);
    }

    public static HTMLEntityString escape(String s) {
        return escape(new StringBuilder(s));
    }

    public static HTMLEntityString escape(StringBuilder s) {
        return convert(s, Escapedness.UNESCAPED, Escapedness.ESCAPED);
    }

    public static HTMLEntityString unescape(String s) {
        return unescape(new StringBuilder(s));
    }

    public static HTMLEntityString unescape(StringBuilder s) {
        return convert(s, Escapedness.ESCAPED, Escapedness.UNESCAPED);
    }

    public static HTMLEntityString convert(StringBuilder s, Escapedness fromEscapedness, Escapedness toEscapedness) {
        HTMLEntityString result = new HTMLEntityString(s.length(), fromEscapedness, toEscapedness);

        int unescapedIndex=0, escapedIndex=0;
        while (escapedIndex < s.length()) {
            String token = fromEscapedness.getTokenAtPosition(s, escapedIndex);
            if (token != null) {
                String escaped = fromEscapedness.escapeToken(toEscapedness, token);
                result.addEntity(new Entity(unescapedIndex, unescapedIndex + token.length(), token, escapedIndex, escapedIndex + escaped.length(), escaped));
                s.replace(escapedIndex, escapedIndex + token.length(), escaped);
                escapedIndex   = escapedIndex + escaped.length();
                unescapedIndex = unescapedIndex + token.length();
            } else {
                unescapedIndex = unescapedIndex + 1;
                escapedIndex = escapedIndex + 1;
            }
        }

        result.setConvertedString(s);

        return result;
    }

    private int originalLength;
    private Escapedness fromEscapedness;
    private Escapedness toEscapedness;
    private List<Entity> entities;
    private StringBuilder convertedText;

    protected HTMLEntityString(int originalLength, Escapedness fromEscapedness, Escapedness toEscapedness) {
        this.originalLength = originalLength;
        this.fromEscapedness = fromEscapedness;
        this.toEscapedness = toEscapedness;
        this.entities = new ArrayList<Entity>();
    }

    public int getOriginalLength() {
        return originalLength;
    }

    public Escapedness getFromEscapedness() {
        return fromEscapedness;
    }

    public Escapedness getToEscapedness() {
        return toEscapedness;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        getEntities().add(entity);
    }

    public StringBuilder getConvertedText() {
        return convertedText;
    }

    public void setConvertedString(StringBuilder convertedText) {
        this.convertedText = convertedText;
    }

    public IndexMapper createIndexMapper() {
        return new IndexMapper(getOriginalLength(), getEntities(), getFromEscapedness(), getToEscapedness());
    }


}