
jQuery(document).ready(function () {
    initFormulaire();
});

function initFormulaire() {
    bindResizeFunction(resizePanel);
    resizePanel();
    mettreTooltips();
    appliquerGestionModification();
    gestionDragDropCycle();
}

function gestionDragDropCycle() {
    jQuery( ".seanceDrag" ).draggable({ 
        cursor: "pointer",
        cursorAt: {left: 16, top:16},
        helper: function( event ) {
            var source = jQuery(this).prop("src");
            var libelle = jQuery(this).parent().prop("title");
            return jQuery( "<span class='dragDropSeance'><img src='" + source + "' />" + libelle + "</span>");
        }
    });
    jQuery(".seanceDrop" ).droppable({
        hoverClass: "dropSeanceHover",
        drop: function( event, ui ) {
            var idDrag = ui.draggable.prop("class"); 
            var idDrop = jQuery(this).prop("class");
            deplacerSeance(idDrag, idDrop);
            
            /*var nomFichier = ui.draggable.prop("title");
            var nomDossierEntier = jQuery(this).text();
            var nomDossier = jQuery.trim(nomDossierEntier);
            var noeudDrop = jQuery(this);
            var fils = noeudDrop.find("input.treeNodeData");
            var nomComplet = fils.prop("value"); 
            deplacerFichier(nomFichier, nomComplet);
            */
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
function resizePanel() { 
    var div = jQuery('#formulaire_principal\\:zoneTable');
    if (div != null) {
        var p = div.position();
        var moins = p.top + 25;
        div.height(jQuery(window).height() - moins );
    }
}

function mettreTooltips() {
    jQuery(".listeGroupeSelected").each(function(index, element) {
        
        var title = element.title;
        var jqElement = jQuery(element);
        jqElement.qtip({
        position: {
            my: 'top left',  // Position my top left...
            at: 'bottom right', // at the bottom right of...
            target: 'mouse',
            adjust: {x: 10,y: 10 }
            
        },
        content: {
                text: title
            }
      });
    });
}
