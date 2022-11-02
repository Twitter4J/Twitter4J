package twitter4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONSchemaKeywordTest {
    @Test
    void object() {
        var extract = JSONSchema.extract("#/", """
                {
                  "keywords": {
                    "type": "object",
                    "properties": {
                      "abstract": {
                        "type": "string"
                      },
                      "continue": {
                        "type": "string"
                      },
                      "for": {
                        "type": "string"
                      },
                      "new": {
                        "type": "string"
                      },
                      "switch": {
                        "type": "string"
                      },
                      "assert": {
                        "type": "string"
                      },
                      "default": {
                        "type": "string"
                      },
                      "if": {
                        "type": "string"
                      },
                      "package": {
                        "type": "string"
                      },
                      "synchronized": {
                        "type": "string"
                      },
                      "boolean": {
                        "type": "string"
                      },
                      "do": {
                        "type": "string"
                      },
                      "goto": {
                        "type": "string"
                      },
                      "private": {
                        "type": "string"
                      },
                      "this": {
                        "type": "string"
                      },
                      "break": {
                        "type": "string"
                      },
                      "double": {
                        "type": "string"
                      },
                      "implements": {
                        "type": "string"
                      },
                      "protected": {
                        "type": "string"
                      },
                      "throw": {
                        "type": "string"
                      },
                      "byte": {
                        "type": "string"
                      },
                      "else": {
                        "type": "string"
                      },
                      "import": {
                        "type": "string"
                      },
                      "public": {
                        "type": "string"
                      },
                      "throws": {
                        "type": "string"
                      },
                      "case": {
                        "type": "string"
                      },
                      "enum": {
                        "type": "string"
                      },
                      "instanceof": {
                        "type": "string"
                      },
                      "return": {
                        "type": "string"
                      },
                      "transient": {
                        "type": "string"
                      },
                      "catch": {
                        "type": "string"
                      },
                      "extends": {
                        "type": "string"
                      },
                      "int": {
                        "type": "string"
                      },
                      "short": {
                        "type": "string"
                      },
                      "try": {
                        "type": "string"
                      },
                      "char": {
                        "type": "string"
                      },
                      "final": {
                        "type": "string"
                      },
                      "interface": {
                        "type": "string"
                      },
                      "static": {
                        "type": "string"
                      },
                      "void": {
                        "type": "string"
                      },
                      "class": {
                        "type": "string"
                      },
                      "finally": {
                        "type": "string"
                      },
                      "long": {
                        "type": "string"
                      },
                      "strictfp": {
                        "type": "string"
                      },
                      "volatile": {
                        "type": "string"
                      },
                      "const": {
                        "type": "string"
                      },
                      "float": {
                        "type": "string"
                      },
                      "native": {
                        "type": "string"
                      },
                      "super": {
                        "type": "string"
                      },
                      "while": {
                        "type": "string"
                      },
                      "_": {
                        "type": "string"
                      },
                      "exports": {
                        "type": "string"
                      },
                      "opens": {
                        "type": "string"
                      },
                      "requires": {
                        "type": "string"
                      },
                      "uses": {
                        "type": "string"
                      },
                      "module": {
                        "type": "string"
                      },
                      "permits": {
                        "type": "string"
                      },
                      "sealed": {
                        "type": "string"
                      },
                      "var": {
                        "type": "string"
                      },
                      "non-sealed": {
                        "type": "string"
                      },
                      "provides": {
                        "type": "string"
                      },
                      "to": {
                        "type": "string"
                      },
                      "with": {
                        "type": "string"
                      },
                      "open": {
                        "type": "string"
                      },
                      "record": {
                        "type": "string"
                      },
                      "transitive": {
                        "type": "string"
                      },
                      "yield": {
                        "type": "string"
                      }
                    }
                  }
                }""");
        JSONSchema problemFields = extract.get("#/keywords");
        assertEquals("""
                this.abstract_ = json.getString("abstract");
                this.continue_ = json.getString("continue");
                this.for_ = json.getString("for");
                this.new_ = json.getString("new");
                this.switch_ = json.getString("switch");
                this.assert_ = json.getString("assert");
                this.default_ = json.getString("default");
                this.if_ = json.getString("if");
                this.package_ = json.getString("package");
                this.synchronized_ = json.getString("synchronized");
                this.boolean_ = json.getString("boolean");
                this.do_ = json.getString("do");
                this.goto_ = json.getString("goto");
                this.private_ = json.getString("private");
                this.this_ = json.getString("this");
                this.break_ = json.getString("break");
                this.double_ = json.getString("double");
                this.implements_ = json.getString("implements");
                this.protected_ = json.getString("protected");
                this.throw_ = json.getString("throw");
                this.byte_ = json.getString("byte");
                this.else_ = json.getString("else");
                this.import_ = json.getString("import");
                this.public_ = json.getString("public");
                this.throws_ = json.getString("throws");
                this.case_ = json.getString("case");
                this.enum_ = json.getString("enum");
                this.instanceof_ = json.getString("instanceof");
                this.return_ = json.getString("return");
                this.transient_ = json.getString("transient");
                this.catch_ = json.getString("catch");
                this.extends_ = json.getString("extends");
                this.int_ = json.getString("int");
                this.short_ = json.getString("short");
                this.try_ = json.getString("try");
                this.char_ = json.getString("char");
                this.final_ = json.getString("final");
                this.interface_ = json.getString("interface");
                this.static_ = json.getString("static");
                this.void_ = json.getString("void");
                this.class_ = json.getString("class");
                this.finally_ = json.getString("finally");
                this.long_ = json.getString("long");
                this.strictfp_ = json.getString("strictfp");
                this.volatile_ = json.getString("volatile");
                this.const_ = json.getString("const");
                this.float_ = json.getString("float");
                this.native_ = json.getString("native");
                this.super_ = json.getString("super");
                this.while_ = json.getString("while");
                this.__ = json.getString("_");
                this.exports_ = json.getString("exports");
                this.opens_ = json.getString("opens");
                this.requires_ = json.getString("requires");
                this.uses_ = json.getString("uses");
                this.module_ = json.getString("module");
                this.permits_ = json.getString("permits");
                this.sealed_ = json.getString("sealed");
                this.var_ = json.getString("var");
                this.nonSealed = json.getString("non-sealed");
                this.provides_ = json.getString("provides");
                this.to_ = json.getString("to");
                this.with_ = json.getString("with");
                this.open_ = json.getString("open");
                this.record_ = json.getString("record");
                this.transitive_ = json.getString("transitive");
                this.yield_ = json.getString("yield");""", problemFields.asConstructorAssignments("twitter4j").codeFragment());
        assertEquals("""
                        @Nullable
                        private final String abstract_;
                                                
                        @Nullable
                        private final String continue_;
                                                
                        @Nullable
                        private final String for_;
                                                
                        @Nullable
                        private final String new_;
                                                
                        @Nullable
                        private final String switch_;
                                                
                        @Nullable
                        private final String assert_;
                                                
                        @Nullable
                        private final String default_;
                                                
                        @Nullable
                        private final String if_;
                                                
                        @Nullable
                        private final String package_;
                                                
                        @Nullable
                        private final String synchronized_;
                                                
                        @Nullable
                        private final String boolean_;
                                                
                        @Nullable
                        private final String do_;
                                                
                        @Nullable
                        private final String goto_;
                                                
                        @Nullable
                        private final String private_;
                                                
                        @Nullable
                        private final String this_;
                                                
                        @Nullable
                        private final String break_;
                                                
                        @Nullable
                        private final String double_;
                                                
                        @Nullable
                        private final String implements_;
                                                
                        @Nullable
                        private final String protected_;
                                                
                        @Nullable
                        private final String throw_;
                                                
                        @Nullable
                        private final String byte_;
                                                
                        @Nullable
                        private final String else_;
                                                
                        @Nullable
                        private final String import_;
                                                
                        @Nullable
                        private final String public_;
                                                
                        @Nullable
                        private final String throws_;
                                                
                        @Nullable
                        private final String case_;
                                                
                        @Nullable
                        private final String enum_;
                                                
                        @Nullable
                        private final String instanceof_;
                                                
                        @Nullable
                        private final String return_;
                                                
                        @Nullable
                        private final String transient_;
                                                
                        @Nullable
                        private final String catch_;
                                                
                        @Nullable
                        private final String extends_;
                                                
                        @Nullable
                        private final String int_;
                                                
                        @Nullable
                        private final String short_;
                                                
                        @Nullable
                        private final String try_;
                                                
                        @Nullable
                        private final String char_;
                                                
                        @Nullable
                        private final String final_;
                                                
                        @Nullable
                        private final String interface_;
                                                
                        @Nullable
                        private final String static_;
                                                
                        @Nullable
                        private final String void_;
                                                
                        @Nullable
                        private final String class_;
                                                
                        @Nullable
                        private final String finally_;
                                                
                        @Nullable
                        private final String long_;
                                                
                        @Nullable
                        private final String strictfp_;
                                                
                        @Nullable
                        private final String volatile_;
                                                
                        @Nullable
                        private final String const_;
                                                
                        @Nullable
                        private final String float_;
                                                
                        @Nullable
                        private final String native_;
                                                
                        @Nullable
                        private final String super_;
                                                
                        @Nullable
                        private final String while_;
                                                
                        @Nullable
                        private final String __;
                                                
                        @Nullable
                        private final String exports_;
                                                
                        @Nullable
                        private final String opens_;
                                                
                        @Nullable
                        private final String requires_;
                                                
                        @Nullable
                        private final String uses_;
                                                
                        @Nullable
                        private final String module_;
                                                
                        @Nullable
                        private final String permits_;
                                                
                        @Nullable
                        private final String sealed_;
                                                
                        @Nullable
                        private final String var_;
                                                
                        @Nullable
                        private final String nonSealed;
                                                
                        @Nullable
                        private final String provides_;
                                                
                        @Nullable
                        private final String to_;
                                                
                        @Nullable
                        private final String with_;
                                                
                        @Nullable
                        private final String open_;
                                                
                        @Nullable
                        private final String record_;
                                                
                        @Nullable
                        private final String transitive_;
                                                
                        @Nullable
                        private final String yield_;
                        """,
                problemFields.asFieldDeclarations("twitter4j.v2", null).codeFragment());
        assertEquals("""
                        @Nullable
                        @Override
                        public String getAbstract() {
                            return abstract_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getContinue() {
                            return continue_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getFor() {
                            return for_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getNew() {
                            return new_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getSwitch() {
                            return switch_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getAssert() {
                            return assert_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getDefault() {
                            return default_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getIf() {
                            return if_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getPackage() {
                            return package_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getSynchronized() {
                            return synchronized_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getBoolean() {
                            return boolean_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getDo() {
                            return do_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getGoto() {
                            return goto_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getPrivate() {
                            return private_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getThis() {
                            return this_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getBreak() {
                            return break_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getDouble() {
                            return double_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getImplements() {
                            return implements_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getProtected() {
                            return protected_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getThrow() {
                            return throw_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getByte() {
                            return byte_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getElse() {
                            return else_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getImport() {
                            return import_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getPublic() {
                            return public_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getThrows() {
                            return throws_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getCase() {
                            return case_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getEnum() {
                            return enum_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getInstanceof() {
                            return instanceof_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getReturn() {
                            return return_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getTransient() {
                            return transient_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getCatch() {
                            return catch_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getExtends() {
                            return extends_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getInt() {
                            return int_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getShort() {
                            return short_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getTry() {
                            return try_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getChar() {
                            return char_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getFinal() {
                            return final_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getInterface() {
                            return interface_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getStatic() {
                            return static_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getVoid() {
                            return void_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getClass() {
                            return class_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getFinally() {
                            return finally_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getLong() {
                            return long_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getStrictfp() {
                            return strictfp_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getVolatile() {
                            return volatile_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getConst() {
                            return const_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getFloat() {
                            return float_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getNative() {
                            return native_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getSuper() {
                            return super_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getWhile() {
                            return while_;
                        }
                                                
                        @Nullable
                        @Override
                        public String get_() {
                            return __;
                        }
                                                
                        @Nullable
                        @Override
                        public String getExports() {
                            return exports_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getOpens() {
                            return opens_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getRequires() {
                            return requires_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getUses() {
                            return uses_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getModule() {
                            return module_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getPermits() {
                            return permits_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getSealed() {
                            return sealed_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getVar() {
                            return var_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getNonSealed() {
                            return nonSealed;
                        }
                                                
                        @Nullable
                        @Override
                        public String getProvides() {
                            return provides_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getTo() {
                            return to_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getWith() {
                            return with_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getOpen() {
                            return open_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getRecord() {
                            return record_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getTransitive() {
                            return transitive_;
                        }
                                                
                        @Nullable
                        @Override
                        public String getYield() {
                            return yield_;
                        }
                        """,
                problemFields.asGetterImplementations("twitter4j.v2", null).codeFragment());
    }
}
