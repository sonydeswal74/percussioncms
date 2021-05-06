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

package com.percussion.taxonomy.service;

import org.hibernate.HibernateException;
import java.util.Collection;
import org.springframework.dao.DataAccessException;

import com.percussion.taxonomy.domain.Related_node;
import com.percussion.taxonomy.repository.Related_nodeDAO;
import com.percussion.taxonomy.repository.Related_nodeServiceInf;
import com.percussion.taxonomy.service.Related_nodeService;

public class Related_nodeService implements Related_nodeServiceInf {

    public Related_nodeDAO related_nodeDAO;

    public Collection getAllRelated_nodes() {
        try {
            return related_nodeDAO.getAllRelated_nodes();
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public Related_node getRelated_node(int id) {
        try {
            return related_nodeDAO.getRelated_node(id);
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public void removeRelated_node(Related_node related_node) {
        try {
            related_nodeDAO.removeRelated_node(related_node);
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public void saveRelated_node(Related_node related_node) {
        try {
            related_nodeDAO.saveRelated_node(related_node);
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public void setRelated_nodeDAO(Related_nodeDAO related_nodeDAO) {
        this.related_nodeDAO = related_nodeDAO;
    }
}