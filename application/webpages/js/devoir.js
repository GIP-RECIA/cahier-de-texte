jQuery(document).ready(function () {
	console.log("devoir bind resize panel");
	bindResizeFunction(resizePanel);
});

function resizePanel() {
	console.log("devoir resizePanel");
    var div = jQuery('#formulaire_principal\\:idListeDesDevoirsPanel');
    if (div != null) {
        div.height(jQuery(window).height() - 220 );
    }
    var div = jQuery('#formulaire_principal\\:idPanelResultat');
    if (div != null) {
        div.height(jQuery(window).height() - 220 -20 );
    }
}

