/*
 *     Percussion CMS
 *     Copyright (C) 1999-2021 Percussion Software, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     Mailing Address:
 *
 *      Percussion Software, Inc.
 *      PO Box 767
 *      Burlington, MA 01803, USA
 *      +01-781-438-9900
 *      support@percussion.com
 *      https://www.percusssion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.utils.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathUtilsTests {


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private String rxdeploydir;

    @Before
    public void setup() throws IOException {

        rxdeploydir = System.getProperty("rxdeploydir");
        System.setProperty("rxdeploydir", temporaryFolder.getRoot().getAbsolutePath());
    }

    @After
    public void teardown(){
        if(rxdeploydir != null)
            System.setProperty("rxdeploydir",rxdeploydir);
    }

    public PathUtilsTests(){}

    //TODO: Finish adding various test cases.
    @Test
    @Ignore
    public void testAutodetect() throws IOException {
        System.setProperty("rxdeploydir","");
        System.setProperty("user.dir", System.getProperty("user.home"));
        PathUtils.clearRxDir();

        Path p = Paths.get(
                System.getProperty("user.home"), PathUtils.USER_FOLDER_CHECK_ITEM);
        if(!Files.exists(p))
            Files.createDirectory(p);

        assertEquals(String.format("%s%s%s", System.getProperty("user.home"),
                File.separator, ".perc_config"), PathUtils.getRxDir(null).getAbsolutePath());


        File dtsBase = temporaryFolder.newFolder("Deployment","Server");
        File rxconfig = temporaryFolder.newFolder("rxconfig");
        PathUtils.clearRxDir();

        assertEquals(temporaryFolder.getRoot().getAbsolutePath(),
                PathUtils.getRxDir(rxconfig.getAbsolutePath()).getAbsolutePath());


        System.setProperty("rxdeploydir","");
        System.setProperty("user.dir", dtsBase.getAbsolutePath());
        PathUtils.clearRxDir();

        assertEquals(temporaryFolder.getRoot().getAbsolutePath(), PathUtils.getRxDir(dtsBase.getAbsolutePath()).getAbsolutePath());

        File jettyBase = temporaryFolder.newFolder("jetty","base");
        System.setProperty("user.dir", jettyBase.getAbsolutePath());
        PathUtils.clearRxDir();

        assertEquals(temporaryFolder.getRoot().getAbsolutePath(), PathUtils.getRxDir(jettyBase.getAbsolutePath()).getAbsolutePath());



    }


    public void testNoObjectStore() throws IOException {


    }

    public void testObjectStore(){

    }

    public void testNoObjectStoreNoRxConfig(){

    }

}