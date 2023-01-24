/*
 * Copyright 1999-2023 Percussion Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.percussion.webservices.transformation.converter;

import com.percussion.services.content.data.PSFieldDescription;
import com.percussion.utils.testing.IntegrationTest;
import com.percussion.webservices.content.PSFieldDescriptionDataType;
import org.junit.experimental.categories.Category;

/**
 * Tests the {@link PSFieldTypeConverter}
 */
@Category(IntegrationTest.class)
public class PSFieldTypeConverterTest extends PSConverterTestBase
{
   /**
    * Tests the conversion from a server to a client object.
    *  
    * @throws Exception If the test fails.
    */
   public void testConversion() throws Exception
   {
      // create the source object
      PSFieldDescription.PSFieldTypeEnum source = 
         PSFieldDescription.PSFieldTypeEnum.NUMBER; 
      
      PSFieldDescription.PSFieldTypeEnum target = 
         (PSFieldDescription.PSFieldTypeEnum) roundTripConversion(
            PSFieldDescription.PSFieldTypeEnum.class, 
            PSFieldDescriptionDataType.class, 
            source);
      
      // verify the the round-trip object is equal to the source object
      assertTrue(source.equals(target));
   }
}

