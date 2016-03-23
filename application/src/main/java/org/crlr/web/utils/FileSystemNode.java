/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.richfaces.model.TreeNodeImpl;

/**
 * DOCUMENTATION INCOMPLETE!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class FileSystemNode extends TreeNodeImpl implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6694036498580133598L;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String path;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    //!!private List<FileSystemNode> children;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private List<FileSystemNode> filesList;

    /**
     * DOCUMENTATION INCOMPLETE!
     */
    private String shortPath;
 

    /**
     * Constructeur.
     * @param path le path.
     */
    public FileSystemNode(boolean leaf, String path) {
        super(leaf);
        this.path = path;
        final int idx = path.lastIndexOf('/');
        if (idx != -1) {
            shortPath = path.substring(idx + 1);
        } else {
            shortPath = path;
        }
        
    }

    private Object[] getResourcePaths() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = facesContext.getExternalContext();
        final Set<String> resourcePaths = externalContext.getResourcePaths(this.path);
        if (resourcePaths != null) {
            return resourcePaths.toArray();
        }
        
        return new Object[0]  ;
    }
    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public List<FileSystemNode> getNodes() {
        final List<FileSystemNode> resulat = new ArrayList<FileSystemNode>();
        
        if (!getChildrenKeysIterator().hasNext()) {
            final Object[] nodes = getResourcePaths();
            for (int i = 0; i < nodes.length; i++) {
                String nodePath = nodes[i].toString();
                if (nodePath.endsWith("/")) {
                    nodePath = nodePath.substring(0, nodePath.length() - 1);
                    final FileSystemNode noeud = new FileSystemNode(false,nodePath);
                    super.addChild(noeud, noeud);
                }
            }               
        }
        
        for (final java.util.Iterator<Object> i = getChildrenKeysIterator(); i.hasNext(); ) {
            final FileSystemNode noeud = (FileSystemNode) i.next();
            resulat.add(noeud);
        }
        
        return resulat;
/*!!        
        
        if (children == null) {
            children = new ArrayList<FileSystemNode>();
            final Object[] nodes = getResourcePaths();
            for (int i = 0; i < nodes.length; i++) {
                String nodePath = nodes[i].toString();
                if (nodePath.endsWith("/")) {
                    nodePath = nodePath.substring(0, nodePath.length() - 1);         
                    children.add(new FileSystemNode(false,nodePath));
                }
            }                
        }

        return children;*/
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public List<FileSystemNode> getFilesOfNodes() {
        if (filesList == null) {
            filesList = new ArrayList<FileSystemNode>();
            final Object[] nodes = getResourcePaths();

            for (int i = 0; i < nodes.length; i++) {
                final String nodePath = nodes[i].toString();
                if (!nodePath.endsWith("/")) {
                    filesList.add(new FileSystemNode(true,nodePath));
                }
            }
        }

        return filesList;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String toString() {
        return shortPath;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getName() {
        return this.shortPath;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public String getPath() {
        return this.path;
    }

    /**
     *
     *
     * @return DOCUMENTATION INCOMPLETE!
     */
    public List<FileSystemNode> getFilesList() {
        return filesList;
    }

    /**
     *
     *
     * @param filesList DOCUMENTATION INCOMPLETE!
     */
    public void setFilesList(List<FileSystemNode> filesList) {
        this.filesList = filesList;
    }
}
