/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dnb.tools.svnfairy.batchindexer.nexusapi;

import java.net.URI;

public class ContentItem {

    private URI resourceURI;
    private String relativePath;
    private String text;
    private boolean leaf;
    private String lastModified;
    private int sizeOnDisk;

    public URI getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(URI resourceURI) {
        this.resourceURI = resourceURI;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getSizeOnDisk() {
        return sizeOnDisk;
    }

    public void setSizeOnDisk(int sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
    }

    @Override
    public String toString() {

        return "Content-Item[ resourceURI=" + resourceURI
                + ", relativePath=" + relativePath
                + ", text=" + text
                + ", leaf=" + leaf
                + ", lastModified=" + lastModified
                + ", sizeOnDisk=" + sizeOnDisk
                + "]";
    }

}
