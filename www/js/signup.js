FrontEnd.onInitPage('pageSignup', function(){
    
    $('#btn-signup').click(function(e){
        var isvalid = $('#form_advanced_validation').valid();
    	if(isvalid){
	        config = {
	                    'data': {
	                                'action': 'signup',
	                                'newUser': myApp.form2Json($('#form_advanced_validation'))
	                    },
	                    'success': function(reponse){
	                    	myApp.setWhoAmI(reponse);
							FrontEnd.displayPage('pageDashboardStudent');
	                    }
	        }
	        myApp.myajax(config);
        }
    });
    
    $('#btn-cancel').click(function(){
        $("#form_advanced_validation").validate().resetForm();
        $('.form-line').removeClass('error focused');
        FrontEnd.displayPage('pageLogin');
    });
    
    FrontEnd.onDisplayPage('pageSignup', function(){
		hideNavBar();
		$('body').removeClass().toggleClass('signup-page');
	});
    
	FrontEnd.onLeavePage('pageLogin', function(){
		$("#form_advanced_validation")[0].reset();
		$('.notification').hide();
	});
    
});