/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: MenuForm.java,v 1.1 2009/03/17 16:30:44 ent_breyton Exp $
 */

package org.crlr.web.application.form;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.crlr.web.application.MenuNodeAction;

/**
 * Formulaire d'authentification.
 *
 * @author breytond
 */
public class MenuForm extends AbstractForm {
    /**
     * 
     */
    private static final long serialVersionUID = 1143163984045542482L;
    /** The Constant serialVersionUID. */
    

    /** Le contenu du menu est un arbre. */
    private TreeModel arbreComplet = new TreeModelBase(new MenuNodeAction());
    
    /** Binding de l'arbre en cours. */
    private HtmlTree tree;

    /**
     * Gets the arbre complet.
     *
     * @return the arbre complet
     */
    public TreeModel getArbreComplet() {
        return arbreComplet;
    }

    /**
     * Sets the arbre complet.
     *
     * @param arbreComplet the new arbre complet
     */
    public void setArbreComplet(TreeModel arbreComplet) {
        this.arbreComplet = arbreComplet;
    }
    
    /**
     * Sets the tree.
     *
     * @param tree the new tree
     */
    public void setTree(HtmlTree tree) {
        this.tree = tree;
    }

    /**
     * Gets the tree.
     *
     * @return the tree
     */
    public HtmlTree getTree() {
        return tree;
    }
   
}
