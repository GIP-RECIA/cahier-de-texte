

function startDropOutConteneur(ctx) {
//dropout = new Effect.DropOut('messagesDeplie',{duration:0.8});
//   setTimeout("afficherDetailMessages('" + ctx + "')", 1000);
}



function handleChargerZoneDeMessages() {
	
	var haveMessagesBloquant = jQuery("#messages").data("haveMessagesBloquant");
	var haveMessagesAvertissant = jQuery("#messages").data("haveMessagesAvertissant");
	logFormat("handleChargerZoneDeMessages bloq {0} avertissant {1}", 
			haveMessagesBloquant,
			haveMessagesAvertissant);
	

	jQuery('#messagesDeplie').hide();
	
	if (haveMessagesBloquant) {		
		jQuery("<div title='Messages'>" + jQuery("#messages").html() + "</div>").dialog({ 
			buttons: { "Ok": function() 
				{ $(this).dialog("close"); $(this).dialog("destory"); }
		} });
				
		jQuery(".ui-dialog-titlebar").removeClass("ui-widget-header").addClass("rf-pp-hdr rf-pp-hdr-cnt");
		return;
	}
	
                        
    if (haveMessagesAvertissant) {
        setTimeout("afficherDetailMessages()", 100);                               
        var dureePopup = 3000;        
        setTimeout("afficherDetailMessages()", dureePopup);                            
    } 
	
}

/**
 * Afficher les details de la zone de
 * messages dans une div
 */
function afficherDetailMessages(noMenu){

	  
	 logFormat("afficherDetailMessages {0} noMenu {1}", ctx, noMenu);
	 
    /* Recherche de la zone de messages du pied*/
	var messages = jQuery('#messages');
	/* Recherche de l'image qui permet d'action la zone de messages */
	var img = jQuery('#listeMessagesAction');
	/* Zone d'affichage des messages sous forme depli */
	var messagesDeplie	= jQuery('#messagesDeplie');
	
	
	/* Meme fonctionnement des 2 divs sur l'evenement click */
	messagesDeplie.onclick = messages.onclick;
			
	var zoneMessageAffichee = messagesDeplie.is(':visible');
	
	if(!zoneMessageAffichee){				
		/* On copie le contenu des messages dans la div */
		messagesDeplie.html( messages.html() );
	 	
		/* on affiche la zone depliee */
		messagesDeplie.show("slide", { direction: "down" }, 1000);
								
		/* on change l'image de l'action deplier/replier */		
		img.src= jQuery("#messages").data("minusImage");
		
		/* on cache la zone pliee */
		messages.hide();	
		
		// Correction problème des combobox qui passent par dessus
		/*if (checkIt('msie')){
			hideSelects('hidden');
		}*/		
	
	} else {
		/* On cache la zone de message depliee*/
		messagesDeplie.hide();
	
		/* on change l'image de l'action deplier/replier */
		img.src= jQuery("#messages").data("plusImage");
		
		/* on raffiche la zone la zone pliee */
		messages.show();
		
		// Correction probl�me des combobox qui passent par dessus
		/*if (checkIt('msie')){
			hideSelects('visible');
		}*/
	}
	
}

 
 