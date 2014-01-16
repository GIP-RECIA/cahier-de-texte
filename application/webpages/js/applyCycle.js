
jQuery(document).ready(function () {
    initFormulaire();
});

function initFormulaire() {
    bindResizeFunction(resizePanel);
    resizePanel();
}

function initModifieEnseignement(select, id) {
    var valueSelect = jQuery(select).val();
    modifieEnseignement(id, valueSelect);
    return true;
}

function resizePanel() { 
    var div = jQuery('#formulaire_principal\\:idPanelListeSeance');
    if (div != null) {
        div.height(jQuery(window).height() - 290 );
    }
}

function afficheErreurEnseignement() {
    jQuery("#idPopupErreurEnseignement").dialog({
        width: "500px",
        modal: true,
        buttons: { 
    "Fermer": function() 
    { $(this).dialog("close"); $(this).dialog("destroy"); }
    } });    
}