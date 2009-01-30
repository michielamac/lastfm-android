// Copyright 2008 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package fm.last.api;

import java.io.Serializable;

/**
 * @author jennings
 *         Date: Oct 20, 2008
 */
public class Friends implements Serializable {
  private static final long serialVersionUID = 50693901684109497L;
  private User[] friends;

  public Friends(User[] friends) {
    this.friends = friends;
  }
  
  public User[] getFriends() {
    return friends;
  }

}
