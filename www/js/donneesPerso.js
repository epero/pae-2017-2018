let user;

FrontEnd.onInitPage('pageDonneesPerso', function () {
	
	$('#btn-modif-mdp').on('click', function () {
	    $('#div-btn-modif-mdp').css('display', 'none');
		//$('#inputs-modif-mdp').css('display', 'block');
		$('#inputs-modif-mdp').fadeIn();
	});

	$('#btn-donnees-perso-cancel').on('click', function () {

	    if (myApp.getWhoAmI().estAdmin === false) {

	        FrontEnd.displayPage('pageDashboardStudent');
	    }
	    else {
	        FrontEnd.displayPage('pageDashboardTeacher');
	    }
	});

	$('#btn-mdp-cancel').on('click', function () {
		//$('#div-btn-modif-mdp').css('display', 'block');
		$('#div-btn-modif-mdp').fadeIn();
	    $('#inputs-modif-mdp').css('display', 'none');
		$('#mdpActuel').val('');
		$('#nouveauMdp1').val('');
		$('#nouveauMdp2').val('');
	});

	$('#btn-mdp-submit').on('click', function () {
	    //pk ne pas laisser ca au validateur?
	    if ($('#form-modif-donnees-perso #mdpActuel').val().length !== 0 && $('#form-modif-donnees-perso #nouveauMdp1').val().length !== 0
	        && $('#form-modif-donnees-perso #nouveauMdp2').val().length !== 0) {

	        var isvalid = $('#form-modif-donnees-perso').valid();
	        if (isvalid) {
	            
	            config = {
	                'data': {
	                    'action': 'setMdpUser',
	                    'userId' : JSON.stringify(myApp.getWhoAmI().idUtilisateur),
	                    'userNumVersion': JSON.stringify(myApp.getWhoAmI().numVersion),
	                    'mdpActuel': $('#form-modif-donnees-perso #mdpActuel').val(),
	                    'nouveauMdp1': $('#form-modif-donnees-perso #nouveauMdp1').val(),
	                    'nouveauMdp2': $('#form-modif-donnees-perso #nouveauMdp2').val()
	                },
	                'success': function (reponse) {
	                    if (reponse != null) {
	                    	myApp.setWhoAmI(reponse);
	                    	setAndShowNotification("Votre mot de passe à été modifié", $('#buttonSuccess'));
							//$('#notification-form-infos-perso').html('<div class="alert alert-dismissible bg-green text-center"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>Votre mot de passe a été modifié</div>');
							$('#inputs-modif-mdp').css('display', 'none');
							$('#div-btn-modif-mdp').css('display', 'block');
	                    }

	                },
	                error: function(e,error, errorThrown) {  
	                    if(e.status&&e.status==409){
		                	var reponse = JSON.parse(e.responseText);
		                	myApp.setWhoAmI(reponse.objetDb);
		                	updateNavBar();
		                    date = new Date(reponse.objetDb.dateNaissance.monthValue + " " + reponse.objetDb.dateNaissance.dayOfMonth + " " + reponse.objetDb.dateNaissance.year);
		                    reponse.objetDb.dateNaissance = dateFormatee(date);
		                	myApp.json2Form($("#form-modif-donnees-perso"), reponse.objetDb);
	                    	setAndShowNotification(reponse.message, $('#buttonAlert'));

		            		//$('.notification').html('<div class="alert alert-dismissible bg-red text-center "><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+reponse.message+'</div>');
		            		//$('.notification').show();
	            		}
	               }
	            }
	            myApp.myajax(config);
	        }
		}
		else {
        	setAndShowNotification("Veuillez remplir les champs mot de passe", $('#buttonAlert'));
			//$('#notification-form-infos-perso').html('<div class="alert alert-dismissible bg-red text-center"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>Veuillez remplir les champs mot de passe</div>');
		}
	});

	$('#btn-donnees-perso-submit').on('click', function () {

	    //vérification si pseudo non changé
	    if ($('#form-modif-donnees-perso #pseudo').val() !== myApp.getWhoAmI().pseudo) {

			setAndShowNotification("Vous ne pouvez pas modifier votre pseudo", $('#buttonAlert'));
	    }
		
	    //Verification si modification
		else if ($('#form-modif-donnees-perso #nom').val() === myApp.getWhoAmI().nom 
				&& $('#form-modif-donnees-perso #prenom').val() === myApp.getWhoAmI().prenom 
				&& $('#form-modif-donnees-perso #dateNaissance').val() === user.dateNaissance
				&& $('#form-modif-donnees-perso #email').val() === myApp.getWhoAmI().email 
	            && $('#form-modif-donnees-perso #tel').val() === myApp.getWhoAmI().tel) {
					
					setAndShowNotification("Vous n\'avez fait aucune modification", $('#buttonAlert'));
		}
	    else {
	        var isvalid = $('#form-modif-donnees-perso').valid();
	        if (isvalid) {
				
	        	var userModif = JSON.parse(myApp.form2Json($("#form-modif-donnees-perso")));
	        	userModif['idUtilisateur'] = myApp.getWhoAmI().idUtilisateur;
	        	userModif['numVersion'] = myApp.getWhoAmI().numVersion;
	        	userModif = JSON.stringify(userModif);
	            config = {
	                'data': {
	                    'action': 'setInfosPersoUser',
	                    'user': userModif
	                },
	                'success': function (reponse) {
	                    if (reponse != null) {
	                    	myApp.setWhoAmI(reponse);
	                    	updateNavBar();
	                    	setAndShowNotification("Vos informations ont été modifées", $('#buttonSuccess'));

	                    }
	                },
	                error: function(e,error, errorThrown) {  
	                    if(e.status&&e.status==409){
		                	var reponse = JSON.parse(e.responseText);
		                	myApp.setWhoAmI(reponse.objetDb);
		                	updateNavBar();
		                    date = new Date(reponse.objetDb.dateNaissance.monthValue + " " + reponse.objetDb.dateNaissance.dayOfMonth + " " + reponse.objetDb.dateNaissance.year);
		                    reponse.objetDb.dateNaissance = dateFormatee(date);
		                	myApp.json2Form($("#form-modif-donnees-perso"), reponse.objetDb);
		                	setAndShowNotification(reponse.message, $('#buttonAlert'));
	            		}
	               }
	            }
	            myApp.myajax(config);
	        }
	    }
	});


	FrontEnd.onDisplayPage('pageDonneesPerso', function () {

	    $('body').removeClass().toggleClass('theme-teal');
	    showNavBar();

	    // Affichage par défaut du formulaire
	    $('#div-btn-modif-mdp').css('display', 'block');
	    $('#inputs-modif-mdp').css('display', 'none');
	    $("#form-modif-donnees-perso").validate().resetForm();
	    $('.form-line').removeClass('error focused');

	    config = {
	        'data': {
	            'action': 'getInfosPersoUser',
	            'selectedStudId' : myApp.getWhoAmI().idUtilisateur
	        },
	        'success': function (reponse) {
				
				user = reponse;
	            date = new Date(reponse.dateNaissance.monthValue + " " + reponse.dateNaissance.dayOfMonth + " " + reponse.dateNaissance.year);
				reponse.dateNaissance = dateFormatee(date);
	            myApp.setWhoAmI(reponse);
	            myApp.json2Form($('#form-modif-donnees-perso'), reponse);
	        }
	    }
		myApp.myajax(config);
		
		//cache les pages actives dans la navbar
		$('#navDashboard a').removeClass('actif');
		$('#navEntreprises a').removeClass('actif');
		$('#navDashboardStudent a').removeClass('actif');
	});
	
	FrontEnd.onLeavePage('pageDonneesPerso', function(){
	    $('#form-modif-donnees-perso')[0].reset();
		$('.notification').text('');
		$('#notification-form-infos-perso').text('');
	    user = undefined;
	});
});

function dateFormatee(date) {
    return moment(date).format('DD/MM/YYYY');
}