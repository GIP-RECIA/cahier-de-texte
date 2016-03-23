package org.crlr.web.utils;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crlr.message.Message;
import org.crlr.web.contexte.ContexteUtilisateur;
import org.crlr.web.dto.FileUploadDTO;

public interface IFacesUtils {


    /**
     * Retourne l'instance {@link HttpServletRequest} courante.
     *
     * @return une instance {@link HttpServletRequest}, ou <code>null</code>
     */
    public HttpServletRequest getRequest();

    /**
     * Retourne l'instance {@link HttpServletResponse} courante.
     *
     * @return une instance {@link HttpServletResponse}, ou <code>null</code>
     */
    public  HttpServletResponse getResponse();

    /**
     * Retourne un paramètre de la requête.
     *
     * @param nom du paramètre
     *
     * @return valeur du paramètre
     */
    public  String getParametre(String nom) ;


    /**
     * Efface les valeurs du formulaire attaché à la requête.
     */
    public  void resetForm() ;

    /**
     * Retourne le <code>viewId</code> de la vue courante.
     *
     * @return <code>viewId</code>, ou <code>null</code> s'il n'existe aucune vue
     */
    public  String getViewId() ;

    /**
     * Retourne l'instance {@link ServletContext} courante.
     *
     * @return une instance {@link ServletContext}, ou <code>null</code>
     */
    public ServletContext getServletContext();

    /**
     * Retourne une URL dont le chemin est relatif au contexte de l'application.
     *
     * @param url relative au contexte
     *
     * @return url comprenant le chemin du contexte
     */
    public String getContextURL(String url) ;

    /**
     * Génère un identifiant pour un objet.
     *
     * @param obj the obj
     *
     * @return un identifiant pour l'objet, ou la chaîne <code>"null"</code> si l'objet
     *         est <code>null</code>
     */
    public String generateId(Object obj);

    /**
     * Generate id counter.
     *
     * @param obj the obj
     *
     * @return the string
     */
    public String generateIdCounter(Object obj);

   
    
    /**
     * Messages statut.
     *
     * @return the string
     */
    public String messagesStatut();

    /**
     * Redirection vers l'URL demandée.
     *
     * @param url url ne contenant pas le context path, celui ci sera ajouté
     *        automatiquement
     */
    public void redirect(final String url) ;
    
    /**
     * Redirection vers l'URL demandée avec un message associé.
     *
     * @param url url ne contenant pas le context path, celui ci sera ajouté
     *        automatiquement.
     * @param message le message.
     */
    public void redirectWithMessage(final String url, final Message message);

    /**
     * Récupére la valeur d'un paramètre de l'application web initialisé dans le
     * fichier de configuration web.xml. Si aucune valeur n'est trouvée, c'est la valeur
     * par défaut fournie en paramètre qui est renvoyée.
     *
     * @param parametre le nom du paramètre
     * @param valeurDefaut the valeur defaut
     *
     * @return la valeur du paramètre
     */
    public String getInitParameter(String parametre, String valeurDefaut);
    
    /**
     * Permet d'évaluer une variable jsf en fonction d'un contexte faces.
     * @param fc le contexte faces.
     * @param variable la variable à évaluer.
     * @return l'évaluation.
     */
    public Object resolveVariable(final FacesContext fc, final String variable);
    
    /**
     * Permet d'évaluer une variable jsf.
     * @param variable la variable à évaluer.
     * @return l'évaluation.
     */
    public Object resolveVariable(final String variable) ;
  
    
    /**
     * Deconnexion d'un utilisateur.
     * @param fCtx le faces contexte.
     * @param contexteUtilisateur le contexte utilisateur.
     */
    public void deconnexionApplication(final FacesContext fCtx, final ContexteUtilisateur contexteUtilisateur);
    
    /**
     * Deconnexion d'un utilisateur.
     * @param fCtx le faces contexte.   
     */
    public void deconnexionApplication(final FacesContext fCtx);
    
    
    /**
     * Déconnexion.
     */
    public  void deconnexionCas();

    /**
     * .
     * @param fileUploadDTO .
     */
    public void pathUploadFile(final FileUploadDTO fileUploadDTO);

}
