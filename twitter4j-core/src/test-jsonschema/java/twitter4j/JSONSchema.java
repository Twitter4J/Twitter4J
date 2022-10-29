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

    default @NotNull String asJavaImpl(@NotNull String packageName, @NotNull String interfacePackageName) {


        String getterImplementation = asGetterImplementations();
        String imports = composeImports(getterImplementation);
        return """
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
                indent(asFieldDeclarations(), 4), indent(asConstructorAssignments(), 8),
                indent(getterImplementation, 4), interfacePackageName);
    }

    default @NotNull String asInterface(@NotNull String interfacePackageName) {
        String getterDeclaration = asGetterDeclarations();
        String imports = composeImports(getterDeclaration);

        return """
                package %1$s;
                %2$s
                /**
                 * %3$s
                 */
                public interface %4$s {
                %5$s
                }
                """.formatted(interfacePackageName, imports, description(), upperCamelCased(typeName()), indent(getterDeclaration, 4));
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
    String getJavaType(boolean notNull);

    static String composeImports(@NotNull String code) {
        String imports = "";
        if (code.contains("@NotNull")) {
            imports += "import org.jetbrains.annotations.NotNull;\n";
        }
        if (code.contains("@Nullable")) {
            imports += "import org.jetbrains.annotations.Nullable;\n";
        }
        if (code.contains("@Range")) {
            imports += "import org.jetbrains.annotations.Range;\n";
        }

        boolean localDateTimeContains = code.contains("LocalDateTime");
        boolean listContains = code.contains("List<");
        if (localDateTimeContains || listContains) {
            if (!imports.isEmpty()) {
                imports += "\n";
            }
            if (localDateTimeContains) {
                imports += "import java.time.LocalDateTime;\n";
            }
            if (listContains) {
                imports += "import java.util.List;\n";
            }
        }

        if (!imports.isEmpty()) {
            imports = "\n" + imports;
        }
        return imports;

    }

    default @NotNull String asFieldDeclaration(boolean notNull) {
        return nullableAnnotation(notNull) + "%sprivate final %s %s;".formatted(getAnnotation(), getJavaType(notNull), lowerCamelCased(typeName()));
    }

    default String nullableAnnotation(boolean notNull) {
//        return isPrimitive() ? "" : notNull ? "@NotNull\n" : "@Nullable\n";
        return notNull ? (isPrimitive() ? "" : "@NotNull\n") : "@Nullable\n";

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
    default String asConstructorAssignments() {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull String asFieldDeclarations() {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull String asGetterDeclarations() {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull String asGetterDeclaration(boolean notNull) {
        return """
                /**
                 * @return %1$s
                 */
                %2$s%3$s get%4$s();
                """.formatted(description(), nullableAnnotation(notNull), getAnnotation() + getJavaType(notNull), upperCamelCased(typeName()));
    }

    default @NotNull String asGetterImplementations() {
        throw new UnsupportedOperationException("not supported");
    }

    default @NotNull String asGetterImplementation(boolean notNull) {
        return """
                %1$s%2$s@Override
                public %3$s get%4$s() {
                    return %5$s;
                }
                """.formatted(nullableAnnotation(notNull), getAnnotation(), getJavaType(notNull), upperCamelCased(typeName()), lowerCamelCased(typeName()));
    }

    default String getAnnotation() {
        return "";
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


        for (String s : jsonObject.keySet()) {
            switch (type) {
                case "integer" -> propsInt.add(s);
                case "array" -> propsArray.add(s);
                case "string" -> propsStr.add(s);
                case "object" -> propsObj.add(s);
                case "number" -> propsNumber.add(s);
                case "boolean" -> propsBoolean.add(s);
                case "ref" -> propsRef.add(s);
                default -> throw new IllegalStateException("unexpected type:" + type);
            }
        }

        JSONSchema schema = switch (type) {
            case "string" -> StringSchema.from(jsonObject, typeName, jsonPointer);
            case "array" -> ArraySchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "integer" -> IntegerSchema.from(jsonObject, typeName, jsonPointer);
            case "number" -> NumberSchema.from(jsonObject, typeName, jsonPointer);
            case "object" -> ObjectSchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "ref" -> RefSchema.from(schemaMap, jsonObject, typeName, jsonPointer);
            case "boolean" -> BooleanSchema.from(jsonObject, typeName, jsonPointer);
            default -> throw new IllegalStateException("unexpected type:" + type);
        };
        schemaMap.put(typeName, schema);
        return schema;
    }

    static List<JSONSchema> toJSONSchemaTypeList(Map<String, JSONSchema> schemaMap, JSONObject jsonObject, String path, String name) {
        List<JSONSchema> list = new ArrayList<>();
        if (jsonObject.has(name)) {
            try {
                JSONObject propertiesJSONObject = jsonObject.getJSONObject(name);
                for (String key : propertiesJSONObject.keySet()) {
                    list.add(toJSONSchemaType(schemaMap, propertiesJSONObject.getJSONObject(key), key, path + "/" + name));
                }
            } catch (JSONException jsone) {
                JSONArray propertiesJSONArray = jsonObject.getJSONArray(name);
                for (int i = 0; i < propertiesJSONArray.length(); i++) {
                    list.add(toJSONSchemaType(schemaMap, propertiesJSONArray.getJSONObject(i), "", path + "/" + ""));
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
        System.out.println("propsNone------------");
        System.out.println(propsRef);
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
    public @NotNull String getJavaType(boolean notNull) {
        return useInt() ?
                notNull ? "int" : "Integer" :
                notNull ? "long" : "Long";
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
    public String getAnnotation() {
        if (minimum == null && maximum == null) {
            return "";
        }
        String rangeMinimum = minimum == null ? getWrapperType() + ".MIN_VALUE" : minimum.toString();
        String rangeMaximum = maximum == null ? getWrapperType() + ".MAX_VALUE" : maximum.toString();
        return "@Range(from = %s, to = %s)\n".formatted(rangeMinimum, rangeMaximum);
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
    public String getAnnotation() {
        if (minimum == null && maximum == null) {
            return "";
        }
        String rangeMinimum = minimum == null ? "Double.MIN_VALUE" : minimum.toString();
        String rangeMaximum = maximum == null ? "Double.MAX_VALUE" : maximum.toString();
        return "@Range(from = %s, to = %s)\n".formatted(rangeMinimum, rangeMaximum);
    }

    @Override
    public @NotNull String getJavaType(boolean notNull) {
        return notNull ? "double" : "Double";
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
    public @NotNull String getJavaType(boolean notNull) {
        return notNull ? "boolean" : "Boolean";
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
    public @NotNull String getJavaType(boolean notNull) {
        return isDateTime() ? "LocalDateTime" : "String";
    }

    private boolean isDateTime() {
        return "date-time".equals(format);
    }


    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String lowerCamelCased = JSONSchema.lowerCamelCased(typeName);
        String getterMethod = isDateTime() ? "getLocalDateTime" : "getString";
        return """
                this.%1$s = json.%2$s("%3$s");""".formatted(lowerCamelCased, getterMethod, typeName);
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
        return new ArraySchema(typeName, jsonPointer,object.getIntValue("minItems"),
                        object.getIntValue("maxItems"),
                 object.getBoolean("uniqueItems"),
                object.getString("description")
                , JSONSchema.toJSONSchemaType(schemaMap, object.getJSONObject("items"), "items", jsonPointer + "/items"));
    }

    @Override
    public boolean isPrimitive() {
        return items.isPrimitive();
    }

    @Override
    public @NotNull String getJavaType(boolean notNull) {
        if (items.isPrimitive()) {
            return items.getJavaType(true) + "[]";
        }
        if (items instanceof StringSchema) {
            return "List<%s>".formatted(items.getJavaType(notNull));
        }
        return "List<%s>".formatted(typeName);
    }

    @Override
    public String nullableAnnotation(boolean notNull) {
        return "";
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
                object.getString("description")
        );
    }


    @Override
    public String description() {
        return typeName;
    }

    @Override
    public @NotNull String getJavaType(boolean notNull) {
        return typeName;
    }

    @Override
    public @NotNull String asFieldDeclarations() {
        return properties.stream().map(e -> e.asFieldDeclaration(required.contains(e.typeName()))).collect(Collectors.joining("\n\n")) + "\n";
    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        String typeName = overrideTypeName != null ? overrideTypeName : this.typeName;
        String lowerCamelCased = JSONSchema.lowerCamelCased(typeName);
        String upperCamelCased = JSONSchema.upperCamelCased(typeName);
        return notNull ? """
                this.%1$s = new %2$s(json.getJSONObject("%3$s"));""".formatted(lowerCamelCased, upperCamelCased, typeName)
                : """
                this.%1$s = json.has("%3$s") ? new %2$s(json.getJSONObject("%3$s")) : null;""".formatted(lowerCamelCased, upperCamelCased, typeName);
    }

    @Override
    public @NotNull String asConstructorAssignmentArray(String name) {
        String lowerCamelCased = JSONSchema.lowerCamelCased(name);
        String upperCamelCased = JSONSchema.upperCamelCased(name);
        return """
                this.%1$s = json.getJSONArrayAsStream("%3$s").map(%3$sImpl::new).collect(Collectors.toList());""".formatted(lowerCamelCased, upperCamelCased, name);
    }

    @Override
    public @NotNull String asConstructorAssignments() {
        return properties.stream().map(e -> e.asConstructorAssignment(this.required.contains(e.typeName()), null)).collect(Collectors.joining("\n"));
    }

    @Override
    public @NotNull String asGetterDeclarations() {
        return properties.stream().map(e -> e.asGetterDeclaration(this.required.contains(e.typeName()))).collect(Collectors.joining("\n"));
    }

    @Override
    public @NotNull String asGetterImplementations() {
        return properties.stream().map(e -> e.asGetterImplementation(this.required.contains(e.typeName()))).collect(Collectors.joining("\n"));
    }

}

record RefSchema(@NotNull Map<String, JSONSchema> map, String typeName, String ref,
                 String jsonPointer) implements JSONSchema {

    static RefSchema from(@NotNull Map<String, JSONSchema> map, JSONObject object, String typeName, String jsonPointer) {
        return new RefSchema(map, typeName, object.getString("$ref"), jsonPointer);
    }

    private JSONSchema delegateTo() {
        return map.values().stream().filter(e -> e.jsonPointer().equals(ref)).findFirst().get();
    }

    @Override
    public String description() {
        return delegateTo().description();
    }

    @Override
    public @NotNull String getJavaType(boolean notNull) {
        return delegateTo().getJavaType(notNull);
    }

    @Override
    public @NotNull String asConstructorAssignment(boolean notNull, @Nullable String overrideTypeName) {
        return delegateTo().asConstructorAssignment(notNull, typeName);
    }
}
