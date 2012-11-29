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

import twitter4j.*;
import javax.annotation.Generated;

import java.util.Date;

/**
 * A data class representing sent/received direct message.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
@Generated(
        value = "generate-lazy-objects.sh",
        comments = "This is Tool Generated Code. DO NOT EDIT",
        date = "2012-11-29"
)
final class LazyDirectMessage implements twitter4j.DirectMessage {
    private twitter4j.internal.http.HttpResponse res;
    private z_T4JInternalFactory factory;
    private DirectMessage target = null;

    LazyDirectMessage(twitter4j.internal.http.HttpResponse res, z_T4JInternalFactory factory) {
        this.res = res;
        this.factory = factory;
    }

    private DirectMessage getTarget() {
        if (target == null) {
            try {
                target = factory.createDirectMessage(res);
            } catch (TwitterException e) {
                throw new TwitterRuntimeException(e);
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
	public UserMentionEntity[] getUserMentionEntities() {
		return getTarget().getUserMentionEntities();
	}

	@Override
	public URLEntity[] getURLEntities() {
		return getTarget().getURLEntities();
	}

	@Override
	public HashtagEntity[] getHashtagEntities() {
		return getTarget().getHashtagEntities();
	}

	@Override
	public MediaEntity[] getMediaEntities() {
		return getTarget().getMediaEntities();
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
