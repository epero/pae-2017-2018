var dtContacts;
var simpleSelect;
var selectedStud;
var ss;
var existeContactAccepteOuEnOrdre;

var dtPersContDash;

var idPersContDash = null;
var nomPersContDash = null;
var contactAccepte = null;
var contactStageEnOrdre = null;

var stage;

FrontEnd.onInitPage('pageDashboardStudent', function() {
	dtPersContDash = initPersonnesContactDT("#dtPersConDash");
	
	$('#confirmStage').on('click', function () {
		var isValid = $('#formAdresseStage').valid();
		
		if (idPersContDash == undefined){
			// set notification
			var msg = "Veuillez préciser le responsable";
			setAndShowNotification(msg, this); // this => button
			return;
		}

		if(isValid && idPersContDash!=undefined){
			
			if(selectedStud!=undefined){
				action = 'confirmDataStageAsProf';
				ss = JSON.stringify(selectedStud);
				
			}
			else{
				action = 'confirmDataStage';
				ss = JSON.stringify(myApp.getWhoAmI());
			}
			
			var dataStage = myApp.form2Json($('#formAdresseStage'));
			dataStage = JSON.parse(dataStage);
			dataStage['responsable'] = idPersContDash;
			dataStage['entreprise'] = contactAccepte.entreprise;

			config = {
					'data': {
						'action': action,
						'stage' : JSON.stringify(dataStage),
						'idContactAcc' : JSON.stringify(contactAccepte.idContact),
						'numVersContactAcc' : JSON.stringify(contactAccepte.numVersion),
						'selectedStud' : ss
					},
					'success': function (reponse) {
						if(selectedStud!=undefined){
							selectedStud = reponse;
						}
						else{
							myApp.setWhoAmI(reponse);
						}
						remplirContacts();
						showCongrats();
					},	            
					error: function(e,error, errorThrown) {  
		                if(e.status&&e.status==409){
			                var reponse = JSON.parse(e.responseText);
			                if(reponse.objetDb!=undefined){
		                		if(selectedStud!=undefined){
		                			selectedStud=reponse.objetDb;
		                		}
		                		else{
		                			myApp.setWhoAmI(reponse.objetDb);
			                		updateNavBar();	                		
		                		}
			               	}
			            	setAndShowNotification(reponse.message, $('#btnSauverEtat'));
			              
			           	}
						remplirContacts();
		           }
				}
			myApp.myajax(config);
		}
	});
	
	// add row
	$('#addRowDash').click(function () {
		dtPersContDash.rows().deselect();

		$('#txtInitiatePersContDash').show();

		var rowHtml = $("#newRow").find("tr")[0].outerHTML
		$('#btnCancelAddPersContDash').show();
		$('#btnAddPerContDash').show();
		$('#addRowDash').hide();
		dtPersContDash.row.add($(rowHtml)).draw();
		dtPersContDash.select.style('api');
	});

	$('#btnCancelAddPersContDash').on('click', function () {
		cancelAddPersContDash();
	});

	$('#btnAddPerContDash').on('click', function () {
		var isvalid = $("#formNewPersContDash").valid();
		var json = JSON.parse(myApp.form2Json($("#formNewPersContDash")));
		//Ajout de l'id entreprise dans JSON
		json.entreprise = contactAccepte.entreprise;
		var dataJson = JSON.stringify(json);

		if (isvalid) {

			config = {
				'data': {
					'action': 'creerPersonneContact',
					'newPersonneContact': dataJson,
				},
				'success': function (reponse) {


					var row = dtPersContDash.row.add(reponse);
					row.draw();
					row.select();
			    	setAndShowNotification("La personne de contact a été ajouté avec succès", $('#buttonSuccess'));

					//$(".notificationPDCAjoute").html('<div class="alert alert-dismissible bg-green text-center"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>La personne de contact a été ajouté avec succès</div>');
					cancelAddPersContDash();
				}
			}
			myApp.myajax(config);
		}
	});
	
	dtPersContDash.on('select', function (e, dt) {
		if (dt != undefined) {
			
			idPersContDash = dt.id();

			var idx = dt.cell('.selected', 0).index();
			var data = dt.row(idx.row).data();
			nomPersContDash = data.prenom + " " + data.nom;
		}
	});

	dtPersContDash.on('deselect', function (e, dt, type, indexes) {
		idPersContDash = null;
		nomPersContDash = null;
	});
	
	dtContacts = $('#dtContacts').DataTable({
		bAutoWidth : false,
		info : false,
		language : {
			"search" : "Rechercher : ",
			"emptyTable": "Pas de données disponibles dans la table",
			"lengthMenu": "Montrer _MENU_ entrées",
			"paginate": {
				"first":      "Premier",
				"last":       "Dernier",
				"next":       "Suivant",
				"previous":   "Précédent"
			}
		},
		/*select : {
			style : 'single'
		},*/
		data : "",
		columns : [ 
			{"data" : "entrepriseDto.denomination"},
			{"data" : null,
			 "render" : simpleSelectEtat
			}, 
			{"data" : getPersonneContact},
			{"data" : getEmail},
			{"data" : getTel}
		],
		rowId : 'idContact'
	});
	
    $('#initContact').click(function(){
		if(selectedStud!=undefined)
			rowSelectorEtudiant = selectedStud.idUtilisateur;
        $('#pageEntreprises').removeClass();
        $('#pageEntreprises').toggleClass("col-lg-12 col-md-12 col-sm-12 col-xs-12");
        $('#confirmInitiateContact').hide();
        $('#btnsContactEntreprises').show();
		FrontEnd.displayPage('pageEntreprises');
    });
    
    $('#selectEtud').on('select2:select', function (e) {
		$('#cardDtPersCont').hide();
		$('#cardPieChart').hide();
		var contactAccepte = null;
		var contactStageEnOrdre = null;
		//clearDataContacts();
		cancelAddPersContDash();
    	selectedStud = e.params.data;
		remplirContacts();
    });
    
    $('#btnSauverEtat').click(function(){
		if(selectedStud!=undefined){
			action = 'modifierEtatContactAsProf';
			ss = JSON.stringify(selectedStud);
		}
		else{
			action = 'modifierEtatContact';
			ss = JSON.stringify(myApp.getWhoAmI());			
		}
		var listeContacts = [];
    	$('table tbody tr td select:not([multiple])').each(function(i,el){
    		el=$(el);
  		  	var rowData = dtContacts.row( el.parent().parent() ).data();
  		  	if(el.val()!=rowData.etat){
  		  		rowData.etat = el.val();
  		  		listeContacts.push(rowData);
  		  	}
  		  	
  		  	
    	});	
  		config={
  				'data':{
  					'action': action,
  					'contacts': JSON.stringify(listeContacts), 
  					'selectedStud' : ss
  				},
  				success:function(response){
					if(selectedStud!=undefined)
						selectedStud=response;
					else
						myApp.setWhoAmI(response);
  					remplirContacts();
  				},
	            error: function(e,error, errorThrown) {  
	                if(e.status&&e.status==409){
	                	var reponse = JSON.parse(e.responseText);
	                	if(reponse.objetDb!=undefined){
	                		if(selectedStud!=undefined){
	                			selectedStud=reponse.objetDb;
	                		}
	                		else{
	                			myApp.setWhoAmI(reponse.objetDb);
		                		updateNavBar();	                		
	                		}
	                	}
	            		setAndShowNotification(reponse.message, $('#btnSauverEtat'));
	                }
					remplirContacts();
	           }
  		}
  		myApp.myajax(config);
    });

	FrontEnd.onDisplayPage('pageDashboardStudent', function() {
		//
		//existeContactAccepte=fctExisteContactAccepte();
		$('body').removeClass().toggleClass('theme-teal');
		showNavBar();
		$('.form-line').removeClass('error focused');

		//Affiche la page active dans la navbar
		if(!($('#navDashboard a').hasClass("actif"))){
			$('#navDashboard a').toggleClass('actif');
			$('#navEntreprises a').removeClass('actif');
			$('#navDashboardStudent a').removeClass('actif');
		}

		if(myApp.getWhoAmI().estAdmin){	
			
			myApp.myajax({
				data : {
					'action' : 'visualiserStudCurYear'
				},
				success : function(reponse) {
					var data = $.map(reponse, function (obj) {
						  obj.id = obj.id || obj.idUtilisateur; // replace pk with your identifier
						  obj.text = obj.text || obj.nom + " " +obj.prenom; // replace name with the property used for the text
						  return obj;
					});
					
					$('#selectEtud').select2({
						width: '35%',
						data: data,
						theme: "bootstrap",
						val : data[0]
						//placeholder: "Selectionnez un étudiant"
					});
					$('#divSelectEtud').show();
					if (rowSelectorEtudiant !== undefined){
						var etud = data.find(etud => etud.idUtilisateur === rowSelectorEtudiant);
						$('#selectEtud').val(rowSelectorEtudiant).trigger('change.select2');
						$('#selectEtud').val(rowSelectorEtudiant).trigger({
						    type: 'select2:select',
						    params: {
						        data: etud
						    }
						});
					} else {
						selectedStud = data[0];
						
						remplirContacts();
					}
				}
			});
			
			//Affiche la page active dans la navbar
			if(!($('#navDashboardStudent a').hasClass("actif")) /*&& !($('#navDashboardStudent a').hasClass("actif"))*/){
				$('#navDashboard a').removeClass('actif');
				$('#navEntreprises a').removeClass('actif');
				$('#navDashboardStudent a').toggleClass('actif');
			}
		}	
		else{
			remplirContacts();
		}
	});
	
	FrontEnd.onLeavePage('pageDashboardStudent', function() {
		$('#divSelectEtud').hide();
		$('#selectEtud').val(null);
		selectedStud = undefined;
		dtPersContDash.rows().deselect();
		$('#formAdresseStage input[name=dateSignature]').val('');
		cancelAddPersContDash();

		$('#nbContacts').text("Contacts : ");
		$('#notification').html('');
		$('#notifContact').html('');
		
		$('#cardDtPersCont').hide();
		$('#cardPieChart').hide();
		$('#cardStageEnOrdre').hide();
	});
});



function drawChartStudent() {
	$('#cardPieChart').show();
    google.charts.load('current', {'packages':['corechart']});
	google.charts.setOnLoadCallback(drawVisualization);
	
	function drawVisualization(){
		if(selectedStud!=undefined){
			action = 'fillChartStudentAsProf';
			ss=selectedStud.idUtilisateur;
		}
		else{
			action = 'fillChartStudent';
			
		}
		var stats;
		
		config = {
			'data': {
				'action': action,
				'selectedStud' : ss
			},
			'success': function (reponse) {
				stats =reponse;

		        var data = google.visualization.arrayToDataTable([
		          ['Task', 'Hours per Day'],
		          ['Contacts initiés', stats.contactsinities],
		          ['Contacts pris', stats.contactspris],
		          ['Contacts refusés', stats.contactsrefuses]
		        ]);

		        var options = {
		          //title: 'My Daily Activities'
		        		chartArea:{width:'100%',height:'70%'},
		        		legend:{position:'bottom'}
		        };

		        var chart = new google.visualization.PieChart(document.getElementById('piechartStudent'));

		        chart.draw(data, options);
			}
		}
		myApp.myajax(config);
	}
	
}

function showStageEnOrdre(){
	if(selectedStud!=undefined){
		action = 'visualiserStageAsProf';
		ss = selectedStud.idUtilisateur;
	}
	else{
		action = 'visualiserStage';
	}
	
	config={
		'data':{
			'action': action,
			'selectedStud' : ss
		},
		success:function(response){
			stage = response;
			date = new Date(stage.dateSignature.monthValue + " " + stage.dateSignature.dayOfMonth + " " + stage.dateSignature.year);
			stage.dateSignature = dateFormatee(date);
			stage.responsablePrenom = stage.responsableDto.prenom;
			stage.responsableNom = stage.responsableDto.nom;
			stage.responsableEmail = stage.responsableDto.email;
			stage.responsableTel = stage.responsableDto.tel;
			$('#nomEntreprise').html('Informations de l\'entreprise : <span class="font-bold font-italic col-teal">' + stage.entrepriseDto.denomination + '</span>');
			myApp.json2Form($('#form-stage-en-ordre'), stage);
			$('#cardStageEnOrdre').show();
		}
	}
	myApp.myajax(config);
}

function drawFormStage(){
	$('#cardDtPersCont').show();
	remplirPersonnesContact(contactAccepte.entreprise ,dtPersContDash);
	preremplirAdresseStage(contactAccepte.entrepriseDto);	
}

function preremplirAdresseStage(entreprise){
	if (entreprise.boite !== null){
		$('#formAdresseStage input[name=boite]').val(entreprise.boite).parent().addClass("focused");
	}
	$('#formAdresseStage input[name=adresse]').val(entreprise.adresse).parent().addClass("focused");
	$('#formAdresseStage input[name=numero]').val(entreprise.numero).parent().addClass("focused");
	$('#formAdresseStage input[name=codePostal]').val(entreprise.codePostal).parent().addClass("focused");
	$('#formAdresseStage input[name=ville]').val(entreprise.ville).parent().addClass("focused");	
}

function remplirContacts() {

	if(selectedStud!=undefined){
		action = 'visualiserContactsAsProf';
		ss = selectedStud.idUtilisateur;
	}
	else{
		action = 'visualiserContacts';
		
	}
	existeContactAccepteOuEnOrdre = false;

	myApp.myajax({
		data : {
			'action' : action,
			'selectedStud' : ss
		},
		success : function(data) {
			$.each(data, function(i, item) {
				if(data[i].etat===3||data[i].etat===4)
					existeContactAccepteOuEnOrdre = true;
			});
			clearDataContacts();
			$('#nbContacts').text('Contacts : ' + data.length);	
			dtContacts.clear();
			dtContacts.rows.add(data).draw();
			$('#cardDtPersCont').hide();
			$('#cardPieChart').hide();
			$('#cardStageEnOrdre').hide();
			if (contactAccepte!=null){
				hideFinalStates();
				drawFormStage();
			} else if (contactStageEnOrdre!=null) {
				hideFinalStates();
				showStageEnOrdre();
			} else {
				showFinalStates();
				drawChartStudent();
			}
			// cacher les boutons si l'étudiant est d'une année académique précédente
			if(myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() !== selectedStud.anneeAcademique){
				hideFinalStates();
			}
			else if (!myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() !== myApp.getWhoAmI().anneeAcademique){
				hideFinalStates();
			}

		}
	});
	
}

function getPersonneContact(data,type,full){
	if (data.personneContactDto===null){
		return "-";
	}else{
		return data.personneContactDto.prenom+" "+data.personneContactDto.nom;
	}
}
function getEmail(data,type,full){
	if (data.personneContactDto === null && data.entrepriseDto.email !== null){
		return data.entrepriseDto.email;
	}
	if (data.personneContactDto !== null && data.personneContactDto.email !== null){
		return data.personneContactDto.email;
	}
	return "-";
}

function getTel(data,type,full){
	if (data.personneContactDto === null && data.entrepriseDto.tel !== null){
		return data.entrepriseDto.tel;
	}
	if (data.personneContactDto !== null && data.personneContactDto.tel !== null){
		return data.personneContactDto.tel;
	}
	return "-";
}

function simpleSelectEtat(data,type,row,meta){
	switch (data.etat){
	case 4:
		simpleSelect='<div class="4 font-bold col-green">Stage en ordre</div>';
		contactStageEnOrdre = data;
		break;
	case 1:
		simpleSelect='<div class="1 col-red">Refusé</div>';
		break;
	case 3:
		simpleSelect='<div class="3 col-green"><b>Accepté</b></div>';
		contactAccepte = data;
		//hideFinalStates();
		break;
	case 0:
		if(existeContactAccepteOuEnOrdre!== null && existeContactAccepteOuEnOrdre){/*****************************************************************************************/
			simpleSelect='<div class="0" >Initié</div>';
			break;
		}
	case 2:
		if(existeContactAccepteOuEnOrdre!== null && existeContactAccepteOuEnOrdre){
			simpleSelect='<div class="2">Pris</div>';
			break;
		}
		simpleSelect='<select onchange="triggerNotification()" class="simpleSelectEtat">'+
							 '<option value="0" '+putSelected(0,data)+'>Initié</option>'+
							 '<option value="2" '+putSelected(2,data)+'>Pris</option>'+
							 '<option value="3" '+putSelected(3,data)+'>Accepté</option>'+	 
							 '<option value="1">Refusé</option>'+
					 '</select>';
		break;
	}
	return simpleSelect;
}

function triggerNotification(){
	
	var placementFrom = "bottom";
    var placementAlign = "left";
    var animateEnter = "animated zoomInLeft";
    var animateExit = "animated zoomOutLeft";
    var colorName = "bg-orange";
    var text = "Attention : cette action est irréversible.\nCliquez sur \"Sauver\" pour confirmer vos modifications.";
    showNotification(colorName, text, placementFrom, placementAlign, animateEnter, animateExit);
};

/**
 * renvoie la chaine 'selected' si les deux paramètres sont egaux.
 * @param optionValue la valeur de la balise <option>
 * @param dataValue la valeur de l'état du contact
 * @returns la chaine 'selected' si optionValue est égale à dataValue, une chaine vide sinon
 */
function putSelected(optionValue,dataValue){
	var etatPlusAvance = myApp.getWhoAmI().etatPlusAvance;
	
	if (optionValue===dataValue.etat){
		return 'selected';
	}
	
	if (etatPlusAvance > optionValue) {
		return 'disabled';
	}
	return '';
}

function cancelAddPersContDash() {
	$('#btnAddPerContDash').hide();
	$('#btnCancelAddPersContDash').hide();
	$('#txtInitiatePersContDash').hide();
	dtPersContDash.row($("#-1")).remove().draw();
	$('#addRowDash').show();
	dtPersContDash.select.style('single');
}


function clearDataContacts(){
	contactAccepte = null;
	contactStageEnOrdre = null;
}

function hideFinalStates(){
	$('#initContact').hide();
	$('#btnSauverEtat').hide();
}

function showCongrats(){
	swal({
        title: 'Félicitations !',
        text: 'Ton stage d\'observation est désormais en ordre, il ne te reste plus qu\'à t\'y présenter.',
        timer: 3000,
        showConfirmButton: true
	});
}

function showFinalStates(){
	$('#initContact').show();
	$('#btnSauverEtat').show();
}