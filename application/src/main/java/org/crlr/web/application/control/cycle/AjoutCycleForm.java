package org.crlr.web.application.control.cycle;

import java.util.ArrayList;
import java.util.List;

import org.crlr.alimentation.DTO.EnseignantDTO;
import org.crlr.dto.application.base.GenericDTO;
import org.crlr.dto.application.base.GroupeDTO;
import org.crlr.dto.application.base.UtilisateurDTO;
import org.crlr.dto.application.cycle.CycleDTO;
import org.crlr.dto.application.cycle.CycleDevoirDTO;
import org.crlr.dto.application.cycle.CycleSeanceDTO;
import org.crlr.dto.application.cycle.TypeDateRemise;
import org.crlr.dto.application.devoir.TypeDevoirDTO;
import org.crlr.web.application.form.AbstractForm;
import org.crlr.web.application.form.AbstractPopupForm;
import org.crlr.web.contexte.utils.ContexteUtils;
import org.crlr.web.dto.FileUploadDTO;

/**
 * Formulaire de creation/modification/consultation d'une sequence pedagoqique.
 * @author G-SAFIR-FRMP
 *
 */
public class AjoutCycleForm extends AbstractPopupForm {

    /** Serial. */
    private static final long serialVersionUID = 8217677504297737620L;

    /** Cycle assoicie au forumaire. */
    private CycleDTO cycle;

    /** Liste temp pour gérer le tri des séances. */
    private List<CycleSeanceDTO> listeSeanceTri;
    
    /** Nom de la class css associé a l'élément drag pour l'ordre de tri :.
     * Contient l'id de seance.  */
    private String idClassDrag;
    
    /** Nom de la class css associé a l'élément drop pour l'ordre de tri. 
     * Contient l'id de seance. */
    private String idClassDrop;
    
    /** Variable volatile qui va stocker l'id de la zone a rafraichir suite à un upload de piece jointe.*/
    private String raffraichirTabAfterUpload;
    
    /** Liste de tous les groupes collaboratif. */
    private List<GroupeDTO> listeGroupe; 
    
    /** Liste de tous les groupes collaboratif avec l'info si selectionne ou non. */
    private List<GenericDTO<Boolean, GroupeDTO>> listeGroupeSelection;
    
    /** Groupe selectionne dans la liste des groupes du cycle. */
    private GroupeDTO groupeSelected;
    
    /** Liste des enseignants du groupe selectionnee.*/
    private List<EnseignantDTO> listeEnseignant;
    
    /** Filtre applique sur la designation groupe dans la popupGroupe. */
    private String filtreGroupe;
    
    /** Seance qui est affichee dans la popup. */
    private CycleSeanceDTO cycleSeanceSelected;
    
    /** Devoir selectionne pour les actions sur PJ ou description. */
    private CycleDevoirDTO cycleDevoirSelected;
    
    /** Indique si on est un sous ecran ou en entree directe. */
    private Boolean afficheRetour; 
    
    /** Fichier PJ selectionne pour la suppression. */
    private FileUploadDTO pieceJointeASupprimer; 

    /** Liste des type de devoir. */
    private List<TypeDevoirDTO> listeTypeDevoir;

    /** Liste des dates de remise. */
    private List<TypeDateRemise> listeDateRemise;
    
    /**
     * Reset les champ du formulaire.
     */
    public void reset() {
        cycle = new CycleDTO();
        raffraichirTabAfterUpload = null;
        mode = AbstractForm.MODE_AJOUT;
        cycleSeanceSelected = null;
        listeSeanceTri = new ArrayList<CycleSeanceDTO>();
    }
    
    /**
     * Constructeur.
     */
    public AjoutCycleForm() {
        super();
        reset();
    }


    /**
     * Accesseur de cycle {@link #cycle}.
     * @return retourne cycle
     */
    public CycleDTO getCycle() {
        return cycle;
    }

    /**
     * Mutateur de cycle {@link #cycle}.
     * @param cycle le cycle to set
     */
    public void setCycle(CycleDTO cycle) {
        this.cycle = cycle;
    }

    /**
     * Accesseur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @return retourne raffraichirTabAfterUpload
     */
    public String getRaffraichirTabAfterUpload() {
        return raffraichirTabAfterUpload;
    }

    /**
     * Mutateur de raffraichirTabAfterUpload {@link #raffraichirTabAfterUpload}.
     * @param raffraichirTabAfterUpload le raffraichirTabAfterUpload to set
     */
    public void setRaffraichirTabAfterUpload(String raffraichirTabAfterUpload) {
        this.raffraichirTabAfterUpload = raffraichirTabAfterUpload;
    }
    
    /**
     * Retourne le libelle a afficher pour le titre de l'ecran en fonction
     * du mode (ajout/modif/consultation).
     * @return un titre.
     */
    public String getTitreEcran() {
        if (cycle == null || cycle.getId() == null) {
            return "Ajout d'une séquence pédagogique";
        } else {
            return "Séquence pédagogique";
        }
    }
    /**
     * Indique si le form est en mode readOnly ou non.
     * Si l'utilisateur n'est pas l'auteur du cycle, on est en mode readOnly. 
     * 
     * Nous utilison le utilisateur réellement 
     * 
     * @return le readOnly
     */
    public Boolean getReadOnly() {
        return  !ContexteUtils.getContexteUtilisateur()
                .getUtilisateurDTOConnecte().getUserDTO()
                .getIdentifiant().equals(cycle.getEnseignantDTO().getId());
    }

    /**
     * Accesseur de listeGroupe {@link #listeGroupe}.
     * @return retourne listeGroupe
     */
    public List<GroupeDTO> getListeGroupe() {
        return listeGroupe;
    }

    /**
     * Mutateur de listeGroupe {@link #listeGroupe}.
     * @param listeGroupe le listeGroupe to set
     */
    public void setListeGroupe(List<GroupeDTO> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }

    /**
     * Accesseur de groupeSelected {@link #groupeSelected}.
     * @return retourne groupeSelected
     */
    public GroupeDTO getGroupeSelected() {
        return groupeSelected;
    }

    /**
     * Mutateur de groupeSelected {@link #groupeSelected}.
     * @param groupeSelected le groupeSelected to set
     */
    public void setGroupeSelected(GroupeDTO groupeSelected) {
        this.groupeSelected = groupeSelected;
    }

    /**
     * Accesseur de filtreGroupe {@link #filtreGroupe}.
     * @return retourne filtreGroupe
     */
    public String getFiltreGroupe() {
        return filtreGroupe;
    }

    /**
     * Mutateur de filtreGroupe {@link #filtreGroupe}.
     * @param filtreGroupe le filtreGroupe to set
     */
    public void setFiltreGroupe(String filtreGroupe) {
        this.filtreGroupe = filtreGroupe;
    }

    /**
     * Accesseur de cycleSeanceSelected {@link #cycleSeanceSelected}.
     * @return retourne cycleSeanceSelected
     */
    public CycleSeanceDTO getCycleSeanceSelected() {
        return cycleSeanceSelected;
    }

    /**
     * Mutateur de cycleSeanceSelected {@link #cycleSeanceSelected}.
     * @param cycleSeanceSelected le cycleSeanceSelected to set
     */
    public void setCycleSeanceSelected(CycleSeanceDTO cycleSeanceSelected) {
        this.cycleSeanceSelected = cycleSeanceSelected;
    }

    
    /**
     * Accesseur de cycleDevoirSelected {@link #cycleDevoirSelected}.
     * @return retourne cycleDevoirSelected
     */
    public CycleDevoirDTO getCycleDevoirSelected() {
        return cycleDevoirSelected;
    }

    /**
     * Mutateur de cycleDevoirSelected {@link #cycleDevoirSelected}.
     * @param cycleDevoirSelected le cycleDevoirSelected to set
     */
    public void setCycleDevoirSelected(CycleDevoirDTO cycleDevoirSelected) {
        this.cycleDevoirSelected = cycleDevoirSelected;
    }

    /**
     * Accesseur de seanceSelectedModifiable {@link #seanceSelectedModifiable}.
     * @return retourne seanceSelectedModifiable
     */
    public Boolean getSeanceSelectedModifiable() {
        if (cycleSeanceSelected != null && cycleSeanceSelected.getEnseignantDTO() != null) {
            final UtilisateurDTO utilisateurDTO = ContexteUtils.getContexteUtilisateur().getUtilisateurDTO();
            if (utilisateurDTO.getUserDTO().getIdentifiant().equals(cycleSeanceSelected.getEnseignantDTO().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Affiche le titre de la popup seance.
     * @return un titre en fonction du mode : lecture / modif / ajout
     */
    public String getSeanceTitre() {
        return "Séance";
    }

    /**
     * Accesseur de afficheRetour {@link #afficheRetour}.
     * @return retourne afficheRetour
     */
    public Boolean getAfficheRetour() {
        return afficheRetour;
    }

    /**
     * Mutateur de afficheRetour {@link #afficheRetour}.
     * @param afficheRetour le afficheRetour to set
     */
    public void setAfficheRetour(Boolean afficheRetour) {
        this.afficheRetour = afficheRetour;
    }

    /**
     * Accesseur de pieceJointeASupprimer {@link #pieceJointeASupprimer}.
     * @return retourne pieceJointeASupprimer
     */
    public FileUploadDTO getPieceJointeASupprimer() {
        return pieceJointeASupprimer;
    }

    /**
     * Mutateur de pieceJointeASupprimer {@link #pieceJointeASupprimer}.
     * @param pieceJointeASupprimer le pieceJointeASupprimer to set
     */
    public void setPieceJointeASupprimer(FileUploadDTO pieceJointeASupprimer) {
        this.pieceJointeASupprimer = pieceJointeASupprimer;
    }

    /**
     * Accesseur de listeTypeDevoir {@link #listeTypeDevoir}.
     * @return retourne listeTypeDevoir
     */
    public List<TypeDevoirDTO> getListeTypeDevoir() {
        return listeTypeDevoir;
    }

    /**
     * Mutateur de listeTypeDevoir {@link #listeTypeDevoir}.
     * @param listeTypeDevoir le listeTypeDevoir to set
     */
    public void setListeTypeDevoir(List<TypeDevoirDTO> listeTypeDevoir) {
        this.listeTypeDevoir = listeTypeDevoir;
    }

    /**
     * Accesseur de listeDateRemise {@link #listeDateRemise}.
     * @return retourne listeDateRemise
     */
    public List<TypeDateRemise> getListeDateRemise() {
        return listeDateRemise;
    }

    /**
     * Mutateur de listeDateRemise {@link #listeDateRemise}.
     * @param listeDateRemise le listeDateRemise to set
     */
    public void setListeDateRemise(List<TypeDateRemise> listeDateRemise) {
        this.listeDateRemise = listeDateRemise;
    }

    /**
     * Accesseur de listeGroupeSelection {@link #listeGroupeSelection}.
     * @return retourne listeGroupeSelection
     */
    public List<GenericDTO<Boolean, GroupeDTO>> getListeGroupeSelection() {
        return listeGroupeSelection;
    }

    /**
     * Mutateur de listeGroupeSelection {@link #listeGroupeSelection}.
     * @param listeGroupeSelection le listeGroupeSelection to set
     */
    public void setListeGroupeSelection(
            List<GenericDTO<Boolean, GroupeDTO>> listeGroupeSelection) {
        this.listeGroupeSelection = listeGroupeSelection;
    }

    /**
     * Accesseur de listeSeanceTri {@link #listeSeanceTri}.
     * @return retourne listeSeanceTri
     */
    public List<CycleSeanceDTO> getListeSeanceTri() {
        return listeSeanceTri;
    }

    /**
     * Mutateur de listeSeanceTri {@link #listeSeanceTri}.
     * @param listeSeanceTri le listeSeanceTri to set
     */
    public void setListeSeanceTri(List<CycleSeanceDTO> listeSeanceTri) {
        this.listeSeanceTri = listeSeanceTri;
    }

    /**
     * Accesseur de idClassDrag {@link #idClassDrag}.
     * @return retourne idClassDrag
     */
    public String getIdClassDrag() {
        return idClassDrag;
    }

    /**
     * Mutateur de idClassDrag {@link #idClassDrag}.
     * @param idClassDrag le idClassDrag to set
     */
    public void setIdClassDrag(String idClassDrag) {
        this.idClassDrag = idClassDrag;
    }

    /**
     * Accesseur de idClassDrop {@link #idClassDrop}.
     * @return retourne idClassDrop
     */
    public String getIdClassDrop() {
        return idClassDrop;
    }

    /**
     * Mutateur de idClassDrop {@link #idClassDrop}.
     * @param idClassDrop le idClassDrop to set
     */
    public void setIdClassDrop(String idClassDrop) {
        this.idClassDrop = idClassDrop;
    }

    /**
     * Accesseur de listeEnseignant {@link #listeEnseignant}.
     * @return retourne listeEnseignant
     */
    public List<EnseignantDTO> getListeEnseignant() {
        return listeEnseignant;
    }

    /**
     * Mutateur de listeEnseignant {@link #listeEnseignant}.
     * @param listeEnseignant le listeEnseignant to set
     */
    public void setListeEnseignant(List<EnseignantDTO> listeEnseignant) {
        this.listeEnseignant = listeEnseignant;
    }

    
}
