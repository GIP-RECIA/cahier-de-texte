$(document).ready(function () {
  initialiserPageInspection();
});

function initialiserPageInspection() {
    bindResizeFunction(onresize);
}

/** 
 * Recalcule les tailles des div 
 * @return 
 */
function onresize() {
    var div = jQuery('#formulaire_principal\\:idDivListingDroitsInspections');
    if (div != null) {
        div.height(jQuery(window).height() - 120);
    }
}
