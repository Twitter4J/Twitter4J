SOURCE=../../../../twitter4j-core/src/main/java/twitter4j/$1.java
TARGET=../../main/java/twitter4j/internal/json/Lazy$1.java
NOW=`date +'%Y-%m-%d'`
cp $SOURCE $TARGET

sed -i "" "s/A data interface/A data class/g" $TARGET
sed -i "" 's/package twitter4j;/\package twitter4j.internal.json;\
\
import twitter4j.*;\
import javax.annotation.Generated;/g' $TARGET
sed -i "" "s/public interface $1/final class Lazy$1/g" $TARGET
sed -i "" 's/final class/@Generated(\
        value = "generate-lazy-objects.sh",\
        comments = "This is Tool Generated Code. DO NOT EDIT",\
        date = "NOW"\
)\
final class/g' $TARGET

sed -i "" "s/extends .*{$/implements twitter4j.$1 {/g" $TARGET
sed -i "" "s/NOW/$NOW/g" $TARGET

sed -i "" 's/{$/{\
    private twitter4j.internal.http.HttpResponse res;\
    private z_T4JInternalFactory factory;\
    private XXX target = null;\
\
    LazyXXX(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {\
        this.res = res;\
        this.factory = factory;\
    }\
\
    private XXX getTarget() {\
        if (target == null) {\
            try {\
                target = factory.createXXX(res);\
            } catch (TwitterException e) {\
                throw new TwitterRuntimeException(e);\
            }\
        }\
        return target;\
    }\
/g' $TARGET

sed -i "" 's/    \(.*\) \(get[^;]*\);/    public \1 \2 {\
        return getTarget().\2;\
    }\
/g' $TARGET

sed -i "" 's/\([^ ]*\ \)\(is[^;]*\);/public \1\2 {\
        return getTarget().\2;\
    }\
/g' $TARGET

sed -i "" 's/^}/RATELIMITSTATUS\COMPARETO\
    @Override\
    public boolean equals(Object o) {\
        if (this == o) return true;\
        if (!(o instanceof XXX)) return false;\
        return getTarget().equals(o);\
    }\
\
    @Override\
    public int hashCode() {\
        return getTarget().hashCode();\
    }\
\
    @Override\
    public String toString() {\
        return "LazyXXX{" +\
                "target=" + getTarget() +\
                "}";\
    }\
}/g' $TARGET

sed -i "" 's/    boolean hasNext();/    public boolean hasNext() {\
        return getTarget().hasNext();\
    }\
/g' $TARGET

sed -i "" 's/    boolean hasPrevious();/    public boolean hasPrevious() {\
        return getTarget().hasPrevious();\
    }\
/g' $TARGET

if test 1 -eq `grep Comparable $SOURCE |wc -l`; then
 sed -i "" 's/COMPARETO/    public int compareTo(XXX target) {\
        return getTarget().compareTo(target);\
    }\
/g' $TARGET
else 
 sed -i "" 's/COMPARETO//g' $TARGET
fi

# generate getRateLimitStatus() and getAccessLevel() if the class is not LazyRateLimitStatus|LazyQueryResult
if test 0 -eq `grep -E "(LazyRateLimitStatus|LazyQueryResult)" $TARGET |wc -l`; then
 sed -i "" 's/RATELIMITSTATUS/    public RateLimitStatus getRateLimitStatus() {\
        return getTarget().getRateLimitStatus();\
    }\
\
    public int getAccessLevel() {\
        return getTarget().getAccessLevel();\
    }\
\
/g' $TARGET
else 
 sed -i "" 's/RATELIMITSTATUS//g' $TARGET
fi

sed -i "" "s/XXX/$1/g" $TARGET
