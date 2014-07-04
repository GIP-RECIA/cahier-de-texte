
jQuery(document).ready(function () {
	hideSaveBouton();
});



function showSaveBouton(){
	jQuery("a.saveBoutonClass").show();
}

function hideSaveBouton() {
	jQuery(".saveBoutonClass").hide();
   jQuery(".pickList td > button").click(function(){
	   showSaveBouton();
   });
}

function showAlertSuppression(){
	var d = jQuery('.alertSuppressionTest');
	if (d.length != 0) {
		return true;
	}
	return false;
}


