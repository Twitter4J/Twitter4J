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

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.3
 */
final class ExceptionDiagnosis implements java.io.Serializable {
    int stackLineHash;
    int lineNumberHash;
    String hexString = "";
    Throwable th;
    private static final long serialVersionUID = 453958937114285988L;

    ExceptionDiagnosis(Throwable th) {
        this(th, new String[]{});
    }

    ExceptionDiagnosis(Throwable th, String[] inclusionFilter) {
        this.th = th;

        StackTraceElement[] stackTrace = th.getStackTrace();
        stackLineHash = 0;
        lineNumberHash = 0;
        for(int i=stackTrace.length-1;i>=0;i--){
            StackTraceElement line = stackTrace[i];
            for(String filter : inclusionFilter){
                if (line.getClassName().startsWith(filter)) {
                    int hash = line.getClassName().hashCode() + line.getMethodName().hashCode();
                    stackLineHash = 31 * stackLineHash + hash;
                    lineNumberHash = 31 * lineNumberHash + line.getLineNumber();
                    break;
                }
            }
        }
        hexString += toHexString(stackLineHash) + "-" + toHexString(lineNumberHash);
        if(null != th.getCause()){
            this.hexString += " " + new ExceptionDiagnosis(th.getCause(), inclusionFilter).asHexString();
        }

    }


    int getStackLineHash() {
        return stackLineHash;
    }

    int getLineNumberHash() {
        return lineNumberHash;
    }

    String asHexString() {
        return hexString;
    }
    private String toHexString(int value){
        String str = "0000000" + Integer.toHexString(value);
        return str.substring(str.length()-8,str.length());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionDiagnosis that = (ExceptionDiagnosis) o;

        if (lineNumberHash != that.lineNumberHash) return false;
        if (stackLineHash != that.stackLineHash) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stackLineHash;
        result = 31 * result + lineNumberHash;
        return result;
    }

    @Override
    public String toString() {
        return "ExceptionDiagnosis{" +
                "stackLineHash=" + stackLineHash +
                ", lineNumberHash=" + lineNumberHash +
                '}';
    }
}
