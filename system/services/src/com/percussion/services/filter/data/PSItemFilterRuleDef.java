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
package com.percussion.services.filter.data;

import com.percussion.error.PSNotFoundException;
import com.percussion.extension.IPSExtensionManager;
import com.percussion.extension.PSExtensionException;
import com.percussion.extension.PSExtensionRef;
import com.percussion.server.PSServer;
import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.filter.IPSItemFilter;
import com.percussion.services.filter.IPSItemFilterRule;
import com.percussion.services.filter.IPSItemFilterRuleDef;
import com.percussion.services.filter.PSFilterException;
import com.percussion.services.guidmgr.PSGuidHelper;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.services.utils.xml.PSXmlSerializationHelper;
import com.percussion.utils.guid.IPSGuid;
import com.percussion.utils.xml.IPSXmlSerialization;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This data object represents a single rule instantiation for an item filter.
 * Rules are applied in rule priority order - the order of the actual defs is
 * not relevant (at least at this point).
 * 
 * @author dougrand
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "PSItemFilterRuleDef")
@Table(name = "PSX_ITEM_FILTER_RULE")
public class PSItemFilterRuleDef implements IPSItemFilterRuleDef,
   Serializable
{
   /**
    * Serial id identifies versions of serialized data
    */
   private static final long serialVersionUID = 1L;
   
   static
   {
      // Register types with XML serializer for read creation of objects
      PSXmlSerializationHelper.addType("parameters", PSItemFilterRuleParam.class);
   }

   /**
    * Primary key
    */
   @Id
   private long filter_rule_id;

   /**
    * Hibernate version column
    */
   @SuppressWarnings("unused")
   @Version
   @Column(name = "VERSION", nullable = false)
   private Integer version;

   /**
    * Name of the rule referenced from the extensions manager, never
    * <code>null</code> or empty after construction
    */
   @Basic
   private String name;

   /**
    * The rule loaded from the extensions manager is cached in this transient
    * member.
    */
   @Transient
   private transient IPSItemFilterRule m_rule = null;

   /**
    * The rule loaded from the extensions manager is cached in this transient
    * member.
    */
   @Transient
   private transient boolean isClientSide = false;

   /**
    * The rule belongs to a specific item filter, this is the pointer to the
    * containing filter for the given rule. 
    */
   @ManyToOne(targetEntity = PSItemFilter.class)
   @JoinColumn(name = "FILTER_ID", nullable = false)
   private PSItemFilter filter;

   /**
    * A rule can reference parameters that control how the rule will  be 
    * invoked. The parameters can be overridden when the rule is invoked.
    */
   @OneToMany(targetEntity = PSItemFilterRuleParam.class, cascade =
   {CascadeType.ALL}, fetch = FetchType.EAGER,orphanRemoval = true)
   @JoinColumn(name = "FILTER_RULE_ID")
   @MapKey(name = "name")
   @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, 
         region = "PSItemFilterRuleDef_Params")
   @Fetch(FetchMode. SUBSELECT)
   private Map<String, PSItemFilterRuleParam> params = new HashMap<>();

   /**
    * Default ctor
    */
   public PSItemFilterRuleDef()
   {
      filter_rule_id = PSGuidHelper.generateNext(
            PSTypeEnum.ITEM_FILTER_RULE_DEF).longValue();
   }
   public PSItemFilterRuleDef(boolean forClient)
   {
      if(!forClient) {
         filter_rule_id = PSGuidHelper.generateNext(
                 PSTypeEnum.ITEM_FILTER_RULE_DEF).longValue();
      }else{
         isClientSide = true;
      }
   }

   
   @IPSXmlSerialization(suppress = true)
   public IPSItemFilterRule getRule() throws PSFilterException
   {
      if (m_rule == null)
      {
         m_rule = lookupRule();
      }
      return m_rule;
   }

   /**
    * Set the hibernate version. Only used for deserialization and other such
    * cases that require manipulation of the version. 
    * @param v the version, could be <code>null</code>
    */
   public void setVersion(Integer v)
   {
      this.version = v;
   }
   
   /**
    * Get the version. The annotation suppresses the inclusion of this property
    * when the object is serialized.
    * @return the version, never <code>null</code> for an object backed by the
    * database.
    */
   @IPSXmlSerialization(suppress=true)
   public Integer getVersion()
   {
      return this.version;
   }
   
   /**
    * Get the name of the rule which is actually an extension name
    * @return the name of the extension, never <code>null</code> or empty
    * @throws PSFilterException
    */
   public String getRuleName() throws PSFilterException
   {
      return name;
   }
 
   /**
    * Lookup the rule from the name
    * 
    * @return the rule, never <code>null</code>
    * @throws PSFilterException if the rule is not found
    */
   @SuppressWarnings("unchecked")
   private IPSItemFilterRule lookupRule() throws PSFilterException
   {
      if (name.equals(TEST_RULE_NAME))
         return null;

      IPSExtensionManager emgr = PSServer.getExtensionManager(null);
      try
      {
         PSExtensionRef filterruleref = new PSExtensionRef(name);
         return (IPSItemFilterRule) emgr.prepareExtension(filterruleref, null);
      }
      catch (PSExtensionException e)
      {
         throw new RuntimeException("Problems with the extensions manager", e);
      }
      catch (PSNotFoundException e)
      {
         throw new RuntimeException("Problem instantiating assembler " + name,
               e);
      }
   }

   public String getParam(String parameterName)
   {
      if (StringUtils.isBlank(parameterName))
      {
         throw new IllegalArgumentException(
               "parameterName may not be null or empty");
      }
      PSItemFilterRuleParam value = params.get(parameterName);
      if (value != null)
      {
         return value.getValue();
      }
      else
      {
         return null;
      }
   }

   /**
    * Get the guid representation of this item filter rule def.
    * @return the guid, never <code>null</code>
    */
   @IPSXmlSerialization(suppress = true)
   public IPSGuid getGUID()
   {
      return new PSGuid(PSTypeEnum.ITEM_FILTER_RULE_DEF, filter_rule_id);
   }

   /**
    * Set the guid representation of the rule def. 
    * @param newguid the new guid, never <code>null</code>
    */
   public void setGUID(IPSGuid newguid) 
   {
      if (newguid == null)
      {
         throw new IllegalArgumentException("newguid may not be null");
      }
      filter_rule_id = newguid.longValue();
   }

   /**
    * Set the name of the rule
    * @param rulename the new rule name, never <code>null</code> or empty
    * @throws PSFilterException
    */
   public void setRuleName(String rulename) throws PSFilterException
   {
      setRule(rulename);
   }
   
   /**
    * Set the rule name
    * @param rulename the new rule name, never <code>null</code> or empty
    */
   public void setRule(String rulename)
   {
      if (StringUtils.isBlank(rulename))
      {
         throw new IllegalArgumentException("rulename may not be null or empty");
      }
      name = rulename;
   }

   /**
    * Set the parameters for the given rule
    * @param params the parameter, if <code>null</code> then the current
    *   parameters will be cleared.
    */
   public void setParams(Map<String, String> params)
   {
      if (params == null)
      {
         if (this.params != null)
         {
            this.params.clear();
         }
      }
      else
      {
         for (Map.Entry<String, String> entry : params.entrySet())
         {
            setParam(entry.getKey(), entry.getValue());
         }
      }
   }

   /**
    * Betwixt method to add a parameter
    * @param parameterName the parameter name, never <code>null</code> or empty
    * @param value the parameter value, never <code>null</code> or empty
    */
   public void addParam(String parameterName, String value)
   {
      setParam(parameterName, value);
   }
   
   public void setParam(String parameterName, String value)
   {
      if (StringUtils.isBlank(parameterName))
      {
         throw new IllegalArgumentException(
               "parameterName may not be null or empty");
      }
      if (StringUtils.isBlank(value))
      {
         throw new IllegalArgumentException("value may not be null or empty");
      }
      PSItemFilterRuleParam param = this.params.get(parameterName);
      if (param == null)
      {
         param = new PSItemFilterRuleParam(isClientSide);
         param.setRuleDef(this);
         param.setName(parameterName);
         param.setValue(value);
         this.params.put(parameterName, param);
      }
      else
      {
         param.setValue(value);
      }
   }

   public void removeParam(String parameterName)
   {
      PSItemFilterRuleParam param = this.params.get(parameterName);
      if (param != null)
      {
         param.setRuleDef(null);
         this.params.remove(parameterName);
      }
   }

   public IPSItemFilter getFilter()
   {
      return filter;
   }

   public void setFilter(IPSItemFilter f)
   {
      filter = (PSItemFilter) f;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PSItemFilterRuleDef)) return false;
      PSItemFilterRuleDef that = (PSItemFilterRuleDef) o;
      return Objects.equals(name, that.name) &&
              Objects.equals(filter, that.filter);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, filter);
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("PSItemFilterRuleDef{");
      sb.append("filter_rule_id=").append(filter_rule_id);
      sb.append(", version=").append(version);
      sb.append(", name='").append(name).append('\'');
      sb.append(", m_rule=").append(m_rule);
      sb.append(", isClientSide=").append(isClientSide);
      sb.append(", filter=").append(filter);
      sb.append(", params=").append(params);
      sb.append('}');
      return sb.toString();
   }

   public int compareTo(Object o)
   {
      PSItemFilterRuleDef b = (PSItemFilterRuleDef) o;
      try
      {
         return b.getRule().getPriority() - getRule().getPriority();
      }
      catch (PSFilterException e)
      {
         return 0; // Can't tell
      }
   }

   public Map<String, String> getParams()
   {
      Map<String, String> rval = new HashMap<>();
      for (Map.Entry<String, PSItemFilterRuleParam> e : this.params.entrySet())
      {
         rval.put(e.getKey(), e.getValue().getValue());
      }
      return Collections.unmodifiableMap(rval);
   }
   
   /**
    * Use this rule name for testing. It makes sure that no lookup is made 
    * through the extension manager.
    */
   public static final String TEST_RULE_NAME = "***TESTRULE***";
}
