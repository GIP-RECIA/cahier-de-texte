jQuery(document).ready(function () {
    gestionDragDropDepot();
    bindResizeFunction(gestionDragDropDepot);
});


function gestionDragDropDepot() {
    jQuery( ".fichierDrag" ).draggable({ 
        cursor: "pointer",
        cursorAt: {left: 16, top:16},
        helper: function( event ) {
            var source = jQuery(this).prop("src");
            return jQuery( "<img src='" + source + "' />");
        }
    });
    jQuery(".rf-trn-cnt" ).droppable({
        hoverClass: "ui-state-active",
        drop: function( event, ui ) {
            var nomFichier = ui.draggable.prop("title");
            var nomDossierEntier = jQuery(this).text();
            var nomDossier = jQuery.trim(nomDossierEntier);
            var noeudDrop = jQuery(this);
            var fils = noeudDrop.find("input.treeNodeData");
            var nomComplet = fils.prop("value"); 
            deplacerFichier(nomFichier, nomComplet);
        }
    });
    
    
    var div = jQuery('#formulaire_principal\\:listingRepertoireCahierScroll');
    if (div != null) {
        div.height(jQuery(window).height() - 132);
    }
    div = jQuery('#formulaire_principal\\:containerRepertoireCahierScroll');
    if (div != null) {
        div.height(jQuery(window).height() - 132);
    }
    
}