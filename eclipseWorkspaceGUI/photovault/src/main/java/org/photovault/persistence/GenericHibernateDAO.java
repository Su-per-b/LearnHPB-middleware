/*
  Copyright (c) 2008 Harri Kaimio
  
  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/

package org.photovault.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 *
 * @author harri
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
        implements GenericDAO<T, ID> {
    
    private Class<T> persistentClass;
    
    private Session session;
    
    public GenericHibernateDAO() {
        Object o = ( (ParameterizedType) getClass().getGenericSuperclass() )
        .getActualTypeArguments()[0];
        if ( o instanceof ParameterizedType ) {
            o = ((ParameterizedType)o).getRawType();
        }
        this.persistentClass = (Class<T>) o;
        
    }
    
    public GenericHibernateDAO( Class<T> persistentClass ) {
        this.persistentClass = persistentClass;
    }
    
    public void setSession(Session s) {
        this.session = s;
    }
    
    protected Session getSession() {
        if (session == null)
            session = HibernateUtil.getSessionFactory()
            .getCurrentSession();
        return session;
    }
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
    
    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) getSession()
            .load(getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) getSession()
            .load(getPersistentClass(), id);
        return entity;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findByCriteria();
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance,
            String... excludeProperty) {
        Criteria crit =
                getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
    
    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit =
                getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) {
//        return (T) getSession().merge(entity);
        getSession().saveOrUpdate( entity );
        return entity;
    }
    
    public void makeTransient(T entity) {
        getSession().delete(entity);
    }
    
    public void flush() {
        getSession().flush();
    }
    
    public void clear() {
        getSession().clear();
    }
}
