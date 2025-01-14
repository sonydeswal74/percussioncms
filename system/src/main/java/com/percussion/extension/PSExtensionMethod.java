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
package com.percussion.extension;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents an extension method. 
 */
public class PSExtensionMethod implements Serializable
{
   /**
    * Compiler generated serial version ID used for serialization.
    */
   private static final long serialVersionUID = 1383116678428785411L;

   /**
    * The method name, never <code>null</code> or empty after construction.
    */
   private String m_name = null;
   
   /**
    * The method description, never <code>null</code> after construction, may
    * be empty.
    */
   private String m_description = null;
   
   /**
    * The method parameters, never <code>null</code>, may be empty. The order
    * of the parameters is important for the way a method is called.
    */
   private List<PSExtensionMethodParam> m_parameters = 
      new ArrayList<>();
   
   /**
    * The method return type, never <code>null</code> or empty after 
    * construction.
    */
   private String m_returnType = null;
   
   /**
    * Convenience constructor that calls {@link #PSExtensionMethod(String, 
    * String, String) PSExtensionMethod(name, returnType, null)}.
    */
   public PSExtensionMethod(String name, String returnType)
   {
      this(name, returnType, null);
   }
   
   /**
    * Construct a new extension method for the supplied parameters.
    * 
    * @param name the method name, not <code>null</code> or empty.
    * @param returnType the method return type, not <code>null</code> or empty.
    * @param description the method description, may be <code>null</code>
    *    or empty.
    */
   public PSExtensionMethod(String name, String returnType, String description)
   {
      // parameter contracts are checked in setters
      setName(name);
      setReturnType(returnType);
      setDescription(description);
   }
   
   /**
    * Construct a new extension method from its XML representation.
    * 
    * @param source the XML element from which to construct this method,
    *    not <code>null</code>.
    * @throws PSExtensionException for any error deserializing the supplied 
    *    source.
    */
   public PSExtensionMethod(Element source) throws PSExtensionException
   {
      // parameter contract is checked in fromXML
      fromXML(source);
   }
   
   /**
    * Set the parameter name.
    * 
    * @param name the new name, not <code>null</code> or empty.
    */
   public void setName(String name)
   {
      if (StringUtils.isBlank(name))
         throw new IllegalArgumentException("name cannot be null or empty");
      
      m_name = name;
   }
   
   /**
    * Get the parameter name.
    * 
    * @return the parameter name, never <code>null</code> or empty.
    */
   public String getName()
   {
      return m_name;
   }
   
   /**
    * Set the parameter description.
    * 
    * @param description the new description, may be <code>null</code> or empty.
    */
   public void setDescription(String description)
   {
      if (description == null)
         description = "";
      
      m_description = description;
   }
   
   /**
    * Get the parameter description.
    * 
    * @return the parameter description, never <code>null</code>, may be empty.
    */
   public String getDescription()
   {
      return m_description;
   }
   
   /**
    * Get all method parameters.
    * 
    * @return the method parameters, never <code>null</code>, may be empty.
    */
   public Iterator<PSExtensionMethodParam> getParameters()
   {
      return m_parameters.iterator();
   }
   
   /**
    * Add a new method parameter. The caller must make sure that the parameters
    * are added in execution order.
    * 
    * @param parameter the new method parameter, not <code>null</code>. If
    *    a parameter with the same name already exists it will be replaced
    *    with the one supplied, otherwise the supplied parameter is appended.
    */
   public void addParameter(PSExtensionMethodParam parameter)
   {
      if (parameter == null)
         throw new IllegalArgumentException("parameter cannot be null");
      
      for (int i=0; i<m_parameters.size(); i++)
      {
         PSExtensionMethodParam param = m_parameters.get(i);
         if (param.getName().equals(parameter.getName()))
         {
            // replace existing parameter
            m_parameters.set(i, parameter);
            return;
         }
      }
      
      // append new parameter
      m_parameters.add(parameter);
   }
   
   /**
    * Remove the identified parameter from this method.
    * 
    * @param name the name or the parameter to be removed, not 
    *    <code>null</code> or empty. Does nothing if the identified parameter
    *    does not exist.
    */
   public void removeParameter(String name)
   {
      if (StringUtils.isBlank(name))
         throw new IllegalArgumentException("name cannot be null or empty");
      
      int index = -1;
      for (int i=0; i<m_parameters.size(); i++)
      {
         PSExtensionMethodParam param = m_parameters.get(i);
         if (param.getName().equals(name))
         {
            index = i;
            break;
         }
      }
      
      if (index != -1)
         m_parameters.remove(index);
   }
   
   /**
    * Set the method return type.
    * 
    * @param returnType the new return type, not <code>null</code> or empty.
    */
   public void setReturnType(String returnType)
   {
      if (StringUtils.isBlank(returnType))
         throw new IllegalArgumentException("returnType cannot be null or empty");
      
      m_returnType = returnType;
   }
   
   /**
    * Get the method return type.
    * 
    * @return the method return type, never <code>null</code> or empty.
    */
   public String getReturnType()
   {
      return m_returnType;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PSExtensionMethod)) return false;
      PSExtensionMethod that = (PSExtensionMethod) o;
      return Objects.equals(m_name, that.m_name) && Objects.equals(m_description, that.m_description) && Objects.equals(m_parameters, that.m_parameters) && Objects.equals(m_returnType, that.m_returnType);
   }

   @Override
   public int hashCode() {
      return Objects.hash(m_name, m_description, m_parameters, m_returnType);
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("PSExtensionMethod{");
      sb.append("m_name='").append(m_name).append('\'');
      sb.append(", m_description='").append(m_description).append('\'');
      sb.append(", m_parameters=").append(m_parameters);
      sb.append(", m_returnType='").append(m_returnType).append('\'');
      sb.append('}');
      return sb.toString();
   }

   /**
    * Constructs this extension method parameter from its xml representation.
    * 
    * @param source the xml element from which to construct this object, 
    *    nor <code>null</code>.
    * @throws PSExtensionException for any error deserializing the supplied xml.
    */
   public void fromXML(Element source) throws PSExtensionException
   {
      if (source == null)
         throw new IllegalArgumentException("source cannot be null");

      if (!source.getTagName().equals(XML_NAME))
         throw new PSExtensionException(
            IPSExtensionErrors.INVALID_XML_ELEMENT, 
            new Object[] { source.getTagName(), XML_NAME });
      
      String test = source.getAttribute(NAME_ATTR);
      if (StringUtils.isBlank(test))
         throw new PSExtensionException(
            IPSExtensionErrors.MISSING_REQUIRED_ATTRIBUTE, NAME_ATTR);
      setName(test);
      
      test = source.getAttribute(RETURNTYPE_ATTR);
      if (StringUtils.isBlank(test))
         throw new PSExtensionException(
            IPSExtensionErrors.MISSING_REQUIRED_ATTRIBUTE, RETURNTYPE_ATTR);
      setReturnType(test);

      setDescription(source.getAttribute(DESCRIPTION_ATTR));
      
      NodeList params = source.getElementsByTagName(
         PSExtensionMethodParam.XML_NAME);
      for (int i=0; i<params.getLength(); i++)
         addParameter(new PSExtensionMethodParam((Element) params.item(i)));
   }

   /**
    * Returns the xml representation for this extension method parameter.
    * 
    * @return the xml representation of this object, never <code>null</code>.
    */
   public Element toXML(Document doc)
   {
      Element element = doc.createElement(XML_NAME);

      element.setAttribute(NAME_ATTR, getName());
      element.setAttribute(RETURNTYPE_ATTR, getReturnType());
      if (!StringUtils.isBlank(getDescription()))
         element.setAttribute(DESCRIPTION_ATTR, getDescription());
      
      Element parameters = doc.createElement(PARAMETERS_ELEM);
      element.appendChild(parameters);
      Iterator<PSExtensionMethodParam> params = getParameters();
      while (params.hasNext())
         parameters.appendChild(params.next().toXML(doc));
      
      return element;
   }
   
   /**
    * The name used for the xml representation of this object.
    */
   public static final String XML_NAME = "PSExtensionMethod";

   // Constants used for xml representation.
   private static final String NAME_ATTR = "name";
   private static final String RETURNTYPE_ATTR = "returntype";
   private static final String DESCRIPTION_ATTR = "description";
   private static final String PARAMETERS_ELEM = "Parameters";
}

