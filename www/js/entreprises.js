var dtEntreprises;
var dtEntreprisesAsProf;
var dtPersonnesContact;
var dtModal;

let idEntreprise = null;
let idModalEntreprise = null;
let idPersonneContact = null;
let denominationEntreprise = null;
let nomPersonneContact = null;

var geocoder;
var map;
var marker;
var infowindow;

FrontEnd.onInitPage('pageEntreprises', function () {
	/**
	 * Initialisation API Google Maps
	 */
	initMap();
	
	/**
	 * Personnes Contact
	 */
	// initialiser DataTables Personnes de contact
	dtPersonnesContact = initPersonnesContactDT("#dtPersonnesContact");
	
	// select row Personnes de contact
	dtPersonnesContact.on('select', function (e, dt) {
		$('#confirmInitiateContact').hide();
		if (!myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === myApp.getWhoAmI().anneeAcademique){
			$('#btnsContactEntreprises').show();
		} else if (myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === selectedStud.anneeAcademique) {
			$('#btnsContactEntreprises').show();
		}else {
			$('#btnsContactEntreprises').hide();
		}
		if (dt != undefined) {
			
			idPersonneContact = dt.id();
			var idx = dt.cell('.selected', 0).index();
			var data = dt.row(idx.row).data();
			nomPersonneContact = data.prenom + " " + data.nom;
		}
	});
	
	// deselect row Personnes de contact
	dtPersonnesContact.on('deselect', function (e, dt, type, indexes) {
		idPersonneContact = null;
		nomPersonneContact = null;
		$('#confirmInitiateContact').hide();
	});
	
	
	// add row DataTables Personnes de contact
	$('#addRow').click(function () {
		dtPersonnesContact.rows().deselect();
		$('#confirmInitiateContact').hide();
		$('#btnAddContact').hide();
		$('#txtInitiatePersonContact').show();
		$('#btnBlacklistEntreprise').hide();
		$('#btnMergeEntreprise').hide();

		var rowHtml = $("#newRow").find("tr")[0].outerHTML
		$('#btnAddPersonContact').show();
		$('#btnCancelAddPersonContact').show();
		$('#addRow').hide();
		dtPersonnesContact.row.add($(rowHtml)).draw();
		dtPersonnesContact.select.style('api');
	});
	
	// ajout d'une personne de contact
	$('#btnAddPersonContact').on('click', function () {
		var isvalid = $("#formNewPersonneContact").valid();
		var json = JSON.parse(myApp.form2Json($("#formNewPersonneContact")));
		//Ajout de l'id entreprise dans JSON
		json.entreprise = idEntreprise;
		var dataJson = JSON.stringify(json);

		if (isvalid) {

			config = {
				'data': {
					'action': 'creerPersonneContact',
					'newPersonneContact': dataJson,
				},
				'success': function (reponse) {
					var row = dtPersonnesContact.row.add(reponse);
					row.draw();
					row.select();
                	setAndShowNotification("La personne de contact a été ajouté avec succès", $('#buttonSuccess'));
					cancelAddPerson();
				}
			}
			myApp.myajax(config);
		}
	});
	
	// annuler l'ajout d'une personne de contact
	$('#btnCancelAddPersonContact').on('click', function () {
		cancelAddPerson();
		// afin d'avoir le meme comportement sur les 2 tables
		if(($('#dtEntreprises, #dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().estBlackListe === false) && (myApp.getWhoAmI().estAdmin === true)) {	
			$('#btnBlacklistEntreprise').show();
			$('#btnMergeEntreprise').show();
		}
	});

	
	/**
	 * Entreprises 
	 */
	// initialiser DataTables Entreprises Professeur/Student et Modal
	initDataTablesEntreprisesAsProf();	
	initEntreprisesDT();
	initModalDT();

	
	//afficher le formulaire de création d'entreprise
	$('#btnCreerEntreprise').on('click',function(){
		$('#formCreerEntreprise').fadeIn('slow');
		$('#pagePersonnesContact').hide();
	});

	//cacher le formulaire de création d'entreprise
	$('#btn-cne-cancel').on('click', function(){
		$('#formCreerEntreprise').fadeOut();
		$("#formCreerEntreprise").validate().resetForm();
		$('.form-line').removeClass('error focused');
		$("#form-creation-entreprise")[0].reset();
	});

	// fusionner l'entreprise
	$('#btnMergeEntreprise').on('click', function(){
		var entreprises = dtEntreprisesAsProf;
		// Left part
		var entrepriseSelect = entreprises.rows('#' + idEntreprise).data()[0];
		$('#modalEntrepriseDenomination').text(entrepriseSelect.denomination);
		$('#modalEntrepriseAdresse').text(entrepriseSelect.numero + " " + entrepriseSelect.adresse);
		$('#modalEntrepriseAdresseSup').text(entrepriseSelect.codePostal + " " + entrepriseSelect.ville);
		$('#modalEntrepriseTel').text(formatTel(entrepriseSelect.tel));
		$('#modalEntrepriseEmail').text(formatEmail(entrepriseSelect.email));
		if(entrepriseSelect.estBlackListe){
			$('#modalEntrepriseBlackliste').html("<span class=\"col-red\">Blacklisté !</span>");
		} else {
			$('#modalEntrepriseBlackliste').html("<span class=\"col-green\">Non blacklisté</span>");
		}

		// Right part
		var allEntreprises = entreprises.rows().data();
		dtModal.clear();
		dtModal.rows.add(allEntreprises).draw();
		dtModal.row('#' + idEntreprise).remove().draw();
	});

	$('#btnmergeEntrepriseFinal').on('click', function(){
		config = {
			'data': {
				'action': 'fusionnerDeuxEntreprises',
				'idEntreprise1': idModalEntreprise,
				'numVersionEntr1' : $('#dtModal').DataTable().row("#" + idModalEntreprise).data().numVersion,
				'idEntreprise2': idEntreprise, 
				'numVersionEntr2' : $('#dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().numVersion
			},
			'success': function (reponse) {
				setAndShowNotification("Les entreprises ont bien &eacute;t&eacute;  fusionn&eacute;es",$('#buttonSuccess'));
				$('#mergeModal').modal('hide');
				rowSelectorEntreprise = '#'+idEntreprise;
				remplirEntreprisesAsProf($('#selectAnneeAcademique').val());
			},
			error: function(e,error, errorThrown) {  
                if(e.status&&e.status==409){
                	var reponse = JSON.parse(e.responseText);
    				$('#mergeModal').modal('hide');
    				rowSelectorEntreprise = '#'+idEntreprise;
                	remplirEntreprisesAsProf($('#selectAnneeAcademique').val());
                	setAndShowNotification(reponse.message, $('#buttonAlert'));
        		}
           }
		}
		myApp.myajax(config);
	});
		
	
	// creer une nouvelle entreprise
	$('#btn-cne-submit').on('click',function(){

		var isValid = $('#form-creation-entreprise').valid();

		if(isValid){

			config = {
				data:{
					action:'insererEntreprise',
					'newEntreprise': myApp.form2Json($('#form-creation-entreprise'))
				},
				success:function(response){
					//TODO
					if(myApp.getWhoAmI().estAdmin){
						response["nEtudiants"] = 0;
						var row = dtEntreprisesAsProf.row.add(response);
					} else {
						var row = dtEntreprises.row.add(response);
					}
	
					row.draw();
					row.select();
					$('#formCreerEntreprise').hide();
					setAndShowNotification("L'entreprise a bien &eacutet&eacute cr&eacute&eacutee",$('#buttonSuccess'));
				}
			}
			myApp.myajax(config);
		}
	});
	
	// blacklister une entreprise
	$('#btnBlacklistEntreprise').on('click', function () {

		if($('#dtEntreprises, #dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().estBlackListe) {
			setAndShowNotification("L\'entreprise est déjà blacklistée", $('#buttonAlert'));
		}
		else{
			swal({
				title: "Êtes vous sûr de vouloir blacklister l\'entreprise <span class=\"font-bold font-italic col-teal\">" + denominationEntreprise + "</span> ?",
				text: "Une fois l'entreprise blacklistée, il n'est plus possible de revenir en arrière.",
				type: "warning",
				html: true,
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "Blacklister",
				cancelButtonText: "Annuler",
				closeOnConfirm: true
			}, function () {
				config = {
					'data': {
						'action': 'blacklistEntreprise',
						'entreprise': idEntreprise,
						'numVersionEntreprise': $('#dtEntreprises, #dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().numVersion,
					},
					'success': function(reponse) {
						setAndShowNotification("L\'entreprise a été blacklistée", $('#buttonSuccess'));
						$('#entrepriseBlackliste').html("<span class=\"col-red\">Blacklisté !</span>");
						$('#btnBlacklistEntreprise').hide();
						$('#dtEntreprises, #dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().numVersion += 1; 
						$('#dtEntreprises, #dtEntreprisesAsProf').DataTable().row("#" + idEntreprise).data().estBlackListe = true;
					},
					error: function(e,error, errorThrown) {  
	                    if(e.status&&e.status==409){
		                	var reponse = JSON.parse(e.responseText);
		    				rowSelectorEntreprise = '#'+idEntreprise;
		                	remplirEntreprisesAsProf($('#selectAnneeAcademique').val());
		                	setAndShowNotification(reponse.message, $('#buttonAlert'));
	            		}
	               }
				}
				myApp.myajax(config);
			});
		}
	});

	// Select Etudiant
    $('#selectEtudPageEntr').on('select2:select', function (e) { 
    	selectedStud = e.params.data;
    });
    
    // Select Annee Academique
    $('#selectAnneeAcademique').on('change', function (e) {
		remplirEntreprisesAsProf(this.value);    	
    });
    
	FrontEnd.onDisplayPage('pageEntreprises', function () {
		
		// comportement Professeur
		if(myApp.getWhoAmI().estAdmin){		
			
			myApp.myajax({
				data : {
					'action' : 'visualiserStudCurYear'
				},
				success : function(reponse) {
					
					// remplir Select
					var data = $.map(reponse, function (obj) {
						  obj.id = obj.id || obj.idUtilisateur; // replace pk with your identifier
						  obj.text = obj.text || obj.nom; // replace name with the property used for the text
						  return obj;
					});
					
					$('#selectEtudPageEntr').select2({
						width: '35%',
						data: data,
						theme: "bootstrap",
						val : data[0]
						//placeholder: "Selectionnez un étudiant"
					});
					$('#divSelectEtudPageEntr').show();
					$('#btnBlacklistEntreprise').show();
					$('#btnMergeEntreprise').show();
					if (rowSelectorEtudiant !== undefined){
						var etud = data.find(etud => etud.idUtilisateur === rowSelectorEtudiant);
						$('#selectEtudPageEntr').val(rowSelectorEtudiant).trigger('change.select2');
						$('#selectEtudPageEntr').val(rowSelectorEtudiant).trigger({
						    type: 'select2:select',
						    params: {
						        data: etud
						    }
						});
					} else {
						selectedStud = data[0];
					}

				}
			});

		}
		
		$('body').removeClass().toggleClass('theme-teal');
		// Modifier ? On vérifie déjà l'admin plus haut
		// remplir DataTables Entreprises
		if(myApp.getWhoAmI().estAdmin){
			remplirSelectAnneeAcademique();
			
			$('#dataTablesEntreprisesProf').show();
		} else {
			remplirEntreprises();
			$('#dataTablesEntreprisesStudent').show();
		}

		showNavBar();

		//Affiche la page active dans la navbar
		if(!($('#navEntreprises a').hasClass("actif"))){
			$('#navDashboard a').removeClass('actif');
			$('#navEntreprises a').toggleClass('actif');
			$('#navDashboardStudent a').removeClass('actif');
		}
	});

	FrontEnd.onLeavePage('pageEntreprises', function () {
		$('#pagePersonnesContact').css('display', 'none');
		$('.notification').text('');
		$('#divSelectEtud').hide();
		$('#selectEtudPageEntr').val(null);
		$('#divSelectEtudPageEntr').hide();
		$('#btnBlacklistEntreprise').hide();
		$('#btnMergeEntreprise').hide();
		$('#dataTablesEntreprisesStudent').hide();
		$('#dataTablesEntreprisesProf').hide();
		$('#formCreerEntreprise').hide();
		$('#form-creation-entreprise')[0].reset();
		$("#formCreerEntreprise").validate().resetForm();
		$('.form-line').removeClass('error focused');
		$('#form-creation-entreprise')[0].reset();
		selectedStud = undefined;
		rowSelectorEntreprise = undefined;
		rowSelectorResponsable = undefined;
	});
});

// initier un contact
$('#btnAddContact').on('click', function () {
	var textToShow = '';
	if (idPersonneContact == null) {
		textToShow = 'Vous allez initier un contact avec l\'entreprise <span class="font-bold font-italic col-teal">' + denominationEntreprise + '</span>. Il n\'y aura pas de personne de contact';
	} else {
		textToShow = 'Vous allez initier un contact avec l\'entreprise <span class="font-bold font-italic col-teal">' + denominationEntreprise + '</span>. La personne de contact sera <span class="font-bold font-italic col-teal">' + nomPersonneContact + '</span>.';
	}
	
	// boite de dialogue de confirmation
	swal({
		title: textToShow,
		text: "Une fois le contact initié, il n'est plus possible de revenir en arrière.",
		html:true,
		type: "warning",
		showCancelButton: true,
		confirmButtonColor: "#4CAF50",
		confirmButtonText: "Initier",
		cancelButtonText: "Annuler",
		closeOnConfirm: true
	},function () {
		//$('#btnsContactEntreprises').hide();
		if(selectedStud!=undefined){
			action = 'creerContactAsProf';
			ss=JSON.stringify(selectedStud)
		}
		else{
			action = 'creerContact';
			ss=JSON.stringify(myApp.getWhoAmI())
			
		}
		dataContact = JSON.stringify({
				idEntreprise : idEntreprise,
				idPersonneContact : idPersonneContact
		})
	config = {
		'data': {
			'action': action,
			'newContact' : dataContact,
			'selectedStud' : ss
		},
		'success': function (reponse) {
			$('#confirmInitiateContact').hide();
			if (!myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === myApp.getWhoAmI().anneeAcademique){
				$('#btnsContactEntreprises').show();
			} else if (myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === selectedStud.anneeAcademique) {
				$('#btnsContactEntreprises').show();
			}else {
				$('#btnsContactEntreprises').hide();
			}
			if(selectedStud!=undefined){
				rowSelectorEtudiant = selectedStud.idUtilisateur;
			}
			else{
				myApp.setWhoAmI(reponse.utilisateurDto);
			}
        	setAndShowNotification("Le contact a été initié avec succès", $('#buttonSuccess'));
			//$('#notifContact').html('<div class="alert alert-dismissible bg-green text-center"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>Le contact a été initié avec succès.</div>');
			FrontEnd.displayPage('pageDashboardStudent');
		},
	    'error': function(e,error, errorThrown) {  
	        if(e.status&&e.status==409){
	        	if(myApp.getWhoAmI().estAdmin!=undefined){
	        		var reponse = JSON.parse(e.responseText);
	        		myApp.setWhoAmI(reponse.objetDb);
	        		updateNavBar();
	        	}
		        setAndShowNotification(reponse.message, $('#buttonAlert'));
			}
	   }
	}
	myApp.myajax(config);
	});
});

// remplissage Select Annee Academique
function remplirSelectAnneeAcademique(){
	myApp.myajax({
		data: {
			'action': 'fillSelectAnneeAcademique',
		},
		success: function (data) {
			$.each(data, function(index) {
				// eviter les duplicate
				if ($("#selectAnneeAcademique option[value='" + data[index] + "']").length == 0){
					$('#selectAnneeAcademique').append($("<option>").val(data[index]).text(data[index])); 
				}
			});
			 
			// trigger change pour remplir la DataTable 
			$('#selectAnneeAcademique').val(data[0]).trigger("change");
		}
	});
}

// remplissage DataTables Entreprises As Prof
function remplirEntreprisesAsProf(anneeAcademique){
	myApp.myajax({
		data: {
			'action': 'visualiserEntreprisesAsProf',
			'anneeAcademique' : anneeAcademique
		},
		success: function (data) {
			dtEntreprisesAsProf.clear();
			dtEntreprisesAsProf.rows.add(data).draw();
			if (rowSelectorEntreprise !== undefined){
				dtEntreprisesAsProf.row(rowSelectorEntreprise).select();		
			}
		}
	});
}


// remplissage DataTables Entreprises
function remplirEntreprises() {
	myApp.myajax({
		data: {
			'action': 'visualiserEntreprises',
		},
		success: function (data) {
			dtEntreprises.clear();
			dtEntreprises.rows.add(data).draw();
			if (rowSelectorEntreprise !== undefined){
				dtEntreprises.row(rowSelectorEntreprise).select();
			}
		}
	});
};

// remplissage DataTables PersonnesContact
function remplirPersonnesContact(entreprise, dt) {
	myApp.myajax({
		data: {
			'action': 'visualiserPersonneContact',
			'idEntreprise': entreprise
		},
		success: function (data) {
			dt.clear();
			dt.rows.add(data).draw();
			if (rowSelectorResponsable !== undefined){
				dtPersonnesContact.row(rowSelectorResponsable).select();
			}
		}
	});
};

function cancelAddPerson() {
	$('#btnAddPersonContact').hide();
	$('#btnCancelAddPersonContact').hide();
	$('#btnAddContact').show();
	$('#txtInitiatePersonContact').hide();
	dtPersonnesContact.row($("#-1")).remove().draw();
	$('#addRow').show();
	dtPersonnesContact.select.style('single');
}

function initDataTablesEntreprisesAsProf(){
	dtEntreprisesAsProf = $('#dtEntreprisesAsProf').DataTable({
		bAutoWidth: false,
		autoWidth: false,
		responsive: true,
		//"scrollY": "200px", // BUG Cannot read property 'sWidth' of undefined
		"scrollCollapse": true,
		"paging": false,
		info: false,
		"dom": "<'pull-left'f>t",
		language: {
			"search": "Rechercher : ",
			"emptyTable": "Pas de données disponibles dans la table",
			"lengthMenu": "Montrer _MENU_",
			"paginate": {
				"first": "Premier",
				"last": "Dernier",
				"next": "Suivant",
				"previous": "Précédent"
			}
		},
		select: {
			style: 'single',
			info: false
		},
		data: "",
		columns: [
			{
				"data": "denomination",
			},
			{
				"data": "nEtudiants"
			}
		],
		rowId: 'idEntreprise'
	});
}

function initEntreprisesDT(){
	dtEntreprises = $('#dtEntreprises').DataTable({
		bAutoWidth: false,
		autoWidth: false,
		responsive: true,
		"scrollY": "200px",
		"scrollCollapse": true,
		"paging": false,
		info: false,
		"dom": "<'pull-left'f>t",
		language: {
			"search": "Rechercher : ",
			"emptyTable": "Pas de données disponibles dans la table",
			"lengthMenu": "Montrer _MENU_",
			"paginate": {
				"first": "Premier",
				"last": "Dernier",
				"next": "Suivant",
				"previous": "Précédent"
			}
		},
		select: {
			style: 'single',
			info: false
		},
		data: "",
		columns: [
			{
				"data": "denomination"
			}
		],
		rowId: 'idEntreprise'
	});
	
	
	//dtEntreprises.on('select', function (e, dt) {
	// afin d'avoir le meme comportement sur les 2 tables
	$('#dtEntreprises, #dtEntreprisesAsProf').DataTable().on('select', function (e, dt) {
		
		if (!myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === myApp.getWhoAmI().anneeAcademique){
			$('#btnsContactEntreprises').show();
		} else if (myApp.getWhoAmI().estAdmin && myApp.getAcademicalYear() === selectedStud.anneeAcademique) {
			$('#btnsContactEntreprises').show();
		}else {
			$('#btnsContactEntreprises').hide();
		}
		$('#formCreerEntreprise').hide();
		$('#confirmInitiateContact').hide();
		$('.notification').text('');
		dtPersonnesContact.rows().deselect();
		idEntreprise = dt.id();

		config = {
			'data': {
				action: 'getEntreprise',
				idEntreprise: idEntreprise
			},
			'success': function (reponse) {
				removeMarker();
				codeAddress(reponse);
				$('#entrepriseNom').text(reponse.denomination);
				$('#entrepriseTel').text(formatTel(reponse.tel));
				$('#entrepriseEmail').text(formatEmail(reponse.email));
				$('#entrepriseAdresse').text(reponse.numero + " " + reponse.adresse + 
					" " + reponse.codePostal + " " + reponse.ville);
				if(reponse.estBlackListe == false){
					$('#entrepriseBlackliste').html("<span class=\"col-green\">Non blacklisté</span>");
					$('#btnBlacklistEntreprise').show();
				}
				else{
					$('#entrepriseBlackliste').html("<span class=\"col-red\">Blacklisté !</span>");
					$('#btnBlacklistEntreprise').hide();
				}
				if(myApp.getWhoAmI().estAdmin === false){
					$('#btnBlacklistEntreprise').hide();
				}
			}
		}
		myApp.myajax(config);

		var idx = dt.cell('.selected', 0).index();
		var data = dt.row(idx.row).data();
		denominationEntreprise = data.denomination;

		cancelAddPerson();
		//$('#pagePersonnesContact').css('display', 'block');
		$('#pagePersonnesContact').fadeIn("slow");
		remplirPersonnesContact(dt.id(), dtPersonnesContact);
		
		
	});

	//dtEntreprises.on('deselect', function (e, dt, type, indexes) {
	// afin d'avoir le meme comportement sur les 2 tables
	$('#dtEntreprises, #dtEntreprisesAsProf').DataTable().on('deselect', function (e, dt, type, indexes) {	
		$('#confirmInitiateContact').hide();
		dtPersonnesContact.rows().deselect();
		removeMarker();
		$('#pagePersonnesContact').hide();
		idEntreprise = null;
		denominationEntreprise = null;
		cancelAddPerson();
	});
}

// initie DataTables pour le modal
function initModalDT(){
	dtModal = $('#dtModal').DataTable({
		bAutoWidth: false,
		autoWidth: false,
		responsive: true,
		//"scrollY": "200px", // BUG Cannot read property 'sWidth' of undefined
		"scrollCollapse": true,
		"paging": false,
		info: false,
		"dom": "<'pull-left'f>t",
		language: {
			"search": "Rechercher : ",
			"emptyTable": "Pas de données disponibles dans la table",
			"lengthMenu": "Montrer _MENU_",
			"paginate": {
				"first": "Premier",
				"last": "Dernier",
				"next": "Suivant",
				"previous": "Précédent"
			}
		},
		select: {
			style: 'single',
			info: false
		},
		data: "",
		columns: [
			{
				"data": "denomination",
			},
			{
				"data": "nEtudiants"
			}
		],
		rowId: 'idEntreprise'
	});

	$('#dtModal').DataTable().on('select', function (e, dt) {
		idModalEntreprise = dt.id();
		var entrepriseSelectModal = $('#dtModal').DataTable().row("#" + idModalEntreprise).data();
		$('#modalEntrepriseDenominationRight').text(entrepriseSelectModal.denomination);
		$('#modalEntrepriseAdresseRight').text(entrepriseSelectModal.numero + " " + entrepriseSelectModal.adresse);
		$('#modalEntrepriseAdresseSupRight').text(entrepriseSelectModal.codePostal + " " + entrepriseSelectModal.ville);
		$('#modalEntrepriseTelRight').text(formatTel(entrepriseSelectModal.tel));
		$('#modalEntrepriseEmailRight').text(formatEmail(entrepriseSelectModal.email));
		if(entrepriseSelectModal.estBlackListe){
			$('#modalEntrepriseBlacklisteRight').html("<span class=\"col-red\">Blacklisté !</span>");
		} else {
			$('#modalEntrepriseBlacklisteRight').html("<span class=\"col-green\">Non blacklisté</span>");
		}

		$('#btnmergeEntrepriseFinal').removeAttr('disabled');
	});

	$('#dtModal').DataTable().on('deselect', function (e, dt) {
		idModalEntreprise = null;
		emptyEntreprisesInfos();
	});

	$('#mergeModal').on('hidden.bs.modal', function () {
		emptyEntreprisesInfos();
	});

	function emptyEntreprisesInfos(){
		$('#modalEntrepriseDenominationRight').text("");
		$('#modalEntrepriseAdresseRight').text("-");;
		$('#modalEntrepriseAdresseSupRight').text("-");
		$('#modalEntrepriseTelRight').text("-");
		$('#modalEntrepriseEmailRight').text("-");
		$('#modalEntrepriseBlacklisteRight').text("-");
		$('#btnmergeEntrepriseFinal').attr('disabled', 'disabled');
	}

}

// initie DataTables Personnes de contact
function initPersonnesContactDT(idDt){
	
	dt = $(idDt).DataTable({
		destroy: true,
		bAutoWidth: false,
		autoWidth: false,
		//autoWidth : true,
		//pageLength : 5,
		//lengthMenu: [ [5, 10, -1], [5, 10, "All"] ],
		paging: false,
		//scrollY: "300px",
		scrollCollapse: true,
		responsive: true,
		//paging:         false,
		info: false,
		"dom": "<'pull-left'f>t",
		language: {
			"search": "Rechercher : ",
			"emptyTable": "Pas de données disponibles dans la table"
		},
		//info : false,
		select: {
			style: 'single',
		},
		data: "",
		columns: [{
			"data": "nom"
		}, {
			"data": "prenom"
		}, {
			"data": "email"
		}, {
			"data": "tel"
		}],
		rowId: 'idPersonneContact'
	});
	
	return dt;
}

function formatTel(tel){
	if (tel === null){
		return "-";
	}
	return tel;
}

function formatEmail(email){
	if (email === null){
		return "-";
	}
	return email;
}

function initMap() {
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(-34.397, 150.644);
	var mapOptions = {
		zoom: 8,
		center: latlng
	}
	map = new google.maps.Map(document.getElementById('map'), mapOptions);
}

function codeAddress(entreprise) {
	geocoder.geocode({ 'address': entreprise.numero + " " + entreprise.adresse + " " + entreprise.codePostal + " " + entreprise.ville }, function (results, status) {
		if (status == 'OK') {
			map.setCenter(results[0].geometry.location);
			marker = new google.maps.Marker({
				map: map,
				position: results[0].geometry.location,
				animation: google.maps.Animation.DROP,
				title: entreprise.denomination
			});

			setInfoWindow(entreprise);
			infowindow.open(map, marker);
		} else {
		}
	});
}

function setInfoWindow(entreprise) {
	infowindow = new google.maps.InfoWindow({
		content: '<div>' + entreprise.numero + " " + entreprise.adresse + "<br>" + entreprise.codePostal + " " + entreprise.ville + '</div>'
		//maxWidth: 200
	});

	marker.addListener('click', function () {
		infowindow.open(map, marker);
	});
}

function removeMarker() {
	if (marker != null && marker != undefined) {
		marker.setMap(null);
		marker = null;
	}
}