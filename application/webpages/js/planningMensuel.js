
var taillePaneauSeancePrecedente = 0;

jQuery(document).ready(function () {
	mettreTooltips();
	
	bindResizeFunction(resizePanel);
	
});

/**
 * Appelle lors du click sur le bouton d'affichage des seance precedente suivantes.
 * Stcoke dans la var la taille courante du paneau pour pouvoir ensuite faire 
 * le bon positionnement.
 */
function stockerTailleScrollSeancePrecedente() {
    var div = jQuery('#idPopupSeance_form\\:contentSeancePrecedente');
    if (div != null) {
        taillePaneauSeancePrecedente = div.height();
    } else {
        taillePaneauSeancePrecedente = 0;
    }
}


/**
 * Positionne le scroll de la div au debut de la suite des seance precedente.
 * la taille est stockee dans taillePaneauSeancePrecedente par la fonction stockerTailleScrollSeancePrecedente.
 */
function scrollSuiteSeance() {
    
    var div = jQuery('#idPopupSeance_form\\:idListeDesSeancePrecedente');
    if (div != null) {
        div.scrollTop(taillePaneauSeancePrecedente);
    }    
}

function resizePanel() { 
	//console.log("resizePanel");
    var div = jQuery('#formulaire_principal\\:zoneTable');
    if (div != null) {
    	
        div.height(jQuery(window).height() - 70 );
        //logMessage("height {0}", div.height());
    }
}

function mettreTooltips() {
	console.log("planningMensuel");
	$(".planningLien").each(function(index, element) {
		
		var title = element.title.replace(/\\n/g, "<br/>");
		
		logFormat("planningLien {0} titre: {1}, {2}", index, title, element);
		
		var jqElement = jQuery(element);
	  // $('.cahierEventIndex' + index).attr("title", title);
		//jqElement.attr("title", title);
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

