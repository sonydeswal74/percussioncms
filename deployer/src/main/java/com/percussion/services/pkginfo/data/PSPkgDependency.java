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

package com.percussion.services.pkginfo.data;

import com.percussion.services.catalog.PSTypeEnum;
import com.percussion.services.guidmgr.data.PSGuid;
import com.percussion.utils.guid.IPSGuid;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Class representing an Package Element dependency object, used to hold
 * information dependencies between design objects in a "solution" package
 * created or installed on a server.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "PSPkgDependency")
@Table(name = "PSX_PKG_DEPENDENCY")
public class PSPkgDependency implements Serializable
{

   /**
    * 
    */
   private static final long serialVersionUID = -6026221348840514395L;

   /**
    * 
    * @return returns the id of the PSPkgDependency object
    */
   public long getId()
   {
      return pkgDependencyId;
   }

   /**
    * Get the GUID to the Package that has the dependency
    * 
    * @return the ownerPackageGuid. Never <code>null</code>.
    */
   public IPSGuid getOwnerPackageGuid()
   {
      return new PSGuid(PSTypeEnum.PACKAGE_INFO, ownerPackageGuid);
   }

   /**
    * Get the GUID to the Package to which there is a dependency
    * 
    * @return the dependentPackageGuid. May be <code>null</code>.
    */
   public IPSGuid getDependentPackageGuid()
   {
      if (dependentPackageGuid == 0)
         return null;
      return new PSGuid(PSTypeEnum.PACKAGE_INFO, dependentPackageGuid);
   }

   /**
    * 
    * @return Returns <code>true</code> if the dependency is impliedDep,
    * <code>false</code> if the dependency is user defined.
    */
   public Boolean isImpliedDep()
   {
      return impliedDep;
   }

   // ------------------------------------------------------------------------------

   /**
    * Set the id of the PSPkgDependency object
    * 
    * @param id the guid to set. Never <code>null</code>.
    */
   public void setId(long id)
   {
      pkgDependencyId = id;
   }

   /**
    * Set the GUID to the Package that has the dependency
    * 
    * @param ownerPackageGuid the ownerPackageGuid to set. Never
    * <code>null</code>.
    */
   public void setOwnerPackageGuid(IPSGuid ownerPackageGuid)
   {
      if (ownerPackageGuid == null)
         throw new IllegalArgumentException(
               "Owner Package GUID may not be null");

      this.ownerPackageGuid = ownerPackageGuid.longValue();
   }

   /**
    * Set the GUID to the Package to which there is a dependency
    * 
    * @param dependentPackageGuid the dependentPackageGuid to set. Never
    * <code>null</code>.
    */
   public void setDependentPackageGuid(IPSGuid dependentPackageGuid)
   {
      if (dependentPackageGuid == null)
         throw new IllegalArgumentException(
               "Dependent Package GUID may not be null");

      this.dependentPackageGuid = dependentPackageGuid.longValue();
   }

   /**
    * Set the type of the dependency, whether it is implied or user defined.
    * 
    * @param impliedDep The type of the dependency, set to <code>true</code>
    * if it is implied dependency, otherwise <code>false</code>.
    */
   public void setImpliedDep(Boolean impliedDep)
   {
      this.impliedDep = impliedDep;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object b)
   {
      if (!(b instanceof PSPkgDependency))
         return false;
      PSPkgDependency second = (PSPkgDependency) b;
      return new EqualsBuilder().append(pkgDependencyId,
            second.pkgDependencyId).append(ownerPackageGuid,
            second.ownerPackageGuid).append(dependentPackageGuid,
            second.dependentPackageGuid).append(impliedDep, second.impliedDep)
            .isEquals();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {

      return new HashCodeBuilder().append(pkgDependencyId).append(
            ownerPackageGuid).append(dependentPackageGuid).append(impliedDep)
            .toHashCode();
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("PSPkgDependency{");
      sb.append("pkgDependencyId=").append(pkgDependencyId);
      sb.append(", ownerPackageGuid=").append(ownerPackageGuid);
      sb.append(", dependentPackageGuid=").append(dependentPackageGuid);
      sb.append(", impliedDep=").append(impliedDep);
      sb.append('}');
      return sb.toString();
   }
// ------------------------------------------------------------------------------

   /**
    * This id of the package dependency table rows.
    */
   @Id
   @Column(name = "PKG_DEPENDENCY_ID", nullable = false)
   private long pkgDependencyId;

   /**
    * GUID of the Package who has the dependency.
    */
   @Column(name = "OWNER_PACKAGE_GUID", nullable = false)
   private long ownerPackageGuid;

   /**
    * GUID of the Package upon which there is a dependency.
    */
   @Column(name = "DEPENDENT_PACKAGE_GUID", nullable = true)
   private long dependentPackageGuid;

   /**
    * A flag to indicate whether the dependency is impliedDep by the objects of
    * the package or user defined.
    */
   @Column(name = "IMPLIED_DEP", nullable = false)
   private Boolean impliedDep;

}
