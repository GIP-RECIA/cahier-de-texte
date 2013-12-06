// Cette variable 'globale' indique si une modification sur la page courante est en cours ou non
// Lors d'un changement de page ou onglet, cette variable détermine si la popup de confirmation 
// de sauvegarde doit être affichée ou non.
var modificationEnCours = false;
var idActionEnCours = "";

jQuery(document).ready(function () {
    setModificationFormulaire(false)
    appliquerGestionModification();
});


/**
 * Mutateur de la la variable de modification en cours modificationEnCours
 * @param value
 */
function setModificationFormulaire(value) {
    modificationEnCours = value;
    console.log("setModificationFormulaire = " + modificationEnCours);    
}

/**
* Mutateur de la la variable de modification en cours modificationEnCours
* @param value
*/
function getModificationFormulaire() {
   return modificationEnCours;    
}

function handleNavigationPage() {
	console.log("Gestion de la sous navigation - Modification en cours = " + modificationEnCours);   
  
  var idNavigation = idActionEnCours;

  // Lecture de l'id du lien de navigation (sous element de type span du lien sur lequel le click est déclenché       
  console.log("idNavigation = " + idNavigation);
  
  // Si une modification est en cours, on affiche la popup de confirmation
  // en positionnant dans le span unique contenu dans cette popup avec l'id du lien de navigation
  // Return false pour annuler l'événement on click
  if (modificationEnCours) {
      var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
      spanLienNavigation.attr('title', idNavigation);
      
      var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
      spanOngletNavigation.attr('title', null);
      
      var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
      spanRFNavigation.attr('title', null);
      
      var spanMenuNavigation = $('#idPopupNavigationMenu');
      spanMenuNavigation.attr('title', 'menu');
      
      console.log("Affectation au span du lien de navigation : " + idNavigation);
      RichFaces.$('formulaire_principal:idPopupConfirmSauvegardeModification').show();
      return false;
  }
  
  // Sans modification en cours, on déclenche l'appel à la méthode :
  // <a4j:jsFunction name="callAction_idMenuAction#{node.action}" action="#{node.actionArbre}"/> 
  // Cette callAction est positionne sous le menuItem pour pallier à un bug RF
  console.log("Debut appel navigation");
  var callMethode = "callAction_" + idNavigation + "()";
  console.log("fonction = " + callMethode);
  eval(callMethode);
}

function appliquerGestionModification() {
	console.log("appliquerGestionModification");
    // Les champs de formulaire nécessitant un check pour sauvegarde avant quitter la page qui code l'événement onchange
    // doivent être positionné avec le styleClass (ou class) checkSauvegarde
    // Si l'evenement est deja gere par le formulaire ce un champ, il faut coder explicitement l'appel à setModificationFormulaire
    jQuery('.checkSauvegarde').change(function(){
        setModificationFormulaire(true);
    });
    
    //Correction IE -- http://stackoverflow.com/questions/672228/ie-6-ie-7-z-index-problem/3998006#3998006
    if (jQuery.browser.msie) {
        jQuery('.navigationPage').parentsUntil("form").addClass('on-top');
    }

    // La class navigationPage est positionnée sur les liens qui déclenche une navigation vers une autres page
    // (ex: boutons du menu principal) 
    // En cliquant sur un lien de cette class, le mecanisme de check sauvegarde est effectué
    jQuery('.navigationPage').click(function() {
    	handleNavigationPage(); 
    	});
    
    
}

/**
 * Cette fonction est appelée avant un changement d'onglet. 
 * Son appel est implémenté automatiquement dans le composant tabPanelChackSauvegarde (surcharge le composant natif tabPanel).   
 * @param event : description de l'événement. 
 * ATTENTION : risque potentiel de régression : la valeur de l'onglet cible est récupéré dans la propriété "event.rf.data.newItem.name" 
 * Aucune documentation RF n'a permis d'identifier une méthode permettant d'accéder autrement à cette valeur.
 */
function beforeChangeOnglet(event) {
    console.log(event);
    console.log('RF DATA = ' + event.rf.data.newItem.name);
    //console.log('ANCIEN Onglet = ' + rf.data.oldItem);
    
    //console.log('NOUVEAU Onglet = ' + event.newItem);
    if (modificationEnCours)  {
    
        // ATTENTION : Pour récupérer l'onglet cible : potentiel problème si RichFaces evolue
        console.log(event);
        var idNavigation = event.rf.data.newItem.name;
        console.log('NEW ONGLET = ' + event.rf.data.newItem.name);
        var rfTabPanel = RichFaces.$(this);
        
        // Si modification, on recupere le lien de la popup
        var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
        spanLienNavigation.attr('title', null);
        
        var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
        spanOngletNavigation.attr('title', idNavigation);
        
        var spanMenuNavigation = $('#idPopupNavigationMenu');
        spanMenuNavigation.attr('title', null);
        
        var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
        spanRFNavigation.attr('title', event.rf.data.id);
        
        // Positionne dans le span le lien a l'origine de l'affichage de la popup
        spanLienNavigation.click(function(){
            var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
            console.log('spanOngletNavigation = ' + spanOngletNavigation);
            var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
            console.log('spanRFNavigation = ' + spanRFNavigation);
            var idRFNavigation = spanRFNavigation.attr('title');
            console.log('idRFNavigation = ' + idRFNavigation);
            var ongletNavigation = spanOngletNavigation.attr('title');
            console.log('ongletNavigation = ' + ongletNavigation);
            var rfTabPanel = RichFaces.$(idRFNavigation);
            console.log('rfTabPanel = ' + rfTabPanel);
            rfTabPanel.switchToItem(ongletNavigation);
        });
        
        // Affiche la popup et retourne false pour rester sur la page
        console.log("Affectation au span du lien de navigation : " + idNavigation);
        RichFaces.$('formulaire_principal:idPopupConfirmSauvegardeModification').show();    
        return false;
    } else {
        console.log('beforeChangeOnglet sans modif en cours'); 
        return true;
    }   
}

function str_replace (search, replace, subject, count) {
    // Replaces all occurrences of search in haystack with replace  
    // 
    // version: 1109.2015
    // discuss at: http://phpjs.org/functions/str_replace
    // +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   improved by: Gabriel Paderni
    // +   improved by: Philip Peterson
    // +   improved by: Simon Willison (http://simonwillison.net)
    // +    revised by: Jonas Raoni Soares Silva (http://www.jsfromhell.com)
    // +   bugfixed by: Anton Ongson
    // +      input by: Onno Marsman
    // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +    tweaked by: Onno Marsman
    // +      input by: Brett Zamir (http://brett-zamir.me)
    // +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   input by: Oleg Eremeev
    // +   improved by: Brett Zamir (http://brett-zamir.me)
    // +   bugfixed by: Oleg Eremeev
    // %          note 1: The count parameter must be passed as a string in order
    // %          note 1:  to find a global variable in which the result will be given
    // *     example 1: str_replace(' ', '.', 'Kevin van Zonneveld');
    // *     returns 1: 'Kevin.van.Zonneveld'
    // *     example 2: str_replace(['{name}', 'l'], ['hello', 'm'], '{name}, lars');
    // *     returns 2: 'hemmo, mars'
    var i = 0,
        j = 0,
        temp = '',
        repl = '',
        sl = 0,
        fl = 0,
        f = [].concat(search),
        r = [].concat(replace),
        s = subject,
        ra = Object.prototype.toString.call(r) === '[object Array]',
        sa = Object.prototype.toString.call(s) === '[object Array]';
    s = [].concat(s);
    if (count) {
        this.window[count] = 0;
    }
 
    for (i = 0, sl = s.length; i < sl; i++) {
        if (s[i] === '') {
            continue;
        }
        for (j = 0, fl = f.length; j < fl; j++) {
            temp = s[i] + '';
            repl = ra ? (r[j] !== undefined ? r[j] : '') : r[0];
            s[i] = (temp).split(f[j]).join(repl);
            if (count && s[i] !== temp) {
                this.window[count] += (temp.length - s[i].length) / f[j].length;
            }
        }
    }
    return sa ? s : s[0];
}

/**
 * Cette méthode est appelé dans la popup de confirmation de sauvegarde suite à une sauvegarde réussie 
 * ou suite à un refus de sauvegarde (bouton non).
 * Ferme la popup, puis déclenche le click sur le lien de navigation de la page / onglet initialement valorisé lors de 
 * l'affichage de la popup. 
 */
function naviguerSuivant() {
    console.log("DEBUT naviguerSuivant");
    setModificationFormulaire(false);
    RichFaces.$('formulaire_principal:idPopupConfirmSauvegardeModification').hide();
    var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
    var idLienNavigation = spanLienNavigation.attr('title');
    
    //Naviation vers une autre page
    if (idLienNavigation != null) {
         var spanActionMenu = $('#idPopupNavigationMenu').attr('title');
         
         if (spanActionMenu != null) {
  			console.log("lien de navigation = " + idLienNavigation);
        
        	var callMethode = "callAction_" + idLienNavigation + "()";
        	console.log("fonction = " + callMethode);
        	eval(callMethode);
        } else {
        	idLienNavigation = str_replace(':', '\\:', idLienNavigation);
        	console.log("lien de navigation = " + idLienNavigation);
        	$('#' + idLienNavigation).click();
        }
    // Navigation vers un autre onglet
    } else {
    	setTimeout('naviguerVersOnglet();',1);
        //spanLienNavigation.click();
    }
    
}
 
 
 function naviguerVersOnglet() {
	 $('#idLienNavigationPopupConfirmSauvegardeModification').click();
 }
 
 function checkSaveBarreSemaine(idNavigation, numeroSemaine) {

	 console.log('idNavigation = ' + idNavigation);

	 // Si une modification est en cours, on affiche la popup de confirmation
	 // en positionnant dans le span unique contenu dans cette popup avec l'id du lien de navigation
	 // Return false pour annuler l'événement on click
	 if (modificationEnCours) {
		 console.log('checkSaveBarreSemaine = ' + idNavigation);
		 var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
		 spanLienNavigation.attr('title', idNavigation);

		 var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
		 spanOngletNavigation.attr('title', null);

		 var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
		 spanRFNavigation.attr('title', null);

		 var spanMenuNavigation = $('#idPopupNavigationMenu');
		 spanMenuNavigation.attr('title', null);

		 console.log("Affectation au span du lien de navigation : " + idNavigation);
		 lancerLienAjax('idOpenPopUpCheck');
		 //RichFaces.$('formulaire_principal:idPopupConfirmSauvegardeModification').show();
		 return false;
	 }
	 
	 lancerLienAjax('idSelectionSemaine',{'idCacheValeurSem': ''+numeroSemaine+''});
 }


