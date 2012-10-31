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

// generated with generate-lazy-objects.sh
package twitter4j.internal.json;

import twitter4j.*;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
final class LazySimilarPlaces extends LazyResponseList<Place> implements SimilarPlaces {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;

    LazySimilarPlaces(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        super();
        this.res = res;
        this.factory = factory;
    }

    protected ResponseList<Place> createActualResponseList() throws TwitterException {
        return factory.createSimilarPlaces(res);
    }

    /**
     * Returns the token needed to be able to create a new place  with {@link twitter4j.api.PlacesGeoResources#createPlace(String, String, String, GeoLocation, String)}.
     *
     * @return token the token needed to be able to create a new place with {@link twitter4j.api.PlacesGeoResources#createPlace(String, String, String, GeoLocation, String)}
     */
    public String getToken() {
        return ((SimilarPlaces) getTarget()).getToken();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimilarPlaces)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazySimilarPlaces{" +
                "target=" + getTarget() +
                "}";
    }
}
