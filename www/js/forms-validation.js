$(function () {
    // Adresse Stage Validation
    $('#formAdresseStage').validate({
        rules: {
            'adresse': {
                required: true,
                minlength: 1,
                maxlength: 30
            },
            'numero': {
                alphanumeric: true,
                minlength: 1,
                maxlength: 5
            },
            'codePostal': {
                customcpbel: true,
                minlength: 1,
                maxlength: 4
            },
            'ville': {
                required: true,
                minlength: 1,
                maxlength: 30
            },
            'dateSignature': {
                customdate: true
            },
            'boite': {
                minlength: 1,
                maxlength: 5
            }
        },

        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });

    // Ajouter Personne Contact Stage Validation
    $('#formNewPersContDash').validate({
        rules: {
            nom: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            prenom: {
                required: true,
                minlength: 1,
                maxlength: 30,
            },
            email: {
                required: true,
                minlength: 1,
                maxlength: 50,
                mailVerif: true
            },
            tel: {
                required: true,
                minlength: 1,
                maxlength: 15,
                telVerif: true
            }
        },
        messages: {
            nom: "Veuillez indiquer un nom",
            prenom: "Veuillez indiquer un prénom",
            email: {
                required: "Veuillez indiquer un mail",
                mailVerif: "Veuillez indiquer un mail valide",
            },
            tel: {
                required: "Veuillez indiquer un numéro de téléphone",
                telVerif: "Veuillez indiquer un numéro de téléphone valide",
            },
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });

    // Ajouter Personne Contact Entreprise
    $('#formNewPersonneContact').validate({
        rules: {
            nom: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            prenom: {
                required: true,
                minlength: 1,
                maxlength: 30,
            },
            email: {
                required: true,
                minlength: 1,
                maxlength: 50,
                mailVerif: true
            },
            tel: {
                required: true,
                minlength: 1,
                maxlength: 15,
                telVerif: true
            }
        },
        messages: {
            nom: "Veuillez indiquer un nom",
            prenom: "Veuillez indiquer un prénom",
            email: {
                required: "Veuillez indiquer un mail",
                mailVerif: "Veuillez indiquer un mail valide",
            },
            tel: {
                required: "Veuillez indiquer un numéro de téléphone",
                telVerif: "Veuillez indiquer un numéro de téléphone valide",
            },
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });

    // Signup Validation
    //TODO changer l'id de ce formulaire
    $("#form_advanced_validation").validate({
        rules: {
            pseudo: {
                required: true,
                minlength: 1,
                maxlength: 30
            },
            mdp: {
                required: true,
                minlength: 1,
                maxlength: 60
            },
            mdpVerif: {
                required: true,
                minlength: 1,
                maxlength: 60,
                equalTo: "#mdp"
            },
            nom: {
                required: true,
                minlength: 1,
                maxlength: 50
            },
            prenom: {
                required: true,
                minlength: 1,
                maxlength: 30
            },
            dateNaissance: {
                required: true,
                dateNaissanceVerif: true
            },
            tel: {
                required: true,
                telVerif: true,
            },
            email: {
                minlength: 1,
                maxlength: 100,
                required: true,
                vinciMail: true
            }
        },
        messages: {
            pseudo: "Veuillez indiquer votre pseudo",
            mdp: "Veuillez indiquer votre mot de passe",
            mdpVerif: {
                required: "Veuillez indiquer votre confirmation de mot de passe",
                equalTo: "Les mots de passe ne correspondent pas"
            },
            nom: "Veuillez indiquer votre nom",
            prenom: "Veuillez indiquer votre prénom",
            dateNaissance: {
                required: "Veuillez indiquer votre date de naissance",
                dateNaissanceVerif: "Le date de naissance doit respecter le format indiqué"
            },
            tel: {
                required: "Veuillez indiquer votre numéro de téléphone",
                telVerif: "Le numéro doit commencer par +32 et contenir entre 11 et 12 chiffres",
            },
            email: {
                required: "Veuillez indiquer votre email",
                vinciMail: "Seul vinci.be ou student.vinci.be est accepté"
            },
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });

    // Modifier Données Personnelles Validation
    $("#form-modif-donnees-perso").validate({
        rules: {
            pseudo: {
                minlength: 1,
                maxlength: 30
            },
            mdpActuel: {
                maxlength: 60
            },
            nouveauMdp1: {
                maxlength: 60
            },
            nouveauMdp2: {
                maxlength: 60,
                equalTo: "#nouveauMdp1"
            },
            nom: {
                required: true,
                maxlength: 50
            },
            prenom: {
                required: true,
                maxlength: 30
            },
            dateNaissance: {
                required: true,
                dateNaissanceVerif: true
            },
            tel: {
                required: true,
                telVerif: true,
            },
            email: {
                maxlength: 100,
                required: true,
                vinciMail: true
            }
        },
        messages: {
            pseudo: "Veuillez indiquer votre pseudo",
            mdpActuel: "Votre mot de passe est trop long",
            nouveauMdp1: "Votre mot de passe est trop long",
            nouveauMdp2: {
                maxlength: "Votre mot de passe est trop long",
                equalTo: "Les mots de passe ne correspondent pas"
            },
            nom: {
                required: "Veuillez indiquer un nom",
                maxlength: "Votre nom est trop long"
            },
            prenom: {
                required: "Veuillez indiquer un prénom",
                maxlength: "Votre prénom est trop long"
            },
            dateNaissance: {
                required: "Veuillez indiquer une date de naissance",
                dateNaissanceVerif: "Le date de naissance doit respecter le format indiqué"
            },
            tel: {
                required: "Veuillez indiquer votre numéro de téléphone",
                telVerif: "Le numéro doit commencer par +32 et contenir entre 11 et 12 chiffres",
            },
            email: {
                required: "Veuillez indiquer votre email",
                vinciMail: "Seul vinci.be ou student.vinci.be est accepté"
            },
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });
    // Login Validation
    $('#formLogin').validate({
        messages: {
            pseudo: "Veuillez indiquer votre pseudo",
            mdp: "Veuillez indiquer votre mot de passe"
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-line').after(error);
        }
    });

    // Création d'entreprise validation
    $('#form-creation-entreprise').validate({
        rules: {
            denomination: {
                required: true,
                maxlength: 50
            },
            email: {
                maxlength: 50,
                mailVerifEntreprise: true
            },
            tel: {
                maxlength: 15,
                telVerifEntreprise: true
            },
            adresse: {
                required: true,
                maxlength: 30
            },
            numero: {
                required: true,
                maxlength: 10
            },
            boite: {
                maxlength: 5
            },
            codePostal: {
                required: true,
                maxlength: 4,
                customcpbel: true
            },
            ville: {
                required: true,
                maxlength: 30
            },
        },
        messages: {
            denomination:{
                required: "Veuillez indiquer une dénomination",
                maxlength: "Max 50 caractères autorisé"
            },
            email: {
                maxlength: "Max 50 caractères autorisé"
            },
            tel: {
                maxlength: "Max 15 caractères autorisé"
            },
            adresse: {
                required: "Veuillez indiquer une adresse",
                maxlength: "Max 30 caractères autorisé"
            },
            numero: {
                required: "Veuillez indiquer un numéro",
                maxlength: "Max 10 caractères autorisé"
            },
            boite: {
                maxlength: "Max 5 caractères autorisé"
            },
            codePostal: {
                required: "Veuillez indiquer un code postal",
                maxlength: "Max 4 caractères autorisé",
            },
            ville: {
                required: "Veuillez indiquer une ville",
                maxlength: "Max 30 caractères autorisé"
            }
        },
        highlight: function (input) {
            $(input).parents('.form-line').addClass('error');
        },
        unhighlight: function (input) {
            $(input).parents('.form-line').removeClass('error');
        },
        errorPlacement: function (error, element) {
            $(element).parents('.form-group').append(error);
        }
    });

// placer ici les autres validations

// Custom Validations ===============================================================================
//customdate
$.validator.addMethod('customdate', function (value, element) {
    return value.match(/^([0-2][0-9]|3[01])\/(0[0-9]|1[0-2])\/[0-9]{4}$/);
},
    'La date doit respecter le format indiqué'
);

//customecodepostalbelgique
$.validator.addMethod('customcpbel', function (value, element) {
    return value.match(/^[1-9]{1}[0-9]{3}$/);
},
    'Le code postal est invalide'
);

//vérification input email jquery validation
jQuery.validator.addMethod("vinciMail", function (value, element) {
    return value.match(/^.+@(vinci|student.vinci).be$/);
}, 'Seul vinci.be ou student.vinci.be est accepté');

//vérification input tel jquery validation
jQuery.validator.addMethod("telVerif", function (value, element) {
        return value.match(/^\+32[0-9]{8,9}$/);
}, 'Le numéro doit commencer par +32 et contenir entre 11 et 12 chiffres');

//vérification input tel créer entreprise jquery validation
jQuery.validator.addMethod("telVerifEntreprise", function (value, element) {
    if($('#tel-cne').val().length !== 0){ // tel création entreprise optionnel
        return value.match(/^\+32[0-9]{8,9}$/);
    }
    else {
        return true;
    }
}, 'Le numéro doit commencer par +32 et contenir entre 11 et 12 chiffres');

//vérification input dateNaissance jquery validation
jQuery.validator.addMethod("dateNaissanceVerif", function (value, element) {
    return value.match(/^([0-2][0-9]|3[01])\/(0[0-9]|1[0-2])\/[0-9]{4}$/);
}, 'La date de naissance doit respecter le format indiqué');

//vérification input email jquery validation
jQuery.validator.addMethod("mailVerif", function (value, element) {
        return value.match(/^.+@(.+)\..+$/);
}, 'Veuillez indiquer un mail valide');

//vérification input email entreprise jquery validation
jQuery.validator.addMethod("mailVerifEntreprise", function (value, element) {
    if($('#email-cne').val().length !== 0){ //email création entreprise optionnel
        return value.match(/^.+@(.+)\..+$/);
    }
    else{
        return true;
    }
}, 'Veuillez indiquer un mail valide');

    // placer ici les autres custom validations

    //==================================================================================================
});