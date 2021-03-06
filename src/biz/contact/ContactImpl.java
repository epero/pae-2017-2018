package biz.contact;

import biz.entreprise.EntrepriseDto;
import biz.pdc.PersonneContactDto;
import biz.user.UserDto;

public class ContactImpl implements ContactBiz {
  private int idContact;
  private int utilisateur;
  private UserDto utilisateurDto;
  private int entreprise;
  private EntrepriseDto entrepriseDto;
  private Integer personneContact;
  private PersonneContactDto personneContactDto;
  private int etat;
  private String anneeAcademique;
  private int numVersion;

  @Override
  public void fillContact(int idContact, int utilisateur, UserDto utilisateurDto, int entreprise,
      EntrepriseDto entrepriseDto, Integer personneContact, PersonneContactDto personneContactDto,
      int etat, String anneeAcademique, int numVersion) {
    this.idContact = idContact;
    this.utilisateur = utilisateur;
    this.utilisateurDto = utilisateurDto;
    this.entreprise = entreprise;
    this.entrepriseDto = entrepriseDto;
    this.personneContact = personneContact;
    this.personneContactDto = personneContactDto;
    this.etat = etat;
    this.anneeAcademique = anneeAcademique;
    this.numVersion = numVersion;
  }

  @Override
  public String toString() {
    return "ContactImpl [idContact=" + idContact + ", utilisateur=" + utilisateur
        + ", utilisateurDto=" + utilisateurDto + ", entreprise=" + entreprise + ", entrepriseDto="
        + entrepriseDto + ", personneContact=" + personneContact + ", personneContactDto="
        + personneContactDto + ", etat=" + etat + ", anneeAcademique=" + anneeAcademique
        + ", numVersion=" + numVersion + "]";
  }

  public int getIdContact() {
    return idContact;
  }

  public int getUtilisateur() {
    return utilisateur;
  }

  public UserDto getUtilisateurDto() {
    return utilisateurDto;
  }

  public int getEntreprise() {
    return entreprise;
  }

  public EntrepriseDto getEntrepriseDto() {
    return entrepriseDto;
  }

  public Integer getPersonneContact() {
    return personneContact;
  }

  public PersonneContactDto getPersonneContactDto() {
    return personneContactDto;
  }

  public int getEtat() {
    return etat;
  }

  public String getAnneeAcademique() {
    return anneeAcademique;
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setIdContact(int idContact) {
    this.idContact = idContact;
  }

  public void setUtilisateur(int utilisateur) {
    this.utilisateur = utilisateur;
  }

  public void setUtilisateurDto(UserDto utilisateurDto) {
    this.utilisateurDto = utilisateurDto;
  }

  public void setEntreprise(int entreprise) {
    this.entreprise = entreprise;
  }

  public void setEntrepriseDto(EntrepriseDto entrepriseDto) {
    this.entrepriseDto = entrepriseDto;
  }

  public void setPersonneContact(Integer personneContact) {
    this.personneContact = personneContact;
  }

  public void setPersonneContactDto(PersonneContactDto personneContactDto) {
    this.personneContactDto = personneContactDto;
  }

  public void setEtat(int etat) {
    this.etat = etat;
  }

  public void setAnneeAcademique(String anneeAcademique) {
    this.anneeAcademique = anneeAcademique;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }
}
