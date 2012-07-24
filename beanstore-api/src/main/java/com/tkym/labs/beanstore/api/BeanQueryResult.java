package com.tkym.labs.beanstore.api;

import java.util.Iterator;
import java.util.List;


public interface BeanQueryResult<QT>{
	public QT asSingle() throws BeanstoreException;
	public Iterator<QT> asIterator() throws BeanstoreException;
	public List<QT> asList() throws BeanstoreException;
}