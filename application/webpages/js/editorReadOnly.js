

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


// config for toolbar, extraPlugins,...
CKEDITOR.editorConfig = function( config )
{
   config.extraPlugins = 'dragmath';
   
   config.language =  'fr';  		 
   config.startupFocus = true;  
      
   config.toolbar_Custom = 
  		 [];
  	
   config.toolbar = "Custom";
};