
//load external plugin
(function() {
	var dragMathSrc = ctx + '/resources/js/dragmath/';

	 console.log("dragMath src %s", dragMathSrc);
	 try {
   CKEDITOR.plugins.addExternal('dragmath', dragMathSrc, 'plugin.js');
	 } catch (ex) {
		 console.log("prob", ex);
	 }
})();


//config for toolbar, extraPlugins,...
CKEDITOR.editorConfig = function( config )
{
config.extraPlugins = 'dragmath';

config.language =  'fr';  		 
config.startupFocus = false;  
config.removePlugins = 'elementspath';
config.disableNativeSpellChecker = false;
config.browserContextMenuOnCtrl = true; 
   //'Scayt'   'iespell'
config.toolbar_Custom = 
		 [
		 { name: 'theme_advanced_buttons1', items : ['NewPage', '-', 'Bold','Italic','Underline','Strike',
		 '-', 'JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock', '-', 'Format', 'Font', 'FontSize', 'iespell'  ] },		
		 '/',
		 { name: 'theme_advanced_buttons2', items : [ 'BulletedList', 'NumberedList', '-',
		 'Outdent', 'Indent', 'Blockquote', '-', 'Undo', 'Redo', '-', 'Link','Unlink', '-',
		 'TextColor','BGColor', '-', 'Table', 'HorizontalRule', 'RemoveFormat', '-', 'Superscript', 'Subscript',
		 '-', 'SpecialChar', 'Dragmath', 'Maximize'
		 ] }
		 ];
	
config.toolbar = "Custom";
};

// Set defaults for tables
CKEDITOR.on( 'dialogDefinition', function( ev )
{
   // Take the dialog name and its definition from the event
   // data.
   var dialogName = ev.data.name;
   var dialogDefinition = ev.data.definition;

   // Check if the definition is from the dialog we're
   // interested on (the "Table" dialog).
   if ( dialogName == 'table' )
   {
       // Get a reference to the "Table Info" tab.
       var infoTab = dialogDefinition.getContents( 'info' );
       txtWidth = infoTab.get( 'txtWidth' );
       txtWidth['default'] = '100%';
    //   cmbWidthType = infoTab.get( 'cmbWidthType' );
    //   cmbWidthType['default'] = 'percents';
      // txtCellPad = infoTab.get( 'txtCellPad' );
      // txtCellPad['default'] = 4;
   }
 });