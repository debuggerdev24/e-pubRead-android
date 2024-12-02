/*
 * Copyright 2023 Readium Foundation. All rights reserved.
 * Use of this source code is governed by the BSD-style license
 * available in the top-level LICENSE file of the project.
 */

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.readium.r2.shared.util.zip.compress.archivers.zip;

/**
 *  A predicate to test if a #ZipArchiveEntry matches a criteria.
 *  Some day this can extend java.util.function.Predicate
 *
 *  @since 1.10
 */
public interface ZipArchiveEntryPredicate {
    /**
     * Indicate if the given entry should be included in the operation
     * @param zipArchiveEntry the entry to test
     * @return true if the entry should be included
     */
    boolean test(ZipArchiveEntry zipArchiveEntry);
}
