package twitter4j;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface JSONSchema {
    @NotNull
    String typeName();

    @NotNull
    String jsonPointer();

    String description();

    @Nullable
    default String overrideTypeName() {
        return "".equals(typeName()) ? null : typeName();
    }

    Set<String> keywords = Set.of("abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "_", "exports", "opens", "requires", "uses", "module", "permits", "sealed", "var", "non-sealed", "provides", "to", "with", "open", "record", "transitive", "yield");

    static String escapeKeywords(String fieldName) {
        return keywords.contains(fieldName) ? fieldName + "_" : fieldName;
    }


    // !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
    Pattern httpPattern = Pattern.compile("https?://[a-zA-Z0-9!-'*-/:-@\\[-`{-~]+");

    static String link(String javadocContent) {
        if (javadocContent == null) {
            return javadocContent;
        }
        StringBuilder builder = new StringBuilder();

        int start = 0;
        Matcher matcher = httpPattern.matcher(javadocContent);
        while (matcher.find()) {
            builder.append(javadocContent, start, matcher.start())
                    .append("<a href=\"")
                    .append(javadocContent, matcher.start(), matcher.end())
                    .append("\">")
                    .append(javadocContent, matcher.start(), matcher.end())
                    .append("</a>");
            start = matcher.end();
        }
        if (start != 0) {
            builder.append(javadocContent.substring(start));
        }
        return builder.length() == 0 ? javadocContent : builder.toString();
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);

    default @NotNull JavaFile asJavaImpl(@NotNull String packageName, @NotNull String interfacePackageName, boolean noPrefix) {
        Code getterImplementation = asGetterImplementations(interfacePackageName, null, noPrefix);
        Code code = asFieldDeclarations(interfacePackageName, null);

        Code constructorAssignments = asConstructorAssignments(interfacePackageName);
        Code generated = getGenerated();
        String imports = composeImports(null, getterImplementation, constructorAssignments, generated);
        String constructorAnnotations = constructorAssignments.methodLevelAnnotations.stream().sorted().collect(Collectors.joining("\n"));
        if (!constructorAnnotations.isEmpty()) {
            constructorAnnotations += "\n    ";
        }
        return new JavaFile(upperCamelCased(typeName()) + "Impl.java", """
                package %1$s;
                %2$s
                /**
                 * %3$s
                 */
                %4$s
                class %5$sImpl implements %6$s.%5$s {
                %7$s
                    
                    %8$s%5$sImpl(JSONObject json) {
                %9$s
                    }
                    
                %10$s
                }
                """.formatted(packageName,//1
                imports,//2
                link(description()),//3
                generated.codeFragment,//4
                upperCamelCased(typeName()), //5
                interfacePackageName, //6
                indent(code.codeFragment, 4),//7
                constructorAnnotations, // 8
                indent(constructorAssignments.codeFragment, 8),//9
                indent(getterImplementation.codeFragment, 4) //10
        ));
    }

    default Code getGenerated() {
        return Code.of("""
                @Generated(value = "%s", date = "%s", comments = "%s")""".formatted(JSONSchema.class.getName(), ZonedDateTime.now(ZoneId.of("GMT")).format(dateTimeFormatter), jsonPointer()), "javax.annotation.processing.Generated");
    }

    default @NotNull JavaFile asInterface(@NotNull String interfacePackageName, boolean noPrefix) {
        Code getterDeclaration = asGetterDeclarations(interfacePackageName, null, noPrefix);
        Code generated = getGenerated();
        String imports = composeImports(interfacePackageName, getterDeclaration, generated);
        return new JavaFile(upperCamelCased(typeName()) + ".java",
                """
                        package %1$s;
                        %2$s
                        /**
                         * %3$s
                         */
                        %4$s
                        public interface %5$s {
                        %6$s
                        }
                        """.formatted(interfacePackageName,//1
                        imports,//2
                        link(description()),//3
                        generated.codeFragment,//4
                        upperCamelCased(typeName()),//5
                        indent(getterDeclaration.codeFragment, 4)//6
                ));
    }

    default boolean hasAnyProperties() {
        return !asGetterDeclarations("twitter4j", null, false).codeFragment.equals("");
    }

    /**
     * @param codeFragment      code fragment
     * @param typesToBeImported types to be imported
     */
    record Code(String codeFragment, Set<String> typesToBeImported, Set<String> methodLevelAnnotations) {
        static Code of(String codeFragment, String... toBeImported) {
            return new Code(codeFragment, Arrays.stream(toBeImported).collect(Collectors.toSet()), new HashSet<>());
        }

        static Code of(String codeFragment, Set<String> typesToBeImported) {
            return new Code(codeFragment, typesToBeImported, new HashSet<>());
        }

        static Code of(String codeFragment, Set<String> typesToBeImported, Set<String> methodLevelAnnotations) {
            return new Code(codeFragment, typesToBeImported, methodLevelAnnotations);
        }

        static Code of(List<Code> codes) {
            return of(codes, false);
        }

        static Code of(List<Code> codes, boolean insertEmptyLine) {
            return new Code(codes.stream().map(e -> e.codeFragment).collect(Collectors.joining(insertEmptyLine ? "\n\n" : "\n")),
                    codes.stream().flatMap(e -> e.typesToBeImported.stream()).collect(Collectors.toSet())
                    , codes.stream().flatMap(e -> e.methodLevelAnnotations.stream()).collect(Collectors.toSet()));
        }

        static Code with(String codeFragment, Code... code) {
            return new Code(codeFragment, Arrays.stream(code)
                    .map(e -> e.typesToBeImported).flatMap(Collection::stream).collect(Collectors.toSet())
                    , Arrays.stream(code)
                    .map(e -> e.methodLevelAnnotations).flatMap(Collection::stream).collect(Collectors.toSet()));
        }

        public static Code empty = new Code("", new HashSet<>(), new HashSet<>());
    }

    static String indent(String tobeFormatted, int numberOfSpaces) {
        String spaces = IntStream.range(0, numberOfSpaces).mapToObj(e -> " ").collect(Collectors.joining());
        StringBuilder result = new StringBuilder();
        String[] split = tobeFormatted.split("\n");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (!s.isEmpty()) {
                result.append(spaces);
            }
            result.append(s);
            if (i != (split.length - 1)) {
                result.append('\n');
            }

        }
        return result.toString();
    }

    @NotNull
    Code getJavaType(boolean notNull, String packageName);

    static String composeImports(@Nullable String ignorePackage, @NotNull Code... code) {
        String nonstandardClasses = Arrays.stream(code).flatMap(e -> e.typesToBeImported.stream())
                .filter(e -> ignorePackage == null || !e.startsWith(ignorePackage))
                .filter(e -> !(e.startsWith("javax.") || e.startsWith("java.")))
                .sorted()
                .map(e -> "import " + e + ";").collect(Collectors.joining("\n"));
        String javaxClasses = Arrays.stream(code).flatMap(e -> e.typesToBeImported.stream())
                .filter(e -> ignorePackage == null || !e.startsWith(ignorePackage))
                .filter(e -> (e.startsWith("javax.")))
                .sorted()
                .map(e -> "import " + e + ";").collect(Collectors.joining("\n"));
        String javaClasses = Arrays.stream(code).flatMap(e -> e.typesToBeImported.stream())
                .filter(e -> ignorePackage == null || !e.startsWith(ignorePackage))
                .filter(e -> e.startsWith("java."))
                .sorted()
                .map(e -> "import " + e + ";").collect(Collectors.joining("\n"));

        String javaImports = (javaxClasses.length() != 0 && javaClasses.length() != 0) ?
                javaxClasses + "\n" + javaClasses :
                javaxClasses + javaClasses;
        String imports = (nonstandardClasses.length() != 0 && javaImports.length() != 0) ?
                nonstandardClasses + "\n\n" + javaImports :
                nonstandardClasses + javaImports;
        if (!imports.isEmpty()) {
            imports = "\n" + imports + "\n";
        }
        return imports;
    }

    default @NotNull Code asFieldDeclaration(boolean notNull, String packageName, @Nullable String overrideTypeName) {
        Code nullableAnnotation = nullableAnnotation(notNull);
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        Set<String> imports = new HashSet<>();
        imports.addAll(nullableAnnotation.typesToBeImported);
        imports.addAll(annotation.typesToBeImported);
        imports.addAll(javaType.typesToBeImported);
        String resolvedTypeName = overrideTypeName != null ? upperCamelCased(overrideTypeName) : typeName();
        return Code.of(nullableAnnotation.codeFragment + "%sprivate final %s %s;".formatted(annotation.codeFragment,
                javaType.codeFragment,
                escapeKeywords(lowerCamelCased(resolvedTypeName))), imports);
    }
    default boolean isDefaultValueSpecified(){
        return false;
    }

    default Code nullableAnnotation(boolean notNull) {

        return notNull || isDefaultValueSpecified() ? (isPrimitive() ? Code.empty : Code.of("@NotNull\n", "org.jetbrains.annotations.NotNull"))
                : Code.of("@Nullable\n", "org.jetbrains.annotations.Nullable");

    }

    default boolean isPrimitive() {
        return false;
    }

    @NotNull
    Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName);

    @NotNull
    default Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        throw new UnsupportedOperationException("not supported:" + typeName() + "/" + jsonPointer());
    }

    @NotNull
    default Code asConstructorAssignments(String packageName) {
        throw new UnsupportedOperationException("not supported:" + typeName() + "/" + jsonPointer());
    }

    default @NotNull Code asFieldDeclarations(String packageName, @Nullable String overrideTypeName) {
        throw new UnsupportedOperationException("not supported:" + typeName() + "/" + jsonPointer());
    }

    default @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        throw new UnsupportedOperationException("not supported:" + typeName() + "/" + jsonPointer());
    }

    default @NotNull Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        Code code = nullableAnnotation(notNull);
        return Code.with("""
                        /**
                         * @return %1$s
                         */
                        %2$s%3$s %4$s();
                        """.formatted(link(referencingSchema != null && !"".equals(referencingSchema.description()) ? referencingSchema.description() : description()),//1
                        code.codeFragment,//2
                        annotation.codeFragment + javaType.codeFragment,//3
                        getterMethodName(noPrefix, notNull, referencingSchema != null ? referencingSchema.overrideTypeName() : null))//4
                , annotation, javaType, code);
    }

    default @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        throw new UnsupportedOperationException("not supported:" + typeName() + "/" + jsonPointer());
    }

    Set<String> keywordsNotForMethodNames = Set.of("abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "_");

    default @NotNull String getterMethodName(boolean noPrefix, boolean notNull, @Nullable String overrideTypeName) {
        String resolvedTypeName = overrideTypeName != null ? overrideTypeName : typeName();
        // nonnull boolean property accessor starts with "is"
        String prefix = notNull && this instanceof BooleanSchema ? "is" : "get";
        String result = noPrefix ? lowerCamelCased(resolvedTypeName) :
                prefix + upperCamelCased(resolvedTypeName);

        if (keywordsNotForMethodNames.contains(result)) {
            result = result + "_";
        }
        return result;
    }

    default @NotNull Code asGetterImplementation(boolean notNull, String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        Code nullableAnnotation = nullableAnnotation(notNull);
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        String resolvedTypeName = overrideTypeName != null ? overrideTypeName : typeName();
        return Code.with("""
                        %1$s%2$s@Override
                        public %3$s %4$s() {
                            return %5$s;
                        }
                        """.formatted(nullableAnnotation.codeFragment, //1
                        annotation.codeFragment, //2
                        javaType.codeFragment, //3
                        getterMethodName(noPrefix, notNull, overrideTypeName),//4
                        escapeKeywords(lowerCamelCased(resolvedTypeName))//5
                )
                , nullableAnnotation, annotation, javaType);
    }

    default Code getAnnotation() {
        return Code.empty;
    }

    static String lowerCamelCased(@NotNull String typeName) {
        String camelCased = upperCamelCased(typeName);
        return camelCased.substring(0, 1).toLowerCase() + camelCased.substring(1);
    }

    static String upperCamelCased(@NotNull String typeName) {
        if ("_".equals(typeName) || "__".equals(typeName)) {
            return typeName;
        }
        StringBuilder result = new StringBuilder();
        for (String s : typeName.replaceAll("-", "_").split("_")) {
            result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
        }
        return result.toString();
    }

    static void ensureOneOf(@NotNull JSONObject object, String names) {
        ensureOneOf(object, names.substring(1, names.length() - 1).replaceAll(" ", "").split(","));
    }

    static void ensureOneOf(@NotNull JSONObject object, String[] names) {

        for (String key : object.keySet()) {
            boolean matches = false;
            for (String name : names) {
                if (name.equals(key)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                throw new IllegalArgumentException("unexpected value:" + key + "\n" + object.toString(1));
            }

        }
    }

    @NotNull
    static List<String> toStringList(JSONObject object, String name) {
        List<String> strList = new ArrayList<>();
        if (object.has(name)) {
            JSONArray anEnum = object.getJSONArray(name);
            for (int i = 0; i < anEnum.length(); i++) {
                strList.add(anEnum.getString(i));
            }
        }
        return strList;

    }

    HashSet<String> types = new HashSet<>();
    HashSet<String> propsInt = new HashSet<>();
    HashSet<String> propsStr = new HashSet<>();
    HashSet<String> propsObj = new HashSet<>();
    HashSet<String> propsNumber = new HashSet<>();
    HashSet<String> propsBoolean = new HashSet<>();
    HashSet<String> propsArray = new HashSet<>();
    HashSet<String> propsRef = new HashSet<>();
    HashSet<String> propsEnum = new HashSet<>();


    @NotNull
    static JSONSchema toJSONSchemaType(Map<String, JSONSchema> schemaMap, JSONObject jsonObject, @NotNull String typeName, @NotNull String jsonPointer) {
        String type = jsonObject.getString("type");
        if (type == null) {
            if (jsonObject.has("$ref")) {
                type = "ref";
            } else {
                type = "object";
            }
        }
        types.add(type);
        if ("string".equals(type) && jsonObject.has("enum") && jsonObject.getJSONArray("enum").length() != 1) {
            type = "enum";
        }


        for (String s : jsonObject.keySet()) {
            switch (type) {
                case "integer" -> propsInt.add(s);
                case "array" -> propsArray.add(s);
                case "string" -> propsStr.add(s);
                case "enum" -> propsEnum.add(s);
                case "object" -> propsObj.add(s);
                case "number" -> propsNumber.add(s);
                case "boolean" -> propsBoolean.add(s);
                case "ref" -> propsRef.add(s);
                default -> throw new IllegalStateException("unexpected type:" + type);
            }
        }

        JSONSchema schema = switch (type) {
            case "string" -> StringSchema.from(jsonObject, typeName, jsonPointer);
            case "enum" -> EnumSchema.from(jsonObject, typeName, jsonPointer);
            case "array" -> ArraySchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "integer" -> IntegerSchema.from(jsonObject, typeName, jsonPointer);
            case "number" -> NumberSchema.from(jsonObject, typeName, jsonPointer);
            case "object" -> ObjectSchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "ref" -> RefSchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "boolean" -> BooleanSchema.from(jsonObject, typeName, jsonPointer);
            default -> throw new IllegalStateException("unexpected type:" + type);
        };
        if (!(schema instanceof RefSchema)) {
            schemaMap.put(schema.jsonPointer(), schema);
        }
        return schema;
    }

    static List<JSONSchema> toJSONSchemaTypeList(Map<String, JSONSchema> schemaMap, JSONObject jsonObject, String path, String name) {
        List<JSONSchema> list = new ArrayList<>();
        if (jsonObject.has(name)) {
            try {
                JSONObject propertiesJSONObject = jsonObject.getJSONObject(name);
                for (String key : propertiesJSONObject.keySet()) {
                    list.add(toJSONSchemaType(schemaMap, propertiesJSONObject.getJSONObject(key), key, path + "/" + name + "/" + key));
                }
            } catch (JSONException jsone) {
                JSONArray propertiesJSONArray = jsonObject.getJSONArray(name);
                for (int i = 0; i < propertiesJSONArray.length(); i++) {
                    list.add(toJSONSchemaType(schemaMap, propertiesJSONArray.getJSONObject(i), "", path + "/" + name));
                }

            }
        }
        return list;
    }


    static Map<String, JSONSchema> extract(String path, @Language("JSON") String jsonAsString) {
        JSONObject jsonObject = new JSONObject(jsonAsString);
        for (String pathPart : path.replaceAll("#", "").split("/")) {
            if (!"".equals(pathPart)) {
                jsonObject = jsonObject.getJSONObject(pathPart);
            }
        }
        Map<String, JSONSchema> schemaMap = new HashMap<>();

        for (String schema : jsonObject.keySet()) {
            toJSONSchemaType(schemaMap, jsonObject.getJSONObject(schema), schema, path + schema);
        }
        System.out.println("types------------");
        System.out.println(types);
        System.out.println("propsInt------------");
        System.out.println(propsInt);
        System.out.println("propsNumber------------");
        System.out.println(propsNumber);
        System.out.println("propsStr------------");
        System.out.println(propsStr);
        System.out.println("propsBoolean------------");
        System.out.println(propsBoolean);
        System.out.println("propsObj------------");
        System.out.println(propsObj);
        System.out.println("propsArray------------");
        System.out.println(propsArray);
        System.out.println("propsRef------------");
        System.out.println(propsRef);
        System.out.println("propsEnum------------");
        System.out.println(propsEnum);
        return schemaMap;
    }

}

record IntegerSchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable Long minimum,
                     @Nullable Long maximum,
                     @Nullable String format,
                     @Nullable String description,
                     @Nullable String example) implements JSONSchema {
    static IntegerSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[format, description, example, maximum, type, minimum]");

        return new IntegerSchema(typeName, jsonPointer,
                object.getLongValue("minimum"),
                object.getLongValue("maximum"),
                object.getString("format"),
                object.getString("description"),
                object.getString("example")
        );
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }


    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return useInt() ?
                notNull ? Code.of("int") : Code.of("Integer") :
                notNull ? Code.of("long") : Code.of("Long");
    }

    private String getWrapperType() {
        return useInt() ? "Integer" : "Long";
    }

    private boolean useInt() {
        return "int32".equals(format) ||
                (
                        (minimum != null && minimum >= Integer.MIN_VALUE) &&
                                (maximum != null && maximum <= Integer.MAX_VALUE)
                );
    }

    @Override
    public Code getAnnotation() {
        if (minimum == null && maximum == null) {
            return Code.empty;
        }
        String rangeMinimum = minimum == null ? getWrapperType() + ".MIN_VALUE" : minimum.toString();
        String rangeMaximum = maximum == null ? getWrapperType() + ".MAX_VALUE" : maximum.toString();
        return Code.of("@Range(from = %s, to = %s)\n".formatted(rangeMinimum, rangeMaximum), "org.jetbrains.annotations.Range");
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String fieldName = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(typeName));
        if (notNull) {
            return Code.of("""
                    this.%1$s = json.get%2$s("%3$s");""".formatted(fieldName, useInt() ? "Int" : "Long", typeName));

        } else {
            return Code.of("""
                    this.%1$s = json.get%2$sValue("%3$s");""".formatted(fieldName, useInt() ? "Int" : "Long", typeName));
        }
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String lowerCamelCased = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(name));
        return Code.of("""
                this.%1$s = json.getIntArray("%2$s");""".formatted(lowerCamelCased, name));
    }
}

record NumberSchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable Long minimum,
                    @Nullable Long maximum,
                    @Nullable String format,
                    @Nullable String description) implements JSONSchema {
    static NumberSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[format, maximum, type, minimum, description]");

        return new NumberSchema(typeName, jsonPointer,
                object.getLongValue("minimum"),
                object.getLongValue("maximum"),
                object.getString("format"),
                object.getString("description"));
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public Code getAnnotation() {
        if (minimum == null && maximum == null) {
            return Code.empty;
        }
        String rangeMinimum = minimum == null ? "Double.MIN_VALUE" : minimum.toString();
        String rangeMaximum = maximum == null ? "Double.MAX_VALUE" : maximum.toString();
        return Code.of("@Range(from = %s, to = %s)\n".formatted(rangeMinimum, rangeMaximum), "org.jetbrains.annotations.Range");
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return Code.of(notNull ? "double" : "Double");
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String fieldName = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(typeName));
        if (notNull) {
            return Code.of("""
                    this.%1$s = json.getDouble("%2$s");""".formatted(fieldName, typeName));

        } else {
            return Code.of("""
                    this.%1$s = json.getDoubleValue("%2$s");""".formatted(fieldName, typeName));
        }
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String lowerCamelCased = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(name));
        return Code.of("""
                this.%1$s = json.getDoubleArray("%2$s");""".formatted(lowerCamelCased, name));
    }
}

record BooleanSchema(@NotNull String typeName, @NotNull String jsonPointer,
                     @Nullable Boolean defaultValue,
                     @Nullable String description, @Nullable String example) implements JSONSchema {
    static BooleanSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[description, type, default, example]");
        return new BooleanSchema(typeName, jsonPointer,
                object.getBooleanValue("default"),
                object.getString("description"),
                object.getString("example")
        );
    }

    @Override
    public boolean isDefaultValueSpecified() {
        return defaultValue != null;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return Code.of(notNull || isDefaultValueSpecified() ? "boolean" : "Boolean");
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String fieldName = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(typeName));
        String defaultValueParameter =isDefaultValueSpecified() ? ", "+ defaultValue : "";
        return Code.of(notNull || isDefaultValueSpecified() ? """
                this.%1$s = json.getBoolean("%2$s"%3$s);""".formatted(fieldName, typeName, defaultValueParameter) :
                """
                        this.%1$s = json.getBooleanValue("%2$s");""".formatted(fieldName, typeName));
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String lowerCamelCased = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(name));
        return Code.of("""
                this.%1$s = json.getBooleanArray("%2$s");""".formatted(lowerCamelCased, name));
    }
}


record StringSchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable String pattern,
                    @Nullable String format,
                    @NotNull List<String> enumList, @Nullable Integer minLength,@Nullable Integer maxLength, @Nullable String description,
                    @Nullable String example) implements JSONSchema {
    static StringSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[minLength, pattern, format, description, type, enum, example, maxLength]");

        List<String> enumArray = JSONSchema.toStringList(object, "enum");

        return new StringSchema(typeName, jsonPointer,
                object.getString("pattern"),
                object.getString("format"),
                enumArray,
                object.getIntValue("minLength"),
                object.getIntValue("maxLength"),
                object.optString("description", typeName),
                object.getString("example")
        );
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return isDateTime() ? Code.of("LocalDateTime", "java.time.LocalDateTime") : Code.of("String");
    }

    private boolean isDateTime() {
        return "date-time".equals(format);
    }


    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String fieldName = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(typeName));
        return Code.of(isDateTime() ?
                """
                        this.%1$s = json.getLocalDateTime("%2$s");""".formatted(fieldName, typeName) :
                """
                        this.%1$s = json.getString("%2$s");""".formatted(fieldName, typeName), Set.of(), notNull ? Set.of("@SuppressWarnings(\"ConstantConditions\")") : Set.of());
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String fieldName = JSONSchema.escapeKeywords(JSONSchema.lowerCamelCased(name));
        return Code.of("""
                this.%1$s = json.getStringList("%2$s");""".formatted(fieldName, name));
    }
}

record EnumSchema(@NotNull String typeName, @NotNull String jsonPointer,
                  @NotNull List<String> enumList,@Nullable String pattern, @Nullable String description, @Nullable String example) implements JSONSchema {
    static EnumSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[description, type, enum, example, pattern]");
        List<String> enumArray = JSONSchema.toStringList(object, "enum");
        return new EnumSchema(typeName, jsonPointer, enumArray,
                object.getString("pattern"),
                object.optString("description", typeName),
                object.getString("example")
        );
    }

    @Override
    @NotNull
    public Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        String enumStr = JSONSchema.indent(this.enumList.stream().map(e -> """
                /**
                 * %s
                 */
                %s("%s")""".formatted(JSONSchema.link(e), e.toUpperCase().replaceAll("-", "_")
                , e)).collect(Collectors.joining(",\n")), 4) + ";";
        Code nullableAnnotation = nullableAnnotation(notNull);
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        String resolvedTypeName = referencingSchema != null ? referencingSchema.typeName() : typeName;
        return Code.with("""
                        /**
                         * %1$s
                         */
                        enum %3$s {
                        %5$s
                            /**
                             * value
                             */
                            public final String value;
                                    
                            %3$s(String value) {
                                this.value = value;
                            }
                                    
                            @Override
                            public String toString() {
                                return value;
                            }
                            /**
                             * Returns the enum constant of the specified enum class with the specified name.
                             *
                             * @param name the name of the constant to return
                             * @return the enum constant of the specified enum class with the specified name,
                             * or null if the enum constant is not found.
                             */
                            public static %3$s of(String name) {
                                for (%3$s value : %3$s.values()) {
                                    if (value.value.equals(name)) {
                                        return value;
                                    }
                                }
                                return null;
                            }
                        }
                                                    
                        /**
                         * @return %1$s
                         */
                        %2$s%3$s %4$s();
                        """.formatted(JSONSchema.link(referencingSchema != null ? referencingSchema.description() : description()),//1
                        nullableAnnotation.codeFragment(), //2
                        annotation.codeFragment() + javaType.codeFragment(), //3
                        getterMethodName(noPrefix, notNull, resolvedTypeName),//4
                        enumStr),//5
                nullableAnnotation, annotation, javaType);
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return Code.of(upperCamelCased);
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String jsonName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String lowerCamelCased = JSONSchema.lowerCamelCased(jsonName);
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return Code.of("""
                this.%1$s = %2$s.of(json.getString("%3$s"));""".formatted(lowerCamelCased, upperCamelCased, jsonName));
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return Code.of("""
                this.%1$s = json.getStringList("%2$s");""".formatted(lowerCamelCased, name));
    }
}

record JavaFile(@NotNull String fileName, @NotNull String content) {
}

record ArraySchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable Integer minItems,
                   @Nullable Integer maxItems, boolean uniqueItems,
                   @Nullable String description, @NotNull JSONSchema items,@Nullable String example) implements JSONSchema {
    static ArraySchema from(Map<String, JSONSchema> schemaMap, JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[minItems, maxItems, uniqueItems, description, type, items, example]");
        return new ArraySchema(typeName, jsonPointer, object.getIntValue("minItems"),
                object.getIntValue("maxItems"),
                object.getBoolean("uniqueItems"),
                object.optString("description", typeName)
                , JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("items"),
                JSONSchema.upperCamelCased(typeName.length() > 1 && typeName.endsWith("s") ? typeName.substring(0, typeName.length() - 1) : "items"),
                jsonPointer + "/items"),
                object.getString("example"));
    }

    @Override
    public boolean isPrimitive() {
        return items.isPrimitive();
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        JSONSchema resolvedSchema = items instanceof RefSchema refSchema ?
                refSchema.delegateTo() : items;
        if (resolvedSchema.isPrimitive()) {
            Code javaType = resolvedSchema.getJavaType(true, packageName);
            return Code.of(javaType.codeFragment() + "[]", javaType.typesToBeImported());
        }
        if (resolvedSchema instanceof StringSchema || resolvedSchema instanceof ObjectSchema) {
            Code javaType = resolvedSchema.getJavaType(notNull, packageName);
            Set<String> imports = new HashSet<>(javaType.typesToBeImported());
            imports.add("java.util.List");
            return Code.of("List<%s>".formatted(javaType.codeFragment()), imports);
        }
        return Code.of("List<%s>".formatted(typeName), "java.util.List");
    }

    @Override
    public Code nullableAnnotation(boolean notNull) {
        return Code.empty;
    }


    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        return items.asConstructorAssignmentArray(packageName, typeName);
    }

}

record ObjectSchema(@NotNull String typeName, @NotNull String jsonPointer,
                    @NotNull List<String> required,
                    @NotNull List<JSONSchema> properties,
                    @NotNull List<JSONSchema> allOf,
                    @NotNull List<JSONSchema> oneOf,
                    @NotNull List<JSONSchema> anyOf,
                    @Nullable String ref,
                    @Nullable JSONSchema items,
                    @Nullable JSONSchema additionalProperties,
                    @Nullable Boolean additionalPropertiesProhibited,
                    @Nullable String example,
                    @Nullable String description) implements JSONSchema {
    static ObjectSchema from(Map<String, JSONSchema> schemaMap, JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[allOf, anyOf, oneOf, description, additionalProperties, type, $ref, required, properties, example, discriminator]");
        List<JSONSchema> properties = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "properties");
        List<JSONSchema> allOf = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "allOf");
        List<JSONSchema> oneOf = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "oneOf");
        List<JSONSchema> anyOf = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "anyOf");
        Boolean additionalPropertiesBoolean = null;
        JSONSchema additionalPropertiesObj = null;
        if (object.has("additionalProperties")) {
            Object additionalPropertiesValue = object.get("additionalProperties");
            if (additionalPropertiesValue instanceof Boolean) {
                additionalPropertiesBoolean = (boolean) additionalPropertiesValue;
            }else{
                additionalPropertiesObj = JSONSchema.toJSONSchemaType(schemaMap,
                        object.getJSONObject("additionalProperties"),
                        "additionalProperties", jsonPointer + "/additionalProperties");
            }
        }
        return new ObjectSchema(typeName, jsonPointer,
                JSONSchema.toStringList(object, "required"), properties, allOf, oneOf, anyOf,
                object.getString("$ref"),
                object.has("items") ? JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("items"),
                        "items", jsonPointer + "/items") : null,
                additionalPropertiesObj,
                additionalPropertiesBoolean,
                object.getString("example"),
                object.optString("description", typeName)
        );
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        if (hasNoElements()) {
            return Code.of("String");
        }
        return Code.of(JSONSchema.upperCamelCased(typeName), packageName + "." + JSONSchema.upperCamelCased(typeName));
    }

    boolean hasNoElements() {
        return (properties.size() + allOf.size() + oneOf.size()) == 0;
    }

    @Override
    public @NotNull Code asFieldDeclarations(String packageName, @Nullable String overrideTypeName) {
        List<Code> codes = new ArrayList<>();
        properties.stream().map(e -> e.asFieldDeclaration(required.contains(e.typeName()), packageName, overrideTypeName)).forEach(codes::add);
        allOf.stream().map(e -> e.asFieldDeclaration(true, packageName, overrideTypeName)).forEach(codes::add);
        oneOf.stream().map(e -> e.asFieldDeclaration(false, packageName, overrideTypeName)).forEach(codes::add);
        anyOf.stream().map(e -> e.asFieldDeclaration(false, packageName, overrideTypeName)).forEach(codes::add);
        return Code.of(codes, true);
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        if (typeName.equals("")) {
            return this.asConstructorAssignments(packageName);
        }else {
            String lowerCamelCased = JSONSchema.lowerCamelCased(overrideTypeName != null ? overrideTypeName : this.typeName);
            String upperCamelCased = JSONSchema.upperCamelCased(typeName);
            return Code.of(hasNoElements() ?
                        """
                                this.%1$s = json.getString("%2$s");""".formatted(lowerCamelCased, this.typeName)
                        :
                        """
                                this.%1$s = json.has("%3$s") ? new %2$s(json.getJSONObject("%3$s")) : null;""".formatted(lowerCamelCased, upperCamelCased + "Impl", overrideTypeName != null ? overrideTypeName : this.typeName)
                    , new HashSet<>(), notNull ? Set.of("@SuppressWarnings(\"ConstantConditions\")") : new HashSet<>());
        }
    }

    @Override
    public @NotNull Code asFieldDeclaration(boolean notNull, String packageName, @Nullable String overrideTypeName) {
        if (typeName.equals("")) {
            return this.asFieldDeclarations(packageName, overrideTypeName);
        }else {
            return JSONSchema.super.asFieldDeclaration(notNull, packageName, overrideTypeName);
        }
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        String impl = JSONSchema.upperCamelCased(typeName);
        return Code.of("""
                this.%1$s = json.getJSONArrayAsStream("%2$s").map(%3$sImpl::new).collect(Collectors.toList());""".formatted(lowerCamelCased, name, impl), "java.util.stream.Collectors");
    }

    @Override
    public @NotNull Code asConstructorAssignments(String packageName) {
        List<Code> codes = properties.stream().map(e -> e.asConstructorAssignment(packageName, this.required.contains(e.typeName()), null)).collect(Collectors.toList());
        allOf.stream().map(e -> e.asConstructorAssignment(packageName, true, null)).forEach(codes::add);
        oneOf.stream().map(e -> e.asConstructorAssignment(packageName, false, null)).forEach(codes::add);
        anyOf.stream().map(e -> e.asConstructorAssignment(packageName, false, null)).forEach(codes::add);
        return Code.of(codes);
    }

    @Override
    public @NotNull Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        if (typeName.equals("")) {
            return this.asGetterDeclarations(packageName,referencingSchema, noPrefix);
        }else {
            return JSONSchema.super.asGetterDeclaration(notNull, packageName, referencingSchema, noPrefix);
        }
    }

    @Override
    public @NotNull Code asGetterImplementation(boolean notNull, String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        if (typeName.equals("")) {
            return this.asGetterImplementations(packageName, overrideTypeName,
                    noPrefix);
        }else {
            return JSONSchema.super.asGetterImplementation(notNull, packageName, overrideTypeName, noPrefix);
        }

    }

    @Override
    public @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        List<Code> codes = properties.stream().map(e -> e.asGetterDeclaration(this.required.contains(e.typeName()), packageName, referencingSchema, noPrefix)).collect(Collectors.toList());
        allOf.stream().map(e -> e.asGetterDeclaration(true, packageName, referencingSchema, noPrefix)).forEach(codes::add);
        oneOf.stream().map(e -> e.asGetterDeclaration(false, packageName, referencingSchema, noPrefix)).forEach(codes::add);
        anyOf.stream().map(e -> e.asGetterDeclaration(false, packageName, referencingSchema, noPrefix)).forEach(codes::add);
        return Code.of(codes);
    }

    @Override
    public @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        List<Code> codes = properties.stream()
                .map(e -> e.asGetterImplementation(this.required.contains(e.typeName()), packageName, overrideTypeName, noPrefix))
                .collect(Collectors.toList());
        allOf.stream()
                .map(e -> e.asGetterImplementation(true, packageName, overrideTypeName, noPrefix))
                .forEach(codes::add);
        oneOf.stream()
                .map(e -> e.asGetterImplementation(false, packageName, overrideTypeName, noPrefix))
                .forEach(codes::add);
        anyOf.stream()
                .map(e -> e.asGetterImplementation(false, packageName, overrideTypeName, noPrefix))
                .forEach(codes::add);
        return Code.of(codes);
    }

    @Override
    public String toString() {
        return "ObjectSchema(" + getJavaType(true, "");
    }

}

record RefSchema(@NotNull Map<String, JSONSchema> map, String typeName, String ref,
                 String jsonPointer) implements JSONSchema {

    static RefSchema from(@NotNull Map<String, JSONSchema> map, JSONObject object, String typeName, String jsonPointer) {
        return new RefSchema(map, typeName, object.getString("$ref"), jsonPointer);
    }

    JSONSchema delegateTo() {
        Optional<JSONSchema> first = map.values().stream()
                .filter(e -> (e.jsonPointer().equals(ref)
                        || (e instanceof RefSchema && ((RefSchema) e).ref.equals(ref)))).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        throw new NoSuchElementException(ref + " not found");
    }

    @Override
    public @NotNull Code asConstructorAssignmentArray(@NotNull String packageName, String name) {
        return delegateTo().asConstructorAssignmentArray(packageName, name);
    }

    @Override
    public String description() {
        String delegateToDescription = delegateTo().description();
        boolean delegateToDoesNotHaveSpecificDescription = delegateToDescription.equals(delegateTo().typeName());
        if (delegateToDoesNotHaveSpecificDescription) {
            return typeName;
        } else {
            if ("".equals(typeName)) {
                return delegateToDescription;
            } else {
                return typeName + ": " + delegateToDescription;
            }
        }
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return delegateTo().getJavaType(notNull, packageName);
    }

    @Override
    public @NotNull Code asFieldDeclaration(boolean notNull, String packageName, @Nullable String overrideTypeName) {
        return delegateTo().asFieldDeclaration(notNull, packageName, overrideTypeName());
    }

    @Override
    public @NotNull Code asFieldDeclarations(String packageName, @Nullable String overrideTypeName) {
        return delegateTo().asFieldDeclarations(packageName, overrideTypeName());
    }

    @Override
    public @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        return delegateTo().asGetterDeclarations(packageName, referencingSchema, noPrefix);
    }

    @Override
    public @NotNull Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema, boolean noPrefix) {
        return delegateTo().asGetterDeclaration(notNull, packageName, this, noPrefix);
    }

    @Override
    public @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        return delegateTo().asGetterImplementations(packageName, overrideTypeName(), noPrefix);
    }

    @Override
    public @NotNull Code asGetterImplementation(boolean notNull, String packageName, @Nullable String overrideTypeName, boolean noPrefix) {
        return delegateTo().asGetterImplementation(notNull, packageName, overrideTypeName(), noPrefix);
    }

    @Override
    public @NotNull Code asConstructorAssignments(String packageName) {
        return delegateTo().asConstructorAssignments(packageName);
    }

    @Override
    public @NotNull Code asConstructorAssignment(@NotNull String packageName, boolean notNull, @Nullable String overrideTypeName) {
        return delegateTo().asConstructorAssignment(packageName, notNull, overrideTypeName());
    }

    @Override
    public String toString() {
        return "RefSchema(" + getJavaType(true, "");
    }
}
