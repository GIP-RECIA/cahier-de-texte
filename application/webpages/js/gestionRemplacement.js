$(document).ready(function () {
  initialiserPageGestionRemplacement();
});

function initialiserPageGestionRemplacement() {
    bindResizeFunction(onresize);
}

/** 
 * Recalcule les tailles des div 
 * @return 
 */
function onresize() {
    var div = jQuery('#formulaire_principal\\:idGestionRemplacement');
    if (div != null) {
        div.height(jQuery(window).height() - 120);
    }
}
