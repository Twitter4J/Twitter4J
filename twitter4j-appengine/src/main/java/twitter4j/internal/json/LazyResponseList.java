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

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterRuntimeException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.4
 */
abstract class LazyResponseList<T> implements ResponseList<T> {
    ResponseList<T> target = null;

    LazyResponseList() {
    }

    protected ResponseList<T> getTarget() {
        if (target == null) {
            try {
                target = createActualResponseList();
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
            }
        }
        return target;
    }

    abstract protected ResponseList<T> createActualResponseList() throws TwitterException;

    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }

    public int size() {
        return getTarget().size();
    }

    public boolean isEmpty() {
        return getTarget().isEmpty();
    }

    public boolean contains(Object o) {
        return getTarget().contains(o);
    }

    public Iterator<T> iterator() {
        return getTarget().iterator();
    }

    public Object[] toArray() {
        return getTarget().toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return getTarget().toArray(ts);
    }

    public boolean add(T t) {
        return getTarget().add(t);
    }

    public boolean remove(Object o) {
        return getTarget().remove(o);
    }

    public boolean containsAll(Collection<?> objects) {
        return getTarget().containsAll(objects);
    }

    public boolean addAll(Collection<? extends T> ts) {
        return getTarget().addAll(ts);
    }

    public boolean addAll(int i, Collection<? extends T> ts) {
        return getTarget().addAll(i, ts);
    }

    public boolean removeAll(Collection<?> objects) {
        return getTarget().removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return getTarget().retainAll(objects);
    }

    public void clear() {
        getTarget().clear();
    }

    public T get(int i) {
        return getTarget().get(i);
    }

    public T set(int i, T t) {
        return getTarget().set(i, t);
    }

    public void add(int i, T t) {
        getTarget().add(i, t);
    }

    public T remove(int i) {
        return getTarget().remove(i);
    }

    public int indexOf(Object o) {
        return getTarget().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return getTarget().lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return getTarget().listIterator();
    }

    public ListIterator<T> listIterator(int i) {
        return getTarget().listIterator(i);
    }

    public List<T> subList(int i, int i1) {
        return getTarget().subList(i, i1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LazyResponseList)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyResponseList{" +
                "target=" + getTarget() +
                "}";
    }

}
