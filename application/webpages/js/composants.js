//IE does not define this by default
if ("undefined" === typeof window.console)
{
	    window.console = {
	        "log": function() { }
	    };	
}

function logFormat() {
	console.log(stringFormat.apply(this, arguments));
}

function logMessage() {
	console.log(stringFormat.apply(this, arguments));
}

jQuery(document).ready(function () {
	if (jQuery.browser.msie) {
		//Pour IE, corrige les tooltips des icones dans le menu
		var menuIcones = jQuery("div.rf-ddm-itm > span > img");
				
		//Prendre le titre du div deux niveaux plus haut
		menuIcones.each(function(index, elem) {
			var jQueryElem = jQuery(elem);
			jQueryElem.attr("alt", jQueryElem.parent().parent().attr("title"));
		});
		
	}

});

function stringFormat() {
  var s = arguments[0];
  for (var i = 0; i < arguments.length - 1; i++) {       
      var reg = new RegExp("\\{" + i + "\\}", "gm");   
      var arg = arguments[i + 1];
      arg = $.isPlainObject(arg) ? printObj(arg) : formatVal(arg);
      s = s.replace(reg, arg);
  }
  return s;
};

function formatVal(val) {
	if (val instanceof Date) {
  	return stringFormat("{0}y-{1}m-{2}d", val.getFullYear(), val.getMonth() + 1, val.getDate());
  } else if ("object" == (typeof val)) {
  	//printObj(val);
  	//console.dir(val);
  	return val;
  	
  } else {
  	return val ;
  }
}

var printObj =  function(obj) {
  var arr = [];
  $.each(obj, function(key, val) {
    var next = key + ": ";
    next += $.isPlainObject(val) ? printObj(val) : formatVal(val);
    
    arr.push( next );
  });
  return "{ " +  arr.join(", ") + " }";
};

//Another IE fix, prevents IE from cacheing ajax calls
//jQuery.ajaxSetup({
   // cache: false
//});

//Nom du formulaire principal de toute page
var oldValeurSaisie = null; 
var nomFormPrincipal = 'formulaire_principal';
var nomAttributMode = 'mode';
var nomFormMode = 'form_mode';
var listeTablesPage = null; //$H(); // liste des tables dans la page courante
var dropout = null; //evenement dropout
var highlight = null; //evenement highlight
var isDebug = false; // mettre a true en developpement
var idCliqueCelPrecedent = null;
var clazzCliqueCelPrecedent = null;
var tablesScrolling = new Array(); //conserve les positions des scroll au sein de l'ensemble des tableaux de la page.
var focusOnId =''; // Identifiant de l'élément ou positionner le prochain focus
var detect = navigator.userAgent.toLowerCase();


function checkIt(string) {
	return detect.indexOf(string) + 1;	
}


/**
 * Conserve l'�tat des scrolls de l'ensemble des tableaux pr�sents dans la page.
 */ 
function saveTablesScrolling() { 
   tablesScrolling = new Array();          
   var tables = document.getElementsByClassName('scroll-corps');
           
   for (var i=0 ; i<tables.length ; i++) {
     tablesScrolling[i] = new Array(tables[i].id,tables[i].scrollTop, tables[i].scrollLeft);
   }
}

/**
 * @param obj la cellule sur laquelle on veut scoller.
 * @div la div pour appliquer le scoll.
 * @moins on supprime des pixels sur la position du scoll. 
 */
function scrollIntoObject(obj, div, moinsX, moinsY){

    if (obj != null && div != null) {
      var x = obj.offsetLeft;
      var y = obj.offsetTop;
        try {
          var yy = (y - moinsY) < 0 ? 0 : (y - moinsY); 
          div.scrollTop = yy  ;
        } catch(e){}
        try {
          var xx = (x - moinsX) < 0 ? 0 : (x - moinsX);
          div.scrollLeft = xx ;
        } catch(e){}
        
    }  
}


/**
 * Restore l'�tat des scrolls de l'ensemble des tableaux pr�sents dans la page.
 */       
function restoreTablesScolling() {       
  for (var i=0 ; i<tablesScrolling.length ; i++) {   
     var obj = document.getElementById(tablesScrolling[i][0]);
     if (obj != null) {
        try {
          obj.scrollTop = tablesScrolling[i][1];
        } catch(e){}
        try {
          obj.scrollLeft = tablesScrolling[i][2];
        } catch(e){}
     }  
  }
  tablesScrolling = new Array();
}

/**
 * Composant : saisieDate
 * formatage de la date au format jj/mm/aaaa
 * 160780 donne 16/07/1980
 * 16071980 donne 16/07/1980
 * /!\ pas de verification de date valide
 */
function saisieDate_formater(elem){
	var valeur = elem.value;
	
	// on ne modifie pas la valeur
	// si elle ne comporte pas le nombre 
	// de caracteres requis    
	
	// Test de la forme de la date
	if(  !valeur.match('[0-9]{6}')
    && !valeur.match('[0-9]{8}')
    && !valeur.match('[0-9]{2}/[0-9]{2}/[0-9]{2}')
    && !valeur.match('[0-9]{2}/[0-9]{2}/[0-9]{4}')
    ){    	 
	   return valeur;
  }
	
    // Suppression des slashes possibles
    var newValeur = '';
    for(var j=0;j<valeur.length;j++){
    	var car = valeur.charAt(j);    	 
    	if(car!='/'){         
           newValeur+=car;
         }
      }       	    	    	    	    
	
	var jour = newValeur.substr(0,2);
	var mois = newValeur.substr(2,2);
	var annee = newValeur.substr(4);

	
	// passage de l'annee en 4 caracteres
	if(annee.length==2){
		if(annee>30){
			annee = '19' + annee;
		} else {
			annee = '20' + annee;
		}
	}
	
	newValeur = jour + '/' + mois + '/' + annee;
	
	return newValeur;
}  

/**
 * Fonction qui verifie et empeche
 * de saisir autre chose qu'un entier
 */
function saisieEntier_verifier(event, elem, autoriserNegatif){
    var key = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
  	var valeur = elem.value;
  	
  	if (valeur != null && valeur != '') {
  		// Des valeurs ont d�j� �t� entr�es
  		// Saisie d'un moins impossible
  		if(key==45){ // -
			stopEvenenement(event);
			return;						
		}
  	}
  	
  	// Pour pallier a la mauvaise gestion des fleches directionnelles
  	// entre IE et FF. Pourait ne plus marcher dans le futur...
  	if ((key >= 37 && key <= 40) && (!event.keyCode || event.which == undefined)) {
		stopEvenenement(event);
	}
  	//Fin--
  	
  	//8 = retour,  9 = tab; 37 � 40 fleches directionnelles
	if ((key < 48 || key > 57) && (key!=8) && 
		(key!=9) && (key < 37 || key > 40) && (key!=13) && (key!=45)) {	
	  stopEvenenement(event);
	} else if (key==13) {
	    try {
	    	elem.onkeyup();
	    } catch (e) {
	    	if (isDebug) {
				alert("Erreur dans le onenter saisie entier : " + e.description);
			}
	    }
	} else if(key==45 && !autoriserNegatif){ // le -		
		stopEvenenement(event);		
	}
}

/**
 * limitation d'une zone de texte � une taille
 * max.
 */
function limitLengthZoneTexte(champ,maxlength){
	try{
		var valeur = $(champ).value;
		if(valeur.length>maxlength){
			var newValeur = 
			$(champ).value = valeur.substr(0,maxlength);
			
		}
	} catch(e){
	}
}


/**
 * Fonction qui verifie et emp�che
 * de saisir autre chose qu'un decimal
 * selon une pr�cision donn�e.
 * De plus on peut autoriser ou non l'utilisation
 * de nombres n�gatifs gr�ce au dernier argument de la 
 * fonction. 
 */
function saisieDouble_verifier(event,elem,precision, autoriserNegatif){		     
	var key = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;		 		 	
  	
  	var valeur = elem.value;
  	var firstPoint = false;
	var precisionMax = false;			
	var j = 0;
	var moins = false;

	//recherche de la pr�sence d'un point	
	if (valeur != null && valeur != '') {										
		for(var i=0;i<valeur.length;i++){					
			if (valeur.charAt(i) == '.' && !firstPoint){
		    	firstPoint = true;
			} else if (firstPoint && j<precision){		    		    
		    	j++;
			} else if(key==45){ // -
					stopEvenenement(event);
					return;						
			}
		 
			if (j>= precision) {
				precisionMax = true;
			}
		}	
	} else if (key==46) {
		//si rien n'a �t� tap� le '.' est interdit
		stopEvenenement(event);
		return;
	}
  	
  	//8 = retour,  9 = tab; 37 � 40 fleches directionnelles
  	// 46 = ., 13 = entr�e
	if ((key < 48 || key > 57) && (key!=8) && 
		(key!=9) && (key!=13) && (key < 37 || key > 40) && (key!=46) && (key!=45)) {	
	  stopEvenenement(event);
	} else if (key==13) {
	//test touche entrer
	    try {
	    	elem.onkeyup();
	    } catch (e) {
	    	if (isDebug) {
				alert("Erreur dans le onenter saisie double : " + e.description);
			}
	    }	    
	} else if (key==46) {
	//test touche .
		if (firstPoint) {
			stopEvenenement(event);		
		}		
	} else if (precisionMax) {
		//si la pr�cision voulu est atteinte on bloque presque tout
		if ((key!=8) && (key!=9) && (key < 37 || key > 40)) {
			stopEvenenement(event);		
		}
	} else if(key==45 && !autoriserNegatif){
		// Test -, autorise t-on les n�gatifs ?
		stopEvenenement(event);
	}
}

/**
 * Permet de bloquer l'evenement.
 */
function stopEvenenement(event) {
	if (window.event) {
	    event.returnValue = false;
		event.cancelBubble = true;			
	} else {
		event.preventDefault();
		event.stopPropagation();
	}
}


/**
 * Vérification d'un champ de type matricule.
 * pattern : [0-9A-Z.]{9}
 * @return le contenu du champ nettoyé (
 * sans les caractères interdits) 
 */
function saisieMatricule_verifier(elem){
	valeur = elem.value;
	
	newValeur="";
	
	for(var i=0;i<valeur.length;i++){
		car = valeur.charAt(i)
		if( estChiffre(car) || estLettre(car) || car=='.'){
			newValeur+=car;
		}			
	}
	
	return newValeur;
}

/**
 * Vérification d'un champ de type matriculeBSN.
 * pattern : [0-9]{10}
 * @return le contenu du champ nettoyé (
 * sans les caractères interdits)
 */
function saisieMatriculeBSN_verifier(elem){
	valeur = elem.value;
	
	newValeur="";
	
	for(var i=0;i<valeur.length;i++){
		car = valeur.charAt(i)
		if( estChiffre(car)){
			newValeur+=car;
		}			
	}	
	return newValeur;
}



/**
 * estChiffre verifie si le caractere
 * passe est un chiffre
 */
function estChiffre(car){
	return (car>='0' && car <='9');
}

/**
 * estNombre verifie si la chaine
 * passee est un nombre entier (sans virgules).
 */
function estNombre(chaine){	
	if(chaine.length==0){
		 return false;
	} else {
	 var ok = true;
	 for(var i=0;i<chaine.length && ok;i++){
	 	ok = estChiffre(chaine.charAt(i));
	 }
	 return ok;
	}
}

/**
 * estLettre verifie si le caractere
 * passe est une lettre de l'alphabet.
 */
function estLettre(car){
	car = car.toLowerCase();
	return (car>='a' && car <='z');
}

/**
 * Fonction d'affichage de sections
 */
function toggleSection(idref){
	if(!$(idref).style.display || $(idref).style.display=='block'){
		$(idref).style.display=='none';
	} else {	
		$(idref).style.display=='block';
	}
}

function showSection(idref){
	$(idref).style.display='block';
}

function hideSection(idref){
	$(idref).style.display='none';
}

/**
 * Fonction d'effacement du contenu d'une zone de formulaire
 */ 
function clear_form(form){
	var inputsElem = $(form).getElementsByTagName('input');
	var selectsElem = $(form).getElementsByTagName('select');
	
	// on vide champ à champ les inputs et les select
	if(inputsElem){
		for (var i = 0; i < inputsElem.length; i++) {
		// Que faire lorsque l'on 'efface' un élément ?
		// Logiquement, on devrait replacer la valeur d'origine
		// Mais comment la connaitre ? pour l'instant on vide tout.
			switch (inputsElem[i].type.toLowerCase()) {
			  case 'submit':
			  case 'hidden':
			  case 'text':
			    Field.clear(inputsElem[i]);
			    break;
			  case 'checkbox':
			  case 'radio':
				inputsElem[i].checked = false;
			    break;
			}
	    }
    }
    
    if(selectsElem){
	    for (var i = 0; i < selectsElem.length; i++) {
	      selectsElem[i].value = '';
	    }
    }
}

/**
 * Effacement d'une liste de zones de formulaires
 */
function clear_forms(forms){
	if(!forms){
		return;
	}
	
	// forms est une suite de nom de form séparés par des ,
	// on enlève tous les espaces
	forms = forms.replace(/\s+/, '');
	
	// on crée un tableau à partir de cette chaine
	// séparateur : ','
	var aforms = forms.split(',');
	
	var chRetour='';
	for (var i = 0, len = aforms.length; i < len; i++) {
	  // effacement des champs du formulaire courant
      clear_form(aforms[i]);
    }	
}


/**
 * Observeur pour la synchronisation d'une table
 * scrollable entre son entete et son corps
 * @param id, l'identifiant de la table fournie pour le composant
 * @param preserveScrollState, doit on pr�server le scrolling
 * apr�s rafra�chissement du tableau.
 */
function synchroniserTable(id,preserveScrollState) {
	var entete = $('thead_'+id);
	var corps = $('tbody_'+id);
	var preserveHHidden = $('tbScrollHPos_'+id);
	var preserveWHidden = $('tbScrollWPos_'+id);
	Event.observe(corps,'scroll',
	    function(event) {
	      entete.scrollLeft = corps.scrollLeft;
	      if(preserveScrollState){
		      try{
		      	preserveHHidden.value = corps.scrollTop;
		      	preserveWHidden.value = corps.scrollLeft;
		      } catch(e){}
	      }
	    }
	);
}

/**
 * Deplacement de la scrollbar verticale d'un table
 * � la position voulue.
 */
function putScrollForTable(id){
	var corps = $('tbody_'+id);
	var preserveHHidden = $('tbScrollHPos_'+id);
	var preserveWHidden = $('tbScrollWPos_'+id);	
	try{
		corps.scrollTop = preserveHHidden.value;
		corps.scrollLeft = preserveWHidden.value;
	} catch(e){}	
}



/**
 * Gestion de la touche entrée sur un
 * champ de type input à l'aide d'un 
 * commandLink dans la page (généralement action
 * de la barre d'action) dont on passe l'id.
 */
function enterKeySubmit(idAction, event){
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	// cas de la touche entr?e
	if (keyCode == 13) {							
		// tout s'est bien passe
		// declenchement du bouton de la barre d'action
		$(idAction).click();
	}
	return true;
}





/**
 * Est ce que la touche tapée est key ?
 * Par défaut key = 13 (touche entrée).
 */
function checkReturnKey(event,key){
	var keyPressed = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	var testKey = key;
	if(testKey==null){
		testKey = 13;
	}
	
	if (keyPressed != testKey) {							
		return false;
	}
	return true;
}


/**
 * onSelected utilisé par le calendrier.
 */
// This function gets called when the end-user clicks on some date.
function selected(cal, date) {
  cal.sel.value = date; // just update the date in the input field.
  if (cal.dateClicked){
  	cal.callCloseHandler();
  }
 // cal.blur();
}

/**
 * onCloseHandler utilise par le calendrier.
 */
// And this gets called when the end-user clicks on the _selected_ date,
// or clicks on the "Close" button.  It just hides the calendar without
// destroying it.
function closeHandler(cal) {
  // hide the calendar
  cal.hide();
  try {
  	//r�cup�re l'identifiant du saisieTexte
  	var idSource = cal.idSource;
  	//r�cup�ration de l'objet
  	var obj = document.getElementById(idSource);
  	//si une valeur est pr�sente
  	if ((obj.value != null) || (obj.value != '')) {
  		//simule que la touche entr�e a �t� press�e
  		var evenement = new Object();
  		//ie
  		if (window.event) {
  		  window.event.keyCode = 13;
  		  obj.onkeyup();
  		} else {
  		 //autres navigateurs
  		  evenement.charCode = 13;
  	   	  obj.onkeyup(evenement); 
  	    }
    }
  } catch(e) {
  	if (isDebug) {
    	alert("erreur calendrier : "+ e.description);
    }
  }
//  cal.destroy();   
  _dynarch_popupCalendar = null;
}

/**
 * Demande d'affichage d'un calendrier.
 */
function showCalendar(id, format, showsTime, showsOtherMonths,x,y) {
  var el = $(id);
  var elButton = $('btdt_'+id); // Bouton associé à la date
  if (_dynarch_popupCalendar != null) {
    // we already have some calendar created
    _dynarch_popupCalendar.hide();                 // so we hide it first.
  } else {
    // first-time call, create the calendar.
    var cal = new Calendar(1, null, selected, closeHandler);
    cal.idSource = id;
    
    // uncomment the following line to hide the week numbers
    // cal.weekNumbers = false;
    if (typeof showsTime == "string") {
      cal.showsTime = true;
      cal.time24 = (showsTime == "24");
    }
    if (showsOtherMonths) {
      cal.showsOtherMonths = true;
    }
    _dynarch_popupCalendar = cal;                  // remember it in the global var
    cal.setRange(1900, 2070);        // min/max year allowed.
    cal.create();
  }
  _dynarch_popupCalendar.setDateFormat(format);    // set the specified date format
  _dynarch_popupCalendar.parseDate(el.value);      // try to parse the text in field
  _dynarch_popupCalendar.sel = el;                 // inform it what input field we use

  // the reference element that we pass to showAtElement is the button that
  // triggers the calendar.  In this example we align the calendar bottom-right
  // to the button.
  _dynarch_popupCalendar.showAtElement(elButton, "Br");        // show the calendar
	//_dynarch_popupCalendar.showAt(x, y + el.offsetHeight);
  return false;
}


/**
 * Fonction javascript utilisée pour palier au bug
 * des onglets tomahawk lorsqu'ils sont gérés côté client.
 * En effet, ils ne sont pas synchronisés côté serveur,
 * et on a beau spécifier l'index que l'on souhaite afficher,
 * rien ne se passe.
 */
function showPanelTab(idOnglet){
 	// Attention !! très spécifique à l'immplémentation fournie
 	// par tomahawk...
 	// on simule le click sur l'onglet que l'on veut afficher
 	// pour que le raffraichissement soit correct
	Element.down('formulaire_principal:'+idOnglet+'_headerCell', '', 0).click();
}

/**
 * Fonction utilisée dans tous les liens et boutons
 * de l'appli qui réinitialise le target du formulaire
 * principal.
 * Le bouton d'édition modifie le target, ce qui 
 * a pour effet de lancer ensuite toute 
 * action/lien dans une autre fenêtre également, ce que 
 * l'on ne veut pas.
 */
function resetFormTarget(){
    try {
	   var objFormPrincipal = document.forms[nomFormPrincipal];
	   if (objFormPrincipal != null) objFormPrincipal.target = '';	   
	} catch(e) {}
}

/**
 * Fontion permettant de mettre en evidence les messages 
 * du contneur de messages.
 */
function startChangeCouleurConteneur() { 
	 //highlight = new Effect.Highlight('zoneDeMessages', 
		//	{duration:1.0, fps:25, from:0.0, to:0.8, endcolor:'#C3C8D5'});
	
	//, startcolor:'#80a0ff', endcolor:'#a00000', restorecolor:'#80a0ff'
}
 

/**
 * Fonction permettant de transf�rer le focus � la source ou � la destination choisie.
 * Si le container de messages ne poss�de pas de messages bloquants, le focus est transf�r� � la destination choisie.
 * Si non, le focus reste sur la source indiqu�e.
 * Le contr�le sur le container de message peut �tre omis, si la source est vide.
 */
function transfertFocus(source, destination, hasErrors) {
	if (hasErrors && source != '') {
		logFormat("changeFocus source {0} ", source);
		changeFocus(source);
	} else {		  
		logFormat("changeFocus dest {0} ", destination);
		setTimeout("changeFocus('" + destination +"');",100); 
	}
}

/**
 * Fonction permettant de transf�rer le focus sur un champ.
 */
function changeFocus(id) {
	try {
		var jqObj = jQuery('[id="' + id + '"]')
		console.log(jqObj.length);
		//var obj = document.getElementById(id);
		console.log(!jqObj.is(":disabled"));
		
		if(jqObj.length > 0 && !jqObj.is(":disabled")) {
			//obj.focus(); 
			
			logFormat("boh changefocus {0}", jqObj.val());
			var value = jqObj.val();
			jqObj.focus();
			jqObj.val('');
			jqObj.val(value);
			
		}
	
	} catch(e) {
		if (isDebug) {
			alert("Erreur dans le transfert de focus : " + e.description);
		}
	}	
}
/**
 * Fonction qui positionne le focus selon un �l�ment du formulaire.
 */ 
 function positionnerFocus(id) {
 	if (id != '') {
 		changeFocus(id);
 	}
 }
 
 /**
  * Empecher le bouton droit de la souris.
  */
 window.oncontextmenu = function (){return false;}
 
 /**
 * Lancement d'un lien ajax existant
 * dans la page. L'attribut params permet
 * de passer des valeurs qui seront positionnÃ©s dans les 
 * champs de saisie correspondant avant appel du lien d'action.
 * Ex params = {'lig':'valLig', 'col':'valCol', ...}
 * NB : les champs de saisie (pouvant être des champs cachés)
 * doivent être déclarés.
 */
function lancerLienAjax(nomLien, params){		
	try{
	logFormat('lancerLienAjax id {0}', nomLien);
	for( var i in params){
		var paramElem = $('#' + i);
		console.log("Param: " + i );
		console.log("Valeur:" + params[i]);
		paramElem.val(params[i]);
	}
	
	var lien = jQuery('#formulaire_principal\\:' + nomLien);
	logFormat("Simule une clique sur le lien : {0}.  Num liens {1}", lien.attr("id"), lien.length);
	lien.click();
	} catch(e){
		console.log("Exception!!" + e);
	}
}

 /**
  * Lancement d'un lien ajax existant a partir de son selecteur jQuery
  * dans la page. L'attribut params permet
  * de passer des valeurs qui seront positionnÃ©s dans les 
  * champs de saisie correspondant avant appel du lien d'action.
  * Ex params = {'lig':'valLig', 'col':'valCol', ...}
  * NB : les champs de saisie (pouvant être des champs cachés)
  * doivent être déclarés.
  */
 function lancerLienSelecteurAjax(selecteur, params){       
     try{
     for( var i in params){
         var paramElem = jQuery(i);
         if (paramElem != null) {
             paramElem.val(params[i]);
         }
     }
     var lien = jQuery(selecteur);
     lien.click();
     } catch(e){
         console.log("Exception!!" + e);
     }
 }
/**
 * Fonction utilisee par le composant saisieDeuxOptionsExclu de maniere 
 * a ne laisser qu'un choix possible parmi les deux checkbox
 * ou aucun choix.
 */
function checkExclu(champ, gaucheOuDroite, valeurGauche, valeurDroite, valeurAucune){
	try{
		var option1 = champ + '_chGauche';
		var option2 = champ + '_chDroite';
		
		$(champ).value=valeurAucune;
		if(gaucheOuDroite){
			$(option2).checked = false;
			if($(option1).checked){
				$(champ).value=valeurGauche;
			}
		} else {
			$(option1).checked = false;
			if($(option2).checked){
				$(champ).value=valeurDroite;
			}
		}
		$(champ).click();	
	} catch(e){
	}
}

/**
 * Initialisation d'un champ de type saisieDeuxOptionsExclu.
 */
 function fillCheckExclu(champ,valeurGauche, valeurDroite){
	try{
		switch($(champ).value){
	   		case valeurGauche:
	   			$(champ+'_chGauche').checked=true;
	   			$(champ+'_chDroite').checked=false;
	   		break;
	   		case valeurDroite:
	   			$(champ+'_chDroite').checked=true;
	   			$(champ+'_chGauche').checked=false;
	   		break;
	   		default:
		   		$(champ+'_chGauche').checked=false;
		   		$(champ+'_chDroite').checked=false;	   			
	   		break;
	    }
	} catch(e){
	};
}

function surbrillanceLigne(id, vraiOuFauxDeselection) {
	if (idCliqueCelPrecedent != null) {
		var objSprecedent = document.getElementById(idCliqueCelPrecedent)
		if (objSprecedent != null) objSprecedent.style.backgroundColor = '#ffffff';  
	}
	
	if (id != null) {
		var color;
		if (vraiOuFauxDeselection && id == idCliqueCelPrecedent) {
			color = '#ffffff';
			idCliqueCelPrecedent = null;
		} else {
			color = 'yellow';
			idCliqueCelPrecedent = id;
		}
		
		var objS = document.getElementById(id);
		
		if (objS != null) {			
			objS.style.backgroundColor = color;  
		} else {
			idCliqueCelPrecedent = null;
		}
	}
}

function surbrillanceLigneMemory(id, vraiOuFauxDeselection) {
	if (idCliqueCelPrecedent != null) {
		var objSprecedent = document.getElementById(idCliqueCelPrecedent)
		if (objSprecedent != null) objSprecedent.style.backgroundColor = clazzCliqueCelPrecedent;  
	}
	
	if (id != null) {
		var color;
		if (vraiOuFauxDeselection && id == idCliqueCelPrecedent) {
			color = clazzCliqueCelPrecedent;
			idCliqueCelPrecedent = null;
			clazzCliqueCelPrecedent = null;
		} else {
			color = 'yellow';
			idCliqueCelPrecedent = id;			 
		}
		
		var objS = document.getElementById(id);
		
		if (objS != null) {			
			clazzCliqueCelPrecedent = objS.style.backgroundColor;
			objS.style.backgroundColor = color;  
		} else {
			idCliqueCelPrecedent = null;
			clazzCliqueCelPrecedent = null;
		}
	}
} 


function seDeplacerSurElement(classNom, identifiant) {
	if (identifiant == null) {
		var elements = document.getElementsByClassName(classNom);
		for (var i =0;i<elements.length;i++) {
	    	elements[i].scrollIntoView();
	   		break;
    	}		
	} else {
	   var obj = document.getElementById(identifiant)
	   if (obj != null) {
	     obj.scrollIntoView();
	   }
	}
}

/**
 * le champ de saisie a t-il une valeur vide ?
 */
function isChampVide(champ){
	var ch = $(champ);
	if(!ch){
		return true;
	}
	return (ch.value.replace(/\s+/, '')=='');
}		

/**
 * Recherche du premier élément commencant par 
 * premiersCars dans la page
 * qui possède un identifiant commençant par prefixIdElements
 * et dont la classe de style vaut classElements.
 * Positionnement sur cet élement s'il existe.
 * NB : permet de se déplacer sur la ppre
 */
function rechercherPremierElementWith(premiersCars, prefixeIdElements, classElements){
	if(!premiersCars ||!prefixeIdElements || !classElements){return false;}			
	var lngPrefixe= prefixeIdElements.length;
	try{
		var caracs = premiersCars.toUpperCase();
		var elementsWithRef = document.getElementsByClassName(classElements);
		if(elementsWithRef){
			for(var i=0 ; i<elementsWithRef.length; i++){						
				if(elementsWithRef[i].id.substr(lngPrefixe,caracs.length)==caracs){										
					elementsWithRef[i].scrollIntoView();					
					break;
				}
			}
		}
	} catch(e){
		alert('rechercherPremierElementWith: erreur inconnue');
		return false;
	}			
	return true;
}

/**
 * Recherche du premier élément contenant cars
 * dans la page
 * qui possède un identifiant commençant par prefixIdElements
 * et dont la classe de style vaut classElements.
 * Positionnement sur cet élement s'il existe.
 * NB : permet de se déplacer sur la ppre
 */
function rechercherPremierElementContains(cars, prefixeIdElements, classElements){
	if(!cars ||!prefixeIdElements || !classElements){return false;}			
	var lngPrefixe= prefixeIdElements.length;
	try{
		var caracs = cars.toUpperCase();
		var elementsWithRef = document.getElementsByClassName(classElements);
		if(elementsWithRef){
			for(var i=0 ; i<elementsWithRef.length; i++){				
				if(elementsWithRef[i].id.indexOf(caracs)>=0){							
					elementsWithRef[i].scrollIntoView();					
					break;
				}
			}
		}
	} catch(e){
		alert('rechercherPremierElementContains: erreur inconnue');
		return false;
	}			
	return true;
}

function getInteger(champ){	
  var ch = $(champ).value; 
  var valeur = 0;
	if(ch!=''){   
      if(ch.charAt(0)=='+'){	        
		    valeur = parseInt(ch.substr(1,ch.length));		    
      } else {	
        valeur = parseInt(ch.substr(0,ch.length));
      }
		}
		return valeur;
  }

/**
 * Fonction de d�crementation pour le composant
 * saisieBornee.
 */
function decrementerSaisieBornee(idChamp, borneMin){
   var valeur = getInteger(idChamp);   
	if(valeur-1 >= borneMin){
     valeur=valeur-1;
     if(valeur>0){
       return '+' + valeur;
     }	else {
       return valeur;
     }
   }
	return valeur;
}


/**
 * Fonction d'incr�mentation pour le composant
 * saisieBornee.
 */	
function incrementerSaisieBornee(idChamp, borneMax){
   var valeur = getInteger(idChamp);   
   valeur++;
   if(valeur>0){
       return '+' + valeur;
   }	else {
     return valeur;
   }  
}

/**
 * Fonction permettant de v�rouiller un champ de saisie, tout en permettant
 * l'utilisation des fleches directionnelles du clavier pour se d�placer.
 */
function bloquerSaisie(event){
    var keyPressed = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
    if (keyPressed == Event.KEY_LEFT || 
        keyPressed == Event.KEY_UP || 
        keyPressed == Event.KEY_RIGHT || 
        keyPressed == Event.KEY_DOWN) {                         
        return false;
    }
    return true;
}



/**
 * Demande de positionnement du focus sur un élément de la page.
 * Le focus ne sera effectif que lorsque la méhode positionnerFocus
 * sera lancée.
 */
function setFocusOnElement(id){	
	focusOnId = id;
}

/**
 * Positionnement du focus dans la page.
 */
function validerSetFocusOnElement(){
	console.log("validerSetFocusOnElement");
	if(focusOnId!='' && focusOnId!=null){
		try{
			$(focusOnId).focus();
		} catch(e){
			if(isDebug){
				alert('erreur sur '+ focusOnId);
			}
		};
	}
}

/**
 * Fonction executée lorsque la page est totalement
 * chargée.
 */	

       
         
function gestionChangementValeurOnBlurEntree(valeurInitiale){
   oldValeurSaisie = valeurInitiale; 
}
          
function gestionChangementValeurOnBlurSortie(newValeur){
    
	if(oldValeurSaisie == newValeur){
	   return false;
	}
	return true;
}

//correction du bogue du double click.       

function desactiveClick(id , valeur) {
    setTimeout("retoreOnclick('" + id +"', " + valeur + ");",500); 
    $(id).click = '';
}                             

function retoreOnclick(id, valeur) {
    if (id != null) {
        var object = $(id);
        if (object != null) {
            object.onclick = valeur;
        }
    }
}

//fin correction du bogue du double click.  
  



/**
 * Methodes de gestion des couleurs en JS
 */
function HLS(h,l,s){this.h=h;this.l=l;this.s=s;this.toRGB=HLStoRGB;}
function RGB(r,g,b){this.r=bound(r);this.g=bound(g);this.b=bound(b);this.toHLS=RGBtoHLS;}
function bound(v) {return Math.min(1.0,Math.max(0.0,v));}
function getpartrgb(x,y,h){
    if (h>=1.0) h-=1.0;if (h<0.0) h +=1.0;if (h<(1/6)) return (x+((y-x)*h*6.0));
    if (h<0.5) return y;if (h<(2/3)) return (x+((y-x)*(4.0-(h*6.0))));
    return x;}
function HLStoRGB(){var rgb=new RGB(0,0,0);if (this.s==0) rgb.r=rgb.g=rgb.b=this.l;
    else if (this.l==0) rgb.r=rgb.g=rgb.b=0;
    else {var x,y;if (this.l<=0.5) y=this.l*(1.0+this.s);
        else y=this.l+this.s-(this.l*this.s);
        x=(2.0*this.l)-y;rgb.r=getpartrgb(x,y,this.h+1/3);
        rgb.g=getpartrgb(x,y,this.h);rgb.b=getpartrgb(x,y,this.h-1/3);
        rgb.r=bound(rgb.r);rgb.g=bound(rgb.g);rgb.b=bound(rgb.b);}
    return rgb;}
function RGBtoHLS(){var hls=new HLS(0,0,0);
    var mmax=Math.max(this.r,Math.max(this.g,this.b));
    var mmin=Math.min(this.r,Math.min(this.g,this.b));
    var mdif=mmax-mmin;msom=mmin-(-mmax);
    hls.l=msom/2.0;if (mdif < 0.00001)  {hls.h=0;hls.s=0;}
    else {if (hls.l<0.5) hls.s=mdif/msom;
        else hls.s=mdif/(2-msom);   mdif*=6.0;
        if ((this.r>=this.g) && (this.r>=this.b))
            hls.h=(this.g-this.b)/mdif;
        else    if (this.g >= this.b) hls.h=1/3+(this.b-this.r)/mdif;
                else hls.h=2/3+(this.r-this.g)/mdif;
        if (hls.h<0.0) hls.h+=1.0;else if (hls.h>=1.0) hls.h-=1.0;}
    hls.h=bound(hls.h);hls.l=bound(hls.l);hls.s=bound(hls.s);return hls;
}
function RGBfromClr(s){if (s.substr(0,1)!='#') return null;
    return new RGB(parseInt(s.substr(1,2),16)/255,parseInt(s.substr(3,2),16)/255,parseInt(s.substr(5,2),16)/255)}
function CfromRGB(c){return '#'+Hex(c.r*255)+Hex(c.g*255)+Hex(c.b*255);}
function Hex(n){var u=Math.round(n);if (u<0) u=0;if (255<u) u=255;return  Str(u>>4)+Str(u%16)}
function Str(n) {var r;if (n<10) r=n.toString();else r=String.fromCharCode(87+n);return r;}

function eclaircirCouleur(c, intensite) {
    var rgb = RGBfromClr(c);
    var hls = rgb.RGBtoHLS();
    hls.l += intensite;
    if (hls.l > 255) hls.l = 255;
    if (hls.l < 0) hls.l = 0;
    
    rgb = hls.HLStoRGB();
    return CfromRGB(rgb);
}

function filterListeGeneriqueAffichage(idTable,indiceColonne,filtre){
    var table = document.getElementById(idTable + ':tb');
    if (table.hasChildNodes()){
        var rows = table.childNodes;
        var nbRows = rows.length;
        var numLigneVisible = 0;
        
        for(var i = 0; i < nbRows ; i++){
            var row = rows[i];
            var colonneValue = row.childNodes[indiceColonne].innerHTML;
            
            var pos=colonneValue.toLowerCase().indexOf(filtre.toLowerCase());
            if (pos >= 0){
                // matche le filtre : on le garde
                row.style.display = "";
                var nomClasse = row.className;
                
                if (numLigneVisible % 2 == 0){
                    //Ligne pair
                    row.className  = nomClasse.replace(" impair"," pair");
                } else {
                    //Ligne impair        
                    row.className    = nomClasse.replace(" pair"," impair");
                }
                
                numLigneVisible = numLigneVisible + 1;
            } else {
                // ne matche pas le filtre : on le cache
                row.style.display = "none";
            }
            //alert(groupe + " " + pos);
        } 
    }
    
}

function onChange_HeureMinute(event) {
	//Mettre valeur dans richfaces selValue element afin de syncroniser la valeur affichée et la valeur stockée.
	var id = event.target.id ;
	id = '#' + id.replace(/Input$/, "").replace(/:/g, "\\:").replace(/_/g, "_") + "selValue";
	logFormat("onKeyPress_HeureMinute on change value : {0}  id : {1}", event.target.value, id);
		
	
	jQuery(id).val(event.target.value);
	
	logFormat("Sel heure / minute.  Valeur stockée {0}", jQuery(id).val());
}

function bindResizeFunction(func) {
  //Initialise une fois
	func();
	
	jQuery(window).resize(function(event) {
		
		//console.log(event);
		
  	//resize est déclenché sur des éléments resizable aussi : http://bugs.jqueryui.com/ticket/7514		
		//Dans IE c'est plutôt le document qui est dans event.target
		if (jQuery.browser.msie && event.target != window && event.target != document) {
			//Faire rien si c'est pas le window
			return true;
		}
		
  	if (!jQuery.browser.msie && event.target != window) {
			//Faire rien si c'est pas le window  		
  		return true;
  	}
  	//console.log("risize window EVENT");
  	
  	return func();
	});
}


/**
* Bascule l'affichage du composant Rich:editor en composant html avec le rendu graphique des balise graph-math.
* @param lienObjId : id du lien sur lequel s'exéctute le code. 
* @param subIdSaisieEditeur suffixe de l'id du composant rich editor (cr:saisieEditeur) 
* @param subIdOutputText suffixe de l'id du composant outputText html 
* @param libelleDescription libellé de l'info bulle affichée sur le bouton de bascule
*/
function visualiserLatex(
       lienObjId,
       subIdSaisieEditeur,
       subIdOutputText,
       libelleDescription
) {
   
   // Recupere les objets
   var reg=new RegExp(":", "g");
   var escapedLienObjId = lienObjId.replace(reg, '\\:');
   var lien = jQuery('#' + escapedLienObjId);
   var parent = lien.closest('table');
   var saisieEditor = parent.find('[id$="' + subIdSaisieEditeur + '"]');
   var latexImage = parent.find('[id$="' + subIdOutputText + '"]');
   var icone = jQuery("img", lien);
   var isModeEdition;
   
   // Bascule l'affichage du cr:saisieEditeur / h:outputText 
   saisieEditor.toggle();
   if (saisieEditor.is(":visible")) {
       latexImage.hide();
       isModeEdition = false;
   } else {
       latexImage.show();
       isModeEdition = true;
   }
   
   // Bascule l'icone et texte info bulle
   if (icone!=null && icone[0]!= null) {
       var motif;
       var repla;
       if (isModeEdition) {
           motif = "aide-saisie.png";
           repla = "modifier.gif";
           icone[0].title="Modifier " + libelleDescription;
       } else {
           motif = "modifier.gif";
           repla = "aide-saisie.png";
           icone[0].title="Visualiser " + libelleDescription;
       }
       var reg=new RegExp("(" + motif + ")", "g");
       var srcOrigine = icone[0].src;
       var scrNouveau = srcOrigine.replace(reg,repla);
       icone[0].src = scrNouveau;
   }

}

/** 
 * Fonction declenchée suite à une sélection d'un fichier. 
 * Ferme la popup de sélection de fichier puis 
 * Déclenche le click sur le idlien. */ 

function lienAfterSelectFile(idpopup, idlien) { 
    var popup = RichFaces.$(idpopup); 
    var lien = jQuery(idlien);
    popup.hide();
    if (lien != null) {
        lien.click();
    } 
}
