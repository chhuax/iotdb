/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.db.storageengine.load.splitter;

import org.apache.iotdb.commons.exception.IllegalPathException;

import org.apache.tsfile.exception.write.PageException;
import org.apache.tsfile.utils.ReadWriteIOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface TsFileData {
  long getDataSize();

  TsFileDataType getType();

  void serialize(DataOutputStream stream) throws IOException;

  static TsFileData deserialize(InputStream stream)
      throws IOException, PageException, IllegalPathException {
    final TsFileDataType type = TsFileDataType.values()[ReadWriteIOUtils.readInt(stream)];
    switch (type) {
      case CHUNK:
        return ChunkData.deserialize(stream);
      case DELETION:
        return DeletionData.deserialize(stream);
      default:
        throw new UnsupportedOperationException("Unknown TsFileData type: " + type);
    }
  }
}
