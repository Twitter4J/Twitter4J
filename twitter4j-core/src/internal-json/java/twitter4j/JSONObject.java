package twitter4j;

/*
Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A JSONObject is an unordered collection of name/value pairs. Its
 * external form is a string wrapped in curly braces with colons between the
 * names and values, and commas between the values and names. The internal form
 * is an object having {@code}get{/code} and {@code}opt{/code} methods for
 * accessing the values by name, and {@code}put{/code} methods for adding or
 * replacing values by name. The values can be any of these types:
 * {@code}Boolean{/code}, {@code}JSONArray{/code}, {@code}JSONObject{/code},
 * {@code}Number{/code}, {@code}String{/code}, or the {@code}JSONObject.NULL{/code}
 * object. A JSONObject constructor can be used to convert an external form
 * JSON text into an internal form whose values can be retrieved with the
 * {@code}get{/code} and {@code}opt{/code} methods, or to convert values into a
 * JSON text using the {@code}put{/code} and {@code}toString{/code} methods.
 * A {@code}get{/code} method returns a value if one can be found, and throws an
 * exception if one cannot be found. An {@code}opt{/code} method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic {@code}get(){/code} and {@code}opt(){/code} methods return an
 * object, which you can cast or query for type. There are also typed
 * {@code}get{/code} and {@code}opt{/code} methods that do type checking and type
 * coercion for you. The opt methods differ from the get methods in that they
 * do not throw. Instead, they return a specified value, such as null.
 * <p>
 * The {@code}put{/code} methods add or replace values in an object. For example,
 * <pre>myString = new JSONObject().put("JSON", "Hello, World!").toString();</pre>
 * produces the string {@code}{"JSON": "Hello, World"}{/code}.
 * <p>
 * The texts produced by the {@code}toString{/code} methods strictly conform to
 * the JSON syntax rules.
 * The constructors are more forgiving in the texts they will accept:
 * <ul>
 * <li>An extra {@code},{/code}&nbsp;<small>(comma)</small> may appear just
 * before the closing brace.</li>
 * <li>Strings may be quoted with {@code}'{/code}&nbsp;<small>(single
 * quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a quote
 * or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * {@code}{ } [ ] / \ : , = ; #{/code} and if they do not look like numbers
 * and if they are not the reserved words {@code}true{/code},
 * {@code}false{/code}, or {@code}null{/code}.</li>
 * <li>Keys can be followed by {@code}={/code} or {@code}=&gt;{/code} as well as
 * by {@code}:{/code}.</li>
 * <li>Values can be followed by {@code};{/code} <small>(semicolon)</small> as
 * well as by {@code},{/code} <small>(comma)</small>.</li>
 * <li>Numbers may have the {@code}0x-{/code} <small>(hex)</small> prefix.</li>
 * </ul>
 *
 * @author JSON.org
 * @version 2010-12-28
 */
public class JSONObject {

    /**
     * JSONObject.NULL is equivalent to the value that JavaScript calls null,
     * whilst Java's null is equivalent to the value that JavaScript calls
     * undefined.
     */
    private static final class Null {

        /**
         * There is only intended to be a single instance of the NULL object,
         * so the clone method returns itself.
         *
         * @return NULL.
         */
        @Override
        protected final Object clone() {
            return this;
        }

        /**
         * A Null object is equal to the null value and to itself.
         *
         * @param object An object to test for nullness.
         * @return true if the object parameter is the JSONObject.NULL object
         * or null.
         */
        public boolean equals(Object object) {
            return object == null || object == this;
        }

        /**
         * Get the "null" string value.
         *
         * @return The string "null".
         */
        public String toString() {
            return "null";
        }
    }


    /**
     * The map where the JSONObject's properties are kept.
     */
    private final Map map;


    /**
     * It is sometimes more convenient and less ambiguous to have a
     * {@code}NULL{/code} object than to use Java's {@code}null{/code} value.
     * {@code}JSONObject.NULL.equals(null){/code} returns {@code}true{/code}.
     * {@code}JSONObject.NULL.toString(){/code} returns {@code}"null"{/code}.
     */
    public static final Object NULL = new Null();


    /**
     * Construct an empty JSONObject.
     */
    public JSONObject() {
        this.map = new HashMap();
    }


    /**
     * Construct a JSONObject from a subset of another JSONObject.
     * An array of strings is used to identify the keys that should be copied.
     * Missing keys are ignored.
     *
     * @param jo    A JSONObject.
     * @param names An array of strings.
     */
    public JSONObject(JSONObject jo, String[] names) {
        this();
        for (String name : names) {
            try {
                putOnce(name, jo.opt(name));
            } catch (Exception ignore) {
            }
        }
    }


    /**
     * Construct a JSONObject from a JSONTokener.
     *
     * @param x A JSONTokener object containing the source string.
     * @throws JSONException If there is a syntax error in the source string
     *                       or a duplicated key.
     */
    public JSONObject(JSONTokener x) throws JSONException {
        this();
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{' found:" + x.nextClean());
        }
        for (; ; ) {
            c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    key = x.nextValue().toString();
            }

// The key is followed by ':'. We will also tolerate '=' or '=>'.

            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            putOnce(key, x.nextValue());

// Pairs are separated by ','. We will also tolerate ';'.

            switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    break;
                case '}':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }


    /**
     * Construct a JSONObject from a Map.
     *
     * @param map A map object that can be used to initialize the contents of
     *            the JSONObject.
     */
    public JSONObject(Map map) {
        this.map = new HashMap();
        if (map != null) {
            for (Object o : map.entrySet()) {
                Map.Entry e = (Map.Entry) o;
                Object value = e.getValue();
                if (value != null) {
                    this.map.put(e.getKey(), wrap(value));
                }
            }
        }
    }


    /**
     * Construct a JSONObject from an Object using bean getters.
     * It reflects on all of the public methods of the object.
     * For each of the methods with no parameters and a name starting
     * with {@code}"get"{/code} or {@code}"is"{/code} followed by an uppercase letter,
     * the method is invoked, and a key and the value returned from the getter method
     * are put into the new JSONObject.
     * <p>
     * The key is formed by removing the {@code}"get"{/code} or {@code}"is"{/code} prefix.
     * If the second remaining character is not upper case, then the first
     * character is converted to lower case.
     * <p>
     * For example, if an object has a method named {@code}"getName"{/code}, and
     * if the result of calling {@code}object.getName(){/code} is {@code}"Larry Fine"{/code},
     * then the JSONObject will contain {@code}"name": "Larry Fine"{/code}.
     *
     * @param bean An object that has getter methods that should be used
     *             to make a JSONObject.
     */
    public JSONObject(Object bean) {
        this();
        populateMap(bean);
    }


    /**
     * Construct a JSONObject from an Object, using reflection to find the
     * public members. The resulting JSONObject's keys will be the strings
     * from the names array, and the values will be the field values associated
     * with those keys in the object. If a key is not found or not visible,
     * then it will not be copied into the new JSONObject.
     *
     * @param object An object that has fields that should be used to make a
     *               JSONObject.
     * @param names  An array of strings, the names of the fields to be obtained
     *               from the object.
     */
    public JSONObject(Object object, String names[]) {
        this();
        Class c = object.getClass();
        for (String name : names) {
            try {
                putOpt(name, c.getField(name).get(object));
            } catch (Exception ignore) {
            }
        }
    }


    /**
     * Construct a JSONObject from a source JSON text string.
     * This is the most commonly used JSONObject constructor.
     *
     * @param source A string beginning
     *               with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     *               with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     * @throws JSONException If there is a syntax error in the source
     *                       string or a duplicated key.
     */
    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }


    /**
     * Construct a JSONObject from a ResourceBundle.
     *
     * @param baseName The ResourceBundle base name.
     * @param locale   The Locale to load the ResourceBundle for.
     * @throws JSONException If any JSONExceptions are detected.
     */
    public JSONObject(String baseName, Locale locale) throws JSONException {
        this();
        ResourceBundle r = ResourceBundle.getBundle(baseName, locale,
                Thread.currentThread().getContextClassLoader());

// Iterate through the keys in the bundle.

        Enumeration<String> keys = r.getKeys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key instanceof String) {

// Go through the path, ensuring that there is a nested JSONObject for each 
// segment except the last. Add the value using the last segment's name into
// the deepest nested JSONObject.

                String[] path = ((String) key).split("\\.");
                int last = path.length - 1;
                JSONObject target = this;
                for (int i = 0; i < last; i += 1) {
                    String segment = path[i];
                    Object object = target.opt(segment);
                    JSONObject nextTarget = object instanceof JSONObject ? (JSONObject) object : null;
                    if (nextTarget == null) {
                        nextTarget = new JSONObject();
                        target.put(segment, nextTarget);
                    }
                    target = nextTarget;
                }
                target.put(path[last], r.getString((String) key));
            }
        }
    }

    /**
     * Append values to the array under a key. If the key does not exist in the
     * JSONObject, then the key is put in the JSONObject with its value being a
     * JSONArray containing the value parameter. If the key was already
     * associated with a JSONArray, then the value parameter is appended to it.
     *
     * @param key   A key string.
     * @param value An object to be accumulated under the key.
     * @return this.
     * @throws JSONException If the key is null or if the current value
     *                       associated with the key is not a JSONArray.
     */
    public JSONObject append(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = opt(key);
        if (object == null) {
            put(key, new JSONArray().put(value));
        } else if (object instanceof JSONArray) {
            put(key, ((JSONArray) object).put(value));
        } else {
            throw new JSONException("JSONObject[" + key +
                    "] is not a JSONArray.");
        }
        return this;
    }

    /**
     * Get the value object associated with a key.
     *
     * @param key A key string.
     * @return The object associated with the key.
     * @throws JSONException if the key is not found.
     */
    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        Object object = opt(key);
        if (object == null) {
            throw new JSONException("JSONObject[" + quote(key) +
                    "] not found.");
        }
        return object;
    }


    /**
     * Get the boolean value associated with a key.
     *
     * @param key A key string.
     * @return The truth.
     * @throws JSONException if the value is not a Boolean or the String "true" or "false".
     */
    public boolean getBoolean(String key) throws JSONException {
        Object object = get(key);
        if (object.equals(Boolean.FALSE) ||
                (object instanceof String &&
                        ((String) object).equalsIgnoreCase("false"))) {
            return false;
        } else if (object.equals(Boolean.TRUE) ||
                (object instanceof String &&
                        ((String) object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONObject[" + quote(key) +
                "] is not a Boolean.");
    }

    /**
     * Get the int value associated with a key.
     *
     * @param key A key string.
     * @return The integer value.
     * @throws JSONException if the key is not found or if the value cannot
     *                       be converted to an integer.
     */
    public int getInt(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ?
                    ((Number) object).intValue() :
                    Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) +
                    "] is not an int.");
        }
    }


    /**
     * Get the JSONArray value associated with a key.
     *
     * @param key A key string.
     * @return A JSONArray which is the value.
     * @throws JSONException if the key is not found or
     *                       if the value is not a JSONArray.
     */
    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new JSONException("JSONObject[" + quote(key) +
                "] is not a JSONArray.");
    }


    /**
     * Get the JSONObject value associated with a key.
     *
     * @param key A key string.
     * @return A JSONObject which is the value.
     * @throws JSONException if the key is not found or
     *                       if the value is not a JSONObject.
     */
    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException("JSONObject[" + quote(key) +
                "] is not a JSONObject.");
    }


    /**
     * Get the long value associated with a key.
     *
     * @param key A key string.
     * @return The long value.
     * @throws JSONException if the key is not found or if the value cannot
     *                       be converted to a long.
     */
    public long getLong(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ?
                    ((Number) object).longValue() :
                    Long.parseLong((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) +
                    "] is not a long.");
        }
    }

    /**
     * Get the string associated with a key.
     *
     * @param key A key string.
     * @return A string which is the value.
     * @throws JSONException if the key is not found.
     */
    public String getString(String key) throws JSONException {
        Object object = get(key);
        return object == NULL ? null : object.toString();
    }


    /**
     * Determine if the JSONObject contains a specific key.
     *
     * @param key A key string.
     * @return true if the key exists in the JSONObject.
     */
    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    /**
     * Determine if the value associated with the key is null or if there is
     * no value.
     *
     * @param key A key string.
     * @return true if there is no value associated with the key or if
     * the value is the JSONObject.NULL object.
     */
    public boolean isNull(String key) {
        return JSONObject.NULL.equals(opt(key));
    }


    /**
     * Get an enumeration of the keys of the JSONObject.
     *
     * @return An iterator of the keys.
     */
    public Iterator keys() {
        return this.map.keySet().iterator();
    }


    /**
     * Get the number of keys stored in the JSONObject.
     *
     * @return The number of keys in the JSONObject.
     */
    public int length() {
        return this.map.size();
    }


    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     *
     * @return A JSONArray containing the key strings, or null if the JSONObject
     * is empty.
     */
    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        return ja.length() == 0 ? null : ja;
    }

    /**
     * Produce a string from a Number.
     *
     * @param number A Number
     * @return A String.
     * @throws JSONException If n is a non-finite number.
     */
    public static String numberToString(Number number)
            throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);

// Shave off trailing zeros and decimal point, if possible.

        String string = number.toString();
        if (string.indexOf('.') > 0 && string.indexOf('e') < 0 &&
                string.indexOf('E') < 0) {
            while (string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }
            if (string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }


    /**
     * Get an optional value associated with a key.
     *
     * @param key A key string.
     * @return An object which is the value, or null if there is no value.
     */
    public Object opt(String key) {
        return key == null ? null : this.map.get(key);
    }


    private void populateMap(Object bean) {
        Class klass = bean.getClass();

// If klass is a System class then set includeSuperClass to false. 

        boolean includeSuperClass = klass.getClassLoader() != null;

        Method[] methods = (includeSuperClass) ?
                klass.getMethods() : klass.getDeclaredMethods();
        for (Method method1 : methods) {
            try {
                if (Modifier.isPublic(method1.getModifiers())) {
                    String name = method1.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if (name.equals("getClass") ||
                                name.equals("getDeclaringClass")) {
                            key = "";
                        } else {
                            key = name.substring(3);
                        }
                    } else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }
                    if (key.length() > 0 &&
                            Character.isUpperCase(key.charAt(0)) &&
                            method1.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() +
                                    key.substring(1);
                        }

                        Object result = method1.invoke(bean, (Object[]) null);
                        if (result != null) {
                            map.put(key, wrap(result));
                        }
                    }
                }
            } catch (Exception ignore) {
            }
        }
    }


    /**
     * Put a key/boolean pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A boolean which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject put(String key, boolean value) throws JSONException {
        put(key, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject put(String key, Collection value) throws JSONException {
        put(key, new JSONArray(value));
        return this;
    }


    /**
     * Put a key/double pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A double which is the value.
     * @return this.
     * @throws JSONException If the key is null or if the number is invalid.
     */
    public JSONObject put(String key, double value) throws JSONException {
        put(key, new Double(value));
        return this;
    }


    /**
     * Put a key/int pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value An int which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject put(String key, int value) throws JSONException {
        put(key, new Integer(value));
        return this;
    }


    /**
     * Put a key/long pair in the JSONObject.
     *
     * @param key   A key string.
     * @param value A long which is the value.
     * @return this.
     * @throws JSONException If the key is null.
     */
    public JSONObject put(String key, long value) throws JSONException {
        put(key, new Long(value));
        return this;
    }


    /**
     * Put a key/value pair in the JSONObject, where the value will be a
     * JSONObject which is produced from a Map.
     *
     * @param key   A key string.
     * @param value A Map value.
     * @return this.
     * @throws JSONException when failed to put value
     */
    public JSONObject put(String key, Map value) throws JSONException {
        put(key, new JSONObject(value));
        return this;
    }


    /**
     * Put a key/value pair in the JSONObject. If the value is null,
     * then the key will be removed from the JSONObject if it is present.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String,
     *              or the JSONObject.NULL object.
     * @return this.
     * @throws JSONException If the value is non-finite number
     *                       or if the key is null.
     */
    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }


    /**
     * Put a key/value pair in the JSONObject, but only if the key and the
     * value are both non-null, and only if there is not already a member
     * with that name.
     *
     * @param key   key to be put
     * @param value value to be put
     * @return his.
     * @throws JSONException if the key is a duplicate
     */
    public JSONObject putOnce(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            if (opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }
            put(key, value);
        }
        return this;
    }


    /**
     * Put a key/value pair in the JSONObject, but only if the
     * key and the value are both non-null.
     *
     * @param key   A key string.
     * @param value An object which is the value. It should be of one of these
     *              types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String,
     *              or the JSONObject.NULL object.
     * @return this.
     * @throws JSONException If the value is a non-finite number.
     */
    public JSONObject putOpt(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            put(key, value);
        }
        return this;
    }


    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places. A backslash will be inserted within &lt;/, producing &lt;\/,
     * allowing JSON text to be delivered in HTML. In JSON text, a string
     * cannot contain a control character or an unescaped quote or backslash.
     *
     * @param string A String
     * @return A String correctly formatted for insertion in a JSON text.
     */
    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
                            (c >= '\u2000' && c < '\u2100')) {
                        hhhh = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hhhh.substring(hhhh.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * Remove a name and its value, if present.
     *
     * @param key The name to be removed.
     * @return The value that was associated with the name,
     * or null if there was no value.
     */
    public Object remove(String key) {
        return this.map.remove(key);
    }

    /**
     * Get an enumeration of the keys of the JSONObject.
     * The keys will be sorted alphabetically.
     *
     * @return An iterator of the keys.
     */
    public Iterator sortedKeys() {
        return new TreeSet(this.map.keySet()).iterator();
    }

    /**
     * Try to convert a string into a number, boolean, or null. If the string
     * can't be converted, return the string.
     *
     * @param string A String.
     * @return A simple JSON value.
     */
    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return JSONObject.NULL;
        }

        /*
         * If it might be a number, try converting it. 
         * We support the non-standard 0x- convention. 
         * If a number cannot be produced, then the value will just
         * be a string. Note that the 0x-, plus, and implied string
         * conventions are non-standard. A JSON parser may accept
         * non-JSON forms as long as it accepts all correct JSON forms.
         */

        char b = string.charAt(0);
        if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
            if (b == '0' && string.length() > 2 &&
                    (string.charAt(1) == 'x' || string.charAt(1) == 'X')) {
                try {
                    return Integer.parseInt(string.substring(2), 16);
                } catch (Exception ignore) {
                }
            }
            try {
                if (string.indexOf('.') > -1 ||
                        string.indexOf('e') > -1 || string.indexOf('E') > -1) {
                    return Double.valueOf(string);
                } else {
                    Long myLong = new Long(string);
                    if (myLong == myLong.intValue()) {
                        return myLong.intValue();
                    } else {
                        return myLong;
                    }
                }
            } catch (Exception ignore) {
            }
        }
        return string;
    }


    /**
     * Throw an exception if the object is a NaN or infinite number.
     *
     * @param o The object to test.
     * @throws JSONException If o is a non-finite number.
     */
    public static void testValidity(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                    throw new JSONException(
                            "JSON does not allow non-finite numbers.");
                }
            } else if (o instanceof Float) {
                if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                    throw new JSONException(
                            "JSON does not allow non-finite numbers.");
                }
            }
        }
    }

    /**
     * Make a JSON text of this JSONObject. For compactness, no whitespace
     * is added. If this would not result in a syntactically correct JSON text,
     * then null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, portable, transmittable
     * representation of the object, beginning
     * with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     * with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     */
    public String toString() {
        try {
            Iterator keys = keys();
            StringBuilder sb = new StringBuilder("{");

            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(this.map.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @return a printable, displayable, portable, transmittable
     * representation of the object, beginning
     * with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     * with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    public String toString(int indentFactor) throws JSONException {
        return toString(indentFactor, 0);
    }


    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @param indent       The indentation of the top level.
     * @return a printable, displayable, transmittable
     * representation of the object, beginning
     * with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     * with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    String toString(int indentFactor, int indent) throws JSONException {
        int i;
        int length = this.length();
        if (length == 0) {
            return "{}";
        }
        Iterator keys = sortedKeys();
        int newindent = indent + indentFactor;
        Object object;
        StringBuilder sb = new StringBuilder("{");
        if (length == 1) {
            object = keys.next();
            sb.append(quote(object.toString()));
            sb.append(": ");
            sb.append(valueToString(this.map.get(object), indentFactor,
                    indent));
        } else {
            while (keys.hasNext()) {
                object = keys.next();
                if (sb.length() > 1) {
                    sb.append(",\n");
                } else {
                    sb.append('\n');
                }
                for (i = 0; i < newindent; i += 1) {
                    sb.append(' ');
                }
                sb.append(quote(object.toString()));
                sb.append(": ");
                sb.append(valueToString(this.map.get(object), indentFactor,
                        newindent));
            }
            if (sb.length() > 1) {
                sb.append('\n');
                for (i = 0; i < indent; i += 1) {
                    sb.append(' ');
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }


    /**
     * Make a JSON text of an Object value. If the object has an
     * value.toJSONString() method, then that method will be used to produce
     * the JSON text. The method is required to produce a strictly
     * conforming text. If the object does not contain a toJSONString
     * method (which is the most common case), then a text will be
     * produced by other means. If the value is an array or Collection,
     * then a JSONArray will be made from it and its toJSONString method
     * will be called. If the value is a MAP, then a JSONObject will be made
     * from it and its toJSONString method will be called. Otherwise, the
     * value's toString method will be called, and the result will be quoted.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value The value to be serialized.
     * @return a printable, displayable, transmittable
     * representation of the object, beginning
     * with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     * with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the value is or contains an invalid number.
     */
    public static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if (value instanceof Boolean || value instanceof JSONObject ||
                value instanceof JSONArray) {
            return value.toString();
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString();
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString();
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString();
        }
        return quote(value.toString());
    }


    /**
     * Make a prettyprinted JSON text of an object value.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value        The value to be serialized.
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @param indent       The indentation of the top level.
     * @return a printable, displayable, transmittable
     * representation of the object, beginning
     * with {@code}{{/code}&nbsp;<small>(left brace)</small> and ending
     * with {@code}}{/code}&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    static String valueToString(Object value, int indentFactor, int indent)
            throws JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject) value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray) {
            return ((JSONArray) value).toString(indentFactor, indent);
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString(indentFactor, indent);
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString(indentFactor, indent);
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString(indentFactor, indent);
        }
        return quote(value.toString());
    }


    /**
     * Wrap an object, if necessary. If the object is null, return the NULL
     * object. If it is an array or collection, wrap it in a JSONArray. If
     * it is a map, wrap it in a JSONObject. If it is a standard property
     * (Double, String, et al) then it is already wrapped. Otherwise, if it
     * comes from one of the java packages, turn it into a string. And if
     * it doesn't, try to wrap it in a JSONObject. If the wrapping fails,
     * then null is returned.
     *
     * @param object The object to wrap
     * @return The wrapped value
     */
    public static Object wrap(Object object) {
        try {
            if (object == null) {
                return NULL;
            }
            if (object instanceof JSONObject || object instanceof JSONArray ||
                    NULL.equals(object) ||
                    object instanceof Byte || object instanceof Character ||
                    object instanceof Short || object instanceof Integer ||
                    object instanceof Long || object instanceof Boolean ||
                    object instanceof Float || object instanceof Double ||
                    object instanceof String) {
                return object;
            }

            if (object instanceof Collection) {
                return new JSONArray((Collection) object);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                return new JSONObject((Map) object);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = (objectPackage != null ? objectPackage.getName() : "");
            if (objectPackageName.startsWith("java.") ||
                    objectPackageName.startsWith("javax.") ||
                    object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        } catch (Exception exception) {
            return null;
        }
    }


    public Writer write(Writer writer) throws JSONException {
        try {
            boolean commanate = false;
            Iterator keys = keys();
            writer.write('{');

            while (keys.hasNext()) {
                if (commanate) {
                    writer.write(',');
                }
                Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(':');
                Object value = this.map.get(key);
                if (value instanceof JSONObject) {
                    ((JSONObject) value).write(writer);
                } else if (value instanceof JSONArray) {
                    ((JSONArray) value).write(writer);
                } else {
                    writer.write(valueToString(value));
                }
                commanate = true;
            }
            writer.write('}');
            return writer;
        } catch (IOException exception) {
            throw new JSONException(exception);
        }
    }
}