package org.crlr.web.utils;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.crlr.dto.application.base.Identifiable;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;

/**
 * Classe utilisé pour pouvoir utiliser des boutons radio / checkbox avec une liste de DTOs.
 * 
 * 
 *
 */
@FacesConverter("selectOneDtoConverter")
public class SelectOneDtoConverter implements Converter {

    protected final Log log = LogFactory.getLog(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
     * , javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        @SuppressWarnings("unchecked")
        final
        List<? extends Identifiable> listeDTO = (List<? extends Identifiable>) component
                .getAttributes().get("converterListe");

        try {
            for (Object o : listeDTO) {
                Identifiable i = (Identifiable) o;
                if (i != null && i.getId() != null && i.getId().toString().equals(value)) {
                    return o;
                }
            }
        } catch (Exception ex) {
            log.error(ex, "ex");
        }
        
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
     * , javax.faces.component.UIComponent, java.lang.Object)
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        String r = "";
        
        if (value != null && value instanceof Identifiable) {
            final Integer id = ((Identifiable) value).getId();
            if (id != null) {
                r = id.toString();
            }
        }

        // log.info("getAsString " + r);
        return r;
    }

}
