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

import java.util.Date;

/**
 * A data class representing sent/received direct message.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
final class LazyDirectMessage implements twitter4j.DirectMessage {
    private twitter4j.internal.http.HttpResponse res;
    private zzzz_T4J_INTERNAL_Factory factory;
    private DirectMessage target = null;

    LazyDirectMessage(twitter4j.internal.http.HttpResponse res, zzzz_T4J_INTERNAL_Factory factory) {
        this.res = res;
        this.factory = factory;
    }

    private DirectMessage getTarget() {
        if (target == null) {
            try {
                target = factory.createDirectMessage(res);
            } catch (TwitterException e) {
                throw new RuntimeException(e);
            }
        }
        return target;
    }


    public long getId() {
        return getTarget().getId();
    }


    public String getText() {
        return getTarget().getText();
    }


    public long getSenderId() {
        return getTarget().getSenderId();
    }


    public long getRecipientId() {
        return getTarget().getRecipientId();
    }


    /**
     * @return created_at
     * @since Twitter4J 1.1.0
     */
    public Date getCreatedAt() {
        return getTarget().getCreatedAt();
    }


    public String getSenderScreenName() {
        return getTarget().getSenderScreenName();
    }


    public String getRecipientScreenName() {
        return getTarget().getRecipientScreenName();
    }



    public User getSender() {
        return getTarget().getSender();
    }



    public User getRecipient() {
        return getTarget().getRecipient();
    }


    public RateLimitStatus getRateLimitStatus() {
        return getTarget().getRateLimitStatus();
    }

    public int getAccessLevel() {
        return getTarget().getAccessLevel();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectMessage)) return false;
        return getTarget().equals(o);
    }

    @Override
    public int hashCode() {
        return getTarget().hashCode();
    }

    @Override
    public String toString() {
        return "LazyDirectMessage{" +
                "target=" + getTarget() +
                "}";
    }
}
