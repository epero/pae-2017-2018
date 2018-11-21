/*!
 * Implémentation généraliste du patron observateur,
 * avec possibilité de gestion via l'API history 
 *
 * Copyright 2017 Donatien Grolaux
 * Released under the MIT license
 * https://opensource.org/licenses/MIT
 *
 * Date: 2017-03-12
 */
Model=function() {

	var listeners={};
	var data={};

	var usingHistory=true;

    if (window.location.protocol == 'file:' || History==undefined || History.pushState==undefined) {
    	usingHistory=false;
    }

	var histdata=null;

	function on(event,fct) {
		if (listeners[event]===undefined) {
			listeners[event]=[];
		}
		listeners[event].push(fct);
	}

	function off(event,fct) {
		if (fct===undefined) {
			delete listeners[event];
		} else {
			var idx=listeners.indexOf(fct);
			if (idx!=-1) {
				listeners[event].splice(idx,1);
			}
		}
	}

	function trigger(event, extra1,extra2) {
		if (listeners[event]===undefined) return;
		for(var i=0; i<listeners[event].length; i++) {
			if (listeners[event][i](extra1,extra2)===false) break;
		}
	}

	function compare(x,y) { // http://stackoverflow.com/questions/1068834/object-comparison-in-javascript
		  if ( x === y ) return true;
		    // if both x and y are null or undefined and exactly the same

		  if ( ! ( x instanceof Object ) || ! ( y instanceof Object ) ) return false;
		    // if they are not strictly equal, they both need to be Objects

		  if ( x.constructor !== y.constructor ) return false;
		    // they must have the exact same prototype chain, the closest we can do is
		    // test their constructor.

		  for ( var p in x ) {
		    if ( ! x.hasOwnProperty( p ) ) continue;
		      // other properties were tested using x.constructor === y.constructor

		    if ( ! y.hasOwnProperty( p ) ) return false;
		      // allows to compare x[ p ] and y[ p ] when set to undefined

		    if ( x[ p ] === y[ p ] ) continue;
		      // if they have the same strict value or identity then they are equal

		    if ( typeof( x[ p ] ) !== "object" ) return false;
		      // Numbers, Strings, Functions, Booleans must be strictly equal

		    if ( ! compare( x[ p ],  y[ p ] ) ) return false;
		      // Objects and Arrays must be tested recursively
		  }

		  for ( p in y ) {
		    if ( y.hasOwnProperty( p ) && ! x.hasOwnProperty( p ) ) return false;
		      // allows x[ p ] to be set to undefined
		  }
		  return true;
	}

	function getSearch() { // http://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
	    var match,
        pl     = /\+/g,  // Regex for replacing addition symbol with a space
        search = /([^&=]+)=?([^&]*)/g,
        decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
        query  = window.location.search.substring(1);

        urlParams = {};
        while (match = search.exec(query))
        	urlParams[decode(match[1])] = decode(match[2]);

        for(var k in urlParams) {
        	var v=histdata[k];
        	if (v!==undefined) {
        		if (v instanceof String || typeof v=="string") {}
        		else if (v instanceof Number || typeof v=="number") { urlParams[k]=parseFloat(urlParams[k]);}
        		else if (v==null && urlParams[k]=="") {urlParams[k]=null;}
        		else urlParams[k]=JSON.parse(urlParams[k]);
        	}
        }
        return urlParams;
	}

	function toSearch(all) {
		var r=getSearch();
        for(var k in all) {
        	var v=histdata[k];
        	if (v!==undefined) {
        		if (v instanceof String || typeof v=="string") {v=all[k];}
        		else if (v instanceof Number || typeof v=="number") { v=all[k];}
        		else if (v==null && all[k]==null) {v="";}
        		else v=JSON.stringify(all[k]);
        		r[k]=v;
        	}
        }
        var o=[];
        for(var k in r) {
        	o.push(k+"="+encodeURIComponent(r[k]));        	
        }
		return o.join("&");
	}

	function history(k,d) {
		if (!usingHistory) {
			data[k]=d;
		} else {
			if (histdata==null) {
				histdata={};
				 History.Adapter.bind(window, 'statechange', function () { // Note: We are using statechange instead of popstate
					 var all=getSearch();
					 for(var k in all) {
						 var old=data[k];
						 if (old===undefined) old=histdata[k];
						 if (!compare(old,all[k])) { // something changed
							 data[k]=all[k];
							 trigger(k,data[k]);
						 }
					 }

	             });
			}
			histdata[k]=d;
			var all=getSearch();
			if (all[k]!==undefined) {
				data[k]=all[k];
			}
		}
	}

	function set(k,v) {
		if (histdata!=null && histdata[k]!==undefined) {
			var all=getSearch();
			all[k]=v;
			History.pushState(null, null, "?" + toSearch(all));
		} else {
			data[k]=v;
			trigger(k,v);
		}
	}

	function get(k) {
		if (histdata!==null && histdata[k]!==undefined) {
			var all=getSearch();
			if (all[k]!==undefined) {
				return all[k];
			} else {
				return histdata[k];
			}
		}

		return data[k];
	}

	function remove(k) {
		if (histdata!==null && histdata[k]!==undefined) {
			delete histdata[k];
		}
		delete data[k];
		trigger(k);
	}

	return {
			on:on,
			off:off,
			trigger:trigger,
			history:history,
			set:set,
			get:get,
			'delete':remove
	}

}
