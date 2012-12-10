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

package twitter4j.internal.json;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

final class HTMLEntity {

    static String escape(String original) {
        StringBuilder buf = new StringBuilder(original);
        escape(buf);
        return buf.toString();
    }

    static void escape(StringBuilder original) {
        int index = 0;
        String escaped;
        while (index < original.length()) {
            escaped = entityEscapeMap.get(original.substring(index, index + 1));
            if (escaped != null) {
                original.replace(index, index + 1, escaped);
                index += escaped.length();
            } else {
                index++;
            }
        }
    }

    static String unescape(String original) {
        String returnValue = null;
        if (original != null) {
            StringBuilder buf = new StringBuilder(original);
            unescape(buf);
            returnValue = buf.toString();
        }
        return returnValue;
    }

    static void unescape(StringBuilder original) {
        int index = 0;
        int semicolonIndex;
        String escaped;
        String entity;
        while (index < original.length()) {
            index = original.indexOf("&", index);
            if (-1 == index) {
                break;
            }
            semicolonIndex = original.indexOf(";", index);
            if (-1 != semicolonIndex) {
                escaped = original.substring(index, semicolonIndex + 1);
                entity = escapeEntityMap.get(escaped);
                if (entity != null) {
                    original.replace(index, semicolonIndex + 1, entity);
                }
                index++;
            } else {
                break;
            }
        }
    }

    static String unescapeAndSlideEntityIncdices(String text, UserMentionEntity[] userMentionEntities,
                                                 URLEntity[] urlEntities, HashtagEntity[] hashtagEntities,
                                                 MediaEntity[] mediaEntities) {
        
        int entityIndexesLength = 0;
        entityIndexesLength += userMentionEntities == null ? 0 : userMentionEntities.length;
        entityIndexesLength += urlEntities == null ? 0 : urlEntities.length;
        entityIndexesLength += hashtagEntities == null ? 0 : hashtagEntities.length;
        entityIndexesLength += mediaEntities == null ? 0 : mediaEntities.length;

        EntityIndex[] entityIndexes = new EntityIndex[entityIndexesLength];
        int copyStartIndex = 0;
        if (userMentionEntities != null) {
            System.arraycopy(userMentionEntities, 0, entityIndexes, copyStartIndex, userMentionEntities.length);
            copyStartIndex += userMentionEntities.length;
        }
        
        if (urlEntities != null) {
            System.arraycopy(urlEntities, 0, entityIndexes, copyStartIndex, urlEntities.length);
            copyStartIndex += urlEntities.length;
        }
        
        if (hashtagEntities != null) {
            System.arraycopy(hashtagEntities, 0, entityIndexes, copyStartIndex, hashtagEntities.length);
            copyStartIndex += hashtagEntities.length;
        }
        
        if (mediaEntities != null) {
            System.arraycopy(mediaEntities, 0, entityIndexes, copyStartIndex, mediaEntities.length);
        }

        Arrays.sort(entityIndexes);
        boolean handlingStart = true;
        int entityIndex = 0;

        int delta = 0;
        int semicolonIndex;
        String escaped;
        String entity;
        StringBuilder unescaped = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '&') {
                semicolonIndex = text.indexOf(";", i);
                if (-1 != semicolonIndex) {
                    escaped = text.substring(i, semicolonIndex + 1);
                    entity = escapeEntityMap.get(escaped);
                    if (entity != null) {
                        unescaped.append(entity);
                        i = semicolonIndex;
                    } else {
                        unescaped.append(c);
                    }
                } else {
                    unescaped.append(c);
                }
            } else {
                unescaped.append(c);
            }
            if (entityIndex < entityIndexes.length) {
                if (handlingStart) {
                    if (entityIndexes[entityIndex].getStart() == (delta + i)) {
                        entityIndexes[entityIndex].setStart(unescaped.length() - 1);
                        handlingStart = false;
                    }
                } else if (entityIndexes[entityIndex].getEnd() == (delta + i)) {
                    entityIndexes[entityIndex].setEnd(unescaped.length() - 1);
                    entityIndex++;
                    handlingStart = true;
                }
            }
        }
        if (entityIndex < entityIndexes.length) {
            if (entityIndexes[entityIndex].getEnd() == (text.length())) {
                entityIndexes[entityIndex].setEnd(unescaped.length());
            }
        }

        return unescaped.toString();
    }

    private static final Map<String, String> entityEscapeMap = new HashMap<String, String>();
    private static final Map<String, String> escapeEntityMap = new HashMap<String, String>();

    static {
        String[][] entities =
                {{"&nbsp;", "&#160;"/* no-break space = non-breaking space */, "\u00A0"}
                        , {"&iexcl;", "&#161;"/* inverted exclamation mark */, "\u00A1"}
                        , {"&cent;", "&#162;"/* cent sign */, "\u00A2"}
                        , {"&pound;", "&#163;"/* pound sign */, "\u00A3"}
                        , {"&curren;", "&#164;"/* currency sign */, "\u00A4"}
                        , {"&yen;", "&#165;"/* yen sign = yuan sign */, "\u00A5"}
                        , {"&brvbar;", "&#166;"/* broken bar = broken vertical bar */, "\u00A6"}
                        , {"&sect;", "&#167;"/* section sign */, "\u00A7"}
                        , {"&uml;", "&#168;"/* diaeresis = spacing diaeresis */, "\u00A8"}
                        , {"&copy;", "&#169;"/* copyright sign */, "\u00A9"}
                        , {"&ordf;", "&#170;"/* feminine ordinal indicator */, "\u00AA"}
                        , {"&laquo;", "&#171;"/* left-pointing double angle quotation mark = left pointing guillemet */, "\u00AB"}
                        , {"&not;", "&#172;"/* not sign = discretionary hyphen */, "\u00AC"}
                        , {"&shy;", "&#173;"/* soft hyphen = discretionary hyphen */, "\u00AD"}
                        , {"&reg;", "&#174;"/* registered sign = registered trade mark sign */, "\u00AE"}
                        , {"&macr;", "&#175;"/* macron = spacing macron = overline = APL overbar */, "\u00AF"}
                        , {"&deg;", "&#176;"/* degree sign */, "\u00B0"}
                        , {"&plusmn;", "&#177;"/* plus-minus sign = plus-or-minus sign */, "\u00B1"}
                        , {"&sup2;", "&#178;"/* superscript two = superscript digit two = squared */, "\u00B2"}
                        , {"&sup3;", "&#179;"/* superscript three = superscript digit three = cubed */, "\u00B3"}
                        , {"&acute;", "&#180;"/* acute accent = spacing acute */, "\u00B4"}
                        , {"&micro;", "&#181;"/* micro sign */, "\u00B5"}
                        , {"&para;", "&#182;"/* pilcrow sign = paragraph sign */, "\u00B6"}
                        , {"&middot;", "&#183;"/* middle dot = Georgian comma = Greek middle dot */, "\u00B7"}
                        , {"&cedil;", "&#184;"/* cedilla = spacing cedilla */, "\u00B8"}
                        , {"&sup1;", "&#185;"/* superscript one = superscript digit one */, "\u00B9"}
                        , {"&ordm;", "&#186;"/* masculine ordinal indicator */, "\u00BA"}
                        , {"&raquo;", "&#187;"/* right-pointing double angle quotation mark = right pointing guillemet */, "\u00BB"}
                        , {"&frac14;", "&#188;"/* vulgar fraction one quarter = fraction one quarter */, "\u00BC"}
                        , {"&frac12;", "&#189;"/* vulgar fraction one half = fraction one half */, "\u00BD"}
                        , {"&frac34;", "&#190;"/* vulgar fraction three quarters = fraction three quarters */, "\u00BE"}
                        , {"&iquest;", "&#191;"/* inverted question mark = turned question mark */, "\u00BF"}
                        , {"&Agrave;", "&#192;"/* latin capital letter A with grave = latin capital letter A grave */, "\u00C0"}
                        , {"&Aacute;", "&#193;"/* latin capital letter A with acute */, "\u00C1"}
                        , {"&Acirc;", "&#194;"/* latin capital letter A with circumflex */, "\u00C2"}
                        , {"&Atilde;", "&#195;"/* latin capital letter A with tilde */, "\u00C3"}
                        , {"&Auml;", "&#196;"/* latin capital letter A with diaeresis */, "\u00C4"}
                        , {"&Aring;", "&#197;"/* latin capital letter A with ring above = latin capital letter A ring */, "\u00C5"}
                        , {"&AElig;", "&#198;"/* latin capital letter AE = latin capital ligature AE */, "\u00C6"}
                        , {"&Ccedil;", "&#199;"/* latin capital letter C with cedilla */, "\u00C7"}
                        , {"&Egrave;", "&#200;"/* latin capital letter E with grave */, "\u00C8"}
                        , {"&Eacute;", "&#201;"/* latin capital letter E with acute */, "\u00C9"}
                        , {"&Ecirc;", "&#202;"/* latin capital letter E with circumflex */, "\u00CA"}
                        , {"&Euml;", "&#203;"/* latin capital letter E with diaeresis */, "\u00CB"}
                        , {"&Igrave;", "&#204;"/* latin capital letter I with grave */, "\u00CC"}
                        , {"&Iacute;", "&#205;"/* latin capital letter I with acute */, "\u00CD"}
                        , {"&Icirc;", "&#206;"/* latin capital letter I with circumflex */, "\u00CE"}
                        , {"&Iuml;", "&#207;"/* latin capital letter I with diaeresis */, "\u00CF"}
                        , {"&ETH;", "&#208;"/* latin capital letter ETH */, "\u00D0"}
                        , {"&Ntilde;", "&#209;"/* latin capital letter N with tilde */, "\u00D1"}
                        , {"&Ograve;", "&#210;"/* latin capital letter O with grave */, "\u00D2"}
                        , {"&Oacute;", "&#211;"/* latin capital letter O with acute */, "\u00D3"}
                        , {"&Ocirc;", "&#212;"/* latin capital letter O with circumflex */, "\u00D4"}
                        , {"&Otilde;", "&#213;"/* latin capital letter O with tilde */, "\u00D5"}
                        , {"&Ouml;", "&#214;"/* latin capital letter O with diaeresis */, "\u00D6"}
                        , {"&times;", "&#215;"/* multiplication sign */, "\u00D7"}
                        , {"&Oslash;", "&#216;"/* latin capital letter O with stroke = latin capital letter O slash */, "\u00D8"}
                        , {"&Ugrave;", "&#217;"/* latin capital letter U with grave */, "\u00D9"}
                        , {"&Uacute;", "&#218;"/* latin capital letter U with acute */, "\u00DA"}
                        , {"&Ucirc;", "&#219;"/* latin capital letter U with circumflex */, "\u00DB"}
                        , {"&Uuml;", "&#220;"/* latin capital letter U with diaeresis */, "\u00DC"}
                        , {"&Yacute;", "&#221;"/* latin capital letter Y with acute */, "\u00DD"}
                        , {"&THORN;", "&#222;"/* latin capital letter THORN */, "\u00DE"}
                        , {"&szlig;", "&#223;"/* latin small letter sharp s = ess-zed */, "\u00DF"}
                        , {"&agrave;", "&#224;"/* latin small letter a with grave = latin small letter a grave */, "\u00E0"}
                        , {"&aacute;", "&#225;"/* latin small letter a with acute */, "\u00E1"}
                        , {"&acirc;", "&#226;"/* latin small letter a with circumflex */, "\u00E2"}
                        , {"&atilde;", "&#227;"/* latin small letter a with tilde */, "\u00E3"}
                        , {"&auml;", "&#228;"/* latin small letter a with diaeresis */, "\u00E4"}
                        , {"&aring;", "&#229;"/* latin small letter a with ring above = latin small letter a ring */, "\u00E5"}
                        , {"&aelig;", "&#230;"/* latin small letter ae = latin small ligature ae */, "\u00E6"}
                        , {"&ccedil;", "&#231;"/* latin small letter c with cedilla */, "\u00E7"}
                        , {"&egrave;", "&#232;"/* latin small letter e with grave */, "\u00E8"}
                        , {"&eacute;", "&#233;"/* latin small letter e with acute */, "\u00E9"}
                        , {"&ecirc;", "&#234;"/* latin small letter e with circumflex */, "\u00EA"}
                        , {"&euml;", "&#235;"/* latin small letter e with diaeresis */, "\u00EB"}
                        , {"&igrave;", "&#236;"/* latin small letter i with grave */, "\u00EC"}
                        , {"&iacute;", "&#237;"/* latin small letter i with acute */, "\u00ED"}
                        , {"&icirc;", "&#238;"/* latin small letter i with circumflex */, "\u00EE"}
                        , {"&iuml;", "&#239;"/* latin small letter i with diaeresis */, "\u00EF"}
                        , {"&eth;", "&#240;"/* latin small letter eth */, "\u00F0"}
                        , {"&ntilde;", "&#241;"/* latin small letter n with tilde */, "\u00F1"}
                        , {"&ograve;", "&#242;"/* latin small letter o with grave */, "\u00F2"}
                        , {"&oacute;", "&#243;"/* latin small letter o with acute */, "\u00F3"}
                        , {"&ocirc;", "&#244;"/* latin small letter o with circumflex */, "\u00F4"}
                        , {"&otilde;", "&#245;"/* latin small letter o with tilde */, "\u00F5"}
                        , {"&ouml;", "&#246;"/* latin small letter o with diaeresis */, "\u00F6"}
                        , {"&divide;", "&#247;"/* division sign */, "\u00F7"}
                        , {"&oslash;", "&#248;"/* latin small letter o with stroke = latin small letter o slash */, "\u00F8"}
                        , {"&ugrave;", "&#249;"/* latin small letter u with grave */, "\u00F9"}
                        , {"&uacute;", "&#250;"/* latin small letter u with acute */, "\u00FA"}
                        , {"&ucirc;", "&#251;"/* latin small letter u with circumflex */, "\u00FB"}
                        , {"&uuml;", "&#252;"/* latin small letter u with diaeresis */, "\u00FC"}
                        , {"&yacute;", "&#253;"/* latin small letter y with acute */, "\u00FD"}
                        , {"&thorn;", "&#254;"/* latin small letter thorn with */, "\u00FE"}
                        , {"&yuml;", "&#255;"/* latin small letter y with diaeresis */, "\u00FF"}
                        , {"&fnof;", "&#402;"/* latin small f with hook = function = florin */, "\u0192"}
/* Greek */
                        , {"&Alpha;", "&#913;"/* greek capital letter alpha */, "\u0391"}
                        , {"&Beta;", "&#914;"/* greek capital letter beta */, "\u0392"}
                        , {"&Gamma;", "&#915;"/* greek capital letter gamma */, "\u0393"}
                        , {"&Delta;", "&#916;"/* greek capital letter delta */, "\u0394"}
                        , {"&Epsilon;", "&#917;"/* greek capital letter epsilon */, "\u0395"}
                        , {"&Zeta;", "&#918;"/* greek capital letter zeta */, "\u0396"}
                        , {"&Eta;", "&#919;"/* greek capital letter eta */, "\u0397"}
                        , {"&Theta;", "&#920;"/* greek capital letter theta */, "\u0398"}
                        , {"&Iota;", "&#921;"/* greek capital letter iota */, "\u0399"}
                        , {"&Kappa;", "&#922;"/* greek capital letter kappa */, "\u039A"}
                        , {"&Lambda;", "&#923;"/* greek capital letter lambda */, "\u039B"}
                        , {"&Mu;", "&#924;"/* greek capital letter mu */, "\u039C"}
                        , {"&Nu;", "&#925;"/* greek capital letter nu */, "\u039D"}
                        , {"&Xi;", "&#926;"/* greek capital letter xi */, "\u039E"}
                        , {"&Omicron;", "&#927;"/* greek capital letter omicron */, "\u039F"}
                        , {"&Pi;", "&#928;"/* greek capital letter pi */, "\u03A0"}
                        , {"&Rho;", "&#929;"/* greek capital letter rho */, "\u03A1"}
/* there is no Sigmaf and no \u03A2 */
                        , {"&Sigma;", "&#931;"/* greek capital letter sigma */, "\u03A3"}
                        , {"&Tau;", "&#932;"/* greek capital letter tau */, "\u03A4"}
                        , {"&Upsilon;", "&#933;"/* greek capital letter upsilon */, "\u03A5"}
                        , {"&Phi;", "&#934;"/* greek capital letter phi */, "\u03A6"}
                        , {"&Chi;", "&#935;"/* greek capital letter chi */, "\u03A7"}
                        , {"&Psi;", "&#936;"/* greek capital letter psi */, "\u03A8"}
                        , {"&Omega;", "&#937;"/* greek capital letter omega */, "\u03A9"}
                        , {"&alpha;", "&#945;"/* greek small letter alpha */, "\u03B1"}
                        , {"&beta;", "&#946;"/* greek small letter beta */, "\u03B2"}
                        , {"&gamma;", "&#947;"/* greek small letter gamma */, "\u03B3"}
                        , {"&delta;", "&#948;"/* greek small letter delta */, "\u03B4"}
                        , {"&epsilon;", "&#949;"/* greek small letter epsilon */, "\u03B5"}
                        , {"&zeta;", "&#950;"/* greek small letter zeta */, "\u03B6"}
                        , {"&eta;", "&#951;"/* greek small letter eta */, "\u03B7"}
                        , {"&theta;", "&#952;"/* greek small letter theta */, "\u03B8"}
                        , {"&iota;", "&#953;"/* greek small letter iota */, "\u03B9"}
                        , {"&kappa;", "&#954;"/* greek small letter kappa */, "\u03BA"}
                        , {"&lambda;", "&#955;"/* greek small letter lambda */, "\u03BB"}
                        , {"&mu;", "&#956;"/* greek small letter mu */, "\u03BC"}
                        , {"&nu;", "&#957;"/* greek small letter nu */, "\u03BD"}
                        , {"&xi;", "&#958;"/* greek small letter xi */, "\u03BE"}
                        , {"&omicron;", "&#959;"/* greek small letter omicron */, "\u03BF"}
                        , {"&pi;", "&#960;"/* greek small letter pi */, "\u03C0"}
                        , {"&rho;", "&#961;"/* greek small letter rho */, "\u03C1"}
                        , {"&sigmaf;", "&#962;"/* greek small letter final sigma */, "\u03C2"}
                        , {"&sigma;", "&#963;"/* greek small letter sigma */, "\u03C3"}
                        , {"&tau;", "&#964;"/* greek small letter tau */, "\u03C4"}
                        , {"&upsilon;", "&#965;"/* greek small letter upsilon */, "\u03C5"}
                        , {"&phi;", "&#966;"/* greek small letter phi */, "\u03C6"}
                        , {"&chi;", "&#967;"/* greek small letter chi */, "\u03C7"}
                        , {"&psi;", "&#968;"/* greek small letter psi */, "\u03C8"}
                        , {"&omega;", "&#969;"/* greek small letter omega */, "\u03C9"}
                        , {"&thetasym;", "&#977;"/* greek small letter theta symbol */, "\u03D1"}
                        , {"&upsih;", "&#978;"/* greek upsilon with hook symbol */, "\u03D2"}
                        , {"&piv;", "&#982;"/* greek pi symbol */, "\u03D6"}
/* General Punctuation */
                        , {"&bull;", "&#8226;"/* bullet = black small circle */, "\u2022"}
/* bullet is NOT the same as bullet operator  ,"\u2219*/
                        , {"&hellip;", "&#8230;"/* horizontal ellipsis = three dot leader */, "\u2026"}
                        , {"&prime;", "&#8242;"/* prime = minutes = feet */, "\u2032"}
                        , {"&Prime;", "&#8243;"/* double prime = seconds = inches */, "\u2033"}
                        , {"&oline;", "&#8254;"/* overline = spacing overscore */, "\u203E"}
                        , {"&frasl;", "&#8260;"/* fraction slash */, "\u2044"}
/* Letterlike Symbols */
                        , {"&weierp;", "&#8472;"/* script capital P = power set = Weierstrass p */, "\u2118"}
                        , {"&image;", "&#8465;"/* blackletter capital I = imaginary part */, "\u2111"}
                        , {"&real;", "&#8476;"/* blackletter capital R = real part symbol */, "\u211C"}
                        , {"&trade;", "&#8482;"/* trade mark sign */, "\u2122"}
                        , {"&alefsym;", "&#8501;"/* alef symbol = first transfinite cardinal */, "\u2135"}
/* alef symbol is NOT the same as hebrew letter alef  ,"\u05D0"}*/
/* Arrows */
                        , {"&larr;", "&#8592;"/* leftwards arrow */, "\u2190"}
                        , {"&uarr;", "&#8593;"/* upwards arrow */, "\u2191"}
                        , {"&rarr;", "&#8594;"/* rightwards arrow */, "\u2192"}
                        , {"&darr;", "&#8595;"/* downwards arrow */, "\u2193"}
                        , {"&harr;", "&#8596;"/* left right arrow */, "\u2194"}
                        , {"&crarr;", "&#8629;"/* downwards arrow with corner leftwards = carriage return */, "\u21B5"}
                        , {"&lArr;", "&#8656;"/* leftwards double arrow */, "\u21D0"}
/* Unicode does not say that lArr is the same as the 'is implied by' arrow but also does not have any other character for that function. So ? lArr can be used for 'is implied by' as ISOtech suggests */
                        , {"&uArr;", "&#8657;"/* upwards double arrow */, "\u21D1"}
                        , {"&rArr;", "&#8658;"/* rightwards double arrow */, "\u21D2"}
/* Unicode does not say this is the 'implies' character but does not have another character with this function so ? rArr can be used for 'implies' as ISOtech suggests */
                        , {"&dArr;", "&#8659;"/* downwards double arrow */, "\u21D3"}
                        , {"&hArr;", "&#8660;"/* left right double arrow */, "\u21D4"}
/* Mathematical Operators */
                        , {"&forall;", "&#8704;"/* for all */, "\u2200"}
                        , {"&part;", "&#8706;"/* partial differential */, "\u2202"}
                        , {"&exist;", "&#8707;"/* there exists */, "\u2203"}
                        , {"&empty;", "&#8709;"/* empty set = null set = diameter */, "\u2205"}
                        , {"&nabla;", "&#8711;"/* nabla = backward difference */, "\u2207"}
                        , {"&isin;", "&#8712;"/* element of */, "\u2208"}
                        , {"&notin;", "&#8713;"/* not an element of */, "\u2209"}
                        , {"&ni;", "&#8715;"/* contains as member */, "\u220B"}
/* should there be a more memorable name than 'ni'? */
                        , {"&prod;", "&#8719;"/* n-ary product = product sign */, "\u220F"}
/* prod is NOT the same character as ,"\u03A0"}*/
                        , {"&sum;", "&#8721;"/* n-ary sumation */, "\u2211"}
/* sum is NOT the same character as ,"\u03A3"}*/
                        , {"&minus;", "&#8722;"/* minus sign */, "\u2212"}
                        , {"&lowast;", "&#8727;"/* asterisk operator */, "\u2217"}
                        , {"&radic;", "&#8730;"/* square root = radical sign */, "\u221A"}
                        , {"&prop;", "&#8733;"/* proportional to */, "\u221D"}
                        , {"&infin;", "&#8734;"/* infinity */, "\u221E"}
                        , {"&ang;", "&#8736;"/* angle */, "\u2220"}
                        , {"&and;", "&#8743;"/* logical and = wedge */, "\u2227"}
                        , {"&or;", "&#8744;"/* logical or = vee */, "\u2228"}
                        , {"&cap;", "&#8745;"/* intersection = cap */, "\u2229"}
                        , {"&cup;", "&#8746;"/* union = cup */, "\u222A"}
                        , {"&int;", "&#8747;"/* integral */, "\u222B"}
                        , {"&there4;", "&#8756;"/* therefore */, "\u2234"}
                        , {"&sim;", "&#8764;"/* tilde operator = varies with = similar to */, "\u223C"}
/* tilde operator is NOT the same character as the tilde  ,"\u007E"}*/
                        , {"&cong;", "&#8773;"/* approximately equal to */, "\u2245"}
                        , {"&asymp;", "&#8776;"/* almost equal to = asymptotic to */, "\u2248"}
                        , {"&ne;", "&#8800;"/* not equal to */, "\u2260"}
                        , {"&equiv;", "&#8801;"/* identical to */, "\u2261"}
                        , {"&le;", "&#8804;"/* less-than or equal to */, "\u2264"}
                        , {"&ge;", "&#8805;"/* greater-than or equal to */, "\u2265"}
                        , {"&sub;", "&#8834;"/* subset of */, "\u2282"}
                        , {"&sup;", "&#8835;"/* superset of */, "\u2283"}
/* note that nsup  'not a superset of  ,"\u2283"}*/
                        , {"&sube;", "&#8838;"/* subset of or equal to */, "\u2286"}
                        , {"&supe;", "&#8839;"/* superset of or equal to */, "\u2287"}
                        , {"&oplus;", "&#8853;"/* circled plus = direct sum */, "\u2295"}
                        , {"&otimes;", "&#8855;"/* circled times = vector product */, "\u2297"}
                        , {"&perp;", "&#8869;"/* up tack = orthogonal to = perpendicular */, "\u22A5"}
                        , {"&sdot;", "&#8901;"/* dot operator */, "\u22C5"}
/* dot operator is NOT the same character as ,"\u00B7"}
/* Miscellaneous Technical */
                        , {"&lceil;", "&#8968;"/* left ceiling = apl upstile */, "\u2308"}
                        , {"&rceil;", "&#8969;"/* right ceiling */, "\u2309"}
                        , {"&lfloor;", "&#8970;"/* left floor = apl downstile */, "\u230A"}
                        , {"&rfloor;", "&#8971;"/* right floor */, "\u230B"}
                        , {"&lang;", "&#9001;"/* left-pointing angle bracket = bra */, "\u2329"}
/* lang is NOT the same character as ,"\u003C"}*/
                        , {"&rang;", "&#9002;"/* right-pointing angle bracket = ket */, "\u232A"}
/* rang is NOT the same character as ,"\u003E"}*/
/* Geometric Shapes */
                        , {"&loz;", "&#9674;"/* lozenge */, "\u25CA"}
/* Miscellaneous Symbols */
                        , {"&spades;", "&#9824;"/* black spade suit */, "\u2660"}
/* black here seems to mean filled as opposed to hollow */
                        , {"&clubs;", "&#9827;"/* black club suit = shamrock */, "\u2663"}
                        , {"&hearts;", "&#9829;"/* black heart suit = valentine */, "\u2665"}
                        , {"&diams;", "&#9830;"/* black diamond suit */, "\u2666"}
                        , {"&quot;", "&#34;" /* quotation mark = APL quote */, "\""}
                        , {"&amp;", "&#38;" /* ampersand */, "\u0026"}
                        , {"&lt;", "&#60;" /* less-than sign */, "\u003C"}
                        , {"&gt;", "&#62;" /* greater-than sign */, "\u003E"}
/* Latin Extended-A */
                        , {"&OElig;", "&#338;" /* latin capital ligature OE */, "\u0152"}
                        , {"&oelig;", "&#339;" /* latin small ligature oe */, "\u0153"}
/* ligature is a misnomer  this is a separate character in some languages */
                        , {"&Scaron;", "&#352;" /* latin capital letter S with caron */, "\u0160"}
                        , {"&scaron;", "&#353;" /* latin small letter s with caron */, "\u0161"}
                        , {"&Yuml;", "&#376;" /* latin capital letter Y with diaeresis */, "\u0178"}
/* Spacing Modifier Letters */
                        , {"&circ;", "&#710;" /* modifier letter circumflex accent */, "\u02C6"}
                        , {"&tilde;", "&#732;" /* small tilde */, "\u02DC"}
/* General Punctuation */
                        , {"&ensp;", "&#8194;"/* en space */, "\u2002"}
                        , {"&emsp;", "&#8195;"/* em space */, "\u2003"}
                        , {"&thinsp;", "&#8201;"/* thin space */, "\u2009"}
                        , {"&zwnj;", "&#8204;"/* zero width non-joiner */, "\u200C"}
                        , {"&zwj;", "&#8205;"/* zero width joiner */, "\u200D"}
                        , {"&lrm;", "&#8206;"/* left-to-right mark */, "\u200E"}
                        , {"&rlm;", "&#8207;"/* right-to-left mark */, "\u200F"}
                        , {"&ndash;", "&#8211;"/* en dash */, "\u2013"}
                        , {"&mdash;", "&#8212;"/* em dash */, "\u2014"}
                        , {"&lsquo;", "&#8216;"/* left single quotation mark */, "\u2018"}
                        , {"&rsquo;", "&#8217;"/* right single quotation mark */, "\u2019"}
                        , {"&sbquo;", "&#8218;"/* single low-9 quotation mark */, "\u201A"}
                        , {"&ldquo;", "&#8220;"/* left double quotation mark */, "\u201C"}
                        , {"&rdquo;", "&#8221;"/* right double quotation mark */, "\u201D"}
                        , {"&bdquo;", "&#8222;"/* double low-9 quotation mark */, "\u201E"}
                        , {"&dagger;", "&#8224;"/* dagger */, "\u2020"}
                        , {"&Dagger;", "&#8225;"/* double dagger */, "\u2021"}
                        , {"&permil;", "&#8240;"/* per mille sign */, "\u2030"}
                        , {"&lsaquo;", "&#8249;"/* single left-pointing angle quotation mark */, "\u2039"}
/* lsaquo is proposed but not yet ISO standardized */
                        , {"&rsaquo;", "&#8250;"/* single right-pointing angle quotation mark */, "\u203A"}
/* rsaquo is proposed but not yet ISO standardized */
                        , {"&euro;", "&#8364;" /* euro sign */, "\u20AC"}};
        for (String[] entity : entities) {
            entityEscapeMap.put(entity[2], entity[0]);
            escapeEntityMap.put(entity[0], entity[2]);
            escapeEntityMap.put(entity[1], entity[2]);
        }
    }
}
