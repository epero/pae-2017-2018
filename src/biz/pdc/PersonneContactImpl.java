package biz.pdc;

import biz.entreprise.EntrepriseDto;

public class PersonneContactImpl implements PersonneContactBiz {

  private int idPersonneContact;
  private String nom;
  private String prenom;
  private String tel;
  private String email;
  private int entreprise;
  private EntrepriseDto entrepriseDto;
  private int numVersion;

  @Override
  public void fillPersonneContact(int idPersonneContact, String nom, String prenom, String tel,
      String email, int entreprise, EntrepriseDto entrepriseDto, int numVersion) {
    this.idPersonneContact = idPersonneContact;
    this.nom = nom;
    this.prenom = prenom;
    this.tel = tel;
    this.email = email;
    this.entreprise = entreprise;
    this.entrepriseDto = entrepriseDto;
    this.numVersion = numVersion;
  }

  @Override
  public int getIdPersonneContact() {
    return idPersonneContact;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public String getTel() {
    return tel;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public int getEntreprise() {
    return entreprise;
  }

  @Override
  public EntrepriseDto getEntrepriseDto() {
    return entrepriseDto;
  }

  public int getNumVersion() {
    return numVersion;
  }

  @Override
  public void setIdPersonneContact(int idPersonneContact) {
    this.idPersonneContact = idPersonneContact;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public void setTel(String tel) {
    this.tel = tel;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setEntreprise(int entreprise) {
    this.entreprise = entreprise;
  }

  @Override
  public void setEntrepriseDto(EntrepriseDto entrepriseDto) {
    this.entrepriseDto = entrepriseDto;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }

  @Override
  public String toString() {
    return "PersonneContactImpl [idPersonneContact=" + idPersonneContact + ", nom=" + nom
        + ", prenom=" + prenom + ", tel=" + tel + ", email=" + email + ", entreprise=" + entreprise
        + ", numVersion=" + numVersion + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + idPersonneContact;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PersonneContactImpl other = (PersonneContactImpl) obj;
    if (idPersonneContact != other.idPersonneContact) {
      return false;
    }
    return true;
  }
}
