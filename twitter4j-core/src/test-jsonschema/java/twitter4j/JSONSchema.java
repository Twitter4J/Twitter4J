package twitter4j;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface JSONSchema {
    @NotNull
    String typeName();

    @NotNull
    String jsonPointer();

    String description();

    default @NotNull JavaFile asJavaImpl(@NotNull String packageName, @NotNull String interfacePackageName) {
        Code getterImplementation = asGetterImplementations(interfacePackageName, null);
        Code code = asFieldDeclarations(interfacePackageName, null);
        String imports = composeImports(null, getterImplementation);
        return new JavaFile(upperCamelCased(typeName()) + ".java", """
                package %1$s;
                %2$s
                /**
                 * %3$s
                 */
                class %4$sImpl implements %9$s.%4$s {
                %6$s
                    
                    %4$sImpl(JSONObject json) {
                %7$s
                    }
                    
                %8$s
                }
                """.formatted(packageName, imports, description(), upperCamelCased(typeName()), lowerCamelCased(typeName()),
                indent(code.codeFragment, 4), indent(asConstructorAssignments(interfacePackageName), 8),
                indent(getterImplementation.codeFragment, 4), interfacePackageName));
    }

    default @NotNull JavaFile asInterface(@NotNull String interfacePackageName) {
        Code getterDeclaration = asGetterDeclarations(interfacePackageName, null);
        String imports = composeImports(interfacePackageName, getterDeclaration);

        return new JavaFile(upperCamelCased(typeName()) + ".java",
                """
                        package %1$s;
                        %2$s
                        /**
                         * %3$s
                         */
                        public interface %4$s {
                        %5$s
                        }
                        """.formatted(interfacePackageName, imports, description(), upperCamelCased(typeName()), indent(getterDeclaration.codeFragment, 4)));
    }

    /**
     * @param codeFragment      code fragment
     * @param typesToBeImported types to be imported
     */
    record Code(String codeFragment, Set<String> typesToBeImported) {
        static Code of(String codeFragment, String... toBeImported) {
            return new Code(codeFragment, Arrays.stream(toBeImported).collect(Collectors.toSet()));
        }

        static Code of(String codeFragment, Set<String> typesToBeImported) {
            return new Code(codeFragment, typesToBeImported);
        }

        static Code of(List<Code> codes) {
            return new Code(codes.stream().map(e -> e.codeFragment).collect(Collectors.joining("\n")),
                    codes.stream().flatMap(e -> e.typesToBeImported.stream()).collect(Collectors.toSet()));
        }

        static Code with(String codeFragment, Code... code) {
            return new Code(codeFragment, Arrays.stream(code)
                    .map(e -> e.typesToBeImported).flatMap(Collection::stream).collect(Collectors.toSet()));
        }

        public static Code empty = new Code("", new HashSet<>());
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
        String annotationImports = Arrays.stream(code).flatMap(e -> e.typesToBeImported.stream())
                .filter(e -> ignorePackage == null || !e.startsWith(ignorePackage))
                .filter(e -> e.startsWith("org.jetbrains.annotations"))
                .sorted()
                .map(e -> "import " + e + ";").collect(Collectors.joining("\n"));
        String classImports = Arrays.stream(code).flatMap(e -> e.typesToBeImported.stream())
                .filter(e -> ignorePackage == null || !e.startsWith(ignorePackage))
                .filter(e -> !e.startsWith("org.jetbrains.annotations"))
                .sorted()
                .map(e -> "import " + e + ";").collect(Collectors.joining("\n"));
        if (annotationImports.isEmpty() && classImports.isEmpty()) {
            return "";
        }
        String imports = "";
        if (!annotationImports.isEmpty()) {
            imports = "\n" + annotationImports + "\n";
        }
        if (!classImports.isEmpty()) {
            imports += "\n" + classImports + "\n";
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
                lowerCamelCased(resolvedTypeName)), imports);
    }

    default Code nullableAnnotation(boolean notNull) {

        return notNull ? (isPrimitive() ? Code.empty : Code.of("@NotNull\n", "org.jetbrains.annotations.NotNull"))
                : Code.of("@Nullable\n", "org.jetbrains.annotations.Nullable");

    }

    default boolean isPrimitive() {
        return false;
    }

    @NotNull
    String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName);

    @NotNull
    default String asConstructorAssignmentArray(String name) {
        throw new UnsupportedOperationException("not supported");
    }

    @NotNull
    default String asConstructorAssignments(String packageName) {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull Code asFieldDeclarations(String packageName, @Nullable String overrideTypeName) {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referfencingSchema) {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema) {
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        Code code = nullableAnnotation(notNull);
        String resolvedTypeName = referencingSchema != null && !"".equals(referencingSchema.typeName())
                ? referencingSchema.typeName() : typeName();
        return Code.with("""
                        /**
                         * @return %1$s
                         */
                        %2$s%3$s get%4$s();
                        """.formatted(referencingSchema != null && !"".equals(referencingSchema.description()) ? referencingSchema.description() : description(),
                        code.codeFragment,
                        annotation.codeFragment + javaType.codeFragment,
                        upperCamelCased(resolvedTypeName))
                , annotation, javaType, code);
    }

    default @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName) {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull Code asGetterImplementation(boolean notNull, String packageName, @Nullable String overrideTypeName) {
        Code nullableAnnotation = nullableAnnotation(notNull);
        Code annotation = getAnnotation();
        Code javaType = getJavaType(notNull, packageName);
        String resolvedTypeName = overrideTypeName != null ? overrideTypeName : typeName();
        return Code.with("""
                        %1$s%2$s@Override
                        public %3$s get%4$s() {
                            return %5$s;
                        }
                        """.formatted(nullableAnnotation.codeFragment, annotation.codeFragment, javaType.codeFragment, upperCamelCased(resolvedTypeName), lowerCamelCased(resolvedTypeName))
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
        StringBuilder result = new StringBuilder();
        for (String s : typeName.split("_")) {
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
        if(!(schema instanceof RefSchema)) {
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
                     @Nullable String description) implements JSONSchema {
    static IntegerSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[format, description, maximum, type, minimum]");

        return new IntegerSchema(typeName, jsonPointer,
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
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        if (notNull) {
            return """
                    this.%1$s = json.get%2$s("%3$s");""".formatted(JSONSchema.lowerCamelCased(typeName), useInt() ? "Int" : "Long", typeName);

        } else {
            return """
                    this.%1$s = json.get%2$sValue("%3$s");""".formatted(JSONSchema.lowerCamelCased(typeName), useInt() ? "Int" : "Long", typeName);
        }
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return """
                this.%1$s = json.getIntArray("%2$s");""".formatted(lowerCamelCased, name);
    }
}

record NumberSchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable Long minimum,
                    @Nullable Long maximum,
                    @Nullable String format,
                    @Nullable String description) implements JSONSchema {
    static NumberSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[format, maximum, type, minimum]");

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
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        if (notNull) {
            return """
                    this.%1$s = json.getDouble("%2$s");""".formatted(JSONSchema.lowerCamelCased(typeName), typeName);

        } else {
            return """
                    this.%1$s = json.getDoubleValue("%2$s");""".formatted(JSONSchema.lowerCamelCased(typeName), typeName);
        }
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return """
                this.%1$s = json.getDoubleArray("%2$s");""".formatted(lowerCamelCased, name);
    }
}

record BooleanSchema(@NotNull String typeName, @NotNull String jsonPointer,
                     @Nullable String description) implements JSONSchema {
    static BooleanSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[description, type]");
        return new BooleanSchema(typeName, jsonPointer,
                object.getString("description"));
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        return Code.of(notNull ? "boolean" : "Boolean");
    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        return notNull ? """
                this.%1$s = json.getBoolean("%2$s");""".formatted(JSONSchema.lowerCamelCased(typeName), typeName) :
                """
                        this.%1$s = json.getBooleanValue("%2$s");""".formatted(JSONSchema.lowerCamelCased(typeName), typeName);
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return """
                this.%1$s = json.getBooleanArray("%2$s");""".formatted(lowerCamelCased, name);
    }
}


record StringSchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable String pattern,
                    @Nullable String format,
                    @NotNull List<String> enumList, @Nullable Integer minLength, @Nullable String description,
                    @Nullable String example) implements JSONSchema {
    static StringSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[minLength, pattern, format, description, type, enum, example]");

        List<String> enumArray = JSONSchema.toStringList(object, "enum");

        return new StringSchema(typeName, jsonPointer,
                object.getString("pattern"),
                object.getString("format"),
                enumArray,
                object.getIntValue("minLength"),
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
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String lowerCamelCased = JSONSchema.lowerCamelCased(typeName);
        return isDateTime() ?
                """
                        this.%1$s = json.getLocalDateTime("%2$s");""".formatted(lowerCamelCased, typeName) :
                """
                        this.%1$s = json.getString("%2$s");""".formatted(lowerCamelCased, typeName);
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return """
                this.%1$s = json.getStringList("%2$s");""".formatted(lowerCamelCased, name);
    }
}

record EnumSchema(@NotNull String typeName, @NotNull String jsonPointer,
                  @NotNull List<String> enumList, @Nullable String description) implements JSONSchema {
    static EnumSchema from(JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[description, type, enum]");
        List<String> enumArray = JSONSchema.toStringList(object, "enum");
        return new EnumSchema(typeName, jsonPointer, enumArray, object.optString("description", typeName)
        );
    }

    @Override
    @NotNull
    public Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema) {
        String enumStr = JSONSchema.indent(this.enumList.stream().map(e -> """
                %s("%s")""".formatted(e.toUpperCase().replaceAll("-", "_")
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
                    public final String value;
                            
                    %3$s(String value) {
                        this.value = value;
                    }
                            
                    @Override
                    public String toString() {
                        return value;
                    }
                            
                    public static %3$s of(String str) {
                        for (%3$s value : %3$s.values()) {
                            if (value.value.equals(str)) {
                                return value;
                            }
                        }
                        return null;
                    }
                }
                                
                /**
                 * @return %1$s
                 */
                %2$s%3$s get%4$s();
                """.formatted(referencingSchema != null ? referencingSchema.description() : description(), nullableAnnotation.codeFragment(), annotation.codeFragment() + javaType.codeFragment(),
                JSONSchema.upperCamelCased(resolvedTypeName), enumStr), nullableAnnotation, annotation, javaType);
    }

    @Override
    public @NotNull Code getJavaType(boolean notNull, String packageName) {
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return Code.of(upperCamelCased);
    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String jsonName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String lowerCamelCased = JSONSchema.lowerCamelCased(jsonName);
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return """
                this.%1$s = %2$s.of(json.getString("%3$s"));""".formatted(lowerCamelCased, upperCamelCased, jsonName);
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        return """
                this.%1$s = json.getStringList("%2$s");""".formatted(lowerCamelCased, name);
    }
}

record JavaFile(@NotNull String fileName, @NotNull String content) {
}

record ArraySchema(@NotNull String typeName, @NotNull String jsonPointer, @Nullable Integer minItems,
                   @Nullable Integer maxItems, boolean uniqueItems,
                   @Nullable String description, @NotNull JSONSchema items) implements JSONSchema {
    static ArraySchema from(Map<String, JSONSchema> schemaMap, JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[minItems, maxItems, uniqueItems, description, type, items]");
        return new ArraySchema(typeName, jsonPointer, object.getIntValue("minItems"),
                object.getIntValue("maxItems"),
                object.getBoolean("uniqueItems"),
                object.optString("description", typeName)
                , JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("items"), JSONSchema.upperCamelCased(typeName.length() > 1 && typeName.endsWith("s") ? typeName.substring(0, typeName.length() - 1) : "items"), jsonPointer + "/items"));
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
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        return items.asConstructorAssignmentArray(typeName);
    }

}

record ObjectSchema(@NotNull String typeName, @NotNull String jsonPointer,
                    @NotNull List<String> required,
                    @NotNull List<JSONSchema> properties,
                    @NotNull List<JSONSchema> allOf,
                    @NotNull List<JSONSchema> oneOf,
                    @Nullable String ref,
                    @Nullable JSONSchema items,
                    @Nullable JSONSchema additionalProperties,
                    @Nullable String example,
                    @Nullable String description) implements JSONSchema {
    static ObjectSchema from(Map<String, JSONSchema> schemaMap, JSONObject object, String typeName, @NotNull String jsonPointer) {
        JSONSchema.ensureOneOf(object, "[allOf, oneOf, description, additionalProperties, type, $ref, required, properties, example, discriminator]");
        List<JSONSchema> properties = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "properties");
        List<JSONSchema> allOf = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "allOf");
        List<JSONSchema> oneOf = JSONSchema.toJSONSchemaTypeList(schemaMap, object, jsonPointer, "oneOf");

        return new ObjectSchema(typeName, jsonPointer,
                JSONSchema.toStringList(object, "required"), properties, allOf, oneOf,
                object.getString("$ref"),
                object.has("items") ? JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("items"),
                        "items", jsonPointer + "/items") : null,
                object.has("additionalProperties") ?
                        JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("additionalProperties"), "additionalProperties", jsonPointer + "/additionalProperties")
                        : null,
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
        return Code.of(codes.stream().map(Code::codeFragment).collect(Collectors.joining("\n\n")) + "\n",
                codes.stream().flatMap(e -> e.typesToBeImported().stream()).collect(Collectors.toSet()));

    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(overrideTypeName != null ? overrideTypeName : this.typeName);
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return hasNoElements() ?
                """
                        this.%1$s = json.getString("%2$s");""".formatted(lowerCamelCased, this.typeName)
                :
                """
                        this.%1$s = json.has("%3$s") ? new %2$s(json.getJSONObject("%3$s")) : null;""".formatted(lowerCamelCased, upperCamelCased, overrideTypeName != null ? overrideTypeName : this.typeName);
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        String upperCamelCased = JSONSchema.upperCamelCased(name);
        return """
                this.%1$s = json.getJSONArrayAsStream("%3$s").map(%3$sImpl::new).collect(Collectors.toList());""".formatted(lowerCamelCased, upperCamelCased, name);
    }

    @Override
    public @NotNull String asConstructorAssignments(String packageName) {
        List<String> codes = properties.stream().map(e -> e.asConstructorAssignment(this.required.contains(e.typeName()), null)).collect(Collectors.toList());
        allOf.stream().map(e -> e.asConstructorAssignment(true, null)).forEach(codes::add);
        oneOf.stream().map(e -> e.asConstructorAssignment(false, null)).forEach(codes::add);
        return String.join("\n", codes);
    }

    @Override
    public @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referencingSchema) {
        List<Code> codes = properties.stream().map(e -> e.asGetterDeclaration(this.required.contains(e.typeName()), packageName, referencingSchema)).collect(Collectors.toList());
        allOf.stream().map(e -> e.asGetterDeclaration(true, packageName, referencingSchema)).forEach(codes::add);
        oneOf.stream().map(e -> e.asGetterDeclaration(false, packageName, referencingSchema)).forEach(codes::add);
        return Code.of(codes);
    }

    @Override
    public @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName) {
        List<Code> codes = properties.stream()
                .map(e -> e.asGetterImplementation(this.required.contains(e.typeName()), packageName, overrideTypeName))
                .collect(Collectors.toList());
        allOf.stream()
                .map(e -> e.asGetterImplementation(true, packageName, overrideTypeName))
                .forEach(codes::add);
        oneOf.stream()
                .map(e -> e.asGetterImplementation(false, packageName, overrideTypeName))
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

    @Nullable
    private String overrideTypeName() {
        return "".equals(typeName) ? null : typeName;
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
    public @NotNull Code asGetterDeclarations(String packageName, @Nullable JSONSchema referencingSchema) {
        return delegateTo().asGetterImplementations(packageName, overrideTypeName());
    }

    @Override
    public @NotNull Code asGetterDeclaration(boolean notNull, String packageName, @Nullable JSONSchema referencingSchema) {
        return delegateTo().asGetterDeclaration(notNull, packageName, this);
    }

    @Override
    public @NotNull Code asGetterImplementations(String packageName, @Nullable String overrideTypeName) {
        return delegateTo().asGetterImplementations(packageName, overrideTypeName());
    }

    @Override
    public @NotNull Code asGetterImplementation(boolean notNull, String packageName, @Nullable String overrideTypeName) {
        return delegateTo().asGetterImplementation(notNull, packageName, overrideTypeName());
    }

    @Override
    public @NotNull String asConstructorAssignments(String packageName) {
        return delegateTo().asConstructorAssignments(packageName);
    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        return delegateTo().asConstructorAssignment(notNull, overrideTypeName());
    }

    @Override
    public String toString() {
        return "RefSchema(" + getJavaType(true, "");
    }
}