/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
package twitter4j;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.0.4
 */
public interface StatusListener {
    void onStatus(Status status);
    /**
     * Called upon deletionNotice notices. Clients are urged to honor deletionNotice requests and discard deleted statuses immediately. At times, status deletionNotice messages may arrive before the status. Even in this case, the late arriving status should be deleted from your backing store.
     * @since Twitter4J 2.1.0
     * @param statusDeletionNotice the deletionNotice notice
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#ParsingResponses">Streaming API Documentation - Parsing Responses</a>
     */
    void onDeletionNotice(StatusDeletionNotice statusDeletionNotice);

    /**
     * This notice will be sent each time a limited stream becomes unlimited.<br>
     * If this number is high and or rapidly increasing, it is an indication that your predicate is too broad, and you should consider a predicate with higher selectivity.
     * @param numberOfLimitedStatuses an enumeration of statuses that matched the track predicate but were administratively limited.
     * @since Twitter4J 2.1.0
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#TrackLimiting">Streaming API Documentation - Track Limiting</a>
     * @see <a href="http://apiwiki.twitter.com/Streaming-API-Documentation#ParsingResponses">- Parsing Responses</a>
     * @see <a href="http://groups.google.co.jp/group/twitter-development-talk/browse_thread/thread/15d0504b3dd7b939">Twitter Development Talk - Track API Limit message meaning</a>
     */
    void onTrackLimitationNotice(int numberOfLimitedStatuses);

    void onException(Exception ex);
}
