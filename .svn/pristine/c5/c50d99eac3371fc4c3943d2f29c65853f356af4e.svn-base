/*!
 * Extension du Framework Front-End minimaliste
 *
 * Copyright 2017 Donatien Grolaux
 * Released under the MIT license
 * https://opensource.org/licenses/MIT
 *
 * Date: 2017-03-12
 */
(function() {

	var model=Model();
	
	FrontEnd._displayPage=FrontEnd.displayPage;
	FrontEnd.displayPage=function(page) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}
		if (FrontEnd.canDisplayPage(page)) {
			model.set('p',page.attr('id'));
		} else {
			return false;
		}
	}

	model.history('p',null);

	model.on('p', function() {
		var p=model.get('p');
		if (p!=null) {
			FrontEnd._displayPage(p, false);
		}
	});

	model.trigger('p');
	
}());

