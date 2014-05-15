
function handleEventAfficherSeance(e) {
	console.log("handleEventAfficherSeance");
	console.log(e);
	if (e.status == "success") {
		RichFaces.$('idPopupSeance').show();
		
	//	jQuery("idPopupSeance_hidelink.hidelink")
	}
	 
}

/* un click sur la flèche cache ou montre la liste.
jQuery("##{formId}\\:#{id}showandhidelinkid").click(function() {
    if (jQuery("##{id}magicBoxList").is(":hidden")) {
        jQuery("##{id}magicBoxList").show();
      } else {
          jQuery("##{id}magicBoxList").hide();
      }
});           
*/