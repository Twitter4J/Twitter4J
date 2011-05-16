package twitter4j.internal.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class StringAppender implements java.io.Serializable, CharSequence {

    private static final long serialVersionUID = 1L;

    private static final Class baseClass;

    private static final Constructor csqConstructor;
    private static final Constructor intConstructor;
    private static final Constructor strConstruuctor;

    private static final Method appendCharSequence;
    private static final Method appendCSE;
    private static final Method appendChar;
    private static final Method appendCharArray;
    private static final Method appendCharArraySE;
    private static final Method appendCodePoint;
    private static final Method insertCharArray4;
    private static final Method insertCharArray2;
    private static final Method insertChar;
    private static final Method insertCharSequence;
    private static final Method insertCharSequence4;
    private static final Method insertString;
    private static final Method methodSubstring;
    private static final Method methodReplace;
    private static final Method methodIndexOf;
    private static final Method methodLastIndexOf;
    private static final Method methodSetLength;
    private static final Method methodDelete;
    private static final Method methodDeleteChatAt;
    private static final Method methodReverse;
    private static final Method methodWriteObject;
    private static final Method methodReadObject;
    private static final Method methodEnsureCapacity;
    private static final Method methodGetChars;
    private static final Method methodSetCharAt;
    private static final Method methodTrimToSize;
    private static final Method methodOffsetByCodePoints;
    private static final Method methodCodePointAt;
    private static final Method methodCodePointBefore;
    private static final Method methodCodePointCount;

    static {
        Class clazz;
        try {
            clazz = Class.forName("java.lang.StringBuilder");
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName("java.lang.StringBuffer");
            } catch (ClassNotFoundException ex) {
                throw new InternalError(ex.getLocalizedMessage());
            }
        }

        baseClass = clazz;

        try {
            Class[] args = new Class[]{ CharSequence.class };
            csqConstructor = clazz.getConstructor(args);

            args[0] = int.class;
            intConstructor = clazz.getConstructor(args);

            args[0] = String.class;
            strConstruuctor = clazz.getConstructor(args);

            // 0 args
            methodReverse = clazz.getMethod("reverse", (Class[]) null);
            methodTrimToSize = clazz.getMethod("trimToSize", (Class[]) null);

            // 1 args
            args[0] = CharSequence.class;
            appendCharSequence = clazz.getMethod("append", args);

            args[0] = char[].class;
            appendCharArray = clazz.getMethod("append", args);

            args[0] = char.class;
            appendChar = clazz.getMethod("append", args);

            args[0] = int.class;
            appendCodePoint = clazz.getMethod("appendCodePoint", args);
            methodSetLength = clazz.getMethod("setLength", args);
            methodDeleteChatAt = clazz.getMethod("deleteCharAt", args);
            methodEnsureCapacity = clazz.getMethod("ensureCapacity", args);
            methodCodePointAt = clazz.getMethod("codePointAt", args);
            methodCodePointBefore = clazz.getMethod("codePointBefore", args);

            args[0] = java.io.ObjectOutputStream.class;
            methodWriteObject = clazz.getDeclaredMethod("writeObject", args);
            methodWriteObject.setAccessible(true);

            args[0] = java.io.ObjectInputStream.class;
            methodReadObject = clazz.getDeclaredMethod("readObject", args);
            methodReadObject.setAccessible(true);

            // 2 args
            args = new Class[]{ int.class, int.class };
            methodSubstring = clazz.getMethod("substring", args);
            methodDelete = clazz.getMethod("delete", args);
            methodOffsetByCodePoints = clazz.getMethod("offsetByCodePoints", args);
            methodCodePointCount = clazz.getMethod("codePointCount", args);

            args[0] = String.class;
            args[1] = int.class;
            methodIndexOf = clazz.getMethod("indexOf", args);
            methodLastIndexOf = clazz.getMethod("lastIndexOf", args);

            args[0] = int.class;
            args[1] = char[].class;
            insertCharArray2 = clazz.getMethod("insert", args);

            args[1] = char.class;
            insertChar = clazz.getMethod("insert", args);
            methodSetCharAt = clazz.getMethod("setCharAt", args);

            args[1] = CharSequence.class;
            insertCharSequence = clazz.getMethod("insert", args);

            args[1] = String.class;
            insertString = clazz.getMethod("insert", args);

            // 3 args
            args = new Class[]{ CharSequence.class, int.class, int.class };
            appendCSE = clazz.getMethod("append", args);

            args[0] = int.class;
            args[1] = int.class;
            args[2] = String.class;
            methodReplace = clazz.getMethod("replace", new Class[]{ int.class, int.class, String.class });

            args[0] = char[].class;
            args[1] = int.class;
            args[2] = int.class;
            appendCharArraySE = clazz.getMethod("append", args);

            // 4 args
            args = new Class[4];
            args[0] = int.class;
            args[1] = char[].class;
            args[2] = int.class;
            args[3] = int.class;
            insertCharArray4 = clazz.getMethod("insert", args);

            args[1] = CharSequence.class;
            insertCharSequence4 = clazz.getMethod("insert", args);

            args[1] = int.class;
            args[2] = char[].class;
            args[3] = int.class;
            methodGetChars = clazz.getMethod("getChars", args);
        } catch (SecurityException e) {
            throw new InternalError(e.getLocalizedMessage());
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.getLocalizedMessage());
        }
    }

    private CharSequence csq;
    private final Object[] args1 = new Object[1];
    private final Object[] args2 = new Object[2];
    private final Object[] args3 = new Object[3];

    public StringAppender() {
        try {
            csq = (CharSequence) baseClass.newInstance();
        } catch (InstantiationException e) {
            throw new InternalError(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            throw new InternalError(e.getLocalizedMessage());
        }
    }

    public StringAppender(CharSequence seq) {
        init(csqConstructor, seq);
    }

    public StringAppender(int capacity) {
        init(intConstructor, Integer.valueOf(capacity));
    }

    public StringAppender(String str) {
        init(strConstruuctor, str);
    }

    private void init(Constructor constructor, Object args) {
        try {
            this.csq = (CharSequence) constructor.newInstance(new Object[]{ args });
        } catch (InstantiationException e) {
            throw new InternalError(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            throw new InternalError(e.getLocalizedMessage());
        } catch (InvocationTargetException e) {
            throw new InternalError(e.getLocalizedMessage());
        }
    }

    private Object invoke(Method method, Object[] args) {
        try {
            return method.invoke(this.csq, args);
        } catch (IllegalAccessException e) {
            throw new InternalError(e.getLocalizedMessage());
        } catch (InvocationTargetException e) {
            throw new InternalError(e.getLocalizedMessage());
        }
    }

    public int length() {
        return this.csq.length();
    }

    public char charAt(int index) {
        return this.csq.charAt(index);
    }

    public int codePointAt(int index) {
        this.args1[0] = Integer.valueOf(index);
        Object result = invoke(methodCodePointAt, this.args1);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    public int codePointBefore(int index) {
        this.args1[0] = Integer.valueOf(index);
        Object result = invoke(methodCodePointBefore, this.args1);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    public int codePointCount(int beginIndex, int endIndex) {
        this.args2[0] = Integer.valueOf(beginIndex);
        this.args2[1] = Integer.valueOf(endIndex);
        Object result = invoke(methodCodePointCount, this.args2);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    public CharSequence subSequence(int start, int end) {
        return this.csq.subSequence(start, end);
    }

    public void ensureCapacity(int minimumCapacity) {
        this.args1[0] = Integer.valueOf(minimumCapacity);
        invoke(methodEnsureCapacity, this.args1);
    }

    public StringAppender append(CharSequence csq) {
        this.args1[0] = csq;
        invoke(appendCharSequence, this.args1);
        return this;
    }

    public StringAppender append(CharSequence csq, int start, int end)
            throws IOException {
        this.args3[0] = csq;
        this.args3[1] = Integer.valueOf(start);
        this.args3[2] = Integer.valueOf(end);
        invoke(appendCSE, this.args3);
        return this;
    }

    public StringAppender append(char c) {
        this.args1[0] = Character.valueOf(c);
        invoke(appendChar, this.args1);
        return this;
    }

    public StringAppender append(double d) {
        return append(Double.toString(d));
    }

    public StringAppender append(Object obj) {
        return append(String.valueOf(obj));
    }

    public StringAppender append(char[] str) {
        this.args1[0] = str;
        invoke(appendCharArray, this.args1);
        return this;
    }

    public StringAppender append(char str[], int offset, int len) {
        this.args3[0] = str;
        this.args3[1] = Integer.valueOf(offset);
        this.args3[2] = Integer.valueOf(len);
        invoke(appendCharArraySE, this.args3);
        return this;
    }

    public StringAppender append(boolean b) {
        return append(Boolean.toString(b));
    }

    public StringAppender append(int i) {
        return append(Integer.toString(i));
    }

    public StringAppender append(long l) {
        return append(Long.toString(l));
    }

    public StringAppender append(float f) {
        return append(Float.toString(f));
    }

    public StringAppender appendCodePoint(int codePoint) {
        this.args1[0] = Integer.valueOf(codePoint);
        invoke(appendCodePoint, this.args1);
        return this;
    }

    public StringAppender delete(int start, int end) {
        this.args2[0] = Integer.valueOf(start);
        this.args2[1] = Integer.valueOf(end);
        invoke(methodDelete, this.args2);
        return this;
    }

    public StringAppender deleteCharAt(int index) {
        this.args1[0] = Integer.valueOf(index);
        invoke(methodDeleteChatAt, this.args1);
        return this;
    }

    public StringAppender insert(int index, char str[], int offset,
            int len) {
        Object[] args = { Integer.valueOf(index), str, Integer.valueOf(offset),
                Integer.valueOf(len) };
        invoke(insertCharArray4, args);
        return this;
    }

    public StringAppender insert(int offset, char str[]) {
        this.args2[0] = Integer.valueOf(offset);
        this.args2[1] = str;
        invoke(insertCharArray2, this.args2);
        return this;
    }

    public StringAppender insert(int offset, boolean b) {
        return insert(offset, Boolean.toString(b));
    }

    public StringAppender insert(int offset, Object obj) {
        return insert(offset, String.valueOf(obj));
    }

    public StringAppender insert(int offset, String str) {
        this.args2[0] = Integer.valueOf(offset);
        this.args2[1] = str;
        invoke(insertString, this.args2);
        return this;
    }

    public StringAppender insert(int offset, CharSequence s) {
        this.args2[0] = Integer.valueOf(offset);
        this.args2[1] = s;
        invoke(insertCharSequence, args2);
        return this;
    }

    public StringAppender insert(int offset, CharSequence s, int start, int end) {
        Object[] args = {
                Integer.valueOf(offset), s, Integer.valueOf(start), Integer.valueOf(end)};
        invoke(insertCharSequence4, args);
        return this;
    }

    public StringAppender insert(int offset, int i) {
        return insert(offset, Integer.toString(i));
    }

    public StringAppender insert(int offset, long l) {
        return insert(offset, Long.toString(l));
    }

    public StringAppender insert(int offset, double d) {
        return insert(offset, Double.toString(d));
    }

    public StringAppender insert(int offset, float f) {
        return insert(offset, Float.toString(f));
    }

    public String substring(int start, int end) {
        this.args2[0] = Integer.valueOf(start);
        this.args2[1] = Integer.valueOf(end);
        Object result = invoke(methodSubstring, this.args2);
        return result != null ? result.toString() : null;
    }

    public String substring(int start) {
        return substring(start, length());
    }

    public StringAppender replace(int start, int end, String str) {
        this.args3[0] = Integer.valueOf(start);
        this.args3[1] = Integer.valueOf(end);
        this.args3[2] = str;
        invoke(methodReplace, this.args3);
        return this;
    }

    public int indexOf(String str, int fromIndex) {
        this.args2[0] = str;
        this.args2[1] = Integer.valueOf(fromIndex);
        Object result = invoke(methodIndexOf, this.args2);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int lastIndexOf(String str, int fromIndex) {
        this.args2[0] = str;
        this.args2[1] = Integer.valueOf(fromIndex);
        Object result = invoke(methodLastIndexOf, this.args2);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, length());
    }

    public StringAppender reverse() {
        invoke(methodReverse, (Object[]) null);
        return this;
    }

    public void setLength(int newLength) {
        this.args1[0] = Integer.valueOf(newLength);
        invoke(methodSetLength, this.args1);
    }

    public void getChars(int srcBegin, int srcEnd, char dst[],
            int dstBegin) {
        Object[] args = {
                Integer.valueOf(srcBegin), Integer.valueOf(srcEnd), dst, Integer.valueOf(dstBegin)
        };
        invoke(methodGetChars, args);
    }

    public void setCharAt(int index, char ch) {
        this.args2[0] = Integer.valueOf(index);
        this.args2[1] = Character.valueOf(ch);
        invoke(methodSetCharAt, this.args2);
    }

    public void trimToSize() {
        invoke(methodTrimToSize, (Object[]) null);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        this.args2[0] = Integer.valueOf(index);
        this.args2[1] = Integer.valueOf(codePointOffset);
        Object result = invoke(methodOffsetByCodePoints, this.args2);
        return result != null ? ((Integer) result).intValue() : -1;
    }

    @Override
    public boolean equals(Object obj) {
        return this.csq.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.csq.hashCode();
    }

    @Override
    public String toString() {
        return this.csq.toString();
    }

    private void writeObject(java.io.ObjectOutputStream s) {
        this.args1[0] = s;
        invoke(methodWriteObject, this.args1);
    }

    private void readObject(java.io.ObjectInputStream s) {
        this.args1[0] = s;
        invoke(methodReadObject, this.args1);
    }
}
