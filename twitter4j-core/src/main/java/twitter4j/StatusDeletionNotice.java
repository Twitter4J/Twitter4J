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

package twitter4j;


/**
 * A data class representing Status deletionNotice<br>
 * Clients are urged to honor deletionNotice requests and discard deleted statuses immediately. At times, status deletionNotice messages may arrive before the status. Even in this case, the late arriving status should be deleted from your backing store.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public interface StatusDeletionNotice extends Comparable<StatusDeletionNotice>, java.io.Serializable {
    long getStatusId();

    long getUserId();
}
