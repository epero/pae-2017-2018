package biz.user;


import java.time.LocalDate;
import com.owlike.genson.annotation.JsonDateFormat;
import util.Util;

public class UserImpl implements UserBiz {

  @Override
  public String toString() {
    return "UserImpl [idUtilisateur=" + idUtilisateur + ", pseudo=" + pseudo + ", mdp=" + mdp
        + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance=" + dateNaissance + ", tel="
        + tel + ", email=" + email + ", dateInscription=" + dateInscription + ", anneeAcademique="
        + anneeAcademique + ", estAdmin=" + estAdmin + ", nbContacts=" + nbContacts
        + ", numVersion=" + numVersion + "]";
  }

  // attributs
  private int idUtilisateur;
  private String pseudo;
  private String mdp;
  private String nom;
  private String prenom;
  @JsonDateFormat("dd-MM-yyyy")
  private LocalDate dateNaissance;
  private String tel;
  private String email;
  private LocalDate dateInscription;
  private String anneeAcademique;
  private boolean estAdmin;
  private int nbContacts;
  private int etatPlusAvance;
  private int numVersion;


  @Override
  public void fillUser(int idUtilisateur, String pseudo, String mdp, String nom, String prenom,
      LocalDate dateNaissance, String tel, String email, LocalDate dateInscription,
      String anneeAcademique, boolean estAdmin, int nbContacts, int etatPlusAvance,
      int numVersion) {
    this.idUtilisateur = idUtilisateur;
    this.pseudo = pseudo;
    this.mdp = mdp;
    this.nom = nom;
    this.prenom = prenom;
    this.dateNaissance = dateNaissance;
    this.tel = tel;
    this.email = email;
    this.dateInscription = dateInscription;
    this.anneeAcademique = anneeAcademique;
    this.estAdmin = estAdmin;
    this.nbContacts = nbContacts;
    this.etatPlusAvance = etatPlusAvance;
    this.numVersion = numVersion;
  }


  @Override
  public boolean verifierMotDePasse(String mdp) {
    return Util.checkpw(mdp, this.mdp);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mdp == null) ? 0 : mdp.hashCode());
    result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
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
    UserImpl other = (UserImpl) obj;
    if (mdp == null) {
      if (other.mdp != null) {
        return false;
      }
    } else if (!mdp.equals(other.mdp)) {
      return false;
    }
    if (pseudo == null) {
      if (other.pseudo != null) {
        return false;
      }
    } else if (!pseudo.equals(other.pseudo)) {
      return false;
    }
    return true;
  }

  public int getIdUtilisateur() {
    return idUtilisateur;
  }

  public String getPseudo() {
    return pseudo;
  }

  public String getMdp() {
    return mdp;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  @JsonDateFormat("dd-MM-yyyy")
  public LocalDate getDateNaissance() {
    return dateNaissance;
  }

  public String getTel() {
    return tel;
  }

  public String getEmail() {
    return email;
  }

  public LocalDate getDateInscription() {
    return dateInscription;
  }

  public String getAnneeAcademique() {
    return anneeAcademique;
  }

  public boolean getEstAdmin() {
    return estAdmin;
  }

  public int getNbContacts() {
    return nbContacts;
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setIdUtilisateur(int idUtilisateur) {
    this.idUtilisateur = idUtilisateur;
  }

  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public void setMdp(String mdp) {
    this.mdp = mdp;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @JsonDateFormat("dd-MM-yyyy")
  public void setDateNaissance(LocalDate dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @JsonDateFormat("dd-MM-yyyy")
  public void setDateInscription(LocalDate dateInscription) {
    this.dateInscription = dateInscription;
  }

  public void setAnneeAcademique(String anneeAcademique) {
    this.anneeAcademique = anneeAcademique;
  }

  public void setEstAdmin(boolean estAdmin) {
    this.estAdmin = estAdmin;
  }

  public void setNbContacts(int nbContacts) {
    this.nbContacts = nbContacts;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }

  public int getEtatPlusAvance() {
    return etatPlusAvance;
  }

  public void setEtatPlusAvance(int etatPlusAvance) {
    this.etatPlusAvance = etatPlusAvance;
  }

}
