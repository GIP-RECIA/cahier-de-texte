function basculeAnnotations(nomFormulaire, idSaisieSeanceXX) {
    var objectAnnotations = jQuery('#' + idSaisieSeanceXX + '_idAnnotationPersonnelle');
    var isHidden = !objectAnnotations.is(":visible");
    objectAnnotations.toggle();
    var objectIconeAnnotations = jQuery(nomFormulaire + '\\:' + idSaisieSeanceXX + '_idIconeAnnotationPersonnelle');
    
    if (objectIconeAnnotations!=null && objectIconeAnnotations[0]!= null) {
        var motif;
        var repla;
        if (isHidden) {
            motif = "arrowDown.png";
            repla = "arrowUp.png";
        } else {
            motif = "arrowUp.png";
            repla = "arrowDown.png";
        }
        var reg=new RegExp("(" + motif + ")", "g");
        var srcOrigine = objectIconeAnnotations.attr("src");
        var scrNouveau = srcOrigine.replace(reg,repla);
        objectIconeAnnotations.attr("src", scrNouveau);
    }
}

function decalFin(elemDebutId, elemFinId, event) {
	logFormat("elemDebutId {0} elem fin {1}", elemDebutId, elemFinId);
	
	/* La suite ne marche pas et provoque une erreur. A REVOIR 
	var eventTarget = event.target;
	
	var isHeure = event.target.id.match(/heureMinuteDebut_heure/) != null;
	
	var idBase = '#' + event.target.id.replace(/Input$/, "").replace(/:/g, "\\:").replace(/_/g, "_")
	var idSelValue = idBase  + "selValue";
	var idInput = idBase + 'Input';
	
	var idBaseFin = idBase.replace(/Debut/g, "Fin");
	var idSelValueFin = idBaseFin  + "selValue";
	var idInputFin = idBaseFin + 'Input'; 
	
	var elemSelValueDebut = jQuery(idSelValue).val();
	var elemInputDebut = jQuery(idInput).val();
	var elemSelValueFin = jQuery(idSelValueFin).val();
	var elemInputFin = jQuery(idInputFin).val();
	
	var originalValue = jQuery(idInput)[0].defaultValue;
	var newValue = jQuery(idInput).val();
	
	var diff = parseInt(newValue, 10) - parseInt(originalValue, 10);
	
	
	var newFinValue = parseInt(jQuery(idInputFin).val(), 10) + diff;
	jQuery(idInputFin).val( newFinValue );
	jQuery(idSelValueFin).val( newFinValue );
  
	//Mettre 'vieux' valeur à jour pour la éventuelle prochaine fois
	jQuery(idInput)[0].defaultValue = newValue;
	
  logFormat("debut {0} {1} fin {2} {3} new val {4} orig val {5} diff {6} {7} {8} {9} {10}", 
  		elemSelValueDebut, elemInputDebut, 
  		elemSelValueFin, elemInputFin,
  		newValue, originalValue, diff,
  		jQuery(idSelValue).defaultValue,
  		jQuery(idSelValue)[0].defaultValue,
  		jQuery(idInput).defaultValue,
  		jQuery(idInput)[0].defaultValue
  );*/
  
}


