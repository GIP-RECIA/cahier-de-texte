function lancerEdition() {
                        setTimeout("lancerPDF();", 300);
} 

function lancerPDF() {
    jQuery('#printPDF').click();
}    

function handleResize() {
	logFormat("window height {0}", $(window).height());
  $(".listSequencesPanel, .listSeancesPanel").css("height", $(window).height() - 217);
}

jQuery(document).ready(function () {
	bindResizeFunction(handleResize);
});

