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
package com.percussion.services.ui.data;

import com.percussion.services.utils.xml.PSXmlSerializationHelper;
import com.percussion.utils.guid.IPSGuid;

import java.io.IOException;
import java.io.Serializable;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.xml.sax.SAXException;

/**
 * Persist an association between a hierarchy node and a hierarchy node 
 * property.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, 
      region = "PSHierarchyNodeProperty")
@IdClass(PSHierarchyNodePropertyPK.class)
@Table(name = "PSX_WB_HIERARCHY_NODE_PROP")
public class PSHierarchyNodeProperty implements Serializable
{
   /**
    * Compiler generated serial version ID used for serialization.
    */
   private static final long serialVersionUID = -982738490837214578L;

   @Id
   @Column(name = "NODE_ID")
   private long nodeId;

   @Id
   @Column(name = "NAME")
   private String name;

   @Version
   @Column(name = "VERSION")
   private Integer version;

   @Basic
   @Column(name = "VALUE")
   private String value;

   /**
    * Default constructor.
    */
   public PSHierarchyNodeProperty()
   {
   }
   
   /**
    * Construct a new hierarchy node property for the supplied name and
    * value.
    * 
    * @param propName the name of the property, not <code>null</code> or empty.
    * @param propValue the value of the property, may be <code>null</code> or
    *    empty.
    */
   public PSHierarchyNodeProperty(String propName, String propValue, 
      IPSGuid parent) 
   {
      if (StringUtils.isBlank(propName))
         throw new IllegalArgumentException("name must not be null/blank");
      
      if (parent == null)
         throw new IllegalArgumentException("parent cannot be null");

      nodeId = parent.longValue();
      setName(propName);
      setValue(propValue);
   }
   
   public long getNodeId()
   {
      return nodeId;
   }
   
   public void setNodeId(long nodeId)
   {
      this.nodeId = nodeId;
   }
   
   /**
    * Get the object version.
    * 
    * @return the object version, <code>null</code> if not initialized yet.
    */
   public Integer getVersion()
   {
      return version;
   }
   
   /**
    * Set the object version. The version can only be set once in the life 
    * cycle of this object. 
    * 
    * @param version the version of the object, must be >= 0.
    */
   public void setVersion(Integer version)
   {
      if (this.version != null)
         throw new IllegalStateException(
            "version can only be initialized once");
      
      if (version < 0)
         throw new IllegalArgumentException("version must be >= 0");
      
      this.version = version;
   }
   
   /**
    * Get the property name.
    * 
    * @return the property name, may be <code>null</code>, never empty.
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * Set a new property name.
    * 
    * @param name the new property name, not <code>null</code> or empty.
    */
   public void setName(String name)
   {
      if (StringUtils.isBlank(name))
         throw new IllegalArgumentException("name cannot be null or empty");
      
      this.name = name;
   }
   
   /**
    * Get the property value.
    * 
    * @return the property value, may be <code>null</code> or empty.
    */
   public String getValue()
   {
      return value;
   }
   
   /**
    * Set a new property value.
    * 
    * @param value the new property value, may be <code>null</code> or empty.
    */
   public void setValue(String value)
   {
      this.value = value;
   }

  

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + (int) (nodeId ^ (nodeId >>> 32));
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      PSHierarchyNodeProperty other = (PSHierarchyNodeProperty) obj;
      if (name == null)
      {
         if (other.name != null)
            return false;
      }
      else if (!name.equals(other.name))
         return false;
      if (nodeId != other.nodeId)
         return false;
      return true;
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("PSHierarchyNodeProperty{");
      sb.append("nodeId=").append(nodeId);
      sb.append(", name='").append(name).append('\'');
      sb.append(", version=").append(version);
      sb.append(", value='").append(value).append('\'');
      sb.append('}');
      return sb.toString();
   }

   /* (non-Javadoc)
    * @see IPSCatalogItem#fromXML(String)
    */
   public void fromXML(String xmlsource) throws IOException, SAXException
   {
      PSXmlSerializationHelper.readFromXML(xmlsource, this);
   }

   /* (non-Javadoc)
    * @see IPSCatalogItem#toXML()
    */
   public String toXML() throws IOException, SAXException
   {
      return PSXmlSerializationHelper.writeToXml(this);
   }
}

