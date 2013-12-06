$(document).ready(function () {
  initialiserPageAjoutSeance();
});

var taillePaneauSeancePrecedente = 0;

function initialiserPageAjoutSeance() {
    
	bindResizeFunction(onchangeOnglet);
	
	appliqueRotationTampon();
}

/** 
 * Declenche lors du changement d'onglet 
 * @return 
 */
function onchangeOnglet() {
    var div = jQuery('#formulaire_principal\\:idListeDesSeancePrecedente');
    if (div != null) {
        div.height(jQuery(window).height() - 120);
    }
    div = jQuery('#formulaire_principal\\:idListeDesDevoirs');
    if (div != null) {
        div.height(jQuery(window).height() - 120 -40);
    }
    div = jQuery('#formulaire_principal\\:idDivTabSeance');
    if (div != null) {
        div.height(jQuery(window).height() - 120);
    }
}
 
 /**
  * Positionne le scroll de la div au debut de la suite des seance precedente.
  * la taille est stockee dans taillePaneauSeancePrecedente par la fonction stockerTailleScrollSeancePrecedente.
  */
 function scrollSuiteSeance() {
     
     var div = jQuery('#formulaire_principal\\:idListeDesSeancePrecedente');
     if (div != null) {
         div.scrollTop(taillePaneauSeancePrecedente);
     }    
 }

 /**
  * Appelle lors du click sur le bouton d'affichage des seance precedente suivantes.
  * Stcoke dans la var la taille courante du paneau pour pouvoir ensuite faire 
  * le bon positionnement.
  */
 function stockerTailleScrollSeancePrecedente() {
     var div = jQuery('#formulaire_principal\\:contentSeancePrecedente');
     if (div != null) {
         taillePaneauSeancePrecedente = div.height();
     } else {
         taillePaneauSeancePrecedente = 0;
     }
 }
