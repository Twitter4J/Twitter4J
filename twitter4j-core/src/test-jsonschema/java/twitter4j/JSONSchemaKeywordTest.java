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
                        private final String yield_;""",
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
                problemFields.asGetterImplementations("twitter4j.v2", null, false).codeFragment());
        assertEquals("""
                        /**
                         * @return abstract
                         */
                        @Nullable
                        String getAbstract();
                                                
                        /**
                         * @return continue
                         */
                        @Nullable
                        String getContinue();
                                                
                        /**
                         * @return for
                         */
                        @Nullable
                        String getFor();
                                                
                        /**
                         * @return new
                         */
                        @Nullable
                        String getNew();
                                                
                        /**
                         * @return switch
                         */
                        @Nullable
                        String getSwitch();
                                                
                        /**
                         * @return assert
                         */
                        @Nullable
                        String getAssert();
                                                
                        /**
                         * @return default
                         */
                        @Nullable
                        String getDefault();
                                                
                        /**
                         * @return if
                         */
                        @Nullable
                        String getIf();
                                                
                        /**
                         * @return package
                         */
                        @Nullable
                        String getPackage();
                                                
                        /**
                         * @return synchronized
                         */
                        @Nullable
                        String getSynchronized();
                                                
                        /**
                         * @return boolean
                         */
                        @Nullable
                        String getBoolean();
                                                
                        /**
                         * @return do
                         */
                        @Nullable
                        String getDo();
                                                
                        /**
                         * @return goto
                         */
                        @Nullable
                        String getGoto();
                                                
                        /**
                         * @return private
                         */
                        @Nullable
                        String getPrivate();
                                                
                        /**
                         * @return this
                         */
                        @Nullable
                        String getThis();
                                                
                        /**
                         * @return break
                         */
                        @Nullable
                        String getBreak();
                                                
                        /**
                         * @return double
                         */
                        @Nullable
                        String getDouble();
                                                
                        /**
                         * @return implements
                         */
                        @Nullable
                        String getImplements();
                                                
                        /**
                         * @return protected
                         */
                        @Nullable
                        String getProtected();
                                                
                        /**
                         * @return throw
                         */
                        @Nullable
                        String getThrow();
                                                
                        /**
                         * @return byte
                         */
                        @Nullable
                        String getByte();
                                                
                        /**
                         * @return else
                         */
                        @Nullable
                        String getElse();
                                                
                        /**
                         * @return import
                         */
                        @Nullable
                        String getImport();
                                                
                        /**
                         * @return public
                         */
                        @Nullable
                        String getPublic();
                                                
                        /**
                         * @return throws
                         */
                        @Nullable
                        String getThrows();
                                                
                        /**
                         * @return case
                         */
                        @Nullable
                        String getCase();
                                                
                        /**
                         * @return enum
                         */
                        @Nullable
                        String getEnum();
                                                
                        /**
                         * @return instanceof
                         */
                        @Nullable
                        String getInstanceof();
                                                
                        /**
                         * @return return
                         */
                        @Nullable
                        String getReturn();
                                                
                        /**
                         * @return transient
                         */
                        @Nullable
                        String getTransient();
                                                
                        /**
                         * @return catch
                         */
                        @Nullable
                        String getCatch();
                                                
                        /**
                         * @return extends
                         */
                        @Nullable
                        String getExtends();
                                                
                        /**
                         * @return int
                         */
                        @Nullable
                        String getInt();
                                                
                        /**
                         * @return short
                         */
                        @Nullable
                        String getShort();
                                                
                        /**
                         * @return try
                         */
                        @Nullable
                        String getTry();
                                                
                        /**
                         * @return char
                         */
                        @Nullable
                        String getChar();
                                                
                        /**
                         * @return final
                         */
                        @Nullable
                        String getFinal();
                                                
                        /**
                         * @return interface
                         */
                        @Nullable
                        String getInterface();
                                                
                        /**
                         * @return static
                         */
                        @Nullable
                        String getStatic();
                                                
                        /**
                         * @return void
                         */
                        @Nullable
                        String getVoid();
                                                
                        /**
                         * @return class
                         */
                        @Nullable
                        String getClass();
                                                
                        /**
                         * @return finally
                         */
                        @Nullable
                        String getFinally();
                                                
                        /**
                         * @return long
                         */
                        @Nullable
                        String getLong();
                                                
                        /**
                         * @return strictfp
                         */
                        @Nullable
                        String getStrictfp();
                                                
                        /**
                         * @return volatile
                         */
                        @Nullable
                        String getVolatile();
                                                
                        /**
                         * @return const
                         */
                        @Nullable
                        String getConst();
                                                
                        /**
                         * @return float
                         */
                        @Nullable
                        String getFloat();
                                                
                        /**
                         * @return native
                         */
                        @Nullable
                        String getNative();
                                                
                        /**
                         * @return super
                         */
                        @Nullable
                        String getSuper();
                                                
                        /**
                         * @return while
                         */
                        @Nullable
                        String getWhile();
                                                
                        /**
                         * @return _
                         */
                        @Nullable
                        String get_();
                                                
                        /**
                         * @return exports
                         */
                        @Nullable
                        String getExports();
                                                
                        /**
                         * @return opens
                         */
                        @Nullable
                        String getOpens();
                                                
                        /**
                         * @return requires
                         */
                        @Nullable
                        String getRequires();
                                                
                        /**
                         * @return uses
                         */
                        @Nullable
                        String getUses();
                                                
                        /**
                         * @return module
                         */
                        @Nullable
                        String getModule();
                                                
                        /**
                         * @return permits
                         */
                        @Nullable
                        String getPermits();
                                                
                        /**
                         * @return sealed
                         */
                        @Nullable
                        String getSealed();
                                                
                        /**
                         * @return var
                         */
                        @Nullable
                        String getVar();
                                                
                        /**
                         * @return non-sealed
                         */
                        @Nullable
                        String getNonSealed();
                                                
                        /**
                         * @return provides
                         */
                        @Nullable
                        String getProvides();
                                                
                        /**
                         * @return to
                         */
                        @Nullable
                        String getTo();
                                                
                        /**
                         * @return with
                         */
                        @Nullable
                        String getWith();
                                                
                        /**
                         * @return open
                         */
                        @Nullable
                        String getOpen();
                                                
                        /**
                         * @return record
                         */
                        @Nullable
                        String getRecord();
                                                
                        /**
                         * @return transitive
                         */
                        @Nullable
                        String getTransitive();
                                                
                        /**
                         * @return yield
                         */
                        @Nullable
                        String getYield();
                        """,
                problemFields.asGetterDeclarations("twitter4j.v2", null, false).codeFragment());


        assertEquals("""
                        @Nullable
                        @Override
                        public String abstract_() {
                            return abstract_;
                        }
                                                
                        @Nullable
                        @Override
                        public String continue_() {
                            return continue_;
                        }
                                                
                        @Nullable
                        @Override
                        public String for_() {
                            return for_;
                        }
                                                
                        @Nullable
                        @Override
                        public String new_() {
                            return new_;
                        }
                                                
                        @Nullable
                        @Override
                        public String switch_() {
                            return switch_;
                        }
                                                
                        @Nullable
                        @Override
                        public String assert_() {
                            return assert_;
                        }
                                                
                        @Nullable
                        @Override
                        public String default_() {
                            return default_;
                        }
                                                
                        @Nullable
                        @Override
                        public String if_() {
                            return if_;
                        }
                                                
                        @Nullable
                        @Override
                        public String package_() {
                            return package_;
                        }
                                                
                        @Nullable
                        @Override
                        public String synchronized_() {
                            return synchronized_;
                        }
                                                
                        @Nullable
                        @Override
                        public String boolean_() {
                            return boolean_;
                        }
                                                
                        @Nullable
                        @Override
                        public String do_() {
                            return do_;
                        }
                                                
                        @Nullable
                        @Override
                        public String goto_() {
                            return goto_;
                        }
                                                
                        @Nullable
                        @Override
                        public String private_() {
                            return private_;
                        }
                                                
                        @Nullable
                        @Override
                        public String this_() {
                            return this_;
                        }
                                                
                        @Nullable
                        @Override
                        public String break_() {
                            return break_;
                        }
                                                
                        @Nullable
                        @Override
                        public String double_() {
                            return double_;
                        }
                                                
                        @Nullable
                        @Override
                        public String implements_() {
                            return implements_;
                        }
                                                
                        @Nullable
                        @Override
                        public String protected_() {
                            return protected_;
                        }
                                                
                        @Nullable
                        @Override
                        public String throw_() {
                            return throw_;
                        }
                                                
                        @Nullable
                        @Override
                        public String byte_() {
                            return byte_;
                        }
                                                
                        @Nullable
                        @Override
                        public String else_() {
                            return else_;
                        }
                                                
                        @Nullable
                        @Override
                        public String import_() {
                            return import_;
                        }
                                                
                        @Nullable
                        @Override
                        public String public_() {
                            return public_;
                        }
                                                
                        @Nullable
                        @Override
                        public String throws_() {
                            return throws_;
                        }
                                                
                        @Nullable
                        @Override
                        public String case_() {
                            return case_;
                        }
                                                
                        @Nullable
                        @Override
                        public String enum_() {
                            return enum_;
                        }
                                                
                        @Nullable
                        @Override
                        public String instanceof_() {
                            return instanceof_;
                        }
                                                
                        @Nullable
                        @Override
                        public String return_() {
                            return return_;
                        }
                                                
                        @Nullable
                        @Override
                        public String transient_() {
                            return transient_;
                        }
                                                
                        @Nullable
                        @Override
                        public String catch_() {
                            return catch_;
                        }
                                                
                        @Nullable
                        @Override
                        public String extends_() {
                            return extends_;
                        }
                                                
                        @Nullable
                        @Override
                        public String int_() {
                            return int_;
                        }
                                                
                        @Nullable
                        @Override
                        public String short_() {
                            return short_;
                        }
                                                
                        @Nullable
                        @Override
                        public String try_() {
                            return try_;
                        }
                                                
                        @Nullable
                        @Override
                        public String char_() {
                            return char_;
                        }
                                                
                        @Nullable
                        @Override
                        public String final_() {
                            return final_;
                        }
                                                
                        @Nullable
                        @Override
                        public String interface_() {
                            return interface_;
                        }
                                                
                        @Nullable
                        @Override
                        public String static_() {
                            return static_;
                        }
                                                
                        @Nullable
                        @Override
                        public String void_() {
                            return void_;
                        }
                                                
                        @Nullable
                        @Override
                        public String class_() {
                            return class_;
                        }
                                                
                        @Nullable
                        @Override
                        public String finally_() {
                            return finally_;
                        }
                                                
                        @Nullable
                        @Override
                        public String long_() {
                            return long_;
                        }
                                                
                        @Nullable
                        @Override
                        public String strictfp_() {
                            return strictfp_;
                        }
                                                
                        @Nullable
                        @Override
                        public String volatile_() {
                            return volatile_;
                        }
                                                
                        @Nullable
                        @Override
                        public String const_() {
                            return const_;
                        }
                                                
                        @Nullable
                        @Override
                        public String float_() {
                            return float_;
                        }
                                                
                        @Nullable
                        @Override
                        public String native_() {
                            return native_;
                        }
                                                
                        @Nullable
                        @Override
                        public String super_() {
                            return super_;
                        }
                                                
                        @Nullable
                        @Override
                        public String while_() {
                            return while_;
                        }
                                                
                        @Nullable
                        @Override
                        public String __() {
                            return __;
                        }
                                                
                        @Nullable
                        @Override
                        public String exports() {
                            return exports_;
                        }
                                                
                        @Nullable
                        @Override
                        public String opens() {
                            return opens_;
                        }
                                                
                        @Nullable
                        @Override
                        public String requires() {
                            return requires_;
                        }
                                                
                        @Nullable
                        @Override
                        public String uses() {
                            return uses_;
                        }
                                                
                        @Nullable
                        @Override
                        public String module() {
                            return module_;
                        }
                                                
                        @Nullable
                        @Override
                        public String permits() {
                            return permits_;
                        }
                                                
                        @Nullable
                        @Override
                        public String sealed() {
                            return sealed_;
                        }
                                                
                        @Nullable
                        @Override
                        public String var() {
                            return var_;
                        }
                                                
                        @Nullable
                        @Override
                        public String nonSealed() {
                            return nonSealed;
                        }
                                                
                        @Nullable
                        @Override
                        public String provides() {
                            return provides_;
                        }
                                                
                        @Nullable
                        @Override
                        public String to() {
                            return to_;
                        }
                                                
                        @Nullable
                        @Override
                        public String with() {
                            return with_;
                        }
                                                
                        @Nullable
                        @Override
                        public String open() {
                            return open_;
                        }
                                                
                        @Nullable
                        @Override
                        public String record() {
                            return record_;
                        }
                                                
                        @Nullable
                        @Override
                        public String transitive() {
                            return transitive_;
                        }
                                                
                        @Nullable
                        @Override
                        public String yield() {
                            return yield_;
                        }
                        """,
                problemFields.asGetterImplementations("twitter4j.v2", null, true).codeFragment());
        assertEquals("""
                        /**
                         * @return abstract
                         */
                        @Nullable
                        String abstract_();
                                                
                        /**
                         * @return continue
                         */
                        @Nullable
                        String continue_();
                                                
                        /**
                         * @return for
                         */
                        @Nullable
                        String for_();
                                                
                        /**
                         * @return new
                         */
                        @Nullable
                        String new_();
                                                
                        /**
                         * @return switch
                         */
                        @Nullable
                        String switch_();
                                                
                        /**
                         * @return assert
                         */
                        @Nullable
                        String assert_();
                                                
                        /**
                         * @return default
                         */
                        @Nullable
                        String default_();
                                                
                        /**
                         * @return if
                         */
                        @Nullable
                        String if_();
                                                
                        /**
                         * @return package
                         */
                        @Nullable
                        String package_();
                                                
                        /**
                         * @return synchronized
                         */
                        @Nullable
                        String synchronized_();
                                                
                        /**
                         * @return boolean
                         */
                        @Nullable
                        String boolean_();
                                                
                        /**
                         * @return do
                         */
                        @Nullable
                        String do_();
                                                
                        /**
                         * @return goto
                         */
                        @Nullable
                        String goto_();
                                                
                        /**
                         * @return private
                         */
                        @Nullable
                        String private_();
                                                
                        /**
                         * @return this
                         */
                        @Nullable
                        String this_();
                                                
                        /**
                         * @return break
                         */
                        @Nullable
                        String break_();
                                                
                        /**
                         * @return double
                         */
                        @Nullable
                        String double_();
                                                
                        /**
                         * @return implements
                         */
                        @Nullable
                        String implements_();
                                                
                        /**
                         * @return protected
                         */
                        @Nullable
                        String protected_();
                                                
                        /**
                         * @return throw
                         */
                        @Nullable
                        String throw_();
                                                
                        /**
                         * @return byte
                         */
                        @Nullable
                        String byte_();
                                                
                        /**
                         * @return else
                         */
                        @Nullable
                        String else_();
                                                
                        /**
                         * @return import
                         */
                        @Nullable
                        String import_();
                                                
                        /**
                         * @return public
                         */
                        @Nullable
                        String public_();
                                                
                        /**
                         * @return throws
                         */
                        @Nullable
                        String throws_();
                                                
                        /**
                         * @return case
                         */
                        @Nullable
                        String case_();
                                                
                        /**
                         * @return enum
                         */
                        @Nullable
                        String enum_();
                                                
                        /**
                         * @return instanceof
                         */
                        @Nullable
                        String instanceof_();
                                                
                        /**
                         * @return return
                         */
                        @Nullable
                        String return_();
                                                
                        /**
                         * @return transient
                         */
                        @Nullable
                        String transient_();
                                                
                        /**
                         * @return catch
                         */
                        @Nullable
                        String catch_();
                                                
                        /**
                         * @return extends
                         */
                        @Nullable
                        String extends_();
                                                
                        /**
                         * @return int
                         */
                        @Nullable
                        String int_();
                                                
                        /**
                         * @return short
                         */
                        @Nullable
                        String short_();
                                                
                        /**
                         * @return try
                         */
                        @Nullable
                        String try_();
                                                
                        /**
                         * @return char
                         */
                        @Nullable
                        String char_();
                                                
                        /**
                         * @return final
                         */
                        @Nullable
                        String final_();
                                                
                        /**
                         * @return interface
                         */
                        @Nullable
                        String interface_();
                                                
                        /**
                         * @return static
                         */
                        @Nullable
                        String static_();
                                                
                        /**
                         * @return void
                         */
                        @Nullable
                        String void_();
                                                
                        /**
                         * @return class
                         */
                        @Nullable
                        String class_();
                                                
                        /**
                         * @return finally
                         */
                        @Nullable
                        String finally_();
                                                
                        /**
                         * @return long
                         */
                        @Nullable
                        String long_();
                                                
                        /**
                         * @return strictfp
                         */
                        @Nullable
                        String strictfp_();
                                                
                        /**
                         * @return volatile
                         */
                        @Nullable
                        String volatile_();
                                                
                        /**
                         * @return const
                         */
                        @Nullable
                        String const_();
                                                
                        /**
                         * @return float
                         */
                        @Nullable
                        String float_();
                                                
                        /**
                         * @return native
                         */
                        @Nullable
                        String native_();
                                                
                        /**
                         * @return super
                         */
                        @Nullable
                        String super_();
                                                
                        /**
                         * @return while
                         */
                        @Nullable
                        String while_();
                                                
                        /**
                         * @return _
                         */
                        @Nullable
                        String __();
                                                
                        /**
                         * @return exports
                         */
                        @Nullable
                        String exports();
                                                
                        /**
                         * @return opens
                         */
                        @Nullable
                        String opens();
                                                
                        /**
                         * @return requires
                         */
                        @Nullable
                        String requires();
                                                
                        /**
                         * @return uses
                         */
                        @Nullable
                        String uses();
                                                
                        /**
                         * @return module
                         */
                        @Nullable
                        String module();
                                                
                        /**
                         * @return permits
                         */
                        @Nullable
                        String permits();
                                                
                        /**
                         * @return sealed
                         */
                        @Nullable
                        String sealed();
                                                
                        /**
                         * @return var
                         */
                        @Nullable
                        String var();
                                                
                        /**
                         * @return non-sealed
                         */
                        @Nullable
                        String nonSealed();
                                                
                        /**
                         * @return provides
                         */
                        @Nullable
                        String provides();
                                                
                        /**
                         * @return to
                         */
                        @Nullable
                        String to();
                                                
                        /**
                         * @return with
                         */
                        @Nullable
                        String with();
                                                
                        /**
                         * @return open
                         */
                        @Nullable
                        String open();
                                                
                        /**
                         * @return record
                         */
                        @Nullable
                        String record();
                                                
                        /**
                         * @return transitive
                         */
                        @Nullable
                        String transitive();
                                                
                        /**
                         * @return yield
                         */
                        @Nullable
                        String yield();
                        """,
                problemFields.asGetterDeclarations("twitter4j.v2", null, true).codeFragment());

    }
}
