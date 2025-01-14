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

package com.percussion.legacy.security.deprecated;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The PSDESTest class tests running the major methods of
 * the PSDESKEY, PSDESEncryptor, and PSDESDecryptor objects.
 */
@Deprecated
public class PSDESTest extends TestCase
{
   /**
    * Construct this test by name
    * 
    * @param name The name of the test.
    */
   public PSDESTest(String name)
   {
      super(name);
   }

   /**
    * Tests encrypting and decrypting.
    * 
    * @throws Exception if there are any errors or failures
    */
   public void testAll() throws Exception
   {
      byte[] keyBytes = {(byte)0x40, (byte)0x40, (byte)0x20, (byte)0x10,
          (byte)0x08, (byte)0x04, (byte)0x02, (byte)0x01};
      System.out.println("The 8 key bytes are:");
      for (int i = 0; i < 8; i++)
      {
         System.out.print(keyBytes[i] + " ");
      }
      System.out.println("\r\n\n");

      byte[] maskArray = {(byte)0x7F, (byte)0x40, (byte)0x20, (byte)0x10,
                  (byte)0x08, (byte)0x04, (byte)0x02, (byte)0x01};
      System.out.println("The 8 mask bytes are:");
      for (int i = 0; i < 8; i++)
      {
         System.out.print(maskArray[i] + " ");
      }
      System.out.println("\r\n\n");

      int[] bitArray = new int[(keyBytes.length)*(maskArray.length)];

      int element = 0;
      for (int j = 0; j < keyBytes.length; j++)
      {
         for (int k = 0; k < maskArray.length; k++)
         {
            if (k == 0)
            {
               if ((keyBytes[j] | maskArray[k]) == maskArray[k])
                  bitArray[element] = 0;
               else
                  bitArray[element] = 1;
               element++;
            }
            else
            {
               if ((keyBytes[j] & maskArray[k]) == 0)
                  bitArray[element] = 0;
               else
                  bitArray[element] = 1;
               element++;
            }
         }
      }

      System.out.println("The 64 key bits are:");
      for (int i = 0; i < bitArray.length; i ++)
      {
         System.out.print(bitArray[i] + " ");
         if (((i+1)%8) == 0)
            System.out.print("\r\n");
      }
      System.out.print("\r\n");

      byte[] encOut;
      byte[] encodedKey = new byte[7];
      int[]  encodedKeyBits = new int[56];
      String inputMessage = "Percussion Software Welcomes You!!!";
      String outputMessage = "";


      PSDESKey desKey = new PSDESKey(keyBytes);

      encodedKey = desKey.getEncodedKey();
      System.out.println("The encoded Key has 7 bytes:");
      for (int i = 0; i < encodedKey.length; i++)
         System.out.print(encodedKey[i] + " ");

      System.out.println("\r\n\n");

      encodedKeyBits = desKey.getEncodedKeyBitArray();
      System.out.println("The encoded Key has 56 bits:");
      for (int i = 0; i < encodedKeyBits.length; i++)
      {
         System.out.print(encodedKeyBits[i]);
         if (((i+1)%7) == 0)
            System.out.print("\r\n");
      }

      // Encryptor
      System.out.println("\r\n");
      System.out.println("Here are the encryptor's data:");
      PSDESEncryptor encryptor = new PSDESEncryptor(desKey);

      // inputMessage = null;
      encOut = encryptor.encrypt(inputMessage);
      System.out.println("InputMessage = " + inputMessage);
      System.out.println("Encryption output size = " + encOut.length);
      //System.out.println("Encryption output = ");
      //System.out.write(encOut);
      System.out.println();

      // Decryptor
      System.out.println("\r\n");
      System.out.println("Here are the decryptor's data:");
      PSDESDecryptor decryptor = new PSDESDecryptor(desKey);
      outputMessage = decryptor.decrypt(encOut);
      System.out.println("Decryption output size = " + outputMessage.length());
      System.out.println("OutputMessage = " + outputMessage);
      System.out.println();      

      assertEquals("Input and output messages not equal", inputMessage,
         outputMessage);
   }
   
   /** collect all tests into a TestSuite and return it */
   public static Test suite()
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new PSDESTest("testAll"));
      return suite;
   }
}
