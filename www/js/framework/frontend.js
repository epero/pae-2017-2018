/*!
 * Framework Front-End minimaliste
 *
 * Copyright 2017 Donatien Grolaux
 * Released under the MIT license
 * https://opensource.org/licenses/MIT
 *
 * Date: 2017-03-12
 */
var FrontEnd=(function() {

	var onInits={};
	var onLeaves={};
	var onDisplays={};
	var inited={};
	var currentPage=null;
	
	/**
	 * La fonction F se lancera quand la page ayant l'id page 
	 * sera "lance" (mise en avant pour la premiere fois). si la fonction F 
	 * renvoie false, la page ne sera pas lancé
	 */
	function onInitPage(page,f) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}
		if (page.length!=1) return false;
		if (inited[page.attr('id')]!==true) {
			setTimeout(function() {f(page);},1);
			inited[page.attr('id')]=true;
		}
		onInits[page.attr('id')]=f;
		return true;
	}
	
	/**
	 * La fonction f se lance quand la page ayant l'id page se quitte
	 * si la fonction f renvoie false, la page ne sera pas quitté (elle gardera le focus)
	 */
	function onLeavePage(page,f) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}
		if (page.length!=1) return false;
		onLeaves[page.attr('id')]=f;
		return true;
	}
	
	/**
	 * lance la fonction f lorsque la page passe en focus. contrairement a la fonction 
	 * onInitPage qui ne se lance que la premiere fois que la page est lancé, la fonction f
	 * décrit ici se lancera a chaque fois que la page sera mise en avant. si la fonction f 
	 * renvoie false, la page ne sera pas mise en display
	 */
	function onDisplayPage(page,f) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}
		if (page.length!=1) return false;
		if (currentPage!=null && currentPage[0]==page[0]) {
			setTimeout(function() {f(page);},1);
		}
		onDisplays[page.attr('id')]=f;
		return true;
	}

	function getCurrentPage() {
		return currentPage;
	}

	/**
	 * Verifie que la page peut etre lancé. s'occupe de lancé la onLeave fonction associé a 
	 * la page déjà presente, lance la onInitPage de la nouvelle page si c'est la premiere fois qu'elle est 
	 * lancé, et lance la onDisplayPage de la nouvelle page. a priori, on ne doit pas utiliser cette 
	 * fonction car elle est automatiquement appellé par displayPage()
	 */
	function canDisplayPage(page) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}
		//si plusieurs page ont le meme id c'est que c'est foireux
		if (page.length!=1) return false;
		
		// ici on s'occupé de quoi faire avec la page actuellement affiché, car cette 
		// page va devoir bougé pour faire place a la nouvelle page.
		// on va donc devoir appellé la fonction indiqué dans onLeaves pour cette page
		// si une page est actuellement affiché, que cette page contient bien une fonction a lancer
		// lorsqu'elle perd le focus, et que cete fonction renvoie false (aka qu'elle ne peut pas perdre le focus)
		// alors on quitte (on affiche pas la page et on laisse l'ancienne page)
		if (currentPage !== null) {
			if (onLeaves[currentPage.attr('id')]!=undefined) {
				if (onLeaves[currentPage.attr('id')](page)===false) {
					return false;
				}
			}
		}
		
		//Arrivé ici on sais qu'on peu enlever la page qui est actuellement affiché. 
		//on regarde si la page a déjà été initialisé. si ce n'est pas le cas on lance la fonction
		// qui a été mise dans onInitPage(si une tel fonction a ete mise). si cette fonction renvoie false, 
		// on initialise pas la page
		if (inited[page.attr('id')]!==true) {
			if (onInits[page.attr('id')]!=undefined) {
				if (onInits[page.attr('id')](page)===false) {
					return false;
				}
			}
			inited[page.attr('id')]=true;
		}
		
		// on lance la fonction associé a la page. si cette fonction renvoie false la page
		// ne se lancera pas
		if (onDisplays[page.attr('id')]!=undefined) {
			if (onDisplays[page.attr('id')](page)===false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Display la page passé en paramentre.
	 */
	function displayPage(page,validate) {
		if (page instanceof String || typeof page=='string') {
			page=$('#'+page);
		}

		if (!(validate===false)) {		
			if (!canDisplayPage(page)) return false;
		}

		if(currentPage!==null) {
			currentPage.css('display','none');
		}
		page.css('display','block');
		currentPage=page;
	}

	function alert(title, question, message, callback) {
		var modal=$('<div class="modal"><div class="modal-content"><div class="modal-header"><span class="close">&times;</span><h2>M</h2></div><div class="modal-body"></div><div class="modal-footer"></div>  </div></div>');
		modal.find('.modal-header h2').text(title);
		modal.find('.modal-body').text(message);
		var btn=question.split("|");
		modal.find('.modal-header span').css('display','none');
		jQuery.each(btn,function(i,item) {
			var btn=$('<button type="button">');
			switch(item) {
			case 'YES':
				btn.text("Oui");
				break;
			case 'NO':
				btn.text("Non");
				break;
			case 'ALWAYSYES':
				btn.text("Toujours oui");
				break;
			case 'ALWAYSNO':
				btn.text("Toujours non");
				break;
			case 'CANCEL':
				btn.text("Annuler");
				modal.find('.modal-header span').css('display','block').on('click',function() {
					btn.trigger('click');
				});
				break;
			}
			btn.on('click', function() {
				modal.remove();
				modal=null;
				if (callback!==undefined) {
					callback(item);
				}
			});
			modal.find('.modal-footer').append(btn);
		});
		$('body').append(modal);
		modal.css('display','block');
	}

	function ajax(config) {
		var fail=config.error;
		config.error=function(jqxhr, status, error) {
			if (fail!==undefined) {
				if (fail(jqxhr, status, error)===true) {
					return;
				}
			}
	        if (jqxhr.status == 400) {
	        	FrontEnd.alert("Erreur", "CANCEL",jqxhr.responseText);
	        } else {
	        	FrontEnd.alert("Erreur grave", "CANCEL",jqxhr.responseText, function() {location.reload();});
	        }
		}
		if (config.type===undefined) config.type='POST';
		if (config.url===undefined) config.url='/dispatch';
		if (config.action!==undefined) {
			if (config.data===undefined) config.data={};
			config.data.action=config.action;
			delete config.action;
		}

		$.ajax(config);
	}

	$('body>.container>.row').css('display','none');

	return {
		onInitPage:onInitPage,
		onLeavePage:onLeavePage,
		onDisplayPage:onDisplayPage,
		displayPage:displayPage,
		canDisplayPage:canDisplayPage,
		currentPage:getCurrentPage,
		ajax:ajax,
		alert:alert
	}

})();
