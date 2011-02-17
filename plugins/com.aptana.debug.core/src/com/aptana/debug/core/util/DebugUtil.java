/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.debug.core.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

import com.aptana.core.resources.IUniformResource;

/**
 * @author Max Stepanov
 */
public final class DebugUtil {

	private DebugUtil() {
	}

	/**
	 * findAdapter
	 * 
	 * @param adaptableObject
	 * @param adapterType
	 * @return Object
	 */
	public static Object findAdapter(IAdaptable adaptableObject, Class<?> adapterType) {
		Object result = null;
		if (adaptableObject != null) {
			result = adaptableObject.getAdapter(adapterType);
			if (result == null) {
				ILaunch launch = (ILaunch) adaptableObject.getAdapter(ILaunch.class);
				if (launch != null) {
					for (IProcess process : launch.getProcesses()) {
						result = process.getAdapter(adapterType);
						if (result != null) {
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns raw path string for the provided element which could be
	 * an uniform resource, URI or plain String.
	 *
	 * @param element
	 * @return path
	 */
	public static String getPath(Object element) {
		if ( element instanceof IUniformResource ) {
			IUniformResource resource = (IUniformResource) element;
			IPath path = (IPath) resource.getAdapter(IPath.class);
			if (path == null) {
				IStorage storage = (IStorage) resource.getAdapter(IStorage.class);
				if (storage != null) {
					path = (IPath) storage.getAdapter(IPath.class);
				}
			}
			if ( path != null ) {
				return path.toOSString();	
			} else {
				return resource.getURI().toString();
			}			
		}
		if ( element instanceof String ) {
			try {
				element = new URI((String) element);
			} catch (URISyntaxException e) {
			}	
		}
		if ( element instanceof URI ) {
			URI uri = (URI) element;
			if ( "file".equals(uri.getScheme()) ) //$NON-NLS-1$
			{
				return uri.getSchemeSpecificPart();
			}
			return uri.toString();
		}
		return null;
	}
}
