$(document).ready(function () {
	bindResizeFunction(resizePanel);
	mettreTooltips();
});

function resizePanel() {
    var div = jQuery('#formulaire_principal\\:idListeDesDevoirsPanel');
    if (div != null) {
        div.height(jQuery(window).height() - 190 );
    }
    var div = jQuery('#formulaire_principal\\:idPanelResultat');
    if (div != null) {
        div.height(jQuery(window).height() - 190 -20 );
    }
}

function executeLien(idLien) {
    var lien = $('#formulaire_principal\\:' + idLien);
    lien.click();
}

function mettreTooltips() {
	
	
	jQuery(".seanceSemaineLien").each(function(index, element) {
		
		var title = element.title.replace(/\\n/g, "<br/>");
		
		
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