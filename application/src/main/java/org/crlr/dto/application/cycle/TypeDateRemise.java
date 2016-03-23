package org.crlr.dto.application.cycle;

import java.util.ArrayList;
import java.util.List;

import org.crlr.dto.application.base.Identifiable;

/** 
 * Liste des Dates génériques de remise des devoirs. 
 * @author G-SAFIR-FRMP
 *
 */
public enum TypeDateRemise implements Identifiable  {
    SEANCE_SUIVANTE_1(1, "Séance suivante"),
    SEANCE_SUIVANTE_2(2, "2ème séance suivante"),
    SEANCE_SUIVANTE_3(3, "3ème séance suivante"),
    SEANCE_SUIVANTE_4(4, "4ème séance suivante"),
    SEMAINE_SUIVANTE_1(101, "Séance de la semaine suivante"),
    SEMAINE_SUIVANTE_2(102, "Séance de la 2ème semaine suivante"),
    SEMAINE_SUIVANTE_3(103, "Séance de la 3ème semaine suivante"),
    SEMAINE_SUIVANTE_4(104, "Séance de la 4ème semaine suivante")
    ;
    
    /** Id stocké en base. */
    private final Integer id;
    
    /** Libelle affichée. */
    private final String libelle;
    
    /** 
     * Constructeur par defaut.
     * @param id : identifiant 
     * @param libelle : libelle
     */
    private TypeDateRemise(final Integer id, final String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    /**
     * Accesseur de id {@link #id}.
     * @return retourne id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Accesseur de libelle {@link #libelle}.
     * @return retourne libelle
     */
    public String getLibelle() {
        return libelle;
    }
    
    /**
     * Retourne le Type de date de remise correspondant au id.
     * @param id : id recherche
     * @return un TypeDateRemise
     */
    public static TypeDateRemise getTypeDateRemise(final Integer id) {
        if (SEANCE_SUIVANTE_1.getId().equals(id)) { 
            return SEANCE_SUIVANTE_1;
        } else if (SEANCE_SUIVANTE_2.getId().equals(id)) {
            return SEANCE_SUIVANTE_2;
        } else if (SEANCE_SUIVANTE_3.getId().equals(id)) {
            return SEANCE_SUIVANTE_3;
        } else if (SEANCE_SUIVANTE_4.getId().equals(id)) {
            return SEANCE_SUIVANTE_4;
        } else if (SEMAINE_SUIVANTE_1.getId().equals(id)) {
            return SEMAINE_SUIVANTE_1;
        } else if (SEMAINE_SUIVANTE_2.getId().equals(id)) {
            return SEMAINE_SUIVANTE_2;
        } else if (SEMAINE_SUIVANTE_3.getId().equals(id)) {
            return SEMAINE_SUIVANTE_3;
        } else if (SEMAINE_SUIVANTE_4.getId().equals(id)) {
            return SEMAINE_SUIVANTE_4;
        } else {
            return null;
        }
    }
    
    /**
     * Charge la liste des type de dates de remise. 
     * @return la liste.
     */
    public static List<TypeDateRemise> loadListeTypeDateRemise() {
        final List<TypeDateRemise> liste = new ArrayList<TypeDateRemise>();
        for (final TypeDateRemise typeDateRemise : TypeDateRemise.values()) {
            liste.add(typeDateRemise);
        }
        return liste;
    }
    
}
