
jQuery(document).ready(function () {
	hideSaveBouton();
	
});

var searchOk=true;

function showSaveBouton(){
	jQuery("a.saveBoutonClass").show();
	setModificationFormulaire(true);
}

function hideSaveBouton() {
	jQuery(".saveBoutonClass").hide();
   jQuery(".pickList td > button").click(function(){
	   showSaveBouton();
   });
	setModificationFormulaire(false);
}

function showAlertSuppression(){
	var d = jQuery('.alertSuppressionTest');
	if (d.length != 0) {
		return true;
	}
	return false;
}

function rechercher(){
	var b = jQuery('.rechercherBouton');
	if (b && searchOk) {
		b.click();
	}
	searchOk = true;
}



function nosearch(){
	searchOk=false;
	true;
}

