/*
 *     Percussion CMS
 *     Copyright (C) 1999-2020 Percussion Software, Inc.
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
 *      https://www.percussion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.utils;

import com.percussion.integritymanagement.data.IPSIntegrityTask;
import com.percussion.share.spring.PSSpringWebApplicationContextUtils;
import com.percussion.test.PSServletTestCase;
import com.percussion.utils.testing.IntegrationTest;
import com.percussion.utils.types.PSPair;
import org.junit.experimental.categories.Category;

import java.util.Map;

@Category(IntegrationTest.class)
public class PSDTSStatusProviderTest extends PSServletTestCase
{

    private PSDTSStatusProvider statsuProvider;

    public PSDTSStatusProvider getStatsuProvider()
    {
        return statsuProvider;
    }

    public void setStatsuProvider(PSDTSStatusProvider statsuProvider)
    {
        this.statsuProvider = statsuProvider;
    }

    @Override
    protected void setUp() throws Exception
    {
        PSSpringWebApplicationContextUtils.injectDependencies(this);
        //FB:IJU_SETUP_NO_SUPER NC 1-16-16
        super.setUp();
    }

   
    public void testGetStatusReport()
    {
        Map<String, PSPair<IPSIntegrityTask.TaskStatus, String>> status = getStatsuProvider().getDTSStatusReport();
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("dts").getFirst());
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("feeds").getFirst() );
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("perc-form-processor").getFirst());
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("perc-comments-services").getFirst());
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("perc-metadata-services").getFirst());
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("perc-membership-services").getFirst());
        assertEquals(IPSIntegrityTask.TaskStatus.SUCCESS, status.get("perc-polls-services").getFirst());
    }

}
