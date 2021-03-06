FrontEnd.onInitPage('pageLogin', function(){
	$('#btnLogin').click(function(){
		var isvalid = $('#formLogin').valid();
		if(isvalid){
			config = {
						'data': {
									'action': 'signin',
									'userAVerif': myApp.form2Json($('#formLogin'))
						},
						'success': function(reponse){
							if(reponse === null){
		                    	setAndShowNotification("Le nom d\'utilisateur ou le mot de passe est incorrect", $('#buttonAlert'));
								//$('.notification').html('<div class="alert alert-dismissible bg-red text-center"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>Le nom d\'utilisateur ou le mot de passe est incorrect.</div>');
							} else {
								myApp.setWhoAmI(reponse);
						        if (myApp.getWhoAmI().estAdmin === false) {
						            FrontEnd.displayPage('pageDashboardStudent');
						        }
						        else {
						            FrontEnd.displayPage('pageDashboardTeacher');
						        }								
							}
						}
					 }
			myApp.myajax(config);
		}
	});
	
	$('#btnSignUp').click(function(){
		$("#formLogin").validate().resetForm();
        $('.form-line').removeClass('error focused');
		FrontEnd.displayPage('pageSignup');
	});
	
	FrontEnd.onDisplayPage('pageLogin', function(){
		hideNavBar();
		$('body').removeClass().toggleClass('login-page');
	});
	
	
	FrontEnd.onLeavePage('pageLogin', function(){
		$('.notification').hide();
	});
});








