var dmAppletWidth = 540;
var dmAppletHeight = 340;



CKEDITOR.plugins.add('dragmath', {
    requires: ['dialog'],
    lang: ['fr'],

    init: function (ckeditor) {
        var dlgCmd = "dragmathDialog";

        ckeditor.idapplet = ckeditor.id + ckeditor.id;

        
        
        var c = ckeditor.addCommand(dlgCmd, new CKEDITOR.dialogCommand(dlgCmd));
        c.modes = {
            wysiwyg: 1,
            source: 0
        };
        c.canUndo = false;
        ckeditor.ui.addButton("Dragmath", {
            label: "DragMath",
            command: dlgCmd,
            icon: this.path + "../../images/dragmath.gif"
        });

        CKEDITOR.dialog.add(dlgCmd, function (editor) {
        	var appletHref = ctx + '/dragmath/DragMath/applet';
        	if (jQuery.browser.msie) {
        		//IE dans la portail utilise parfois le host du portail plutôt que cahier de texte, alors, prefix le vrai host
        		appletHref = window.location.protocol + "//" + window.location.host + appletHref;
        	}
        	
            return {
                title: 'DragMath',
                resizable: CKEDITOR.DIALOG_RESIZE_BOTH,
                minWidth: dmAppletWidth,
                minHeight: dmAppletHeight,
                  
                 
                onOk: function () {
                    var tex = document.getElementById(editor.idapplet).getMathExpression();
                    var sInsert = tex.replace('<', '&lt;').replace('>', '&gt;').replace(/^\$\$/, '&lt;Latex&gt;').replace(/\$\$$/, '&lt;/Latex&gt;');

                    if (sInsert.length > 0) {
                        console.log(sInsert);
                        editor.insertHtml(sInsert);
                    }
                },
                onLoad: function () {
                  //dialog = this;
                  console.log("onload");
                  setTimeout("resizeApplet()", 500);
                  this.setupContent(); 
              },
                contents: [{
                    id: "drnfo",
                    elements: [{
                        type: 'html',
                        html: '<div class="dragmathbg"><applet id="' + editor.idapplet + '" codebase="' + appletHref + '" code="Display/MainApplet.class" ' + 'archive="DragMath.jar,lib/AbsoluteLayout.jar,lib/swing-layout-1.0.jar,lib/jdom.jar,lib/jep.jar" ' + 'width="' + dmAppletWidth + '" height="' + dmAppletHeight + '" alt="Vous devez télécharger une machine virtuelle JAVA pour utiliser DragMath : http://java.com/fr/"><param name="language" value="fr" /><param name="outputFormat" value="MoodleTex" /></applet></div>'
                    }]
                }]
            };
        });
    }
});




function resizeApplet() {
    jQuery(".dragmathbg applet").css({
        'width': dmAppletWidth + 'px',
        'height': dmAppletHeight + 'px'
    });

}
